import request from '@/utils/request'

// 查询学生基本信息及成就统计列表
export function listStudent(query) {
  return request({
    url: '/Stu/student/list',
    method: 'get',
    params: query
  })
}

// 查询学生基本信息及成就统计详细
export function getStudent(studentId) {
  return request({
    url: '/Stu/student/' + studentId,
    method: 'get'
  })
}

// 新增学生基本信息及成就统计
export function addStudent(data) {
  return request({
    url: '/Stu/student',
    method: 'post',
    data: data
  })
}

// 修改学生基本信息及成就统计
export function updateStudent(data) {
  return request({
    url: '/Stu/student',
    method: 'put',
    data: data
  })
}

// 删除学生基本信息及成就统计
export function delStudent(studentId) {
  return request({
    url: '/Stu/student/' + studentId,
    method: 'delete'
  })
}
