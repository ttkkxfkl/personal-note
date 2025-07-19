<template>
  <div class="rich-editor-container">
    <!-- 工具栏 -->
    <div class="editor-toolbar">
      <el-button-group size="small">
        <el-button @click="toggleMode">
          <el-icon><Edit /></el-icon>
          {{ isRichMode ? 'Markdown' : '富文本' }}
        </el-button>
        <el-button @click="insertTemplate" v-if="showTemplateBtn">
          <el-icon><Document /></el-icon>
          模板
        </el-button>
        <el-button @click="showUploadDialog = true">
          <el-icon><Picture /></el-icon>
          图片
        </el-button>
        <el-button @click="showAttachDialog = true">
          <el-icon><Paperclip /></el-icon>
          附件
        </el-button>
      </el-button-group>
      
      <div class="editor-info">
        <span class="word-count">{{ wordCount }} 字</span>
        <span class="reading-time">预计阅读 {{ readingTime }} 分钟</span>
      </div>
    </div>

    <!-- 富文本编辑器 -->
    <div v-show="isRichMode" class="rich-editor">
      <div ref="editorRef" class="editor-content"></div>
    </div>

    <!-- Markdown编辑器 -->
    <div v-show="!isRichMode" class="markdown-editor">
      <div class="editor-wrapper">
        <div class="editor-panel">
          <el-input
            v-model="markdownContent"
            type="textarea"
            :rows="20"
            placeholder="请输入Markdown内容..."
            @input="handleMarkdownChange"
          />
        </div>
        <div class="preview-panel" v-if="showPreview">
          <div class="markdown-preview" v-html="renderedMarkdown"></div>
        </div>
      </div>
      
      <div class="markdown-toolbar">
        <el-button-group size="small">
          <el-button @click="showPreview = !showPreview">
            <el-icon><View /></el-icon>
            {{ showPreview ? '隐藏预览' : '显示预览' }}
          </el-button>
          <el-button @click="insertMarkdownSyntax('**', '**')">
            <strong>B</strong>
          </el-button>
          <el-button @click="insertMarkdownSyntax('*', '*')">
            <em>I</em>
          </el-button>
          <el-button @click="insertMarkdownSyntax('`', '`')">
            Code
          </el-button>
          <el-button @click="insertMarkdownSyntax('# ', '')">
            H1
          </el-button>
          <el-button @click="insertMarkdownSyntax('- ', '')">
            List
          </el-button>
          <el-button @click="insertMarkdownSyntax('[', '](url)')">
            Link
          </el-button>
        </el-button-group>
      </div>
    </div>

    <!-- 模板选择对话框 -->
    <TemplateDialog 
      v-model="showTemplateDialog"
      @select="handleTemplateSelect"
    />

    <!-- 图片上传对话框 -->
    <ImageUploadDialog
      v-model="showUploadDialog"
      :note-id="noteId"
      @uploaded="handleImageUploaded"
    />

    <!-- 附件上传对话框 -->
    <AttachmentDialog
      v-model="showAttachDialog"
      :note-id="noteId"
      @uploaded="handleAttachmentUploaded"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch, computed, nextTick } from 'vue'
import { createEditor, createToolbar } from '@wangeditor/editor'
import '@wangeditor/editor/dist/css/style.css'
import MarkdownIt from 'markdown-it'
import hljs from 'highlight.js'
import 'highlight.js/styles/github.css'
import { debounce } from 'lodash-es'
import TemplateDialog from './TemplateDialog.vue'
import ImageUploadDialog from './ImageUploadDialog.vue'
import AttachmentDialog from './AttachmentDialog.vue'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  markdownValue: {
    type: String,
    default: ''
  },
  noteId: {
    type: Number,
    default: null
  },
  mode: {
    type: String,
    default: 'markdown' // 'markdown' | 'rich'
  },
  showTemplateBtn: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits(['update:modelValue', 'update:markdownValue', 'change'])

// 编辑器相关
const editorRef = ref()
const editor = ref(null)
const toolbar = ref(null)
const isRichMode = ref(props.mode === 'rich')
const markdownContent = ref(props.markdownValue || props.modelValue)
const showPreview = ref(true)

// 对话框状态
const showTemplateDialog = ref(false)
const showUploadDialog = ref(false)
const showAttachDialog = ref(false)

// Markdown渲染器
const md = new MarkdownIt({
  highlight: function (str, lang) {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return hljs.highlight(str, { language: lang }).value
      } catch (__) {}
    }
    return ''
  }
})

// 计算属性
const renderedMarkdown = computed(() => {
  return md.render(markdownContent.value)
})

