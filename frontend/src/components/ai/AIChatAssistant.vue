<template>
  <div class="ai-chat-assistant">
    <el-card class="chat-panel" shadow="hover">
      <template #header>
        <div class="chat-header">
          <el-icon class="chat-icon"><ChatDotRound /></el-icon>
          <span class="chat-title">AI智能助手</span>
          <div class="chat-actions">
            <el-button 
              type="text" 
              @click="clearChat"
              icon="Delete"
              title="清空对话"
            />
            <el-button 
              type="text" 
              @click="togglePanel"
              :icon="panelVisible ? 'ArrowDown' : 'ArrowUp'"
              title="收起/展开"
            />
          </div>
        </div>
      </template>

      <div v-show="panelVisible" class="chat-content">
        <!-- 聊天消息区域 -->
        <div class="chat-messages" ref="messagesContainer">
          <div v-if="chatHistory.length === 0" class="welcome-message">
            <div class="welcome-card">
              <el-icon class="welcome-icon"><Robot /></el-icon>
              <h3>你好！我是AI助手</h3>
              <p>我可以帮助您：</p>
              <ul class="feature-list">
                <li>📝 协助写作和编辑</li>
                <li>💡 回答问题和提供建议</li>
                <li>🔍 分析和总结内容</li>
                <li>🌐 翻译多种语言</li>
                <li>📊 数据分析和解释</li>
              </ul>
              <div class="quick-actions">
                <el-button 
                  v-for="action in quickActions" 
                  :key="action.text"
                  size="small"
                  type="primary"
                  plain
                  @click="sendQuickMessage(action.message)"
                >
                  {{ action.text }}
                </el-button>
              </div>
            </div>
          </div>

          <div 
            v-for="(message, index) in chatHistory" 
            :key="index"
            class="message-wrapper"
            :class="{ 'user-message': message.role === 'user', 'ai-message': message.role === 'assistant' }"
          >
            <div class="message-avatar">
              <el-avatar 
                :size="32"
                :icon="message.role === 'user' ? UserFilled : Robot"
                :style="{ backgroundColor: message.role === 'user' ? '#409EFF' : '#67C23A' }"
              />
            </div>
            <div class="message-content">
              <div class="message-header">
                <span class="message-role">{{ message.role === 'user' ? '您' : 'AI助手' }}</span>
                <span class="message-time">{{ formatTime(message.timestamp) }}</span>
              </div>
              <div class="message-text" v-html="formatMessage(message.content)"></div>
              <div v-if="message.role === 'assistant' && message.suggestedQuestions" class="suggested-questions">
                <p class="suggestions-title">相关问题：</p>
                <div class="suggestion-buttons">
                  <el-button
                    v-for="question in message.suggestedQuestions"
                    :key="question"
                    size="small"
                    type="info"
                    plain
                    @click="sendMessage(question)"
                  >
                    {{ question }}
                  </el-button>
                </div>
              </div>
            </div>
            <div class="message-actions">
              <el-button
                type="text"
                size="small"
                @click="copyMessage(message.content)"
                icon="CopyDocument"
                title="复制"
              />
              <el-button
                v-if="message.role === 'assistant'"
                type="text"
                size="small"
                @click="insertToEditor(message.content)"
                icon="Plus"
                title="插入编辑器"
              />
            </div>
          </div>

          <!-- AI思考中状态 -->
          <div v-if="isThinking" class="message-wrapper ai-message thinking">
            <div class="message-avatar">
              <el-avatar 
                :size="32"
                :icon="Robot"
                style="backgroundColor: #67C23A"
              />
            </div>
            <div class="message-content">
              <div class="message-header">
                <span class="message-role">AI助手</span>
                <span class="message-time">正在思考...</span>
              </div>
              <div class="thinking-animation">
                <div class="thinking-dots">
                  <span></span>
                  <span></span>
                  <span></span>
                </div>
                <span class="thinking-text">正在为您分析和组织答案...</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 输入区域 -->
        <div class="chat-input-area">
          <div class="input-tools">
            <el-popover
              placement="top"
              :width="300"
              trigger="click"
            >
              <template #reference>
                <el-button type="text" icon="Paperclip" title="文件上传" />
              </template>
              <div class="file-upload-options">
                <el-upload
                  :show-file-list="false"
                  :before-upload="handleFileUpload"
                  accept="image/*,.pdf,.txt,.doc,.docx"
                >
                  <el-button type="primary" icon="Picture">上传图片</el-button>
                </el-upload>
                <el-upload
                  :show-file-list="false"
                  :before-upload="handleAudioUpload"
                  accept="audio/*"
                >
                  <el-button type="success" icon="Microphone">语音转录</el-button>
                </el-upload>
              </div>
            </el-popover>

            <el-button 
              type="text" 
              icon="MagicStick" 
              @click="showPromptLibrary = true"
              title="提示词库"
            />
          </div>

          <div class="input-container">
            <el-input
              v-model="inputMessage"
              type="textarea"
              :rows="inputRows"
              placeholder="输入您的问题或需求..."
              resize="none"
              :maxlength="2000"
              show-word-limit
              @keydown="handleKeyDown"
              @input="adjustTextareaHeight"
            />
            <div class="input-actions">
              <el-button 
                type="primary" 
                @click="sendMessage"
                :loading="isThinking"
                :disabled="!inputMessage.trim()"
                icon="Promotion"
              >
                发送
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 提示词库对话框 -->
    <el-dialog
      v-model="showPromptLibrary"
      title="AI提示词库"
      width="600px"
      class="prompt-library-dialog"
    >
      <div class="prompt-library">
        <el-tabs v-model="activePromptCategory">
          <el-tab-pane
            v-for="category in promptCategories"
            :key="category.key"
            :label="category.label"
            :name="category.key"
          >
            <div class="prompt-list">
              <div 
                v-for="prompt in category.prompts"
                :key="prompt.title"
                class="prompt-item"
                @click="usePrompt(prompt)"
              >
                <h4>{{ prompt.title }}</h4>
                <p>{{ prompt.description }}</p>
                <div class="prompt-preview">{{ prompt.template }}</div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, nextTick, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { ChatDotRound, Robot, UserFilled } from '@element-plus/icons-vue'
