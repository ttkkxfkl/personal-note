<template>
  <div class="sidebar-container">
    <!-- 顶部LOGO -->
    <div class="sidebar-header">
      <h2 class="logo">📝 个人笔记</h2>
    </div>
    
    <!-- 快速操作 -->
    <div class="quick-actions">
      <el-button 
        type="primary" 
        size="small" 
        class="new-note-btn"
        @click="createNewNote"
      >
        <el-icon><Plus /></el-icon>
        新建笔记
      </el-button>
    </div>
    
    <!-- 导航菜单 -->
    <div class="nav-menu">
      <el-menu
        :default-active="activeMenu"
        :router="true"
        class="sidebar-menu"
        @select="handleMenuSelect"
      >
        <el-menu-item index="/notes">
          <el-icon><Document /></el-icon>
          <span>所有笔记</span>
        </el-menu-item>
        
        <el-menu-item index="/notes/favorites">
          <el-icon><Star /></el-icon>
          <span>收藏夹</span>
        </el-menu-item>
        
        <el-menu-item index="/notes/trash">
          <el-icon><Delete /></el-icon>
          <span>回收站</span>
        </el-menu-item>
      </el-menu>
    </div>
    
    <!-- 笔记本列表 -->
    <div class="notebook-section">
      <div class="section-header">
        <span class="section-title">笔记本</span>
        <el-button 
          type="text" 
          size="small"
          @click="showNotebookDialog = true"
        >
          <el-icon><Plus /></el-icon>
        </el-button>
      </div>
      
      <div class="notebook-list">
        <div 
          v-for="notebook in notebooks" 
          :key="notebook.id"
          class="notebook-item"
          :class="{ active: selectedNotebook === notebook.id }"
          @click="selectNotebook(notebook)"
        >
          <div class="notebook-info">
            <div 
              class="notebook-color" 
              :style="{ backgroundColor: notebook.color || '#409EFF' }"
            ></div>
            <span class="notebook-name">{{ notebook.name }}</span>
            <span class="note-count">({{ notebook.noteCount || 0 }})</span>
          </div>
          <el-dropdown @click.stop trigger="click">
            <el-button type="text" size="small">
              <el-icon><More /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="editNotebook(notebook)">
                  编辑
                </el-dropdown-item>
                <el-dropdown-item @click="deleteNotebook(notebook)" divided>
                  删除
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
    
    <!-- 标签列表 -->
    <div class="tag-section">
      <div class="section-header">
        <span class="section-title">标签</span>
        <el-button 
          type="text" 
          size="small"
          @click="showTagDialog = true"
        >
          <el-icon><Plus /></el-icon>
        </el-button>
      </div>
      
      <div class="tag-list">
        <el-tag
          v-for="tag in tags"
          :key="tag.id"
          :color="tag.color"
          size="small"
          class="tag-item"
          @click="selectTag(tag)"
        >
          {{ tag.name }} ({{ tag.noteCount || 0 }})
        </el-tag>
      </div>
    </div>
    
    <!-- 笔记本创建/编辑对话框 -->
    <NotebookDialog 
      v-model="showNotebookDialog"
      :notebook="currentNotebook"
      @success="loadNotebooks"
    />
    
    <!-- 标签创建对话框 -->
    <TagDialog 
      v-model="showTagDialog"
      @success="loadTags"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { notebookApi } from '@/api/notebook'
import { tagApi } from '@/api/tag'
import NotebookDialog from './NotebookDialog.vue'
import TagDialog from './TagDialog.vue'

const router = useRouter()
const route = useRoute()

// 响应式数据
const notebooks = ref([])
const tags = ref([])
const showNotebookDialog = ref(false)
const showTagDialog = ref(false)
const currentNotebook = ref(null)
const selectedNotebook = ref(null)

// 计算当前激活的菜单
const activeMenu = computed(() => {
  return route.path
})

// 方法
const createNewNote = () => {
  router.push('/notes/note/new')
}

const handleMenuSelect = (index) => {
  router.push(index)
}

const selectNotebook = (notebook) => {
  selectedNotebook.value = notebook.id
  router.push(`/notes/notebook/${notebook.id}`)
}

const selectTag = (tag) => {
  router.push(`/notes/tag/${tag.id}`)
}

const editNotebook = (notebook) => {
  currentNotebook.value = notebook
  showNotebookDialog.value = true
}

const deleteNotebook = async (notebook) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除笔记本"${notebook.name}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await notebookApi.delete(notebook.id)
    ElMessage.success('删除成功')
    loadNotebooks()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除笔记本失败:', error)
    }
  }
}

const loadNotebooks = async () => {
  try {
    notebooks.value = await notebookApi.getAll()
  } catch (error) {
    console.error('加载笔记本失败:', error)
  }
}

const loadTags = async () => {
  try {
    tags.value = await tagApi.getAll()
  } catch (error) {
    console.error('加载标签失败:', error)
  }
}

// 生命周期
onMounted(() => {
  loadNotebooks()
  loadTags()
})
</script>

<style scoped>
.sidebar-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 0;
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid var(--border-color);
}

.logo {
  margin: 0;
  font-size: 18px;
  font-weight: bold;
  color: var(--text-color-primary);
}

.quick-actions {
  padding: 16px;
  border-bottom: 1px solid var(--border-color);
}

.new-note-btn {
  width: 100%;
}

.nav-menu {
  border-bottom: 1px solid var(--border-color);
}

.sidebar-menu {
  border: none;
  background: transparent;
}

.sidebar-menu .el-menu-item {
  height: 40px;
  line-height: 40px;
  margin: 0 12px;
  border-radius: 6px;
}

.notebook-section,
.tag-section {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}

.section-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-color-regular);
}

.notebook-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.notebook-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.notebook-item:hover {
  background-color: var(--bg-color-primary);
}

.notebook-item.active {
  background-color: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
}

.notebook-info {
  display: flex;
  align-items: center;
  flex: 1;
  min-width: 0;
}

.notebook-color {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  margin-right: 8px;
  flex-shrink: 0;
}

.notebook-name {
  font-size: 14px;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.note-count {
  font-size: 12px;
  color: var(--text-color-secondary);
  margin-left: 4px;
}

.tag-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.tag-item {
  cursor: pointer;
  transition: opacity 0.2s;
}

.tag-item:hover {
  opacity: 0.8;
}
</style>