<template>
  <el-dialog
    v-model="visible"
    title="上传图片"
    width="600px"
    class="image-upload-dialog"
  >
    <div class="upload-container">
      <!-- 拖拽上传区域 -->
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
        accept="image/*"
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">
          将图片拖拽到此处，或<em>点击上传</em>
        </div>
        <template #tip>
          <div class="el-upload__tip">
            支持 jpg/png/gif 格式，单张图片不超过 {{ maxSize }}MB
          </div>
        </template>
      </el-upload>

      <!-- 上传进度 -->
      <div v-if="uploading" class="upload-progress">
        <el-progress :percentage="uploadProgress" :status="progressStatus" />
        <p class="progress-text">{{ progressText }}</p>
      </div>

      <!-- 已上传图片预览 -->
      <div v-if="uploadedImages.length > 0" class="uploaded-images">
        <h4>已上传图片</h4>
        <div class="image-grid">
          <div 
            v-for="(image, index) in uploadedImages" 
            :key="index"
            class="image-item"
          >
            <div class="image-preview">
              <img :src="image.thumbnailPath || image.filePath" :alt="image.altText" />
              <div class="image-overlay">
                <el-button
                  type="primary"
                  size="small"
                  circle
                  @click="previewImage(image)"
                >
                  <el-icon><ZoomIn /></el-icon>
                </el-button>
                <el-button
                  type="info"
                  size="small"
                  circle
                  @click="editImageInfo(image)"
                >
                  <el-icon><Edit /></el-icon>
                </el-button>
                <el-button
                  type="danger"
                  size="small"
                  circle
                  @click="removeImage(index)"
                >
                  <el-icon><Delete /></el-icon>
                </el-button>
              </div>
            </div>
            <div class="image-info">
              <p class="image-name">{{ image.originalFilename }}</p>
              <p class="image-size">{{ formatFileSize(image.fileSize) }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button 
          type="primary" 
          @click="confirmUpload"
          :disabled="uploadedImages.length === 0"
        >
          确认插入 ({{ uploadedImages.length }})
        </el-button>
      </div>
    </template>

    <!-- 图片预览对话框 -->
    <el-dialog
      v-model="showPreview"
      title="图片预览"
      width="80%"
      class="image-preview-dialog"
    >
      <div class="preview-container">
        <img :src="previewImageUrl" :alt="previewImageAlt" />
      </div>
    </el-dialog>

    <!-- 图片信息编辑对话框 -->
    <el-dialog
      v-model="showEditInfo"
      title="编辑图片信息"
      width="400px"
    >
      <el-form :model="editingImage" label-width="80px">
        <el-form-item label="描述文字">
          <el-input
            v-model="editingImage.altText"
            placeholder="请输入图片描述"
          />
        </el-form-item>
        <el-form-item label="文件名">
          <el-input
            v-model="editingImage.originalFilename"
            placeholder="请输入文件名"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditInfo = false">取消</el-button>
        <el-button type="primary" @click="saveImageInfo">保存</el-button>
      </template>
    </el-dialog>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { UploadFilled, ZoomIn, Edit, Delete } from '@element-plus/icons-vue'

const props = defineProps({
  modelValue: Boolean,
  noteId: {
    type: Number,
    required: true
  },
  maxSize: {
    type: Number,
    default: 10 // MB
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
const uploadedImages = ref([])
const uploading = ref(false)
const uploadProgress = ref(0)
const progressStatus = ref('')
const progressText = ref('')

// 预览相关
const showPreview = ref(false)
const previewImageUrl = ref('')
const previewImageAlt = ref('')

// 编辑信息相关
const showEditInfo = ref(false)
const editingImage = ref({})
const editingIndex = ref(-1)

// 上传配置
const uploadUrl = computed(() => '/api/files/upload/image')
const uploadData = computed(() => ({
  noteId: props.noteId
}))
const uploadHeaders = computed(() => ({
  // 如果需要认证头，在这里添加
}))

// 方法
const beforeUpload = (file) => {
  // 检查文件类型
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }

  // 检查文件大小
  const isLtMaxSize = file.size / 1024 / 1024 < props.maxSize
  if (!isLtMaxSize) {
    ElMessage.error(`图片大小不能超过 ${props.maxSize}MB!`)
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
    uploadedImages.value.push(response.data)
    ElMessage.success('图片上传成功!')
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
  ElMessage.error('图片上传失败!')
}

const previewImage = (image) => {
  previewImageUrl.value = image.filePath
  previewImageAlt.value = image.altText || image.originalFilename
  showPreview.value = true
}

const editImageInfo = (image) => {
  editingImage.value = { ...image }
  editingIndex.value = uploadedImages.value.findIndex(img => img.id === image.id)
  showEditInfo.value = true
}

const saveImageInfo = () => {
  if (editingIndex.value >= 0) {
    uploadedImages.value[editingIndex.value] = { ...editingImage.value }
  }
  showEditInfo.value = false
}

const removeImage = (index) => {
  uploadedImages.value.splice(index, 1)
}

const confirmUpload = () => {
  uploadedImages.value.forEach(image => {
    emit('uploaded', image)
  })
  visible.value = false
  uploadedImages.value = []
  fileList.value = []
}

const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 监听器
watch(() => props.modelValue, (val) => {
  visible.value = val
  if (val) {
    // 重置状态
    uploadedImages.value = []
    fileList.value = []
    uploading.value = false
    uploadProgress.value = 0
  }
})

watch(visible, (val) => {
  emit('update:modelValue', val)
})
</script>

<style scoped>
.image-upload-dialog :deep(.el-dialog__body) {
  padding: 20px;
}

.upload-container {
  min-height: 200px;
}

.upload-dragger {
  margin-bottom: 20px;
}

.upload-dragger :deep(.el-upload-dragger) {
  border: 2px dashed var(--el-border-color);
  border-radius: 8px;
  padding: 40px;
  text-align: center;
  background: var(--bg-color-secondary);
  transition: all 0.3s;
}

.upload-dragger :deep(.el-upload-dragger:hover) {
  border-color: var(--el-color-primary);
  background: var(--el-color-primary-light-9);
}

.upload-dragger :deep(.el-icon--upload) {
  font-size: 48px;
  color: var(--el-color-primary);
  margin-bottom: 16px;
}

.upload-dragger :deep(.el-upload__text) {
  color: var(--text-color-regular);
  font-size: 16px;
  margin-bottom: 8px;
}

.upload-dragger :deep(.el-upload__text em) {
  color: var(--el-color-primary);
}

.upload-dragger :deep(.el-upload__tip) {
  color: var(--text-color-secondary);
  font-size: 12px;
}

.upload-progress {
  margin: 20px 0;
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

.uploaded-images {
  margin-top: 20px;
}

.uploaded-images h4 {
  margin: 0 0 16px 0;
  color: var(--text-color-primary);
  font-size: 16px;
}

.image-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 16px;
}

.image-item {
  border: 1px solid var(--border-color);
  border-radius: 8px;
  overflow: hidden;
  background: var(--bg-color-primary);
}

.image-preview {
  position: relative;
  width: 100%;
  height: 120px;
  overflow: hidden;
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  opacity: 0;
  transition: opacity 0.2s;
}

.image-preview:hover .image-overlay {
  opacity: 1;
}

.image-info {
  padding: 8px;
}

.image-name {
  margin: 0 0 4px 0;
  font-size: 12px;
  color: var(--text-color-primary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.image-size {
  margin: 0;
  font-size: 11px;
  color: var(--text-color-secondary);
}

.image-preview-dialog :deep(.el-dialog__body) {
  padding: 0;
  text-align: center;
}

.preview-container {
  background: #000;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 400px;
}

.preview-container img {
  max-width: 100%;
  max-height: 70vh;
  object-fit: contain;
}

.dialog-footer {
  text-align: right;
}
</style>