import { aiApi } from '@/api/ai'

const emit = defineEmits(['insert-content'])

// 响应式数据
const panelVisible = ref(true)
const chatHistory = ref([])
const inputMessage = ref('')
const inputRows = ref(2)
const isThinking = ref(false)
const messagesContainer = ref()
const showPromptLibrary = ref(false)
const activePromptCategory = ref('writing')

// 快速操作
const quickActions = [
  { text: '帮我写作', message: '我需要帮助写一篇文章，请问您可以协助我吗？' },
  { text: '总结内容', message: '请帮我总结一下文档的主要内容' },
  { text: '翻译文本', message: '请帮我翻译以下内容' },
  { text: '数据分析', message: '请帮我分析这些数据的趋势和特点' }
]

// 提示词库
const promptCategories = [
  {
    key: 'writing',
    label: '写作助手',
    prompts: [
      {
        title: '文章大纲生成',
        description: '为指定主题生成详细的文章大纲',
        template: '请为"{topic}"这个主题生成一个详细的文章大纲，包括引言、主体段落和结论。'
      },
      {
        title: '内容扩写',
        description: '将简短的要点扩展成完整段落',
        template: '请将以下要点扩展成完整的段落：{points}'
      },
      {
        title: '风格改写',
        description: '将内容改写为指定风格',
        template: '请将以下内容改写为{style}风格：{content}'
      }
    ]
  },
  {
    key: 'analysis',
    label: '分析总结',
    prompts: [
      {
        title: '内容摘要',
        description: '生成内容的简洁摘要',
        template: '请为以下内容生成一个简洁的摘要（不超过{words}字）：{content}'
      },
      {
        title: '关键信息提取',
        description: '提取文本中的关键信息',
        template: '请从以下文本中提取关键信息和要点：{content}'
      },
      {
        title: '数据解读',
        description: '解释数据的含义和趋势',
        template: '请分析以下数据并解释其含义和趋势：{data}'
      }
    ]
  },
  {
    key: 'translation',
    label: '翻译润色',
    prompts: [
      {
        title: '多语言翻译',
        description: '翻译为指定语言',
        template: '请将以下{source_lang}文本翻译为{target_lang}：{text}'
      },
      {
        title: '语言润色',
        description: '改善文本的语言表达',
        template: '请润色以下文本，使其更加流畅和准确：{text}'
      }
    ]
  },
  {
    key: 'creative',
    label: '创意助手',
    prompts: [
      {
        title: '创意构思',
        description: '为项目提供创意想法',
        template: '请为"{project}"项目提供5个创新的想法和建议。'
      },
      {
        title: '问题解决',
        description: '分析问题并提供解决方案',
        template: '请分析以下问题并提供可行的解决方案：{problem}'
      }
    ]
  }
]

