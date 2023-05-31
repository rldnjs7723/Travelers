<template>
  <div>
    <b-row>
      <b-col>
        <div class="alert alert-primary mt-3 text-center fw-bold" role="alert">글 목록</div>
      </b-col>
    </b-row>
    <article-search-item lg="10" />
    <b-row class="mt-2">
      <b-col lg="2" offset-lg="2">
        <router-link :to="{name: 'ArticlelistCommon'}"><img :src="imgPath.articleTypeCommonImgPath" width="30px" /></router-link>
      </b-col>
      <b-col lg="2">
        <router-link :to="{name: 'ArticlelistArticle'}"><img :src="imgPath.articleTypeArticleImgPath" width="30px" /></router-link>
      </b-col>
      <b-col lg="2">
        <router-link :to="{name: 'ArticlelistHotplace'}"><img :src="imgPath.articleTypeHotplaceImgPath" width="30px" /></router-link>
      </b-col>
      <b-col lg="2">
        <router-link :to="{name: 'ArticlelistPlan'}"><img :src="imgPath.articleTypePlanImgPath" width="30px"  /></router-link>
      </b-col>
    </b-row>
    <b-row class="mb-1">
      <b-col class="text-right" lg="2" offset-lg="10" >
        <b-button variant="outline-primary" size="sm" @click="moveWrite()"
          >글쓰기</b-button
        >
      </b-col>
    </b-row>
    <router-view />
  </div>
</template>

<script>
import ArticleSearchItem from "./item/ArticleSearchItem.vue";

export default {
  name: "ArticleList",
  components: { ArticleSearchItem },
  data() {
    return {
      imgPath: {
        articleTypeCommonImgPath: require(`@/assets/img/icon/announcement.png`),
        articleTypeArticleImgPath: require(`@/assets/img/icon/article.png`),
        articleTypeHotplaceImgPath: require(`@/assets/img/icon/hotplace.png`),
        articleTypePlanImgPath: require(`@/assets/img/icon/plan.png`),
      },
    };
  },
  methods: {
    moveWrite() {
      this.$router.push({ name: "Articlewrite" });
    },
    viewArticle(article) {
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
    showAll() {
      this.$router.push({ name: "ArticlelistArticle" })
    },
    showArticle() {
      this.$router.push({ name: "ArticlelistCommon" })
    },
    showHotplace() {
      this.$router.push({ name: "ArticlelistHotplace" })
    },
    showPlan() {
      this.$router.push({ name: "ArticlelistPlan" })
    },
  },
};
</script>

<style scoped>
.tdClass {
  width: 50px;
  text-align: center;
}
.tdSubject {
  width: 300px;
  text-align: left;
}
</style>
