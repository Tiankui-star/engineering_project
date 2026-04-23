import request from '@/utils/request'

// 获取总体概览数据
export function getOverview() {
  return request({
    url: '/dashboard/overview',
    method: 'get'
  })
}

// 获取教师职称分布
export function getFacultyTitleDistribution() {
  return request({
    url: '/dashboard/faculty/title-distribution',
    method: 'get'
  })
}

// 获取学生学籍状态分布
export function getStudentStatusDistribution() {
  return request({
    url: '/dashboard/student/status-distribution',
    method: 'get'
  })
}

// 获取论文年度趋势
export function getPaperYearlyTrend() {
  return request({
    url: '/dashboard/paper/yearly-trend',
    method: 'get'
  })
}

// 获取项目资金年度趋势
export function getFundingYearlyTrend() {
  return request({
    url: '/dashboard/funding/yearly-trend',
    method: 'get'
  })
}

// 获取院系统计对比
export function getDepartmentComparison() {
  return request({
    url: '/dashboard/department/comparison',
    method: 'get'
  })
}

// 获取项目级别分布
export function getProjectLevelDistribution() {
  return request({
    url: '/dashboard/project/level-distribution',
    method: 'get'
  })
}

// 获取专利类型分布
export function getPatentTypeDistribution() {
  return request({
    url: '/dashboard/patent/type-distribution',
    method: 'get'
  })
}

// 手动刷新统计数据
export function refreshStatistics() {
  return request({
    url: '/dashboard/statistics/refresh',
    method: 'post'
  })
}