// 方法
const togglePanel = () => {
  panelVisible.value = !panelVisible.value
}

const sendMessage = async (message = null) => {
  const messageToSend = message || inputMessage.value.trim()
  if (!messageToSend) return

  // 添加用户消息
  const userMessage = {
    role: 'user',
    content: messageToSend,
    timestamp: new Date()
  }
  chatHistory.value.push(userMessage)
  
  // 清空输入
  if (!message) {
    inputMessage.value = ''
    inputRows.value = 2
  }

  // 滚动到底部
  await nextTick()
  scrollToBottom()

  // 显示AI思考状态
  isThinking.value = true

  try {
    // 调用AI接口
    const response = await aiApi.chat(messageToSend, chatHistory.value.slice(0, -1))
    
    if (response.success) {
      const aiMessage = {
        role: 'assistant',
        content: response.data.response,
        timestamp: new Date(),
        suggestedQuestions: response.data.suggestedQuestions,
        confidence: response.data.confidence
      }
      chatHistory.value.push(aiMessage)
    } else {
      // 错误处理
      const errorMessage = {
        role: 'assistant',
        content: '抱歉，我现在无法处理您的请求。请稍后再试。',
        timestamp: new Date()
      }
      chatHistory.value.push(errorMessage)
      ElMessage.error(response.message || '请求失败')
    }
  } catch (error) {
    const errorMessage = {
      role: 'assistant',
      content: '抱歉，遇到了技术问题。请稍后再试。',
      timestamp: new Date()
    }
    chatHistory.value.push(errorMessage)
    ElMessage.error('网络错误：' + error.message)
  } finally {
    isThinking.value = false
    await nextTick()
    scrollToBottom()
  }
}

const sendQuickMessage = (message) => {
  sendMessage(message)
}

const clearChat = () => {
  chatHistory.value = []
  ElMessage.success('对话已清空')
}

const handleKeyDown = (event) => {
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault()
    sendMessage()
  }
}

const adjustTextareaHeight = () => {
  const lines = inputMessage.value.split('\n').length
  inputRows.value = Math.min(Math.max(lines, 2), 6)
}

const scrollToBottom = () => {
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

const formatTime = (timestamp) => {
  return new Date(timestamp).toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit'
  })
}

const formatMessage = (content) => {
  // 简单的Markdown格式支持
  return content
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/\*(.*?)\*/g, '<em>$1</em>')
    .replace(/`(.*?)`/g, '<code>$1</code>')
    .replace(/\n/g, '<br>')
}

const copyMessage = async (content) => {
  try {
    await navigator.clipboard.writeText(content)
    ElMessage.success('已复制到剪贴板')
  } catch (error) {
    ElMessage.error('复制失败')
  }
}

const insertToEditor = (content) => {
  emit('insert-content', content)
  ElMessage.success('已插入编辑器')
}

const handleFileUpload = async (file) => {
  try {
    let response
    if (file.type.startsWith('image/')) {
      // 图片描述
      response = await aiApi.describeImage(file)
      if (response.success) {
        sendMessage(`请帮我分析这张图片：${response.data.description}`)
      }
    } else {
      // OCR文字提取
      response = await aiApi.extractTextFromImage(file)
      if (response.success) {
        sendMessage(`请帮我分析以下文本内容：\n${response.data.extractedText}`)
      }
    }
  } catch (error) {
    ElMessage.error('文件处理失败：' + error.message)
  }
  return false // 阻止默认上传
}

const handleAudioUpload = async (file) => {
  try {
    const response = await aiApi.transcribeAudio(file)
    if (response.success) {
      sendMessage(response.data.text)
    }
  } catch (error) {
    ElMessage.error('语音转录失败：' + error.message)
  }
  return false // 阻止默认上传
}

const usePrompt = (prompt) => {
  inputMessage.value = prompt.template
  showPromptLibrary.value = false
  adjustTextareaHeight()
}

// 生命周期
onMounted(() => {
  // 可以在这里加载历史对话记录
})
</script>

