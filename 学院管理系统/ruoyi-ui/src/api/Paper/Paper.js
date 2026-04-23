import request from '@/utils/request'

// 查询学术论文信息列表
export function listPaper(query) {
  return request({
    url: '/Paper/Paper/list',
    method: 'get',
    params: query
  })
}

// 查询学术论文信息详细
export function getPaper(paperId) {
  return request({
    url: '/Paper/Paper/' + paperId,
    method: 'get'
  })
}

// 新增学术论文信息
export function addPaper(data) {
  return request({
    url: '/Paper/Paper',
    method: 'post',
    data: data
  })
}

// 修改学术论文信息
export function updatePaper(data) {
  return request({
    url: '/Paper/Paper',
    method: 'put',
    data: data
  })
}

// 删除学术论文信息
export function delPaper(paperId) {
  return request({
    url: '/Paper/Paper/' + paperId,
    method: 'delete'
  })
}
