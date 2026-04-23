import request from '@/utils/request'

// 查询专利信息列表
export function listPatent(query) {
  return request({
    url: '/Patent/Patent/list',
    method: 'get',
    params: query
  })
}

// 查询专利信息详细
export function getPatent(patentId) {
  return request({
    url: '/Patent/Patent/' + patentId,
    method: 'get'
  })
}

// 新增专利信息
export function addPatent(data) {
  return request({
    url: '/Patent/Patent',
    method: 'post',
    data: data
  })
}

// 修改专利信息
export function updatePatent(data) {
  return request({
    url: '/Patent/Patent',
    method: 'put',
    data: data
  })
}

// 删除专利信息
export function delPatent(patentId) {
  return request({
    url: '/Patent/Patent/' + patentId,
    method: 'delete'
  })
}
