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
  height: 50px;
  line-height: 50px;
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
    width: 35px;
    height: 35px;
    margin-right: 6px;
    margin-left : 6px;
    display: inline-block;
    flex-shrink: 0;
  }

  & .sidebar-title {
    display: inline-block;
    margin-top: 20px;
    color: #fff;
    font-weight: 700;
    line-height: 50px;
    font-size: 16px;
    font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
    vertical-align: middle;
    max-width: calc(100% - 36px);
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
  }

  &.collapse {
    width: 54px;
    height: 50px;

    .sidebar-logo-link {
      justify-content: center;
    }

    .sidebar-logo {
      width: 30px;
      height: 30px;
      margin: 0;
    }

    .sidebar-title {
      display: none;
    }
  }
}
</style>
