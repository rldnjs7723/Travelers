<template>
  <div class="item" @click="clickEvent">
    <b-img class="icon icon-container" :src="img" fluid alt="Icon"></b-img>
    <div class="item-text">{{ text }}</div>
  </div>
</template>

<script>
export default {
  props: ["img", "text", "click", "clickParam"],
  methods: {
    clickEvent() {
      this.click(this.clickParam);
    },
  },
};
</script>

<style scoped>
.item {
  display: inline-flex;
  align-items: center;
  border: 1px solid #ccc;
  border-radius: 20px;
  padding: 10px;
  margin: 10px;
  cursor: pointer;
}

.item-text {
  margin: auto;
}

.icon-container {
  margin-right: 10px;
}

.icon {
  width: 25px;
  height: 25px;
}
</style>