<style scoped>
.ai-chat-assistant {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.chat-panel {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.chat-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.chat-icon {
  color: var(--el-color-primary);
  margin-right: 8px;
}

.chat-title {
  font-weight: 600;
  color: var(--text-color-primary);
}

.chat-actions {
  display: flex;
  gap: 4px;
}

.chat-content {
  height: 500px;
  display: flex;
  flex-direction: column;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px 0;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.welcome-message {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.welcome-card {
  text-align: center;
  padding: 32px;
  max-width: 400px;
}

.welcome-icon {
  font-size: 48px;
  color: var(--el-color-success);
  margin-bottom: 16px;
}

.welcome-card h3 {
  margin: 0 0 12px 0;
  color: var(--text-color-primary);
}

.welcome-card p {
  margin: 0 0 16px 0;
  color: var(--text-color-regular);
}

.feature-list {
  text-align: left;
  margin: 0 0 24px 0;
  padding-left: 20px;
  color: var(--text-color-regular);
}

.feature-list li {
  margin-bottom: 8px;
}

.quick-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  justify-content: center;
}

.message-wrapper {
  display: flex;
  gap: 12px;
  animation: fadeInUp 0.3s ease-out;
}

.message-wrapper.user-message {
  flex-direction: row-reverse;
}

.message-wrapper.user-message .message-content {
  background: var(--el-color-primary-light-9);
  border: 1px solid var(--el-color-primary-light-7);
}

.message-wrapper.ai-message .message-content {
  background: var(--bg-color-secondary);
  border: 1px solid var(--border-color);
}

.message-avatar {
  flex-shrink: 0;
}

.message-content {
  flex: 1;
  padding: 12px 16px;
  border-radius: 12px;
  min-width: 0;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.message-role {
  font-size: 12px;
  font-weight: 600;
  color: var(--text-color-primary);
}

.message-time {
  font-size: 11px;
  color: var(--text-color-placeholder);
}

.message-text {
  line-height: 1.6;
  color: var(--text-color-primary);
  word-wrap: break-word;
}

.message-text :deep(code) {
  background: var(--bg-color-primary);
  padding: 2px 4px;
  border-radius: 3px;
  font-family: 'Monaco', 'Menlo', monospace;
  font-size: 0.9em;
}

.suggested-questions {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid var(--border-color-lighter);
}

.suggestions-title {
  margin: 0 0 8px 0;
  font-size: 12px;
  color: var(--text-color-secondary);
}

.suggestion-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.message-actions {
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
  opacity: 0;
  transition: opacity 0.2s;
}

.message-wrapper:hover .message-actions {
  opacity: 1;
}

.thinking {
  opacity: 0.8;
}

.thinking-animation {
  display: flex;
  align-items: center;
  gap: 12px;
}

.thinking-dots {
  display: flex;
  gap: 4px;
}

.thinking-dots span {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--el-color-primary);
  animation: thinking 1.5s infinite ease-in-out;
}

.thinking-dots span:nth-child(2) {
  animation-delay: 0.2s;
}

.thinking-dots span:nth-child(3) {
  animation-delay: 0.4s;
}

.thinking-text {
  font-size: 14px;
  color: var(--text-color-secondary);
}

.chat-input-area {
  border-top: 1px solid var(--border-color);
  padding: 16px 0 0 0;
}

.input-tools {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.input-container {
  display: flex;
  gap: 12px;
  align-items: flex-end;
}

.input-container .el-textarea {
  flex: 1;
}

.input-actions {
  flex-shrink: 0;
}

.file-upload-options {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.prompt-library {
  max-height: 500px;
  overflow-y: auto;
}

.prompt-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.prompt-item {
  padding: 16px;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
}

.prompt-item:hover {
  border-color: var(--el-color-primary);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.prompt-item h4 {
  margin: 0 0 8px 0;
  color: var(--text-color-primary);
  font-size: 16px;
}

.prompt-item p {
  margin: 0 0 12px 0;
  color: var(--text-color-regular);
  font-size: 14px;
}

.prompt-preview {
  background: var(--bg-color-secondary);
  padding: 12px;
  border-radius: 6px;
  font-size: 13px;
  color: var(--text-color-secondary);
  border-left: 3px solid var(--el-color-primary);
}

/* 动画 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes thinking {
  0%, 60%, 100% {
    transform: scale(0.8);
    opacity: 0.5;
  }
  30% {
    transform: scale(1.2);
    opacity: 1;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .chat-content {
    height: 400px;
  }
  
  .input-container {
    flex-direction: column;
    gap: 8px;
  }
  
  .input-actions {
    align-self: flex-end;
  }
  
  .quick-actions {
    flex-direction: column;
  }
  
  .quick-actions .el-button {
    width: 100%;
  }
  
  .suggestion-buttons {
    flex-direction: column;
  }
  
  .suggestion-buttons .el-button {
    width: 100%;
    justify-content: flex-start;
  }
}
</style>