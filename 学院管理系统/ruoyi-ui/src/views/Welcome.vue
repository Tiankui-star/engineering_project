<template>
  <div class="welcome-container">
    <el-card class="welcome-card" shadow="never">
      <div class="welcome-body">
        <div class="welcome-icon">
          <i class="el-icon-office-building"></i>
        </div>
        <h2 class="welcome-title">欢迎使用学院科研管理系统</h2>
        <p class="welcome-sub">Hi，{{ name }}，请从左侧菜单选择功能模块开始操作</p>
        <el-divider />
        <el-row :gutter="20" class="welcome-tips" type="flex" justify="center">
          <el-col :span="6" class="tip-item">
            <i class="el-icon-document"></i>
            <span>管理科研论文</span>
          </el-col>
          <el-col :span="6" class="tip-item">
            <i class="el-icon-medal"></i>
            <span>管理专利成果</span>
          </el-col>
          <el-col :span="6" class="tip-item">
            <i class="el-icon-s-management"></i>
            <span>管理科研项目</span>
          </el-col>
          <el-col :span="6" class="tip-item">
            <i class="el-icon-user"></i>
            <span>维护个人档案</span>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Welcome',
  computed: {
    ...mapGetters(['name'])
  }
}
</script>

<style lang="scss" scoped>
.welcome-container {
  padding: 40px;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  min-height: 60vh;
}

.welcome-card {
  width: 100%;
  max-width: 700px;
  border-radius: 12px;
}

.welcome-body {
  text-align: center;
  padding: 20px 0;
}

.welcome-icon {
  font-size: 64px;
  color: #409EFF;
  margin-bottom: 16px;
}

.welcome-title {
  font-size: 24px;
  color: #303133;
  margin: 0 0 10px;
}

.welcome-sub {
  font-size: 15px;
  color: #909399;
  margin-bottom: 0;
}

.welcome-tips {
  margin-top: 10px;
}

.tip-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  color: #606266;
  font-size: 13px;

  i {
    font-size: 28px;
    color: #409EFF;
  }
}
</style>
