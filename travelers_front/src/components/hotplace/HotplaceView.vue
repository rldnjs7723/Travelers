<template>
  <div>
    <b-row>
      <b-col>
        <div class="alert alert-primary mt-3 text-center fw-bold" role="alert">
          핫 플레이스 리뷰
        </div>
      </b-col>
    </b-row>
    <b-row class="mb-1">
      <b-col class="text-left">
        <b-button variant="outline-primary" size="sm" @click="moveList">목록</b-button>
      </b-col>
      <b-col class="text-right" v-if="hotplace.userId == userInfo.id || userInfo.grade == 'admin'">
        <b-button variant="outline-info" size="sm" @click="moveModifyHotplace" class="mr-2"
          >수정</b-button
        >
        <b-button variant="outline-danger" size="sm" @click="delHotplace">삭제</b-button>
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
              <img :src="imgPath.articleTypeHotplaceImgPath" width="30px" />
              {{ hotplace.articleNo }}. {{ hotplace.title }}<br />
              [{{ selectedAttraction.contentTypeId | contentTypeFormatter }}]
              {{ selectedAttraction.title }} ({{ hotplace.rate / 2 }} /
              {{ hotplace.totalRate / 2 }})
            </h4>
            <div>
              <h6>
                {{ hotplace.userId }} &bull;
                <img :src="imgPath.viewImgPath" width="20px" />
                {{ hotplace.hit }} &bull;
                <img :src="imgPath.likeImgPath" width="20px" />
                {{ hotplace.like }} &bull;
                {{ hotplace.writeTime | timeFormatter }}
              </h6>
            </div>
          </template>
          <b-card-body class="text-left">
            <trip-info-map :attraction="selectedAttraction"></trip-info-map>
            <b-row>
              <b-col lg="6" class="mt-2 mb-2 pt-2 pb-2">
                <b-carousel
                  id="carousel-1"
                  v-model="img.slide"
                  :interval="4000"
                  controls
                  indicators
                  background="#ababab"
                  img-width="480"
                  img-height="480"
                  style="text-shadow: 1px 1px 2px #333"
                >
                  <b-carousel-slide
                    v-for="(img, index) in hotplace.fileInfos"
                    :key="index"
                    :img-src="
                      require(`@/assets/img/springboot/img/${img.saveFolder}/${img.saveFile}`)
                    "
                  />
                </b-carousel>
              </b-col>
              <b-col lg="6" class="mt-2 mb-2 pt-2 pb-2">
                <div v-html="message"></div>
              </b-col>
            </b-row>
          </b-card-body>
          <template #footer>
            <b-row>
              <b-col offset-lg="5" lg="1">
                <b-icon v-if="!isLike" @click="clickLike" icon="suit-heart" font-scale="3"></b-icon>
                <b-icon v-else @click="clickLike" icon="suit-heart-fill" font-scale="3"></b-icon>
              </b-col>
              <b-col lg="1">
                <b-icon
                  v-if="!isBookmark"
                  @click="clickBookmark"
                  icon="bookmark-plus"
                  font-scale="3"
                ></b-icon>
                <b-icon
                  v-else
                  @click="clickBookmark"
                  icon="bookmark-plus-fill"
                  font-scale="3"
                ></b-icon>
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
import { getHotplace, deleteHotplace } from "@/api/hotplace";
import { updateLike, toggleBookmark, getArticleInfo } from "@/api/article";
import CommentList from "@/components/comment/CommentList.vue";
import TripInfoMap from "../tripinfo/TripInfoMap.vue";

export default {
  name: "HotplaceView",
  components: { CommentList, TripInfoMap },
  data() {
    return {
      // articles: [],
      hotplace: {},
      // mainProps: {
      //   center: true,
      //   fluidGrow: true,
      //   blank: true,
      //   blankColor: "#bbb",
      //   width: 600,
      //   height: 400,
      //   class: "my-5",
      // },
      imgPath: {
        articleTypeHotplaceImgPath: require(`@/assets/img/icon/hotplace.png`),
        viewImgPath: require(`@/assets/img/icon/views.png`),
        likeImgPath: require(`@/assets/img/icon/like.png`),
        bookmarkImgPath: require(`@/assets/img/icon/bookmark.png`),
      },
      isLike: false,
      isBookmark: false,
      img: {
        slide: 0,
        sliding: null,
      },
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
  //   contentTypeFormatter: function (data) {
  //     if (data == 12) { return "관광지"}
  //     else if (data == 14) { return "문화시설"}
  //     else if (data == 16) { return "축제공연행사"}
  //     else if (data == 25) { return "여행코스"}
  //     else if (data == 28) { return "레포츠"}
  //     else if (data == 32) { return "숙박"}
  //     else if (data == 38) { return "쇼핑"}
  //     else if (data == 39) { return "음식점"}
  //   }
  // },
  computed: {
    ...mapState("tripInfoStore", ["selectedAttraction"]),
    ...mapState("userStore", ["userInfo"]),
    message() {
      if (this.hotplace.content) return this.hotplace.content.split("\n").join("<br>");
      return "";
    },
  },
  async created() {
    await getHotplace(
      this.$route.params.articleNo,
      ({ data }) => {
        this.hotplace = data;
      },
      (err) => {
        console.log(err);
      }
    );
    const articleInfo = {
      articleNo: this.$route.params.articleNo,
      email: this.userInfo.email,
    };
    await getArticleInfo(
      articleInfo,
      ({ data }) => {
        console.log("ㅇ");
        console.dir(data);
        this.isLike = data.like;
        this.isBookmark = data.bookmark;
      },
      (err) => {
        console.log(err);
      }
    );
    await this.renewComments({ articleNo: this.$route.params.articleNo });
    await this.getDetail(this.hotplace.contentId);
  },
  methods: {
    ...mapActions("articleStore", ["renewComments"]),
    ...mapActions("tripInfoStore", ["getDetail"]),
    moveModifyHotplace() {
      this.$router.replace({
        name: "Hotplacemodify",
        params: {
          articleNo: this.hotplace.articleNo,
        },
      });
    },
    async delHotplace() {
      if (confirm("정말 삭제하시겠습니까?")) {
        await deleteHotplace(
          this.hotplace.articleNo,
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
          articleNo: this.hotplace.articleNo,
          articleType: "hotplace",
        };
        await updateLike(
          param,
          ({ data }) => {
            this.isLike = true;
            this.hotplace = data;
          },
          (err) => {
            console.log(err);
          }
        );
      }
    },
    async clickBookmark() {
      const param = {
        email: this.userInfo.email,
        articleNo: this.hotplace.articleNo,
        articleType: "hotplace",
        bookmark: this.isBookmark,
      };
      await toggleBookmark(
        param,
        () => {
          this.isBookmark = !this.isBookmark;
        },
        (err) => {
          console.log(err);
        }
      );
    },
  },
};
</script>

<style scoped></style>