const wordCount = computed(() => {
  const text = isRichMode.value ? 
    (editor.value?.getText() || '') : 
    markdownContent.value.replace(/[#*`\[\]()_~]/g, '')
  return text.length
})

const readingTime = computed(() => {
  return Math.ceil(wordCount.value / 200) // 假设每分钟阅读200字
})

// 方法
const toggleMode = () => {
  isRichMode.value = !isRichMode.value
  nextTick(() => {
    if (isRichMode.value) {
      initRichEditor()
    } else {
      destroyRichEditor()
    }
  })
}

const initRichEditor = () => {
  if (!editorRef.value || editor.value) return

  const editorConfig = {
    placeholder: '请输入内容...',
    MENU_CONF: {
      uploadImage: {
        server: '/api/files/upload/image',
        fieldName: 'file',
        meta: {
          noteId: props.noteId
        },
        onSuccess: (file, res) => {
          console.log('图片上传成功', res)
        },
        onError: (file, err, res) => {
          console.error('图片上传失败', err)
        }
      }
    }
  }

  editor.value = createEditor({
    selector: editorRef.value,
    config: editorConfig,
    content: markdownToHtml(markdownContent.value)
  })

  // 监听内容变化
  editor.value.on('change', debounce(() => {
    const html = editor.value.getHtml()
    emit('update:modelValue', html)
    emit('change', {
      html,
      markdown: markdownContent.value,
      wordCount: wordCount.value,
      readingTime: readingTime.value
    })
  }, 300))
}

const destroyRichEditor = () => {
  if (editor.value) {
    editor.value.destroy()
    editor.value = null
  }
  if (toolbar.value) {
    toolbar.value.destroy()
    toolbar.value = null
  }
}

const markdownToHtml = (markdown) => {
  return md.render(markdown)
}

const handleMarkdownChange = debounce(() => {
  emit('update:markdownValue', markdownContent.value)
  emit('update:modelValue', markdownToHtml(markdownContent.value))
  emit('change', {
    html: markdownToHtml(markdownContent.value),
    markdown: markdownContent.value,
    wordCount: wordCount.value,
    readingTime: readingTime.value
  })
}, 300)

const insertMarkdownSyntax = (before, after) => {
  const textarea = document.querySelector('.markdown-editor textarea')
  if (!textarea) return

  const start = textarea.selectionStart
  const end = textarea.selectionEnd
  const selectedText = markdownContent.value.substring(start, end)
  
  const newText = markdownContent.value.substring(0, start) + 
                  before + selectedText + after + 
                  markdownContent.value.substring(end)
  
  markdownContent.value = newText
  
  nextTick(() => {
    textarea.focus()
    textarea.setSelectionRange(start + before.length, start + before.length + selectedText.length)
  })
}

const insertTemplate = () => {
  showTemplateDialog.value = true
}

const handleTemplateSelect = (template) => {
  if (isRichMode.value) {
    editor.value?.setHtml(template.contentTemplate || template.markdownTemplate)
  } else {
    markdownContent.value = template.markdownTemplate || template.contentTemplate
  }
  showTemplateDialog.value = false
}

const handleImageUploaded = (imageInfo) => {
  if (isRichMode.value) {
    editor.value?.insertNode({
      type: 'image',
      src: imageInfo.filePath,
      alt: imageInfo.altText || imageInfo.originalFilename
    })
  } else {
    const imageMarkdown = `![${imageInfo.altText || imageInfo.originalFilename}](${imageInfo.filePath})`
    markdownContent.value += '\n' + imageMarkdown
  }
}

const handleAttachmentUploaded = (attachment) => {
  if (isRichMode.value) {
    const linkHtml = `<a href="${attachment.filePath}" download="${attachment.originalFilename}">${attachment.originalFilename}</a>`
    editor.value?.insertNode({
      type: 'paragraph',
      children: [{ text: linkHtml }]
    })
  } else {
    const linkMarkdown = `[${attachment.originalFilename}](${attachment.filePath})`
    markdownContent.value += '\n' + linkMarkdown
  }
}

// 监听器
watch(() => props.modelValue, (newVal) => {
  if (isRichMode.value && editor.value && newVal !== editor.value.getHtml()) {
    editor.value.setHtml(newVal)
  }
})

watch(() => props.markdownValue, (newVal) => {
  if (newVal !== markdownContent.value) {
    markdownContent.value = newVal
  }
})

// 生命周期
onMounted(() => {
  if (isRichMode.value) {
    nextTick(() => {
      initRichEditor()
    })
  }
})

onBeforeUnmount(() => {
  destroyRichEditor()
})
</script>

<style scoped>
.rich-editor-container {
  border: 1px solid var(--border-color);
  border-radius: 8px;
  overflow: hidden;
}

.editor-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: var(--bg-color-secondary);
  border-bottom: 1px solid var(--border-color);
}

.editor-info {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: var(--text-color-secondary);
}

.rich-editor {
  min-height: 400px;
}

.editor-content {
  min-height: 400px;
  background: var(--bg-color-primary);
}

.markdown-editor {
  background: var(--bg-color-primary);
}

.editor-wrapper {
  display: flex;
  min-height: 400px;
}

.editor-panel {
  flex: 1;
  border-right: 1px solid var(--border-color);
}

.preview-panel {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
  background: var(--bg-color-secondary);
}

.markdown-preview {
  line-height: 1.6;
}

.markdown-preview h1,
.markdown-preview h2,
.markdown-preview h3,
.markdown-preview h4,
.markdown-preview h5,
.markdown-preview h6 {
  margin: 16px 0 8px 0;
  color: var(--text-color-primary);
}

.markdown-preview p {
  margin: 8px 0;
  color: var(--text-color-regular);
}

.markdown-preview code {
  background: var(--bg-color-secondary);
  padding: 2px 4px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
}

.markdown-preview pre {
  background: var(--bg-color-secondary);
  padding: 12px;
  border-radius: 6px;
  overflow-x: auto;
}

.markdown-preview blockquote {
  border-left: 4px solid var(--el-color-primary);
  margin: 16px 0;
  padding: 8px 16px;
  background: var(--bg-color-secondary);
}

.markdown-toolbar {
  padding: 12px 16px;
  background: var(--bg-color-secondary);
  border-top: 1px solid var(--border-color);
}

:deep(.el-textarea__inner) {
  border: none;
  border-radius: 0;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 14px;
  line-height: 1.6;
}
</style>