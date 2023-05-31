<template>
  <b-row class="mr-lg-3 pd-0">
    <b-col lg="3" offset-lg="1">
      <b-form-select
        v-model="param.key"
        :options="options"
      ></b-form-select>
    </b-col>
    <b-col >
      <b-form-input
        v-model="param.word"
        placeholder="검색어를 입력하세요"
        @keyup.enter="searchList"
      ></b-form-input>
    </b-col>
    <b-col class="text-left">
      <b-button variant="success" @click="searchList">검색</b-button>
    </b-col>
  </b-row>
</template>

<script>
import { mapActions } from "vuex";

export default {
  name: "ArticleSearchItem",
  components: {},
  data() {
    return {
      param: {
        pg: 1,
        spp: 20,
        key: "all",
        word: "",
      },
      options: [
        { value: "all", text: "검색 조건을 선택하세요" },
        { value: "title", text: "제목" },
        { value: "content", text: "내용" },
        { value: "userId", text: "작성자" },
      ],
    };
  },
  created() {},
  methods: {
    ...mapActions("articleStore", ["articleSearch"]),
    async searchList() {
      await this.articleSearch(this.param);
    },
  },
};
</script>

<style scoped></style>
