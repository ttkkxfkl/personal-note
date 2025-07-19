import request from './request'

export const notebookApi = {
  // 获取所有笔记本
  getAll() {
    return request.get('/notebooks/all')
  },

  // 分页获取笔记本
  getPage(params = {}) {
    return request.get('/notebooks', { params })
  },

  // 根据ID获取笔记本
  getById(id) {
    return request.get(`/notebooks/${id}`)
  },

  // 创建笔记本
  create(data) {
    return request.post('/notebooks', data)
  },

  // 更新笔记本
  update(id, data) {
    return request.put(`/notebooks/${id}`, data)
  },

  // 删除笔记本
  delete(id) {
    return request.delete(`/notebooks/${id}`)
  },

  // 搜索笔记本
  search(keyword, params = {}) {
    return request.get('/notebooks/search', {
      params: { keyword, ...params }
    })
  }
}