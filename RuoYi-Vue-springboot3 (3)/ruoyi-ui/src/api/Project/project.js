import request from '@/utils/request'

// 查询学院承担的科研项目信息列表
export function listProject(query) {
  return request({
    url: '/Project/project/list',
    method: 'get',
    params: query
  })
}

// 查询学院承担的科研项目信息详细
export function getProject(projectId) {
  return request({
    url: '/Project/project/' + projectId,
    method: 'get'
  })
}

// 新增学院承担的科研项目信息
export function addProject(data) {
  return request({
    url: '/Project/project',
    method: 'post',
    data: data
  })
}

// 修改学院承担的科研项目信息
export function updateProject(data) {
  return request({
    url: '/Project/project',
    method: 'put',
    data: data
  })
}

// 删除学院承担的科研项目信息
export function delProject(projectId) {
  return request({
    url: '/Project/project/' + projectId,
    method: 'delete'
  })
}
