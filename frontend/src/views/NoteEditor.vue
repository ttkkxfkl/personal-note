<template>
  <div class="note-editor-container">
    <div class="editor-header">
      <h1>{{ isEdit ? '编辑笔记' : '新建笔记' }}</h1>
      <div class="editor-actions">
        <el-button @click="goBack">取消</el-button>
        <el-button type="primary" @click="saveNote">保存</el-button>
      </div>
    </div>
    
    <div class="editor-content">
      <el-form :model="noteForm" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="noteForm.title" placeholder="请输入笔记标题" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input 
            v-model="noteForm.content" 
            type="textarea" 
            :rows="20"
            placeholder="请输入笔记内容"
          />
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const noteForm = ref({
  title: '',
  content: ''
})

const isEdit = computed(() => {
  return route.name === 'EditNote'
})

const goBack = () => {
  router.back()
}

const saveNote = () => {
  console.log('保存笔记:', noteForm.value)
  // TODO: 实现保存逻辑
  router.back()
}
</script>

<style scoped>
.note-editor-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 20px;
}

.editor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid var(--border-color);
}

.editor-header h1 {
  margin: 0;
}

.editor-content {
  flex: 1;
  overflow-y: auto;
}
</style>