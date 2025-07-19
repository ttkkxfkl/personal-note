<template>
  <el-dialog
    v-model="visible"
    title="附件管理"
    width="700px"
    class="attachment-dialog"
  >
    <div class="attachment-container">
      <!-- 上传区域 -->
      <div class="upload-section">
        <el-upload
          ref="uploadRef"
          class="upload-dragger"
          :action="uploadUrl"
          :data="uploadData"
          :headers="uploadHeaders"
          :before-upload="beforeUpload"
          :on-success="handleSuccess"
          :on-error="handleError"
          :on-progress="handleProgress"
          :file-list="fileList"
          :auto-upload="autoUpload"
          multiple
          drag
        >
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">
            将文件拖拽到此处，或<em>点击选择文件</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              支持文档、图片、音频、视频等格式，单个文件不超过 {{ maxSize }}MB
            </div>
          </template>
        </el-upload>

        <!-- 上传进度 -->
        <div v-if="uploading" class="upload-progress">
          <el-progress :percentage="uploadProgress" :status="progressStatus" />
          <p class="progress-text">{{ progressText }}</p>
        </div>
      </div>

      <!-- 附件列表 -->
      <div class="attachment-list">
        <div class="list-header">
          <h4>附件列表 ({{ attachments.length }})</h4>
          <div class="list-actions">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索附件..."
              size="small"
              clearable
              style="width: 200px"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-select v-model="filterType" placeholder="文件类型" size="small" style="width: 120px">
              <el-option label="全部" value="" />
              <el-option label="图片" value="IMAGE" />
              <el-option label="文档" value="DOCUMENT" />
              <el-option label="音频" value="AUDIO" />
              <el-option label="视频" value="VIDEO" />
              <el-option label="其他" value="OTHER" />
            </el-select>
          </div>
        </div>

        <div class="attachment-grid">
          <div 
            v-for="(attachment, index) in filteredAttachments"
            :key="attachment.id || index"
            class="attachment-item"
            :class="{ 'selected': selectedAttachments.includes(attachment.id) }"
            @click="toggleSelection(attachment.id)"
          >
            <div class="attachment-icon">
              <el-icon>
                <component :is="getFileIcon(attachment.fileType, attachment.mimeType)" />
              </el-icon>
            </div>
            <div class="attachment-info">
              <h5 class="attachment-name" :title="attachment.originalFilename">
                {{ attachment.originalFilename }}
              </h5>
              <p class="attachment-meta">
                <span class="file-size">{{ formatFileSize(attachment.fileSize) }}</span>
                <span class="file-type">{{ getFileTypeText(attachment.fileType) }}</span>
              </p>
              <p class="attachment-time">
                {{ formatDate(attachment.createdTime) }}
              </p>
            </div>
            <div class="attachment-actions">
              <el-button
                type="primary"
                size="small"
                circle
                @click.stop="downloadAttachment(attachment)"
                :title="'下载'"
              >
                <el-icon><Download /></el-icon>
              </el-button>
              <el-button
                type="info"
                size="small"
                circle
                @click.stop="previewAttachment(attachment)"
                v-if="canPreview(attachment)"
                :title="'预览'"
              >
                <el-icon><View /></el-icon>
              </el-button>
              <el-button
                type="danger"
                size="small"
                circle
                @click.stop="removeAttachment(attachment, index)"
                :title="'删除'"
              >
                <el-icon><Delete /></el-icon>
              </el-button>
            </div>
          </div>
        </div>

        <!-- 空状态 -->
        <div v-if="filteredAttachments.length === 0" class="empty-state">
          <el-empty description="暂无附件" />
        </div>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <div class="selection-info">
          <span v-if="selectedAttachments.length > 0">
            已选择 {{ selectedAttachments.length }} 个附件
          </span>
        </div>
        <div class="footer-actions">
          <el-button @click="visible = false">关闭</el-button>
          <el-button 
            type="primary" 
            @click="insertSelectedAttachments"
            :disabled="selectedAttachments.length === 0"
          >
            插入选中 ({{ selectedAttachments.length }})
          </el-button>
        </div>
      </div>
    </template>

    <!-- 预览对话框 -->
    <el-dialog
      v-model="showPreview"
      :title="previewTitle"
      width="80%"
      class="preview-dialog"
    >
      <div class="preview-content">
        <!-- 图片预览 -->
        <img 
          v-if="previewType === 'image'" 
          :src="previewUrl" 
          :alt="previewTitle"
          class="preview-image"
        />
        <!-- 文本预览 -->
        <div v-else-if="previewType === 'text'" class="preview-text">
          <pre>{{ previewContent }}</pre>
        </div>
        <!-- PDF预览 -->
        <iframe 
          v-else-if="previewType === 'pdf'" 
          :src="previewUrl" 
          class="preview-iframe"
        ></iframe>
        <!-- 其他类型 -->
        <div v-else class="preview-unsupported">
          <el-icon size="48"><Document /></el-icon>
          <p>此文件类型不支持预览</p>
          <el-button type="primary" @click="downloadAttachment(previewAttachment)">
            <el-icon><Download /></el-icon>
            下载文件
          </el-button>
        </div>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  UploadFilled, Search, Download, View, Delete, Document, 
  Picture, VideoCamera, Headphones, Files, FolderOpened
} from '@element-plus/icons-vue'
import { formatDate } from '@/utils/date'

