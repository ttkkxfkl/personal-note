import request from './request'

export const tagApi = {
  // 获取所有标签
  getAll() {
    return request.get('/tags/all')
  }
}