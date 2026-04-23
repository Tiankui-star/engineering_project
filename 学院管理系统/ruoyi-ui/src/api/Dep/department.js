import request from '@/utils/request'

// 查询院系信息管理列表
export function listDepartment(query) {
  return request({
    url: '/Dep/department/list',
    method: 'get',
    params: query
  })
}

// 查询院系信息管理详细
export function getDepartment(departmentId) {
  return request({
    url: '/Dep/department/' + departmentId,
    method: 'get'
  })
}

// 新增院系信息管理
export function addDepartment(data) {
  return request({
    url: '/Dep/department',
    method: 'post',
    data: data
  })
}

// 修改院系信息管理
export function updateDepartment(data) {
  return request({
    url: '/Dep/department',
    method: 'put',
    data: data
  })
}

// 删除院系信息管理
export function delDepartment(departmentId) {
  return request({
    url: '/Dep/department/' + departmentId,
    method: 'delete'
  })
}

// 模糊查询教师列表（用于系主任选择）
export function searchFaculty(query) {
  return request({
    url: '/Dep/department/searchFaculty',
    method: 'get',
    params: query
  })
}
