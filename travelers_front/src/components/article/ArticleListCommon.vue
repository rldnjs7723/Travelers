<template>
  <b-row>
    <b-col>
      <b-table
        hover
        :items="articleList"
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
  name: "ArticleListCommon",
  components: {},
  data() {
    return {
      // articles: [],
      fields: [
        { key: "articleNo", label: "글번호", tdClass: "tdClass" },
        { key: "articleType", label: "분류", tdClass: "tdClass" },
        { key: "title", label: "제목", tdClass: "tdSubject" },
        { key: "userId", label: "작성자", tdClass: "tdClass" },
        { key: "writeTime", label: "작성일", tdClass: "tdClass" },
        { key: "hit", label: "조회수", tdClass: "tdClass" },
        { key: "like", label: "좋아요", tdClass: "tdClass" },
      ],
    };
  },
  created() {
    let param = {
      pg: 1,
      spp: 20,
      key: null,
      word: null,
    };
    this.articleSearch(param);
  },
  methods: {
    ...mapActions("articleStore", ["articleSearch"]),
    viewArticle(article) {
      console.log(0)
      if (article.articleType == "hotplace") {
        this.$router.push({
          name: "Hotplaceview",
          params: {
            articleNo: article.articleNo,
          },
        });
      } else if (article.articleType == "schedule") {
        this.$router.push({
          name: "Planview",
          params: {
            articleNo: article.articleNo,
          },
        });
      } else {
        this.$router.push({
          name: "Articleview",
          params: {
            articleNo: article.articleNo,
            articleType: article.articleType,
          },
        });
      }
    },
  },
  computed: {
    ...mapState("articleStore", ["articleList"]),
  },
};
</script>

<style scoped></style>
