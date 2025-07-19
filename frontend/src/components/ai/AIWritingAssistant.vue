<template>
  <div class="ai-writing-assistant">
    <!-- AI写作助手面板 -->
    <el-card class="ai-panel" shadow="hover">
      <template #header>
        <div class="panel-header">
          <el-icon class="ai-icon"><Magic /></el-icon>
          <span class="panel-title">AI写作助手</span>
          <el-button 
            type="text" 
            @click="togglePanel"
            class="toggle-btn"
          >
            <el-icon>
              <component :is="panelVisible ? 'ArrowDown' : 'ArrowUp'" />
            </el-icon>
          </el-button>
        </div>
      </template>

      <div v-show="panelVisible" class="panel-content">
        <el-tabs v-model="activeTab" class="ai-tabs">
          <!-- 内容生成 -->
          <el-tab-pane label="内容生成" name="generate">
            <el-form :model="generateForm" :rules="generateRules" ref="generateFormRef" label-width="80px">
              <el-form-item label="主题" prop="topic">
                <el-input
                  v-model="generateForm.topic"
                  placeholder="请输入要生成内容的主题"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
              
              <el-row :gutter="16">
                <el-col :span="12">
                  <el-form-item label="风格">
                    <el-select v-model="generateForm.style" placeholder="选择写作风格">
                      <el-option label="正式" value="formal" />
                      <el-option label="随意" value="casual" />
                      <el-option label="学术" value="academic" />
                      <el-option label="创意" value="creative" />
                      <el-option label="商务" value="business" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="类型">
                    <el-select v-model="generateForm.type" placeholder="选择文档类型">
                      <el-option label="文章" value="article" />
                      <el-option label="邮件" value="email" />
                      <el-option label="报告" value="report" />
                      <el-option label="摘要" value="summary" />
                      <el-option label="大纲" value="outline" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="16">
                <el-col :span="12">
                  <el-form-item label="长度">
                    <el-radio-group v-model="generateForm.length">
                      <el-radio-button :label="1">短</el-radio-button>
                      <el-radio-button :label="2">中</el-radio-button>
                      <el-radio-button :label="3">长</el-radio-button>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="语言">
                    <el-select v-model="generateForm.language">
                      <el-option label="中文" value="zh-CN" />
                      <el-option label="English" value="en-US" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-form-item label="上下文">
                <el-input
                  v-model="generateForm.context"
                  type="textarea"
                  :rows="3"
                  placeholder="提供额外的背景信息或上下文（可选）"
                  maxlength="500"
                  show-word-limit
                />
              </el-form-item>

              <el-form-item label="特殊要求">
                <el-input
                  v-model="generateForm.requirements"
                  placeholder="如：包含具体数据、引用案例等"
                  maxlength="300"
                  show-word-limit
                />
              </el-form-item>

              <el-form-item>
                <el-button 
                  type="primary" 
                  @click="generateContent"
                  :loading="loading.generate"
                  icon="Magic"
                >
                  生成内容
                </el-button>
                <el-button @click="resetGenerateForm">重置</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>

          <!-- 内容续写 -->
          <el-tab-pane label="内容续写" name="continue">
            <div class="continue-section">
              <el-form :model="continueForm" label-width="80px">
                <el-form-item label="已有内容">
                  <el-input
                    v-model="continueForm.content"
                    type="textarea"
                    :rows="6"
                    placeholder="粘贴您已有的内容，AI将为您续写"
                    maxlength="2000"
                    show-word-limit
                  />
                </el-form-item>

                <el-form-item label="续写方向">
                  <el-input
                    v-model="continueForm.context"
                    placeholder="描述您希望续写的方向或重点（可选）"
                    maxlength="200"
                  />
                </el-form-item>

                <el-form-item>
                  <el-button 
                    type="primary" 
                    @click="continueWriting"
                    :loading="loading.continue"
                    icon="Edit"
                  >
                    AI续写
                  </el-button>
                </el-form-item>
              </el-form>
            </div>
          </el-tab-pane>

          <!-- 内容改写 -->
          <el-tab-pane label="内容改写" name="rewrite">
            <div class="rewrite-section">
              <el-form :model="rewriteForm" label-width="80px">
                <el-form-item label="原始内容">
                  <el-input
                    v-model="rewriteForm.content"
                    type="textarea"
                    :rows="6"
                    placeholder="粘贴需要改写的内容"
                    maxlength="2000"
                    show-word-limit
                  />
                </el-form-item>

                <el-form-item label="改写风格">
                  <el-select v-model="rewriteForm.style" placeholder="选择目标风格">
                    <el-option label="更正式" value="formal" />
                    <el-option label="更随意" value="casual" />
                    <el-option label="更简洁" value="concise" />
                    <el-option label="更详细" value="detailed" />
                    <el-option label="更专业" value="professional" />
                    <el-option label="更生动" value="vivid" />
                  </el-select>
                </el-form-item>

                <el-form-item>
                  <el-button 
                    type="primary" 
                    @click="rewriteContent"
                    :loading="loading.rewrite"
                    icon="Refresh"
                  >
                    AI改写
                  </el-button>
                </el-form-item>
              </el-form>
            </div>
          </el-tab-pane>

          <!-- 快速工具 -->
          <el-tab-pane label="快速工具" name="tools">
            <div class="tools-section">
              <el-form :model="toolsForm" label-width="80px">
                <el-form-item label="文本内容">
                  <el-input
                    v-model="toolsForm.content"
                    type="textarea"
                    :rows="8"
                    placeholder="粘贴文本内容，选择需要的AI功能"
                    maxlength="3000"
                    show-word-limit
                  />
                </el-form-item>

                <el-form-item>
                  <div class="tool-buttons">
                    <el-button 
                      @click="generateSummary"
                      :loading="loading.summary"
                      icon="Document"
                    >
                      生成摘要
                    </el-button>
                    <el-button 
                      @click="extractKeywords"
                      :loading="loading.keywords"
                      icon="Collection"
                    >
                      提取关键词
                    </el-button>
                    <el-button 
                      @click="analyzeSentiment"
                      :loading="loading.sentiment"
                      icon="Sunny"
                    >
                      情感分析
                    </el-button>
                    <el-button 
                      @click="checkGrammar"
                      :loading="loading.grammar"
                      icon="Select"
                    >
                      语法检查
                    </el-button>
                    <el-button 
                      @click="getWritingSuggestions"
                      :loading="loading.suggestions"
                      icon="ChatDotRound"
                    >
                      写作建议
                    </el-button>
                    <el-button 
                      @click="classifyContent"
                      :loading="loading.classify"
                      icon="FolderOpened"
                    >
                      智能分类
                    </el-button>
                  </div>
                </el-form-item>
              </el-form>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>

    <!-- 结果展示区域 -->
    <el-card v-if="aiResult" class="result-panel" shadow="hover">
      <template #header>
        <div class="result-header">
          <el-icon class="result-icon"><DocumentCopy /></el-icon>
          <span class="result-title">AI处理结果</span>
          <div class="result-actions">
            <el-button 
              type="text" 
              @click="insertResult"
              icon="Plus"
            >
              插入编辑器
            </el-button>
            <el-button 
              type="text" 
              @click="copyResult"
              icon="CopyDocument"
            >
              复制
            </el-button>
            <el-button 
              type="text" 
              @click="clearResult"
              icon="Close"
            >
              清除
            </el-button>
          </div>
        </div>
      </template>

      <div class="result-content">
        <!-- 写作结果 -->
        <div v-if="aiResult.type === 'writing'" class="writing-result">
          <div class="result-meta">
            <el-tag>{{ aiResult.data.style || '默认风格' }}</el-tag>
            <el-tag type="info">{{ aiResult.data.wordCount }} 字</el-tag>
            <el-tag type="success">置信度: {{ (aiResult.data.confidence * 100).toFixed(1) }}%</el-tag>
          </div>
          <div class="result-text">{{ aiResult.data.content }}</div>
          <div v-if="aiResult.data.suggestions" class="suggestions">
            <h4>改进建议：</h4>
            <ul>
              <li v-for="suggestion in aiResult.data.suggestions" :key="suggestion">
                {{ suggestion }}
              </li>
            </ul>
          </div>
        </div>

        <!-- 摘要结果 -->
        <div v-else-if="aiResult.type === 'summary'" class="summary-result">
          <div class="result-meta">
            <el-tag>压缩比: {{ (aiResult.data.compressionRatio * 100).toFixed(1) }}%</el-tag>
            <el-tag type="info">{{ aiResult.data.summaryLength }} 字</el-tag>
          </div>
          <div class="result-text">{{ aiResult.data.summary }}</div>
          <div v-if="aiResult.data.keyPoints" class="key-points">
            <h4>关键要点：</h4>
            <ul>
              <li v-for="point in aiResult.data.keyPoints" :key="point">
                {{ point }}
              </li>
            </ul>
          </div>
        </div>

        <!-- 关键词结果 -->
        <div v-else-if="aiResult.type === 'keywords'" class="keywords-result">
          <div class="keyword-tags">
            <el-tag 
              v-for="keyword in aiResult.data" 
              :key="keyword"
              class="keyword-tag"
            >
              {{ keyword }}
            </el-tag>
          </div>
        </div>

        <!-- 情感分析结果 -->
        <div v-else-if="aiResult.type === 'sentiment'" class="sentiment-result">
          <div class="sentiment-overview">
            <el-tag 
              :type="getSentimentType(aiResult.data.overallSentiment)"
              size="large"
            >
              {{ getSentimentText(aiResult.data.overallSentiment) }}
            </el-tag>
            <span class="confidence">置信度: {{ (aiResult.data.confidence * 100).toFixed(1) }}%</span>
          </div>
          <div class="emotion-details">
            <div class="emotion-scores">
              <div class="score-item">
                <span>积极：</span>
                <el-progress 
                  :percentage="aiResult.data.positiveScore * 100" 
                  color="#67C23A"
                  :show-text="false"
                />
                <span>{{ (aiResult.data.positiveScore * 100).toFixed(1) }}%</span>
              </div>
              <div class="score-item">
                <span>消极：</span>
                <el-progress 
                  :percentage="aiResult.data.negativeScore * 100" 
                  color="#F56C6C"
                  :show-text="false"
                />
                <span>{{ (aiResult.data.negativeScore * 100).toFixed(1) }}%</span>
              </div>
              <div class="score-item">
                <span>中性：</span>
                <el-progress 
                  :percentage="aiResult.data.neutralScore * 100" 
                  color="#909399"
                  :show-text="false"
                />
                <span>{{ (aiResult.data.neutralScore * 100).toFixed(1) }}%</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 其他结果类型 -->
        <div v-else class="generic-result">
          <pre>{{ JSON.stringify(aiResult.data, null, 2) }}</pre>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { aiApi } from '@/api/ai'

