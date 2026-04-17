<template>
  <div class="dashboard-container">
    <!-- 顶部标题栏 -->
    <div class="dashboard-header">
      <div class="header-left">
        <h1 class="dashboard-title">
          <i class="el-icon-data-analysis"></i>
          学院数据统计大屏
        </h1>
        <p class="dashboard-subtitle">Real-time Data Analytics Dashboard</p>
      </div>
      <div class="header-right">
        <el-button 
          type="primary" 
          size="medium" 
          icon="el-icon-refresh" 
          @click="handleRefresh"
          :loading="refreshing"
          class="refresh-btn">
          刷新数据
        </el-button>
      </div>
    </div>

    <!-- 核心指标卡片 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :xs="12" :sm="8" :md="6" :lg="3" v-for="card in statCards" :key="card.label">
        <div class="stat-card" :style="{ borderTop: '4px solid ' + card.color }">
          <div class="stat-icon" :style="{ background: card.color }">
            <i :class="card.icon"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ card.value || 0 }}</div>
            <div class="stat-label">{{ card.label }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域第一行：四个饼图并列 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24" :sm="24" :md="6">
        <el-card shadow="hover" class="chart-card">
          <div slot="header" class="chart-header">
            <i class="el-icon-user"></i>
            <span>教师职称分布</span>
          </div>
          <div ref="facultyTitleChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="6">
        <el-card shadow="hover" class="chart-card">
          <div slot="header" class="chart-header">
            <i class="el-icon-s-custom"></i>
            <span>学生学籍状态</span>
          </div>
          <div ref="studentStatusChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="6">
        <el-card shadow="hover" class="chart-card">
          <div slot="header" class="chart-header">
            <i class="el-icon-s-claim"></i>
            <span>项目级别分布</span>
          </div>
          <div ref="projectLevelChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="6">
        <el-card shadow="hover" class="chart-card">
          <div slot="header" class="chart-header">
            <i class="el-icon-medal"></i>
            <span>专利类型分布</span>
          </div>
          <div ref="patentTypeChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域第二行：院系统计对比（居中全宽） -->
    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24" :sm="24" :md="24">
        <el-card shadow="hover" class="chart-card">
          <div slot="header" class="chart-header">
            <i class="el-icon-office-building"></i>
            <span>院系统计对比</span>
          </div>
          <div ref="departmentChart" class="chart-container-large"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域第三行：两个趋势图 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24" :sm="24" :md="12">
        <el-card shadow="hover" class="chart-card">
          <div slot="header" class="chart-header">
            <i class="el-icon-document"></i>
            <span>论文发表趋势</span>
          </div>
          <div ref="paperTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12">
        <el-card shadow="hover" class="chart-card">
          <div slot="header" class="chart-header">
            <i class="el-icon-coin"></i>
            <span>项目资金趋势</span>
          </div>
          <div ref="fundingTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { 
  getOverview,
  getFacultyTitleDistribution,
  getStudentStatusDistribution,
  getPaperYearlyTrend,
  getFundingYearlyTrend,
  getDepartmentComparison,
  getProjectLevelDistribution,
  getPatentTypeDistribution,
  refreshStatistics
} from '@/api/dashboard'

