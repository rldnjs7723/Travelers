<template>
  <b-card>
    <b-card-text style="font-size: small">
      <b-row>
        <b-col lg="10" class="text-left">
          {{ item.userId }} ({{ item.writeTime | timeFormatter }})
        </b-col>
        <b-col lg="2" v-if="isOwner && !isModify">
          <div class="text-right">
            <b-button
              variant="outline-info"
              size="sm"
              @click="setModifyComment"
              class="mr-2"
              >수정</b-button
            >
            <b-button variant="outline-danger" size="sm" @click="delComment"
              >삭제</b-button
            >
          </div>
        </b-col>
      </b-row>
      <b-row>
        <b-col lg="10" class="text-left">
          <b-card-text v-if="!isModify" class="py-1">
            <div class="pl-6">
              {{ item.content }}
            </div>
          </b-card-text>
          <b-card-text v-if="isModify" class="py-1">
            <b-form @submit="submitModifiedComment" ref="form">
              <div class="d-flex" style="font-size: small">
                <b-textarea
                  style="font-size: small"
                  outlined
                  v-model="modifiedComment"
                  :disabled="false"
                  pd
                />
                <div class="flex-column">
                  <b-button
                    variant="outline-info"
                    size="sm"
                    class="mr-2"
                    @click="submitModifiedComment"
                    >완료</b-button
                  >
                  <b-button
                    variant="outline-danger"
                    size="sm"
                    @click="setModifyComment"
                    >취소</b-button
                  >
                </div>
              </div>
            </b-form>
          </b-card-text>
        </b-col>
      </b-row>
    </b-card-text>
  </b-card>
</template>

<script>
//   import { mapActions } from "vuex";
import { mapActions, mapState } from "vuex";
import { modifyComment, deleteComment } from "@/api/article";

export default {
  name: "CommentListItem",
  props: {
    item: {
      type: Object,
    },
  },
  computed: {
    ...mapState("userStore", ["userInfo"]),
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
  data() {
    return {
      isModify: false,
      modifiedComment: this.item.content,
      commentNo: "",
      textSize: 8,
      isOwner: false,
    };
  },
  methods: {
    ...mapActions("articleStore", ["renewComments"]),
    setModifyComment() {
      this.isModify = !this.isModify;
      if (this.textSize == 8) {
        this.textSize = 9;
      } else {
        this.textSize = 8;
      }
    },
    async submitModifiedComment() {
      const param = {
        commentNo: this.item.commentNo,
        content: this.modifiedComment,
      };
      await modifyComment(
        param,
        ({ data }) => {
          this.modifiedComment = data.content;
        },
        (err) => {
          console.log(err);
        }
      );
      // this.isModify = false;
      await this.renewComments({ articleNo: this.item.articleNo })
      this.setModifyComment();
    },
    async delComment() {
      if (confirm("정말 삭제하시겠습니다?")) {
        const param = {
            commentNo: this.item.commentNo,
            content: this.modifiedComment,
        };
        await deleteComment(
            param,
            () => {

            },
            (err) => {
            console.log(err);
            }
        )
        await this.renewComments({ articleNo: this.item.articleNo })
      }
      // await this.requestDeleteCommentAtBoardToSpring(this.item.commentNo);
      // await this.$router.go();
    },
  },
  created() {
    if (this.item.userId === this.userInfo.id || this.userInfo.id === "admin") {
      this.isOwner = true;
    }
  },
};
</script>
<style scoped>
/* * {
  margin: 0;
  padding: 0;
} */
</style>
