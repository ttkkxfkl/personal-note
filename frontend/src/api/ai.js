import request from '@/utils/request'

const AI_BASE_URL = '/ai'

export const aiApi = {
  // AI写作助手
  generateContent(data) {
    return request({
      url: `${AI_BASE_URL}/writing/generate`,
      method: 'post',
      data
    })
  },

  continueWriting(content, context) {
    return request({
      url: `${AI_BASE_URL}/writing/continue`,
      method: 'post',
      params: { content, context }
    })
  },

  rewriteContent(content, style) {
    return request({
      url: `${AI_BASE_URL}/writing/rewrite`,
      method: 'post',
      params: { content, style }
    })
  },

  // 智能摘要
  generateSummary(content, maxLength = 200) {
    return request({
      url: `${AI_BASE_URL}/summary`,
      method: 'post',
      params: { content, maxLength }
    })
  },

  // 关键词提取
  extractKeywords(content, maxCount = 10) {
    return request({
      url: `${AI_BASE_URL}/keywords`,
      method: 'post',
      params: { content, maxCount }
    })
  },

  // 情感分析
  analyzeSentiment(content) {
    return request({
      url: `${AI_BASE_URL}/sentiment`,
      method: 'post',
      params: { content }
    })
  },

  // 智能问答
  askQuestion(question, context) {
    return request({
      url: `${AI_BASE_URL}/qa`,
      method: 'post',
      params: { question, context }
    })
  },

  // 语音转录
  transcribeAudio(file) {
    const formData = new FormData()
    formData.append('file', file)
    
    return request({
      url: `${AI_BASE_URL}/transcription`,
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // OCR文字提取
  extractTextFromImage(file) {
    const formData = new FormData()
    formData.append('file', file)
    
    return request({
      url: `${AI_BASE_URL}/ocr`,
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // 图片描述
  describeImage(file) {
    const formData = new FormData()
    formData.append('file', file)
    
    return request({
      url: `${AI_BASE_URL}/image/describe`,
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // 文本翻译
  translateText(text, fromLang, toLang) {
    return request({
      url: `${AI_BASE_URL}/translation`,
      method: 'post',
      params: { text, fromLang, toLang }
    })
  },

  // 智能分类
  classifyContent(content) {
    return request({
      url: `${AI_BASE_URL}/classification`,
      method: 'post',
      params: { content }
    })
  },

  // 标签推荐
  recommendTags(content, maxCount = 5) {
    return request({
      url: `${AI_BASE_URL}/tags/recommend`,
      method: 'post',
      params: { content, maxCount }
    })
  },

  // 语法检查
  checkGrammar(content) {
    return request({
      url: `${AI_BASE_URL}/grammar`,
      method: 'post',
      params: { content }
    })
  },

  // 写作建议
  getWritingSuggestions(content) {
    return request({
      url: `${AI_BASE_URL}/writing/suggestions`,
      method: 'post',
      params: { content }
    })
  },

  // 相似笔记检索
  findSimilarNotes(content, excludeNoteId) {
    return request({
      url: `${AI_BASE_URL}/similarity`,
      method: 'post',
      params: { content, excludeNoteId }
    })
  },

  // AI聊天对话
  chat(message, chatHistory = []) {
    return request({
      url: `${AI_BASE_URL}/chat`,
      method: 'post',
      params: { message },
      data: chatHistory
    })
  },

  // 批量AI处理
  batchProcess(data) {
    return request({
      url: `${AI_BASE_URL}/batch`,
      method: 'post',
      data
    })
  }
}

// AI相关的工具函数
export const aiUtils = {
  // 格式化AI响应
  formatResponse(response) {
    if (!response || !response.success) {
      return null
    }
    return response.data
  },

  // 获取置信度颜色
  getConfidenceColor(confidence) {
    if (confidence >= 0.8) return '#67C23A'
    if (confidence >= 0.6) return '#E6A23C'
    return '#F56C6C'
  },

  // 格式化置信度文本
  formatConfidence(confidence) {
    return `${(confidence * 100).toFixed(1)}%`
  },

  // 获取情感分析颜色
  getSentimentColor(sentiment) {
    switch (sentiment) {
      case 'POSITIVE':
        return '#67C23A'
      case 'NEGATIVE':
        return '#F56C6C'
      default:
        return '#909399'
    }
  },

  // 格式化文件大小
  formatFileSize(bytes) {
    if (bytes === 0) return '0 B'
    const k = 1024
    const sizes = ['B', 'KB', 'MB', 'GB']
    const i = Math.floor(Math.log(bytes) / Math.log(k))
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
  },

  // 检查文件类型
  isImageFile(file) {
    return file && file.type && file.type.startsWith('image/')
  },

  isAudioFile(file) {
    return file && file.type && file.type.startsWith('audio/')
  },

  isVideoFile(file) {
    return file && file.type && file.type.startsWith('video/')
  },

  // 语言代码映射
  getLanguageName(langCode) {
    const langMap = {
      'zh-CN': '中文',
      'zh-TW': '繁体中文',
      'en-US': '英语',
      'ja-JP': '日语',
      'ko-KR': '韩语',
      'fr-FR': '法语',
      'de-DE': '德语',
      'es-ES': '西班牙语',
      'it-IT': '意大利语',
      'pt-PT': '葡萄牙语',
      'ru-RU': '俄语',
      'ar-SA': '阿拉伯语'
    }
    return langMap[langCode] || langCode
  },

  // 支持的语言列表
  getSupportedLanguages() {
    return [
      { code: 'zh-CN', name: '中文' },
      { code: 'en-US', name: 'English' },
      { code: 'ja-JP', name: '日本語' },
      { code: 'ko-KR', name: '한국어' },
      { code: 'fr-FR', name: 'Français' },
      { code: 'de-DE', name: 'Deutsch' },
      { code: 'es-ES', name: 'Español' },
      { code: 'it-IT', name: 'Italiano' },
      { code: 'pt-PT', name: 'Português' },
      { code: 'ru-RU', name: 'Русский' }
    ]
  },

  // 写作风格选项
  getWritingStyles() {
    return [
      { value: 'formal', label: '正式', description: '严谨、专业的语言风格' },
      { value: 'casual', label: '随意', description: '轻松、自然的表达方式' },
      { value: 'academic', label: '学术', description: '学术论文的严谨风格' },
      { value: 'creative', label: '创意', description: '富有想象力和创造性' },
      { value: 'business', label: '商务', description: '商业沟通的专业风格' },
      { value: 'technical', label: '技术', description: '技术文档的精确表达' },
      { value: 'journalistic', label: '新闻', description: '新闻报道的客观风格' },
      { value: 'literary', label: '文学', description: '文学作品的艺术表达' }
    ]
  },

  // 文档类型选项
  getDocumentTypes() {
    return [
      { value: 'article', label: '文章', description: '通用文章格式' },
      { value: 'email', label: '邮件', description: '电子邮件格式' },
      { value: 'report', label: '报告', description: '正式报告格式' },
      { value: 'summary', label: '摘要', description: '内容摘要格式' },
      { value: 'outline', label: '大纲', description: '结构化大纲' },
      { value: 'proposal', label: '提案', description: '项目提案格式' },
      { value: 'presentation', label: '演示', description: '演示文稿内容' },
      { value: 'blog', label: '博客', description: '博客文章格式' }
    ]
  },

  // 验证AI请求参数
  validateWritingRequest(request) {
    const errors = []
    
    if (!request.topic || request.topic.trim().length === 0) {
      errors.push('主题不能为空')
    }
    
    if (request.topic && request.topic.length > 200) {
      errors.push('主题长度不能超过200个字符')
    }
    
    if (request.context && request.context.length > 500) {
      errors.push('上下文长度不能超过500个字符')
    }
    
    if (request.requirements && request.requirements.length > 300) {
      errors.push('特殊要求长度不能超过300个字符')
    }
    
    return errors
  },

  // 生成提示词模板变量
  generatePromptVariables(template, variables) {
    let result = template
    Object.keys(variables).forEach(key => {
      const placeholder = `{${key}}`
      result = result.replace(new RegExp(placeholder, 'g'), variables[key] || placeholder)
    })
    return result
  },

  // AI模型信息
  getModelInfo() {
    return {
      'gpt-3.5-turbo': {
        name: 'GPT-3.5 Turbo',
        description: '快速、高效的对话模型',
        maxTokens: 4096,
        pricing: '较低'
      },
      'gpt-4': {
        name: 'GPT-4',
        description: '最先进的多模态模型',
        maxTokens: 8192,
        pricing: '较高'
      },
      'claude-3': {
        name: 'Claude-3',
        description: '安全、有用的AI助手',
        maxTokens: 200000,
        pricing: '中等'
      }
    }
  },

  // 错误处理
  handleAIError(error) {
    const errorMessages = {
      'RATE_LIMIT_EXCEEDED': 'API调用频率超限，请稍后再试',
      'INVALID_API_KEY': 'API密钥无效，请检查配置',
      'INSUFFICIENT_QUOTA': 'API配额不足，请检查账户余额',
      'MODEL_OVERLOADED': 'AI模型负载过高，请稍后再试',
      'CONTENT_POLICY_VIOLATION': '内容违反使用政策，请修改后重试',
      'NETWORK_ERROR': '网络连接错误，请检查网络状态',
      'TIMEOUT': '请求超时，请稍后重试'
    }
    
    const message = errorMessages[error.code] || error.message || '未知错误'
    return {
      code: error.code || 'UNKNOWN_ERROR',
      message,
      timestamp: new Date().toISOString()
    }
  }
}

export default aiApi