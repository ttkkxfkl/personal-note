import { onMounted, onUnmounted } from 'vue'
import hotkeys from 'hotkeys-js'

export function useHotkeys() {
  const hotkeyMap = new Map()

  // 注册快捷键
  const registerHotkey = (key, callback, options = {}) => {
    const {
      scope = 'all',
      element = null,
      keyup = false,
      keydown = true,
      splitKey = '+'
    } = options

    // 设置作用域
    hotkeys.setScope(scope)
    
    // 注册快捷键
    hotkeys(key, { scope, element, keyup, keydown, splitKey }, callback)
    
    // 存储映射关系
    hotkeyMap.set(key, { callback, options })
  }

  // 注销快捷键
  const unregisterHotkey = (key, scope = 'all') => {
    hotkeys.unbind(key, scope)
    hotkeyMap.delete(key)
  }

  // 注销所有快捷键
  const unregisterAllHotkeys = () => {
    hotkeyMap.forEach((_, key) => {
      hotkeys.unbind(key)
    })
    hotkeyMap.clear()
  }

  // 切换作用域
  const setScope = (scope) => {
    hotkeys.setScope(scope)
  }

  // 获取当前作用域
  const getScope = () => {
    return hotkeys.getScope()
  }

  // 暂停快捷键
  const pauseHotkeys = () => {
    hotkeys.pause()
  }

  // 恢复快捷键
  const resumeHotkeys = () => {
    hotkeys.unpause()
  }

  // 预定义的快捷键组合
  const registerCommonHotkeys = (actions = {}) => {
    const defaultActions = {
      // 编辑器快捷键
      save: () => console.log('保存'),
      newNote: () => console.log('新建笔记'),
      search: () => console.log('搜索'),
      toggleSidebar: () => console.log('切换侧边栏'),
      togglePreview: () => console.log('切换预览'),
      bold: () => console.log('加粗'),
      italic: () => console.log('斜体'),
      underline: () => console.log('下划线'),
      code: () => console.log('代码'),
      link: () => console.log('链接'),
      image: () => console.log('图片'),
      table: () => console.log('表格'),
      list: () => console.log('列表'),
      quote: () => console.log('引用'),
      heading1: () => console.log('标题1'),
      heading2: () => console.log('标题2'),
      heading3: () => console.log('标题3'),
      // 导航快捷键
      goToNotes: () => console.log('转到笔记'),
      goToNotebooks: () => console.log('转到笔记本'),
      goToTags: () => console.log('转到标签'),
      goToFavorites: () => console.log('转到收藏'),
      goToTrash: () => console.log('转到回收站'),
      // 系统快捷键
      toggleDarkMode: () => console.log('切换暗色模式'),
      showSettings: () => console.log('显示设置'),
      showHelp: () => console.log('显示帮助'),
      ...actions
    }

    // 编辑器快捷键
    registerHotkey('ctrl+s,cmd+s', defaultActions.save, { scope: 'editor' })
    registerHotkey('ctrl+n,cmd+n', defaultActions.newNote)
    registerHotkey('ctrl+f,cmd+f', defaultActions.search)
    registerHotkey('ctrl+\\,cmd+\\', defaultActions.toggleSidebar)
    registerHotkey('ctrl+shift+v,cmd+shift+v', defaultActions.togglePreview, { scope: 'editor' })
    
    // 格式化快捷键
    registerHotkey('ctrl+b,cmd+b', defaultActions.bold, { scope: 'editor' })
    registerHotkey('ctrl+i,cmd+i', defaultActions.italic, { scope: 'editor' })
    registerHotkey('ctrl+u,cmd+u', defaultActions.underline, { scope: 'editor' })
    registerHotkey('ctrl+shift+c,cmd+shift+c', defaultActions.code, { scope: 'editor' })
    registerHotkey('ctrl+k,cmd+k', defaultActions.link, { scope: 'editor' })
    registerHotkey('ctrl+shift+i,cmd+shift+i', defaultActions.image, { scope: 'editor' })
    registerHotkey('ctrl+shift+t,cmd+shift+t', defaultActions.table, { scope: 'editor' })
    registerHotkey('ctrl+shift+l,cmd+shift+l', defaultActions.list, { scope: 'editor' })
    registerHotkey('ctrl+shift+q,cmd+shift+q', defaultActions.quote, { scope: 'editor' })
    registerHotkey('ctrl+1,cmd+1', defaultActions.heading1, { scope: 'editor' })
    registerHotkey('ctrl+2,cmd+2', defaultActions.heading2, { scope: 'editor' })
    registerHotkey('ctrl+3,cmd+3', defaultActions.heading3, { scope: 'editor' })

    // 导航快捷键
    registerHotkey('alt+1', defaultActions.goToNotes)
    registerHotkey('alt+2', defaultActions.goToNotebooks)
    registerHotkey('alt+3', defaultActions.goToTags)
    registerHotkey('alt+4', defaultActions.goToFavorites)
    registerHotkey('alt+5', defaultActions.goToTrash)

    // 系统快捷键
    registerHotkey('ctrl+shift+d,cmd+shift+d', defaultActions.toggleDarkMode)
    registerHotkey('ctrl+comma,cmd+comma', defaultActions.showSettings)
    registerHotkey('f1', defaultActions.showHelp)
    registerHotkey('escape', () => {
      // ESC键用于关闭模态框、退出编辑等
      const event = new CustomEvent('hotkey:escape')
      document.dispatchEvent(event)
    })
  }

  // 获取所有已注册的快捷键
  const getRegisteredHotkeys = () => {
    return Array.from(hotkeyMap.keys())
  }

  // 检查快捷键是否被占用
  const isHotkeyRegistered = (key) => {
    return hotkeyMap.has(key)
  }

  // 获取快捷键帮助信息
  const getHotkeyHelp = () => {
    return {
      '编辑器快捷键': [
        { key: 'Ctrl+S / Cmd+S', desc: '保存当前笔记' },
        { key: 'Ctrl+N / Cmd+N', desc: '新建笔记' },
        { key: 'Ctrl+F / Cmd+F', desc: '搜索' },
        { key: 'Ctrl+\\ / Cmd+\\', desc: '切换侧边栏' },
        { key: 'Ctrl+Shift+V / Cmd+Shift+V', desc: '切换预览模式' }
      ],
      '格式化快捷键': [
        { key: 'Ctrl+B / Cmd+B', desc: '加粗文本' },
        { key: 'Ctrl+I / Cmd+I', desc: '斜体文本' },
        { key: 'Ctrl+U / Cmd+U', desc: '下划线' },
        { key: 'Ctrl+Shift+C / Cmd+Shift+C', desc: '代码块' },
        { key: 'Ctrl+K / Cmd+K', desc: '插入链接' },
        { key: 'Ctrl+Shift+I / Cmd+Shift+I', desc: '插入图片' },
        { key: 'Ctrl+1-3 / Cmd+1-3', desc: '插入标题' }
      ],
      '导航快捷键': [
        { key: 'Alt+1', desc: '转到笔记列表' },
        { key: 'Alt+2', desc: '转到笔记本' },
        { key: 'Alt+3', desc: '转到标签' },
        { key: 'Alt+4', desc: '转到收藏夹' },
        { key: 'Alt+5', desc: '转到回收站' }
      ],
      '系统快捷键': [
        { key: 'Ctrl+Shift+D / Cmd+Shift+D', desc: '切换暗色模式' },
        { key: 'Ctrl+, / Cmd+,', desc: '打开设置' },
        { key: 'F1', desc: '显示帮助' },
        { key: 'Esc', desc: '关闭弹窗/取消操作' }
      ]
    }
  }

  // 创建快捷键提示组件数据
  const createHotkeyTooltip = (key, description) => {
    // 格式化快捷键显示
    const formatKey = (keyStr) => {
      return keyStr
        .replace(/ctrl/gi, '⌘')
        .replace(/cmd/gi, '⌘')
        .replace(/shift/gi, '⇧')
        .replace(/alt/gi, '⌥')
        .replace(/meta/gi, '⌘')
        .replace(/,/g, ' / ')
    }

    return {
      key: formatKey(key),
      description
    }
  }

  // 组件卸载时清理
  onUnmounted(() => {
    unregisterAllHotkeys()
  })

  return {
    registerHotkey,
    unregisterHotkey,
    unregisterAllHotkeys,
    setScope,
    getScope,
    pauseHotkeys,
    resumeHotkeys,
    registerCommonHotkeys,
    getRegisteredHotkeys,
    isHotkeyRegistered,
    getHotkeyHelp,
    createHotkeyTooltip
  }
}