const emit = defineEmits(['insert-content'])

// 响应式数据
const panelVisible = ref(true)
const activeTab = ref('generate')
const aiResult = ref(null)

// 加载状态
const loading = reactive({
  generate: false,
  continue: false,
  rewrite: false,
  summary: false,
  keywords: false,
  sentiment: false,
  grammar: false,
  suggestions: false,
  classify: false
})

// 表单数据
const generateForm = reactive({
  topic: '',
  style: 'casual',
  type: 'article',
  length: 2,
  context: '',
  language: 'zh-CN',
  requirements: ''
})

const continueForm = reactive({
  content: '',
  context: ''
})

const rewriteForm = reactive({
  content: '',
  style: 'formal'
})

const toolsForm = reactive({
  content: ''
})

// 表单验证规则
const generateRules = {
  topic: [
    { required: true, message: '请输入主题', trigger: 'blur' },
    { min: 2, max: 200, message: '主题长度在 2 到 200 个字符', trigger: 'blur' }
  ]
}

const generateFormRef = ref()

// 方法
const togglePanel = () => {
  panelVisible.value = !panelVisible.value
}

const generateContent = async () => {
  try {
    await generateFormRef.value.validate()
    
    loading.generate = true
    const response = await aiApi.generateContent(generateForm)
    
    if (response.success) {
      aiResult.value = {
        type: 'writing',
        data: response.data
      }
      ElMessage.success('内容生成成功！')
    } else {
      ElMessage.error(response.message || '生成失败')
    }
  } catch (error) {
    if (error !== false) { // 不是表单验证错误
      ElMessage.error('生成失败：' + error.message)
    }
  } finally {
    loading.generate = false
  }
}

