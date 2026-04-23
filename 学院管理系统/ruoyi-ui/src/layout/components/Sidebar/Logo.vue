<template>
  <div class="sidebar-logo-container" :class="{'collapse':collapse}" :style="{ backgroundColor: sideTheme === 'theme-dark' && navType !== 3 ? variables.menuBackground : variables.menuLightBackground }">
    <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <img v-if="logo" :src="logo" class="sidebar-logo" />
        <h1 v-else class="sidebar-title" :style="{ color: sideTheme === 'theme-dark' && navType !== 3 ? variables.logoTitleColor : variables.logoLightTitleColor }">{{ title }} </h1>
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <img v-if="logo" :src="logo" class="sidebar-logo" />
        <h1 class="sidebar-title" :style="{ color: sideTheme === 'theme-dark' && navType !== 3 ? variables.logoTitleColor : variables.logoLightTitleColor }">{{ title }} </h1>
      </router-link>
    </transition>
  </div>
</template>

<script>
import logoImg from '@/assets/logo/logo.png'
import variables from '@/assets/styles/variables.scss'

export default {
  name: 'SidebarLogo',
  props: {
    collapse: {
      type: Boolean,
      required: true
    }
  },
  computed: {
    variables() {
      return variables
    },
    sideTheme() {
      return this.$store.state.settings.sideTheme
    },
    navType() {
      return this.$store.state.settings.navType
    }
  },
  data() {
    return {
      title: process.env.VUE_APP_TITLE,
      logo: logoImg
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  // 增加Logo区域高度，与菜单项保持一致
  height: 56px;
  line-height: 56px;
  background: #1a233b;
  text-align: center;
  overflow: hidden;

  & .sidebar-logo-link {
    display: inline-flex !important;
    flex-direction: row !important;
    align-items: center !important;
    justify-content: flex-start !important;
    height: 100%;
    width: 100%;
    padding: 0 8px;
    gap: 6px;
  }

  & .sidebar-logo {
    // 增大Logo图标尺寸
    width: 40px;
    height: 40px;
    margin-right: 8px;
    margin-left : 8px;
    display: inline-block;
    flex-shrink: 0;
  }

  & .sidebar-title {
    display: inline-block;
    margin-top: 20px;
    color: #fff;
    font-weight: 700;
    line-height: 56px;
    // 增大标题字体
    font-size: 17px;
    font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
    vertical-align: middle;
    max-width: calc(100% - 36px);
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
  }

  &.collapse {
    width: 64px;
    height: 56px;

    .sidebar-logo-link {
      justify-content: center;
    }

    .sidebar-logo {
      // 折叠状态下的Logo尺寸
      width: 34px;
      height: 34px;
      margin: 0;
    }

    .sidebar-title {
      display: none;
    }
  }
}
</style>
