<template>
  <b-card elevation="0" class="px-10 py-1" color="#e1f0eb">
    <!-- <b-card-text>
          <div style="font-size: small">
            댓글 작성
          </div>
      </b-card-text> -->

    <!-- 작성창 -->
    <!-- v-if="this.$store.state.isAuthenticated == true" -->
    <!-- <b-form @submit="writeComment" ref="form"> -->
    <b-form ref="form">
      <div class="d-flex">
        <b-textarea
          style="font-size: small"
          outlined
          v-model="comment.content"
          :disabled="false"
          placeholder="댓글 작성"
        />
        <b-button
          variant="outline-success"
          size="sm"
          @click="executeWriteComment"
          class="ml-2"
          >등록</b-button
        >
        <!-- <b-btn type="submit" class="ml-2" outlined small color="#356859"> 등록 </b-btn> -->
      </div>
    </b-form>
  </b-card>
</template>

<script>
import { mapActions, mapState } from "vuex";
import { writeComment } from "@/api/article";

export default {
  name: "CommentWrite",
  data() {
    return {
      comment: {
        articleNo: "",
        content: "",
        userId: "",
      },
    };
  },
  computed: {
    ...mapState("userStore", ["userInfo"]),
  },
  methods: {
    ...mapActions("articleStore", ["renewComments"]),
    //   writeComment(){
    //     this.member_id = this.$store.state.loginUserProfile.id
    //     const { member_id, comment } = this
    //     this.$emit("submit", { member_id, comment})
    //   }
    async executeWriteComment() {
      if (this.comment.content) {
        this.comment.articleNo = this.$route.params.articleNo;
        this.comment.userId = this.userInfo.id;
        // console.dir(this.comment)
        await writeComment(
          this.comment,
          () => {},
          (err) => {
            console.log(err);
          }
        );
        this.comment.content = "";
        await this.renewComments({ articleNo: this.$route.params.articleNo });
      }
    },
  },
  created() {},
};
</script>
