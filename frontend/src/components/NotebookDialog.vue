<template>
  <el-dialog
    v-model="visible"
    :title="notebook ? '编辑笔记本' : '新建笔记本'"
    width="400px"
  >
    <el-form :model="form" label-width="80px">
      <el-form-item label="名称">
        <el-input v-model="form.name" placeholder="请输入笔记本名称" />
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
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
import { ref, watch, defineProps, defineEmits } from 'vue'

const props = defineProps({
  modelValue: Boolean,
  notebook: Object
})

const emit = defineEmits(['update:modelValue', 'success'])

const visible = ref(false)
const form = ref({
  name: '',
  description: '',
  color: '#409EFF'
})

watch(() => props.modelValue, (val) => {
  visible.value = val
  if (val && props.notebook) {
    form.value = { ...props.notebook }
  } else if (val) {
    form.value = {
      name: '',
      description: '',
      color: '#409EFF'
    }
  }
})

watch(visible, (val) => {
  emit('update:modelValue', val)
})

const handleSubmit = () => {
  // TODO: 实现保存逻辑
  console.log('保存笔记本:', form.value)
  visible.value = false
  emit('success')
}
</script>