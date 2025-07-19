<template>
  <div class="layout-container">
    <!-- 侧边栏 -->
    <div class="sidebar" :class="{ show: sidebarVisible }">
      <Sidebar />
    </div>
    
    <!-- 主内容区 -->
    <div class="main-content">
      <!-- 顶部导航 -->
      <div class="content-header">
        <Header @toggle-sidebar="toggleSidebar" />
      </div>
      
      <!-- 内容区域 -->
      <div class="content-body">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watchEffect } from 'vue'
import { useWindowSize } from '@vueuse/core'
import Sidebar from '@/components/Sidebar.vue'
import Header from '@/components/Header.vue'

const { width } = useWindowSize()
const sidebarVisible = ref(false)

const toggleSidebar = () => {
  sidebarVisible.value = !sidebarVisible.value
}

// 监听窗口大小变化，大屏幕时自动显示侧边栏
watchEffect(() => {
  if (width.value >= 768) {
    sidebarVisible.value = false
  }
})
</script>

<style scoped>
@media (max-width: 768px) {
  .sidebar.show {
    transform: translateX(0) !important;
  }
}
</style>