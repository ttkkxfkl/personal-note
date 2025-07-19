<template>
  <div class="notes-container">
    <div class="notes-header">
      <h2>{{ currentTitle }}</h2>
      <p class="notes-count">共 {{ notes.length }} 条笔记</p>
    </div>
    
    <div class="notes-list">
      <div 
        v-for="note in notes" 
        :key="note.id"
        class="note-item"
        @click="viewNote(note)"
      >
        <div class="note-content">
          <h3 class="note-title">{{ note.title }}</h3>
          <p class="note-excerpt">{{ note.excerpt }}</p>
          <div class="note-meta">
            <span class="note-date">{{ formatDate(note.updatedTime) }}</span>
            <span v-if="note.isFavorite" class="favorite-icon">⭐</span>
          </div>
        </div>
      </div>
      
      <div v-if="notes.length === 0" class="empty-state">
        <p>暂无笔记</p>
        <el-button type="primary" @click="createNote">创建第一条笔记</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { formatDate } from '@/utils/date'

const router = useRouter()
const route = useRoute()

const notes = ref([
  {
    id: 1,
    title: '示例笔记',
    excerpt: '这是一个示例笔记的摘要内容...',
    isFavorite: true,
    updatedTime: new Date()
  }
])

const currentTitle = computed(() => {
  const { name } = route
  switch (name) {
    case 'Notes':
      return '所有笔记'
    case 'Favorites':
      return '收藏夹'
    case 'Trash':
      return '回收站'
    case 'NotebookNotes':
      return '笔记本笔记'
    case 'TagNotes':
      return '标签笔记'
    default:
      return '笔记'
  }
})

const viewNote = (note) => {
  router.push(`/notes/note/${note.id}`)
}

const createNote = () => {
  router.push('/notes/note/new')
}

onMounted(() => {
  // TODO: 根据路由参数加载对应的笔记
  console.log('当前路由:', route.name, route.params)
})
</script>

<style scoped>
.notes-container {
  height: 100%;
  padding: 20px;
  overflow-y: auto;
}

.notes-header {
  margin-bottom: 20px;
}

.notes-header h2 {
  margin: 0 0 8px 0;
  font-size: 24px;
  color: var(--text-color-primary);
}

.notes-count {
  margin: 0;
  font-size: 14px;
  color: var(--text-color-secondary);
}

.notes-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.note-item {
  padding: 16px;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
  background: var(--bg-color-primary);
}

.note-item:hover {
  border-color: var(--el-color-primary);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.note-title {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: 500;
  color: var(--text-color-primary);
}

.note-excerpt {
  margin: 0 0 12px 0;
  font-size: 14px;
  color: var(--text-color-regular);
  line-height: 1.5;
}

.note-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.note-date {
  font-size: 12px;
  color: var(--text-color-secondary);
}

.favorite-icon {
  font-size: 14px;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: var(--text-color-secondary);
}
</style>