<template>
  <!-- <b-row class="mb-1">
    <b-col style="text-align: left"> -->
  <b-form @submit="onSubmit" @reset="onReset" b-pa>
    <b-row>
      <b-col>
        <b-form-group id="title-group" label="제목:" label-for="title" label-cols-lg="1" label-align-lg="left">
          <b-form-input id="title" v-model="article.title" type="text" required placeholder="제목을 입력해주세요"></b-form-input>
        </b-form-group>
      </b-col>
    </b-row>
    <b-row>
      <b-col>
        <b-form-group id="content-group" label="내용:" label-for="content" label-cols-lg="1" label-align-lg="left">
          <b-form-textarea id="content" v-model="article.content" placeholder="내용을 입력해주세요" rows="10" max-rows="15"
            required></b-form-textarea>
        </b-form-group>
      </b-col>
    </b-row>
    <b-row align-h="center">
      <b-button type="submit" variant="outline-info" size="sm" class="m-1" v-if="this.type === 'register'">등록</b-button>
      <b-button type="submit" variant="outline-info" size="sm" class="m-1" v-else>수정</b-button>
      <b-button type="reset" variant="outline-danger" size="sm" class="m-1">취소</b-button>
    </b-row>
  </b-form>
  <!-- </b-col>
  </b-row> -->
</template>

<script>
import { writeArticle, getArticle, modifyArticle } from "@/api/article";
import { mapState } from "vuex";

export default {
  name: "ArticleInputItem",
  data() {
    return {
      article: {
        articleNo: 0,
        articleType: "article",
        userId: "",
        title: "",
        content: "",
      },
    };
  },
  computed: {
    ...mapState("userStore", ["userInfo"]),
  },
  props: {
    type: { type: String },
  },
  created() {
    if (this.type === "modify") {
      const article = {
        articleNo: this.$route.params.articleNo,
        articleType: this.$route.params.articleType,
      };
      getArticle(
        article,
        ({ data }) => {
          this.article = data;
        },
        (err) => {
          console.log(err);
        }
      );
    }
  },
  methods: {
    onSubmit(event) {
      event.preventDefault();
      this.article.userId = this.userInfo.id

      let err = true;
      let msg = "";
      !this.article.userId &&
        ((msg = "작성자 입력해주세요"),
          (err = false));
      err &&
        !this.article.title &&
        ((msg = "제목 입력해주세요"), (err = false));
      err &&
        !this.article.content &&
        ((msg = "내용 입력해주세요"),
          (err = false));

      if (!err) alert(msg);
      else
        this.type === "register" ? this.registArticle() : this.modifyArticle();
    },
    onReset(event) {
      event.preventDefault();
      this.article.articleNo = 0;
      this.article.title = "";
      this.article.content = "";
      this.moveList();
    },
    async registArticle() {
      await writeArticle(
        this.article,
        () => {
        },
        (err) => {
          alert(err);
        }
      );
      this.moveList();

    },
    async modifyArticle() {
      await modifyArticle(
        this.article,
        () => {
        },
        (err) => {
          alert(err);
        }
      );
      this.moveList();
      // http
      //   .put(`/article`, {
      //     articleNo: this.article.articleNo,
      //     userId: this.article.userId,
      //     title: this.article.title,
      //     content: this.article.content,
      //   })
      //   .then(({ data }) => {
      //     let msg = "수정 처리시 문제가 발생했습니다.";
      //     if (data === "success") {
      //       msg = "수정이 완료되었습니다.";
      //     }
      //     alert(msg);
      //     // 현재 route를 /list로 변경.
      // this.moveList();
      //   });
    },
    moveList() {
      this.$router.push({ name: "article" });
    },
  },
};
</script>

<style></style>