const continueWriting = async () => {
  if (!continueForm.content.trim()) {
    ElMessage.warning('请输入已有内容')
    return
  }

  try {
    loading.continue = true
    const response = await aiApi.continueWriting(continueForm.content, continueForm.context)
    
    if (response.success) {
      aiResult.value = {
        type: 'writing',
        data: response.data
      }
      ElMessage.success('续写完成！')
    } else {
      ElMessage.error(response.message || '续写失败')
    }
  } catch (error) {
    ElMessage.error('续写失败：' + error.message)
  } finally {
    loading.continue = false
  }
}

const rewriteContent = async () => {
  if (!rewriteForm.content.trim()) {
    ElMessage.warning('请输入需要改写的内容')
    return
  }

  try {
    loading.rewrite = true
    const response = await aiApi.rewriteContent(rewriteForm.content, rewriteForm.style)
    
    if (response.success) {
      aiResult.value = {
        type: 'writing',
        data: response.data
      }
      ElMessage.success('改写完成！')
    } else {
      ElMessage.error(response.message || '改写失败')
    }
  } catch (error) {
    ElMessage.error('改写失败：' + error.message)
  } finally {
    loading.rewrite = false
  }
}

const generateSummary = async () => {
  if (!toolsForm.content.trim()) {
    ElMessage.warning('请输入文本内容')
    return
  }

  try {
    loading.summary = true
    const response = await aiApi.generateSummary(toolsForm.content, 200)
    
    if (response.success) {
      aiResult.value = {
        type: 'summary',
        data: response.data
      }
      ElMessage.success('摘要生成成功！')
    } else {
      ElMessage.error(response.message || '摘要生成失败')
    }
  } catch (error) {
    ElMessage.error('摘要生成失败：' + error.message)
  } finally {
    loading.summary = false
  }
}