export default {
  name: 'DataScreen',
  data() {
    return {
      refreshing: false,
      // 核心指标数据
      overviewData: {
        totalFaculty: 0,
        totalStudent: 0,
        totalPaper: 0,
        totalPatent: 0,
        totalProject: 0,
        totalAward: 0,
        totalFunding: 0
      },
      // 图表实例
      charts: {},
      // 图表数据
      chartData: {
        facultyTitle: [],
        studentStatus: [],
        projectLevel: [],
        paperTrend: [],
        fundingTrend: [],
        department: [],
        patentType: []
      }
    }
  },
  computed: {
    statCards() {
      return [
        { label: '教师总数', value: this.overviewData.totalFaculty, icon: 'el-icon-user', color: '#409EFF' },
        { label: '学生总数', value: this.overviewData.totalStudent, icon: 'el-icon-s-custom', color: '#67C23A' },
        { label: '论文总数', value: this.overviewData.totalPaper, icon: 'el-icon-document', color: '#E6A23C' },
        { label: '专利总数', value: this.overviewData.totalPatent, icon: 'el-icon-medal', color: '#F56C6C' },
        { label: '项目总数', value: this.overviewData.totalProject, icon: 'el-icon-s-claim', color: '#909399' },
        { label: '获奖总数', value: this.overviewData.totalAward, icon: 'el-icon-trophy', color: '#8B5CF6' },
        { label: '资金总额(万)', value: this.formatFunding(this.overviewData.totalFunding), icon: 'el-icon-coin', color: '#EC4899' }
      ]
    }
  },
  mounted() {
    this.loadData()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    this.disposeCharts()
  },
  methods: {
    // 加载所有数据
    async loadData() {
      await Promise.all([
        this.loadOverview(),
        this.loadFacultyTitle(),
        this.loadStudentStatus(),
        this.loadPaperTrend(),
        this.loadFundingTrend(),
        this.loadDepartment(),
        this.loadProjectLevel(),
        this.loadPatentType()
      ])
      this.$message.success('数据加载完成')
    },
    
    // 加载概览数据
    async loadOverview() {
      try {
        const res = await getOverview()
        this.overviewData = res.data || this.overviewData
      } catch (error) {
        console.error('加载概览数据失败:', error)
      }
    },
    
    // 加载教师职称分布
    async loadFacultyTitle() {
      try {
        const res = await getFacultyTitleDistribution()
        // 后端已通过 SQL CASE WHEN 做了中文映射
        this.chartData.facultyTitle = res.data || []
        this.initPieChart('facultyTitleChart', this.chartData.facultyTitle, '教师职称分布')
      } catch (error) {
        console.error('加载教师职称数据失败:', error)
      }
    },
    
    // 加载学生状态分布
    async loadStudentStatus() {
      try {
        const res = await getStudentStatusDistribution()
        const rawData = res.data || []
        // 中文映射：学生学籍状态
        this.chartData.studentStatus = rawData.map(item => ({
          ...item,
          name: this.mapStudentStatus(item.name)
        }))
        this.initPieChart('studentStatusChart', this.chartData.studentStatus, '学生学籍状态')
      } catch (error) {
        console.error('加载学生状态数据失败:', error)
      }
    },
    
    // 加载项目级别分布
    async loadProjectLevel() {
      try {
        const res = await getProjectLevelDistribution()
        const rawData = res.data || []
        // 中文映射：项目级别
        this.chartData.projectLevel = rawData.map(item => ({
          ...item,
          name: this.mapProjectLevel(item.name)
        }))
        this.initPieChart('projectLevelChart', this.chartData.projectLevel, '项目级别分布')
      } catch (error) {
        console.error('加载项目级别数据失败:', error)
      }
    },
    
    // 加载论文趋势
    async loadPaperTrend() {
      try {
        const res = await getPaperYearlyTrend()
        this.chartData.paperTrend = res.data || []
        this.initLineChart('paperTrendChart', this.chartData.paperTrend, '论文发表趋势', '篇')
      } catch (error) {
        console.error('加载论文趋势数据失败:', error)
      }
    },
    
    // 加载资金趋势
    async loadFundingTrend() {
      try {
        const res = await getFundingYearlyTrend()
        this.chartData.fundingTrend = res.data || []
        this.initLineChart('fundingTrendChart', this.chartData.fundingTrend, '项目资金趋势', '元')
      } catch (error) {
        console.error('加载资金趋势数据失败:', error)
      }
    },
    
    // 加载院系统计
    async loadDepartment() {
      try {
        const res = await getDepartmentComparison()
        this.chartData.department = res.data || []
        this.initBarChart('departmentChart', this.chartData.department)
      } catch (error) {
        console.error('加载院系统计数据失败:', error)
      }
    },
    
    // 加载专利类型分布
    async loadPatentType() {
      try {
        const res = await getPatentTypeDistribution()
        const rawData = res.data || []
        // 中文映射：专利类型
        this.chartData.patentType = rawData.map(item => ({
          ...item,
          name: this.mapPatentType(item.name)
        }))
        this.initPieChart('patentTypeChart', this.chartData.patentType, '专利类型分布')
      } catch (error) {
        console.error('加载专利类型数据失败:', error)
      }
    },
    
    // 初始化饼图
    initPieChart(refName, data, title) {
      const chart = echarts.init(this.$refs[refName])
      this.charts[refName] = chart
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          top: 'middle'
        },
        series: [
          {
            name: title,
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['60%', '55%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: data
          }
        ]
      }
      chart.setOption(option)
    },
    
    // 初始化折线图
    initLineChart(refName, data, title, unit) {
      const chart = echarts.init(this.$refs[refName])
      this.charts[refName] = chart
      
      const years = data.map(item => item.year)
      const values = data.map(item => item.value)
      
      const option = {
        tooltip: {
          trigger: 'axis',
          formatter: '{b}<br/>{a}: {c} ' + unit
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: years
        },
        yAxis: {
          type: 'value',
          name: unit
        },
        series: [
          {
            name: title,
            type: 'line',
            smooth: true,
            areaStyle: {
              opacity: 0.3
            },
            data: values
          }
        ]
      }
      chart.setOption(option)
    },
    
    // 初始化柱状图
    initBarChart(refName, data) {
      const chart = echarts.init(this.$refs[refName])
      this.charts[refName] = chart
      
      const departments = data.map(item => item.departmentName)
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['教师', '学生', '论文', '专利', '项目']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: departments
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '教师',
            type: 'bar',
            data: data.map(item => item.facultyCount)
          },
          {
            name: '学生',
            type: 'bar',
            data: data.map(item => item.studentCount)
          },
          {
            name: '论文',
            type: 'bar',
            data: data.map(item => item.paperCount)
          },
          {
            name: '专利',
            type: 'bar',
            data: data.map(item => item.patentCount)
          },
          {
            name: '项目',
            type: 'bar',
            data: data.map(item => item.projectCount)
          }
        ]
      }
      chart.setOption(option)
    },
    
    // 刷新数据
    async handleRefresh() {
      this.refreshing = true
      try {
        await refreshStatistics()
        await this.loadData()
        this.$message.success('统计数据刷新成功')
      } catch (error) {
        this.$message.error('刷新失败: ' + (error.message || '未知错误'))
      } finally {
        this.refreshing = false
      }
    },
    
    // 窗口大小变化
    handleResize() {
      Object.values(this.charts).forEach(chart => {
        chart && chart.resize()
      })
    },
    
    // 销毁图表
    disposeCharts() {
      Object.values(this.charts).forEach(chart => {
        chart && chart.dispose()
      })
      this.charts = {}
    },
    
    // 格式化资金
    formatFunding(value) {
      if (!value) return '0'
      return (value / 10000).toFixed(2)
    },
    
    // ==================== 枚举值中文映射 ====================
    // 注意：教师职称已通过后端 SQL CASE WHEN 映射，无需前端处理
    
    // 学生学籍状态映射
    mapStudentStatus(status) {
      const statusMap = {
        'ENROLLED': '在读',
        'GRADUATED': '已毕业',
        'SUSPENDED': '休学'
      }
      return statusMap[status] || status
    },
    
    // 项目级别映射
    mapProjectLevel(level) {
      const levelMap = {
        'NATIONAL': '国家级',
        'PROVINCIAL': '省级',
        'UNIVERSITY': '校级',
        'ENTERPRISE': '企业',
        'OTHER': '其他'
      }
      return levelMap[level] || level
    },
    
    // 专利类型映射
    mapPatentType(type) {
      const typeMap = {
        'INVENTION': '发明专利',
        'UTILITY_MODEL': '实用新型',
        'DESIGN': '外观设计'
      }
      return typeMap[type] || type
    }
  }
}
</script>

