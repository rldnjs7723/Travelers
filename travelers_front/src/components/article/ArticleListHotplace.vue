<!-- <template>
  <div>
    <b-col>
      <b-table
        hover
        :items="articles"
        :fields="fields"
        @row-clicked="viewArticle"
      >
        <template #cell(articleType)="data">
          {{ data.item.articleType | articleTypeFormatter }}
        </template>
        <template #cell(writeTime)="data">
          {{ data.item.writeTime | timeFormatter }}
        </template>
      </b-table>
      <table>
        <b-row class="pb-3 pt-1">
          <b-col lg="1">글번호</b-col>
          <b-col lg="2">분류</b-col>
          <b-col lg="3">제목</b-col>
          <b-col lg="2">작성자</b-col>
          <b-col lg="2">작성일</b-col>
          <b-col lg="1">조회수</b-col>
          <b-col lg="1">좋아요</b-col>
        </b-row>
        <b-row v-for="(article, index) in articles" :key="index" @click="viewArticle(article)">
          <b-col lg="1">{{ article.articleNo }}</b-col>
          <b-col lg="2">{{ article.articleType | articleTypeFormatter }}</b-col>
          <b-col lg="3">{{ article.title }}</b-col>
          <b-col lg="2">{{ article.userId }}</b-col>
          <b-col lg="2">{{ article.writeTime | timeFormatter }}</b-col>
          <b-col lg="1">{{ article.hit }}</b-col>
          <b-col lg="1">{{ article.like }}</b-col>
        </b-row>
      </table>
    </b-col>
  </div>
</template> -->

<template>
  <b-row>
    <b-col>
      <b-table
        hover
        :items="articles"
        :fields="fields"
        @row-clicked="viewArticle"
      >
        <template #cell(articleType)="data">
          {{ data.item.articleType | articleTypeFormatter }}
        </template>
        <template #cell(writeTime)="data">
          {{ data.item.writeTime | timeFormatter }}
        </template>
      </b-table>
    </b-col>
  </b-row>
</template>

<script>
import { mapActions, mapState } from "vuex";

export default {
  name: "ArticleListHotplace",
  components: {},
  data() {
    return {
      articles: [],
      fields: [
        { key: "articleNo", label: "글번호", tdClass: "tdClass" },
        { key: "articleType", label: "분류", tdClass: "tdClass" },
        { key: "title", label: "제목", tdClass: "tdSubject" },
        { key: "userId", label: "작성자", tdClass: "tdClass" },
        { key: "writeTime", label: "작성일", tdClass: "tdClass" },
        { key: "hit", label: "조회수", tdClass: "tdClass" },
        { key: "like", label: "좋아요", tdClass: "tdClass" },
      ],
    }
  },
  async created() {
    let param = {
      pg: 1,
      spp: 20,
      key: null,
      word: null,
    };
    await this.articleSearch(param);
    this.articleList.forEach(item => {
      if (item.articleType === "hotplace") {
        this.articles.push(item)
      }
    });
  },
  methods: {
    ...mapActions("articleStore", ["articleSearch"]),
    viewArticle(article) {
      this.$router.push({
        name: "Hotplaceview",
        params: {
          articleNo: article.articleNo,
        },
      });
    },
  },
  computed: {
    ...mapState("articleStore", ["articleList"]),
  },
  // filters: {
  //   articleTypeFormatter: function (data) {
  //     if (data === "hotplace") {
  //       return "핫플 후기"
  //     } else if (data === "schedule") {
  //       return "여행 계획"
  //     } else return "게시글"
  //   },
  //   timeFormatter: function (data) {
  //     const today = new Date();
  //     const microWriteTime = new Date(data);
  //     const diffMSec = (today.getTime() - microWriteTime.getTime()) / 1000;
  //     if (diffMSec < 60) return ~~diffMSec + "초 전";
  //     else if (diffMSec < 60 * 60) return ~~(diffMSec / 60) + "분 전";
  //     else if (diffMSec < 60 * 60 * 24)
  //       return ~~(diffMSec / (60 * 60)) + "시간 전";
  //     else if (diffMSec < 60 * 60 * 24 * 30)
  //       return ~~(diffMSec / (60 * 60 * 24)) + "일 전";
  //     else if (diffMSec < 60 * 60 * 24 * 30 * 12)
  //       return ~~(diffMSec / (60 * 60 * 24 * 30)) + "달 전";
  //     else return ~~(diffMSec / (60 * 60 * 24 * 30)) + "년 전";
  //   },
  // },
};
</script>

<style scoped></style>
