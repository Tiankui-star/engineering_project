import request from '@/utils/request'
// 查询教职工信息及科研统计列表
export function listFaculty(query) {
  return request({
    url: '/Faculty/faculty/list',
    method: 'get',
    params: query
  })
}

// 查询教职工信息及科研统计详细
export function getFaculty(facultyId) {
  return request({
    url: '/Faculty/faculty/' + facultyId,
    method: 'get'
  })
}

// 新增教职工信息及科研统计
export function addFaculty(data) {
  return request({
    url: '/Faculty/faculty',
    method: 'post',
    data: data
  })
}

// 修改教职工信息及科研统计
export function updateFaculty(data) {
  return request({
    url: '/Faculty/faculty',
    method: 'put',
    data: data
  })
}

// 删除教职工信息及科研统计
export function delFaculty(facultyId) {
  return request({
    url: '/Faculty/faculty/' + facultyId,
    method: 'delete'
  })
}