const props = defineProps({
  modelValue: Boolean,
  noteId: {
    type: Number,
    required: true
  },
  maxSize: {
    type: Number,
    default: 50 // MB
  },
  autoUpload: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits(['update:modelValue', 'uploaded'])

const visible = ref(false)
const uploadRef = ref()
const fileList = ref([])
const attachments = ref([])
const selectedAttachments = ref([])
const uploading = ref(false)
const uploadProgress = ref(0)
const progressStatus = ref('')
const progressText = ref('')

// 搜索和过滤
const searchKeyword = ref('')
const filterType = ref('')

// 预览相关
const showPreview = ref(false)
const previewType = ref('')
const previewUrl = ref('')
const previewTitle = ref('')
const previewContent = ref('')
const previewAttachment = ref(null)

// 上传配置
const uploadUrl = computed(() => '/api/files/upload')
const uploadData = computed(() => ({
  noteId: props.noteId
}))
const uploadHeaders = computed(() => ({
  // 如果需要认证头，在这里添加
}))

// 过滤后的附件列表
const filteredAttachments = computed(() => {
  let result = attachments.value

  // 按类型过滤
  if (filterType.value) {
    result = result.filter(item => item.fileType === filterType.value)
  }

  // 按关键词搜索
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(item => 
      item.originalFilename.toLowerCase().includes(keyword) ||
      (item.altText && item.altText.toLowerCase().includes(keyword))
    )
  }

  return result
})

// 方法
const beforeUpload = (file) => {
  // 检查文件大小
  const isLtMaxSize = file.size / 1024 / 1024 < props.maxSize
  if (!isLtMaxSize) {
    ElMessage.error(`文件大小不能超过 ${props.maxSize}MB!`)
    return false
  }

  uploading.value = true
  uploadProgress.value = 0
  progressStatus.value = 'active'
  progressText.value = '准备上传...'

  return true
}

const handleProgress = (event, file, fileList) => {
  uploadProgress.value = Math.round(event.percent)
  progressText.value = `上传中... ${uploadProgress.value}%`
}

const handleSuccess = (response, file, fileList) => {
  uploading.value = false
  uploadProgress.value = 100
  progressStatus.value = 'success'
  progressText.value = '上传成功!'

  if (response.success) {
    attachments.value.unshift(response.data)
    ElMessage.success('文件上传成功!')
  } else {
    ElMessage.error(response.message || '上传失败')
  }

  setTimeout(() => {
    uploading.value = false
    uploadProgress.value = 0
  }, 1500)
}

const handleError = (error, file, fileList) => {
  uploading.value = false
  progressStatus.value = 'exception'
  progressText.value = '上传失败'
  console.error('上传失败:', error)
  ElMessage.error('文件上传失败!')
}

const toggleSelection = (attachmentId) => {
  const index = selectedAttachments.value.indexOf(attachmentId)
  if (index > -1) {
    selectedAttachments.value.splice(index, 1)
  } else {
    selectedAttachments.value.push(attachmentId)
  }
}

const downloadAttachment = (attachment) => {
  const link = document.createElement('a')
  link.href = `/api/files/download/${attachment.id}`
  link.download = attachment.originalFilename
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

const canPreview = (attachment) => {
  const imageTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp']
  const textTypes = ['text/plain', 'text/html', 'text/css', 'text/javascript']
  const pdfTypes = ['application/pdf']
  
  return imageTypes.includes(attachment.mimeType) || 
         textTypes.includes(attachment.mimeType) ||
         pdfTypes.includes(attachment.mimeType)
}

const previewAttachment = async (attachment) => {
  previewAttachment.value = attachment
  previewTitle.value = attachment.originalFilename
  previewUrl.value = attachment.filePath

  if (attachment.mimeType.startsWith('image/')) {
    previewType.value = 'image'
  } else if (attachment.mimeType === 'application/pdf') {
    previewType.value = 'pdf'
  } else if (attachment.mimeType.startsWith('text/')) {
    previewType.value = 'text'
    // 加载文本内容
    try {
      const response = await fetch(attachment.filePath)
      previewContent.value = await response.text()
    } catch (error) {
      previewContent.value = '无法加载文件内容'
    }
  } else {
    previewType.value = 'unsupported'
  }

  showPreview.value = true
}

const removeAttachment = async (attachment, index) => {
  try {
    await ElMessageBox.confirm('确定要删除这个附件吗？', '确认删除', {
      type: 'warning'
    })
    
    // 调用删除API
    // await attachmentApi.delete(attachment.id)
    
    attachments.value.splice(index, 1)
    selectedAttachments.value = selectedAttachments.value.filter(id => id !== attachment.id)
    ElMessage.success('附件删除成功!')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败!')
    }
  }
}

const insertSelectedAttachments = () => {
  const selected = attachments.value.filter(item => 
    selectedAttachments.value.includes(item.id)
  )
  
  selected.forEach(attachment => {
    emit('uploaded', attachment)
  })
  
  selectedAttachments.value = []
  visible.value = false
}

const getFileIcon = (fileType, mimeType) => {
  switch (fileType) {
    case 'IMAGE':
      return Picture
    case 'VIDEO':
      return VideoCamera
    case 'AUDIO':
      return Headphones
    case 'DOCUMENT':
      return Document
    default:
      if (mimeType?.startsWith('image/')) return Picture
      if (mimeType?.startsWith('video/')) return VideoCamera
      if (mimeType?.startsWith('audio/')) return Headphones
      return Files
  }
}

const getFileTypeText = (fileType) => {
  const typeMap = {
    'IMAGE': '图片',
    'VIDEO': '视频',
    'AUDIO': '音频',
    'DOCUMENT': '文档',
    'OTHER': '其他'
  }
  return typeMap[fileType] || '未知'
}

const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

const loadAttachments = async () => {
  try {
    // 从API加载附件列表
    // const response = await attachmentApi.getByNoteId(props.noteId)
    // attachments.value = response || []
    
    // 临时使用模拟数据
    attachments.value = []
  } catch (error) {
    console.error('加载附件失败:', error)
    ElMessage.error('加载附件失败!')
  }
}

// 监听器
watch(() => props.modelValue, (val) => {
  visible.value = val
  if (val) {
    loadAttachments()
    selectedAttachments.value = []
    fileList.value = []
  }
})

watch(visible, (val) => {
  emit('update:modelValue', val)
})

onMounted(() => {
  if (props.modelValue) {
    loadAttachments()
  }
})
</script>

<style scoped>
.attachment-dialog :deep(.el-dialog__body) {
  padding: 20px;
}

.attachment-container {
  max-height: 600px;
  overflow-y: auto;
}

.upload-section {
  margin-bottom: 24px;
}

.upload-dragger :deep(.el-upload-dragger) {
  border: 2px dashed var(--el-border-color);
  border-radius: 8px;
  padding: 30px;
  text-align: center;
  background: var(--bg-color-secondary);
  transition: all 0.3s;
}

.upload-dragger :deep(.el-upload-dragger:hover) {
  border-color: var(--el-color-primary);
  background: var(--el-color-primary-light-9);
}

.upload-dragger :deep(.el-icon--upload) {
  font-size: 40px;
  color: var(--el-color-primary);
  margin-bottom: 12px;
}

.upload-progress {
  margin: 16px 0;
  padding: 16px;
  background: var(--bg-color-secondary);
  border-radius: 8px;
}

.progress-text {
  margin: 8px 0 0 0;
  text-align: center;
  color: var(--text-color-regular);
  font-size: 14px;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--border-color);
}