const extractKeywords = async () => {
  if (!toolsForm.content.trim()) {
    ElMessage.warning('请输入文本内容')
    return
  }

  try {
    loading.keywords = true
    const response = await aiApi.extractKeywords(toolsForm.content, 10)
    
    if (response.success) {
      aiResult.value = {
        type: 'keywords',
        data: response.data
      }
      ElMessage.success('关键词提取成功！')
    } else {
      ElMessage.error(response.message || '关键词提取失败')
    }
  } catch (error) {
    ElMessage.error('关键词提取失败：' + error.message)
  } finally {
    loading.keywords = false
  }
}

const analyzeSentiment = async () => {
  if (!toolsForm.content.trim()) {
    ElMessage.warning('请输入文本内容')
    return
  }

  try {
    loading.sentiment = true
    const response = await aiApi.analyzeSentiment(toolsForm.content)
    
    if (response.success) {
      aiResult.value = {
        type: 'sentiment',
        data: response.data
      }
      ElMessage.success('情感分析完成！')
    } else {
      ElMessage.error(response.message || '情感分析失败')
    }
  } catch (error) {
    ElMessage.error('情感分析失败：' + error.message)
  } finally {
    loading.sentiment = false
  }
}

const checkGrammar = async () => {
  if (!toolsForm.content.trim()) {
    ElMessage.warning('请输入文本内容')
    return
  }

  try {
    loading.grammar = true
    const response = await aiApi.checkGrammar(toolsForm.content)
    
    if (response.success) {
      aiResult.value = {
        type: 'grammar',
        data: response.data
      }
      ElMessage.success('语法检查完成！')
    } else {
      ElMessage.error(response.message || '语法检查失败')
    }
  } catch (error) {
    ElMessage.error('语法检查失败：' + error.message)
  } finally {
    loading.grammar = false
  }
}

const getWritingSuggestions = async () => {
  if (!toolsForm.content.trim()) {
    ElMessage.warning('请输入文本内容')
    return
  }

  try {
    loading.suggestions = true
    const response = await aiApi.getWritingSuggestions(toolsForm.content)
    
    if (response.success) {
      aiResult.value = {
        type: 'suggestions',
        data: response.data
      }
      ElMessage.success('写作建议生成完成！')
    } else {
      ElMessage.error(response.message || '写作建议生成失败')
    }
  } catch (error) {
    ElMessage.error('写作建议生成失败：' + error.message)
  } finally {
    loading.suggestions = false
  }
}

const classifyContent = async () => {
  if (!toolsForm.content.trim()) {
    ElMessage.warning('请输入文本内容')
    return
  }

  try {
    loading.classify = true
    const response = await aiApi.classifyContent(toolsForm.content)
    
    if (response.success) {
      aiResult.value = {
        type: 'classification',
        data: response.data
      }
      ElMessage.success('智能分类完成！')
    } else {
      ElMessage.error(response.message || '智能分类失败')
    }
  } catch (error) {
    ElMessage.error('智能分类失败：' + error.message)
  } finally {
    loading.classify = false
  }
}

