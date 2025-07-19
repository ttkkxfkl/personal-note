<template>
  <el-dialog
    v-model="visible"
    title="选择笔记模板"
    width="800px"
    class="template-dialog"
  >
    <div class="template-container">
      <!-- 分类筛选 -->
      <div class="category-filter">
        <el-radio-group v-model="selectedCategory" @change="loadTemplates">
          <el-radio-button label="">全部</el-radio-button>
          <el-radio-button label="工作">工作</el-radio-button>
          <el-radio-button label="学习">学习</el-radio-button>
          <el-radio-button label="生活">生活</el-radio-button>
          <el-radio-button label="项目">项目</el-radio-button>
        </el-radio-group>
      </div>

      <!-- 模板列表 -->
      <div class="template-grid">
        <div 
          v-for="template in filteredTemplates" 
          :key="template.id"
          class="template-card"
          :class="{ 'system-template': template.isSystem }"
          @click="selectTemplate(template)"
        >
          <div class="template-header">
            <div class="template-icon">
              <el-icon v-if="template.icon">
                <component :is="template.icon" />
              </el-icon>
              <el-icon v-else><Document /></el-icon>
            </div>
            <div class="template-info">
              <h4 class="template-name">{{ template.name }}</h4>
              <p class="template-description">{{ template.description }}</p>
            </div>
            <div class="template-badge" v-if="template.isSystem">
              <el-tag size="small" type="success">系统</el-tag>
            </div>
          </div>
          
          <div class="template-preview">
            <div class="preview-content" v-html="getPreviewContent(template)"></div>
          </div>
          
          <div class="template-footer">
            <span class="usage-count">
              <el-icon><User /></el-icon>
              {{ template.usageCount || 0 }}
            </span>
            <span class="template-category" v-if="template.category">
              {{ template.category }}
            </span>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="filteredTemplates.length === 0" class="empty-state">
        <el-empty description="暂无模板">
          <el-button type="primary" @click="showCreateDialog = true">
            创建模板
          </el-button>
        </el-empty>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="showCreateDialog = true">
          <el-icon><Plus /></el-icon>
          创建模板
        </el-button>
      </div>
    </template>

    <!-- 创建模板对话框 -->
    <CreateTemplateDialog 
      v-model="showCreateDialog"
      @created="handleTemplateCreated"
    />
  </el-dialog>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { templateApi } from '@/api/template'
import CreateTemplateDialog from './CreateTemplateDialog.vue'

const props = defineProps({
  modelValue: Boolean
})

const emit = defineEmits(['update:modelValue', 'select'])

const visible = ref(false)
const templates = ref([])
const selectedCategory = ref('')
const showCreateDialog = ref(false)
const loading = ref(false)

// 预定义模板数据（如果后端暂无数据）
const defaultTemplates = [
  {
    id: 1,
    name: '会议记录',
    description: '记录会议要点、决策和行动项',
    category: '工作',
    icon: 'Calendar',
    isSystem: true,
    usageCount: 156,
    markdownTemplate: `# 会议记录

## 会议信息
- **会议主题**: 
- **参会人员**: 
- **会议时间**: 
- **会议地点**: 

## 会议议题
1. 
2. 
3. 

## 讨论要点


## 决策事项
- [ ] 
- [ ] 
- [ ] 

## 行动项
| 任务 | 负责人 | 截止日期 |
|------|--------|----------|
|      |        |          |

## 下次会议
- **时间**: 
- **议题**: `
  },
  {
    id: 2,
    name: '项目计划',
    description: '制定项目计划和里程碑',
    category: '项目',
    icon: 'Flag',
    isSystem: true,
    usageCount: 89,
    markdownTemplate: `# 项目计划

## 项目概述
- **项目名称**: 
- **项目目标**: 
- **开始时间**: 
- **预计完成**: 

## 项目范围


## 里程碑
- [ ] **阶段一**: 需求分析 (预计: )
- [ ] **阶段二**: 设计开发 (预计: )
- [ ] **阶段三**: 测试部署 (预计: )
- [ ] **阶段四**: 上线维护 (预计: )

## 资源需求
### 人力资源
- 
- 

### 技术资源
- 
- 

## 风险评估
| 风险 | 影响程度 | 应对策略 |
|------|----------|----------|
|      |          |          |

## 成功标准
1. 
2. 
3. `
  },
  {
    id: 3,
    name: '学习笔记',
    description: '系统化整理学习内容',
    category: '学习',
    icon: 'Reading',
    isSystem: true,
    usageCount: 234,
    markdownTemplate: `# 学习笔记

## 基本信息
- **主题**: 
- **来源**: 
- **学习日期**: 
- **难度等级**: ⭐⭐⭐

## 核心概念


## 重点内容
### 要点一


### 要点二


### 要点三


## 实例/案例


## 问题与思考
1. 
2. 
3. 

## 总结
### 收获


### 应用场景


## 延伸阅读
- [ ] 
- [ ] 
- [ ] `
  },
  {
    id: 4,
    name: '日计划',
    description: '制定每日工作计划',
    category: '生活',
    icon: 'Clock',
    isSystem: true,
    usageCount: 312,
    markdownTemplate: `# 日计划 - {{ date }}

## 今日目标
🎯 

## 重要任务
### 紧急重要
- [ ] 
- [ ] 

### 重要不紧急
- [ ] 
- [ ] 

### 紧急不重要
- [ ] 
- [ ] 

## 时间安排
| 时间 | 任务 | 状态 |
|------|------|------|
| 09:00-10:00 |  | ⏳ |
| 10:00-11:00 |  | ⏳ |
| 11:00-12:00 |  | ⏳ |
| 14:00-15:00 |  | ⏳ |
| 15:00-16:00 |  | ⏳ |
| 16:00-17:00 |  | ⏳ |

## 今日反思
### 完成情况


### 收获与感悟


### 明日改进
- 
- `
  }
]

