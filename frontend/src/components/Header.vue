<template>
  <div class="header-container">
    <div class="header-left">
      <el-button 
        type="text" 
        size="large"
        class="mobile-menu-btn"
        @click="$emit('toggle-sidebar')"
      >
        <el-icon><Menu /></el-icon>
      </el-button>
      <h1 class="page-title">{{ $route.meta.title || '个人笔记' }}</h1>
    </div>
    
    <div class="header-right">
      <el-input
        v-model="searchQuery"
        placeholder="搜索笔记..."
        style="width: 200px"
        clearable
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

defineEmits(['toggle-sidebar'])

const searchQuery = ref('')
</script>

<style scoped>
.header-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  height: 100%;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.mobile-menu-btn {
  display: none;
}

.page-title {
  margin: 0;
  font-size: 18px;
  font-weight: 500;
  color: var(--text-color-primary);
}

@media (max-width: 768px) {
  .mobile-menu-btn {
    display: flex;
  }
}
</style>