<style scoped lang="scss">
.dashboard-container {
  padding: 20px;
  background: #f0f2f5;
  min-height: calc(100vh - 84px);
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 30px 40px;
  background: linear-gradient(135deg, #1e3c72 0%, #2a5298 50%, #4a90e2 100%);
  border-radius: 12px;
  box-shadow: 0 4px 20px 0 rgba(30, 60, 114, 0.4);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: -50%;
    right: -10%;
    width: 300px;
    height: 300px;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 50%;
    pointer-events: none;
  }
  
  &::after {
    content: '';
    position: absolute;
    bottom: -30%;
    left: 10%;
    width: 200px;
    height: 200px;
    background: rgba(255, 255, 255, 0.05);
    border-radius: 50%;
    pointer-events: none;
  }
}

.header-left {
  flex: 1;
  z-index: 1;
}

.header-right {
  z-index: 1;
}

.dashboard-title {
  margin: 0;
  font-size: 32px;
  font-weight: 700;
  color: #fff;
  letter-spacing: 2px;
  display: flex;
  align-items: center;
  gap: 12px;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  
  i {
    font-size: 36px;
    animation: pulse 2s ease-in-out infinite;
  }
}

.dashboard-subtitle {
  margin: 8px 0 0 0;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.85);
  letter-spacing: 1px;
  font-weight: 300;
}

.refresh-btn {
  padding: 12px 24px;
  font-size: 15px;
  font-weight: 600;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.95);
  border: none;
  color: #1e3c72;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.25);
    background: #fff;
  }
  
  &:active {
    transform: translateY(0);
  }
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

.stats-cards {
  margin-bottom: 20px;
}

.stat-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
  margin-bottom: 20px;
  
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
  }
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  
  i {
    font-size: 28px;
    color: #fff;
  }
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  line-height: 1;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.chart-row {
  margin-bottom: 20px;
}

.chart-card {
  margin-bottom: 20px;
  
  ::v-deep .el-card__header {
    padding: 15px 20px;
    border-bottom: 1px solid #ebeef5;
  }
}

.chart-header {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  
  i {
    margin-right: 8px;
    font-size: 18px;
    color: #409EFF;
  }
}

.chart-container {
  height: 300px;
  width: 100%;
}

.chart-container-large {
  height: 400px;
  width: 100%;
}

@media (max-width: 768px) {
  .dashboard-container {
    padding: 10px;
  }
  
  .dashboard-header {
    flex-direction: column;
    align-items: flex-start;
    
    .dashboard-title {
      font-size: 20px;
      margin-bottom: 10px;
    }
  }
  
  .stat-value {
    font-size: 22px;
  }
  
  .stat-icon {
    width: 50px;
    height: 50px;
    
    i {
      font-size: 24px;
    }
  }
}
</style>
