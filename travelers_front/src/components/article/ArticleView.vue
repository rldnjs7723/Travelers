<template>
  <div>
    <b-row>
      <b-col>
        <div class="alert alert-primary mt-3 text-center fw-bold" role="alert">게시글 보기</div>
      </b-col>
    </b-row>
    <b-row class="mb-1">
      <b-col class="text-left">
        <b-button variant="outline-primary" size="sm" @click="moveList">목록</b-button>
      </b-col>
      <b-col class="text-right" v-if="article.userId == userInfo.id || userInfo.grade == 'admin'">
        <b-button
          variant="outline-info"
          size="sm"
          @click="moveModifyArticle"
          class="mr-2"
          >글수정</b-button
        >
        <b-button variant="outline-danger" size="sm" @click="delArticle"
          >글삭제</b-button
        >
      </b-col>
    </b-row>
    <b-row class="mb-1">
      <b-col>
        <b-card
          header-tag="header"
          footer-tag="footer"
          class="mb-2 text-left"
          border-variant="dark"
          no-body
        >
          <template #header>
            <h4>
              <img :src="imgPath.articleTypeArticleImgPath" width="30px" />
              {{ article.articleNo }}. {{ article.title }}
            </h4>
            <div>
              <h6>
                {{ article.userId }} &bull;
                <img :src="imgPath.viewImgPath" width="20px" />
                {{ article.hit }} &bull;
                <img :src="imgPath.likeImgPath" width="20px" />
                {{ article.like }} &bull;
                {{ article.writeTime | timeFormatter }}
              </h6>
            </div>
          </template>
          <b-card-body class="text-left">
            <div v-html="message"></div>
          </b-card-body>
          <template #footer>
            <b-row>
              <b-col offset-lg="5" lg="1">
                <!-- <b-card-img :src="imgPath.likeImgPath" alt="like" @click="clickLike" :class="{ 'bg-secondary' : isLike }" /> -->
                <b-icon v-if="!isLike" @click="clickLike" icon="suit-heart" font-scale="3"></b-icon>
                <b-icon v-else @click="clickLike" icon="suit-heart-fill" font-scale="3"></b-icon>
              </b-col>
              <b-col lg="1">
                <!-- <b-card-img :src="imgPath.bookmarkImgPath" alt="bookmark" @click="clickBookmark" :class="{ 'bg-secondary' : isBookmark }" /> -->
                <b-icon v-if="!isBookmark" @click="clickBookmark" icon="bookmark-plus" font-scale="3"></b-icon>
                <b-icon v-else @click="clickBookmark" icon="bookmark-plus-fill" font-scale="3"></b-icon>
              </b-col>
            </b-row>
          </template>
        </b-card>
      </b-col>
    </b-row>
    <b-row>
      <b-col>
        <comment-list />
      </b-col>
    </b-row>
  </div>
</template>

<script>
import { mapActions, mapState } from "vuex";
import { getArticle, deleteArticle, updateLike, toggleBookmark, getArticleInfo } from "@/api/article";
import CommentList from "@/components/comment/CommentList.vue";

export default {
  name: "ArticleView",
  components: { CommentList },
  data() {
    return {
      article: {},
      imgPath: {
        articleTypeArticleImgPath: require(`@/assets/img/icon/article.png`),
        viewImgPath: require(`@/assets/img/icon/views.png`),
        likeImgPath: require(`@/assets/img/icon/like.png`),
        bookmarkImgPath: require(`@/assets/img/icon/bookmark.png`),
      },
      isLike: false,
      isBookmark: false,
    };
  },
  // filters: {
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
  computed: {
    ...mapState("userStore", ["userInfo"]),
    message() {
      if (this.article.content)
        return this.article.content.split("\n").join("<br>");
      return "";
    },
  },
  async created() {
    // http.get(`/board/${this.$route.params.articleno}`).then(({ data }) => {
    //   this.article = data;
    // });
    const article = {
      articleNo: this.$route.params.articleNo,
      articleType: this.$route.params.articleType,
    };
    await getArticle(
      article,
      ({ data }) => {
        this.article = data;
      },
      (err) => {
        console.log(err);
      }
    );
    const articleInfo = {
      articleNo: this.$route.params.articleNo,
      email: this.userInfo.email,
    }
    await getArticleInfo(
      articleInfo,
      ({ data }) => {
        console.dir(data)
        this.isLike = data.like
        this.isBookmark = data.bookmark
      },
      (err) => {
        console.log(err)
      }
    )
    await this.renewComments({ articleNo: this.$route.params.articleNo });
  },
  methods: {
    ...mapActions("articleStore", ["renewComments"]),
    moveModifyArticle() {
      this.$router.replace({
        name: "Articlemodify",
        params: {
          articleNo: this.article.articleNo,
          articleType: this.article.articleType,
        },
      });
      //   this.$router.push({ path: `/board/modify/${this.article.articleno}` });
    },
    async delArticle() {
      if (confirm("정말 삭제하시겠습니까?")) {
        const article = {
          articleNo: this.article.articleNo,
          articleType: this.article.articleType,
        };
        await deleteArticle(
          article,
          () => {},
          (err) => {
            alert(err);
          }
        );
        this.$router.replace({ name: "Articlelist" });
      }
    },
    moveList() {
      this.$router.push({ name: "Articlelist" });
    },
    async clickLike() {
      if (!this.isLike) {
        const param = {
          email: this.userInfo.email,
          articleNo: this.article.articleNo,
          articleType: "article",
        }
        await updateLike(
          param,
          ({ data }) => {
            this.isLike = true
            this.article = data
          },
          (err) => {
            console.log(err)
          }
        )
      }
    },
    async clickBookmark() {
      const param = {
        email: this.userInfo.email,
        articleNo: this.article.articleNo,
        articleType: "article",
        bookmark: this.isBookmark,
      }
      await toggleBookmark(
        param,
        () => {
          this.isBookmark = !this.isBookmark
        },
        (err) => {
          console.log(err)
        }
      )
    }
  },
};
</script>

<style scoped></style>
