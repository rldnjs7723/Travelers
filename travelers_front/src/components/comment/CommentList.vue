<template>
  <b-card elevation="0">
    <b-card elevation="0" color="#e1f0eb">
      <!-- <b-card-text class="px-12 py-4" style="font-size: small"> -->
      <b-card-text class="text-left" style="font-size: small">
        <a href="#comment-write" style="">{{ comments.length }} 개의 댓글이 달렸습니다.</a>
      </b-card-text>
    </b-card>

    <comment-list-item
      class="px-8"
      v-for="item in comments"
      :key="item.commentNo"
      :item="item"
    />
      <!-- @submit="submitModifiedComment" -->
    
    <comment-write id="comment-write"></comment-write>

  </b-card>
</template>
  
  <script>
import CommentListItem from "@/components/comment/CommentListItem";
import CommentWrite from "@/components/comment/CommentWrite";
import { mapState } from "vuex";

export default {
  name: "CommentList",
  components: { CommentListItem, CommentWrite },
  data() {
    return {
      // comments: [],
    };
  },
  computed: {
    ...mapState("articleStore", ["comments"]),
  },
  // props: {
  //   comments: {
  //     type: Array,
  //     required: true,
  //   },
  // },
};
</script>