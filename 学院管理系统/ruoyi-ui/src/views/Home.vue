<template>
  <data-screen v-if="showDashboard" />
  <welcome v-else />
</template>

<script>
import DataScreen from '@/views/dashboard/DataScreen'
import Welcome from '@/views/Welcome'

export default {
  name: 'Home',
  components: { DataScreen, Welcome },
  computed: {
    showDashboard() {
      // 超级管理员或有 dashboard:view 权限的角色才显示数据大屏
      const roles = this.$store.getters.roles || []
      const permissions = this.$store.getters.permissions || []
      // 超级管理员（roles 为 ['admin']）始终显示大屏
      if (roles.includes('admin')) return true
      // 有 dashboard:view 权限则显示大屏（学院管理员/领导）
      return permissions.includes('dashboard:view') || permissions.includes('*:*:*')
    }
  }
}
</script>
