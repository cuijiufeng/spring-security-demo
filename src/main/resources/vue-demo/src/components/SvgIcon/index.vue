<template>
  <svg :class="svgClass" aria-hidden="true">
    <use :xlink:href="iconName"/>
  </svg>
</template>

<script>
export default {
  name: 'svg-icon',
  props: {
    iconClass: {
      type: String,
      required: true
    },
    className: {
      type: String
    },
  },
  computed: {
    iconName () {
      return `#icon-${this.iconClass}`
    },
    svgClass () {
      return this.className 
        ? 'svg-icon ' + this.className 
        : 'svg-icon';
    },
  }
}
</script>

<style scope lang="less">
.svg-icon {
  width: 1em;
  height: 1em;
  position: relative;
  top: 0.06em;
  fill: currentColor;
}
</style>
