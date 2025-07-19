<template>
  <el-dialog
    v-model="visible"
    title="新建标签"
    width="300px"
  >
    <el-form :model="form" label-width="60px">
      <el-form-item label="名称">
        <el-input v-model="form.name" placeholder="请输入标签名称" />
      </el-form-item>
      <el-form-item label="颜色">
        <el-color-picker v-model="form.color" />
      </el-form-item>
    </el-form>
    
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  modelValue: Boolean
})

const emit = defineEmits(['update:modelValue', 'success'])

const visible = ref(false)
const form = ref({
  name: '',
  color: '#409EFF'
})

watch(() => props.modelValue, (val) => {
  visible.value = val
  if (val) {
    form.value = {
      name: '',
      color: '#409EFF'
    }
  }
})

watch(visible, (val) => {
  emit('update:modelValue', val)
})

const handleSubmit = () => {
  // TODO: 实现保存逻辑
  console.log('保存标签:', form.value)
  visible.value = false
  emit('success')
}
</script>