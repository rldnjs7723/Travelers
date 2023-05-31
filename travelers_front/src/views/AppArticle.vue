<template>
  <div>
    <b-container class="bv-example-row mb-3 mt-3 text-center">
      <h3>자유게시판</h3>
      <router-view></router-view>
    </b-container>
  </div>
</template>

<script>
export default {
  name: "AppArticle",
  components: {},
  data() {
    return {
    };
  },
  created() {},
  methods: {},
};
</script>

<style scoped>
.underline-hotpink {
  display: inline-block;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0) 70%, rgba(231, 27, 139, 0.3) 30%);
}
</style>