// 计算属性
const filteredTemplates = computed(() => {
  if (!selectedCategory.value) {
    return templates.value
  }
  return templates.value.filter(template => 
    template.category === selectedCategory.value
  )
})

// 方法
const loadTemplates = async () => {
  loading.value = true
  try {
    // 先使用默认模板，实际项目中从API获取
    templates.value = defaultTemplates
    
    // const response = await templateApi.getAll()
    // templates.value = response || defaultTemplates
  } catch (error) {
    console.error('加载模板失败:', error)
    templates.value = defaultTemplates
  } finally {
    loading.value = false
  }
}

const selectTemplate = (template) => {
  emit('select', template)
  visible.value = false
}

const getPreviewContent = (template) => {
  const content = template.markdownTemplate || template.contentTemplate || ''
  // 简单处理Markdown，显示前几行作为预览
  const lines = content.split('\n').slice(0, 5)
  return lines.map(line => {
    if (line.startsWith('# ')) {
      return `<h4>${line.substring(2)}</h4>`
    } else if (line.startsWith('## ')) {
      return `<h5>${line.substring(3)}</h5>`
    } else if (line.startsWith('- ')) {
      return `<li>${line.substring(2)}</li>`
    } else {
      return `<p>${line}</p>`
    }
  }).join('')
}

const handleTemplateCreated = (newTemplate) => {
  templates.value.unshift(newTemplate)
  showCreateDialog.value = false
}

// 监听器
watch(() => props.modelValue, (val) => {
  visible.value = val
  if (val && templates.value.length === 0) {
    loadTemplates()
  }
})

watch(visible, (val) => {
  emit('update:modelValue', val)
})

onMounted(() => {
  if (props.modelValue) {
    loadTemplates()
  }
})
</script>

<style scoped>
.template-dialog :deep(.el-dialog__body) {
  padding: 20px;
}

.template-container {
  max-height: 600px;
  overflow-y: auto;
}

.category-filter {
  margin-bottom: 20px;
  text-align: center;
}

.template-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 16px;
}

.template-card {
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.2s;
  background: var(--bg-color-primary);
}

.template-card:hover {
  border-color: var(--el-color-primary);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.template-card.system-template {
  border-left: 4px solid var(--el-color-success);
}

.template-header {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 12px;
}

.template-icon {
  flex-shrink: 0;
  width: 40px;
  height: 40px;
  border-radius: 8px;
  background: var(--el-color-primary-light-9);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--el-color-primary);
}

.template-info {
  flex: 1;
  min-width: 0;
}

.template-name {
  margin: 0 0 4px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-color-primary);
}

.template-description {
  margin: 0;
  font-size: 12px;
  color: var(--text-color-secondary);
  line-height: 1.4;
}

.template-badge {
  flex-shrink: 0;
}

.template-preview {
  margin-bottom: 12px;
  padding: 12px;
  background: var(--bg-color-secondary);
  border-radius: 6px;
  height: 120px;
  overflow: hidden;
}

.preview-content {
  font-size: 12px;
  line-height: 1.4;
  color: var(--text-color-regular);
}

.preview-content h4,
.preview-content h5 {
  margin: 4px 0;
  color: var(--text-color-primary);
}

.preview-content p {
  margin: 2px 0;
}

.preview-content li {
  margin: 1px 0;
  list-style: none;
  padding-left: 16px;
  position: relative;
}

.preview-content li:before {
  content: '•';
  position: absolute;
  left: 0;
}

.template-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: var(--text-color-secondary);
}

.usage-count {
  display: flex;
  align-items: center;
  gap: 4px;
}

.template-category {
  padding: 2px 8px;
  background: var(--el-color-info-light-8);
  border-radius: 12px;
  color: var(--el-color-info);
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
}

.dialog-footer {
  display: flex;
  justify-content: space-between;
}
</style>