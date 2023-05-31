<template>
  <div>
    <b-row>
      <b-col>
        <div class="alert alert-primary mt-3 text-center fw-bold" role="alert">게시글 작성</div>
      </b-col>
    </b-row>
    <article-input-item type="register" />
  </div>
</template>

<script>
import ArticleInputItem from "@/components/article/item/ArticleInputItem";

export default {
  name: 'ArticleWrite',
  components: {
    ArticleInputItem,
  },
  data() {
    return {
    };
  },
  created() {},
  methods: {},
};
</script>

<style scoped></style>