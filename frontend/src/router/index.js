import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/notes'
  },
  {
    path: '/notes',
    name: 'Layout',
    component: () => import('@/views/Layout.vue'),
    children: [
      {
        path: '',
        name: 'Notes',
        component: () => import('@/views/Notes.vue'),
        meta: { title: '所有笔记' }
      },
      {
        path: 'notebook/:id',
        name: 'NotebookNotes',
        component: () => import('@/views/Notes.vue'),
        meta: { title: '笔记本' }
      },
      {
        path: 'tag/:id',
        name: 'TagNotes',
        component: () => import('@/views/Notes.vue'),
        meta: { title: '标签' }
      },
      {
        path: 'favorites',
        name: 'Favorites',
        component: () => import('@/views/Notes.vue'),
        meta: { title: '收藏夹' }
      },
      {
        path: 'trash',
        name: 'Trash',
        component: () => import('@/views/Notes.vue'),
        meta: { title: '回收站' }
      },
      {
        path: 'note/:id',
        name: 'NoteDetail',
        component: () => import('@/views/NoteDetail.vue'),
        meta: { title: '笔记详情' }
      },
      {
        path: 'note/new',
        name: 'NewNote',
        component: () => import('@/views/NoteEditor.vue'),
        meta: { title: '新建笔记' }
      },
      {
        path: 'note/:id/edit',
        name: 'EditNote',
        component: () => import('@/views/NoteEditor.vue'),
        meta: { title: '编辑笔记' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router