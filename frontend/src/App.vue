<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup>
import { provide } from 'vue'
import { useDark, useToggle } from '@vueuse/core'

const isDark = useDark()
const toggleDark = useToggle(isDark)

provide('isDark', isDark)
provide('toggleDark', toggleDark)
</script>

<style>
#app {
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

body {
  margin: 0;
  padding: 0;
}

* {
  box-sizing: border-box;
}
</style>