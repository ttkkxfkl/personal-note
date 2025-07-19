<template>
  <div class="note-detail-container">
    <div class="note-header">
      <h1>笔记详情页</h1>
      <p>笔记ID: {{ $route.params.id }}</p>
    </div>
    <div class="note-content">
      <p>这里将显示笔记的详细内容...</p>
    </div>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'

const route = useRoute()
</script>

<style scoped>
.note-detail-container {
  padding: 20px;
}

.note-header {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid var(--border-color);
}

.note-content {
  font-size: 16px;
  line-height: 1.6;
}
</style>