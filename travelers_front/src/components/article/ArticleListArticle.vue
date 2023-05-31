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
  name: "ArticleListArticle",
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
      if (item.articleType === "article") {
        this.articles.push(item)
      }
    });
  },
  methods: {
    ...mapActions("articleStore", ["articleSearch"]),
    viewArticle(article) {
      this.$router.push({
        name: "Articleview",
        params: {
          articleNo: article.articleNo,
          articleType: article.articleType,
        },
      });
    },
  },
  computed: {
    ...mapState("articleStore", ["articleList"]),
  },
};
</script>

<style scoped></style>