.list-header h4 {
  margin: 0;
  color: var(--text-color-primary);
}

.list-actions {
  display: flex;
  gap: 12px;
}

.attachment-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 8px;
}

.attachment-item {
  display: flex;
  align-items: center;
  padding: 12px;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
  background: var(--bg-color-primary);
}

.attachment-item:hover {
  border-color: var(--el-color-primary);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.attachment-item.selected {
  border-color: var(--el-color-primary);
  background: var(--el-color-primary-light-9);
}

.attachment-icon {
  flex-shrink: 0;
  width: 40px;
  height: 40px;
  margin-right: 12px;
  border-radius: 8px;
  background: var(--el-color-info-light-8);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--el-color-info);
  font-size: 20px;
}

.attachment-info {
  flex: 1;
  min-width: 0;
}

.attachment-name {
  margin: 0 0 4px 0;
  font-size: 14px;
  font-weight: 500;
  color: var(--text-color-primary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.attachment-meta {
  margin: 0 0 2px 0;
  font-size: 12px;
  color: var(--text-color-secondary);
}

.attachment-meta span {
  margin-right: 12px;
}

.attachment-time {
  margin: 0;
  font-size: 11px;
  color: var(--text-color-placeholder);
}

.attachment-actions {
  flex-shrink: 0;
  display: flex;
  gap: 6px;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
}

.dialog-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.selection-info {
  color: var(--text-color-secondary);
  font-size: 14px;
}

.footer-actions {
  display: flex;
  gap: 12px;
}

.preview-dialog :deep(.el-dialog__body) {
  padding: 0;
}

.preview-content {
  text-align: center;
  min-height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.preview-image {
  max-width: 100%;
  max-height: 70vh;
  object-fit: contain;
}

.preview-text {
  width: 100%;
  height: 500px;
  overflow: auto;
  text-align: left;
  padding: 20px;
  background: var(--bg-color-secondary);
}

.preview-text pre {
  margin: 0;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 14px;
  line-height: 1.6;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.preview-iframe {
  width: 100%;
  height: 600px;
  border: none;
}

.preview-unsupported {
  padding: 40px;
  color: var(--text-color-secondary);
}

.preview-unsupported p {
  margin: 16px 0 24px 0;
  font-size: 16px;
}
</style>