const resetGenerateForm = () => {
  Object.assign(generateForm, {
    topic: '',
    style: 'casual',
    type: 'article',
    length: 2,
    context: '',
    language: 'zh-CN',
    requirements: ''
  })
}

const insertResult = () => {
  if (!aiResult.value) return
  
  let content = ''
  if (aiResult.value.type === 'writing') {
    content = aiResult.value.data.content
  } else if (aiResult.value.type === 'summary') {
    content = aiResult.value.data.summary
  } else if (aiResult.value.type === 'keywords') {
    content = aiResult.value.data.join(', ')
  }
  
  if (content) {
    emit('insert-content', content)
    ElMessage.success('内容已插入编辑器')
  }
}

const copyResult = async () => {
  if (!aiResult.value) return
  
  let content = ''
  if (aiResult.value.type === 'writing') {
    content = aiResult.value.data.content
  } else if (aiResult.value.type === 'summary') {
    content = aiResult.value.data.summary
  } else if (aiResult.value.type === 'keywords') {
    content = aiResult.value.data.join(', ')
  }
  
  if (content) {
    try {
      await navigator.clipboard.writeText(content)
      ElMessage.success('已复制到剪贴板')
    } catch (error) {
      ElMessage.error('复制失败')
    }
  }
}

const clearResult = () => {
  aiResult.value = null
}

const getSentimentType = (sentiment) => {
  switch (sentiment) {
    case 'POSITIVE':
      return 'success'
    case 'NEGATIVE':
      return 'danger'
    default:
      return 'info'
  }
}

const getSentimentText = (sentiment) => {
  switch (sentiment) {
    case 'POSITIVE':
      return '积极'
    case 'NEGATIVE':
      return '消极'
    default:
      return '中性'
  }
}
</script>

<style scoped>
.ai-writing-assistant {
  margin-bottom: 20px;
}

.ai-panel, .result-panel {
  margin-bottom: 16px;
}

.panel-header, .result-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.ai-icon, .result-icon {
  color: var(--el-color-primary);
  margin-right: 8px;
}

.panel-title, .result-title {
  font-weight: 600;
  color: var(--text-color-primary);
}

.toggle-btn {
  padding: 4px;
}

.ai-tabs {
  margin-top: 16px;
}

.tool-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.result-meta {
  margin-bottom: 12px;
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.result-text {
  background: var(--bg-color-secondary);
  padding: 16px;
  border-radius: 6px;
  white-space: pre-wrap;
  line-height: 1.6;
  margin-bottom: 12px;
}

.suggestions, .key-points {
  margin-top: 16px;
}

.suggestions h4, .key-points h4 {
  margin: 0 0 8px 0;
  font-size: 14px;
  color: var(--text-color-primary);
}

.suggestions ul, .key-points ul {
  margin: 0;
  padding-left: 20px;
}

.suggestions li, .key-points li {
  margin-bottom: 4px;
  color: var(--text-color-regular);
}

.keyword-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.keyword-tag {
  cursor: pointer;
}

.sentiment-overview {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;
}

.confidence {
  color: var(--text-color-secondary);
  font-size: 14px;
}

.emotion-details {
  background: var(--bg-color-secondary);
  padding: 16px;
  border-radius: 6px;
}

.emotion-scores {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.score-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.score-item span:first-child {
  width: 40px;
  font-size: 14px;
  color: var(--text-color-regular);
}

.score-item .el-progress {
  flex: 1;
}

.score-item span:last-child {
  width: 50px;
  text-align: right;
  font-size: 14px;
  color: var(--text-color-secondary);
}

.generic-result pre {
  background: var(--bg-color-secondary);
  padding: 16px;
  border-radius: 6px;
  font-size: 12px;
  overflow-x: auto;
}

.result-actions {
  display: flex;
  gap: 8px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .tool-buttons {
    flex-direction: column;
  }
  
  .tool-buttons .el-button {
    width: 100%;
  }
  
  .emotion-scores {
    gap: 8px;
  }
  
  .score-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
  }
  
  .score-item .el-progress {
    width: 100%;
  }
}
</style>