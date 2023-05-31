<template>
    <div>
        <b-row>
            <b-col>
                <div
                    class="alert alert-primary mt-3 text-center fw-bold"
                    role="alert"
                >
                    여행 계획
                </div>
            </b-col>
        </b-row>
        <b-row class="mb-1">
            <b-col class="text-left">
                <b-button variant="outline-primary" size="sm" @click="moveList"
                    >목록</b-button
                >
            </b-col>
            <b-col class="text-right">
                <b-button
                    variant="outline-info"
                    size="sm"
                    @click="moveModifyPlan"
                    class="mr-2"
                    >복사</b-button
                >
                <b-button
                    v-if="
                        plan.userId == userInfo.id || userInfo.grade == 'admin'
                    "
                    variant="outline-danger"
                    size="sm"
                    @click="delPlan"
                    >삭제</b-button
                >
            </b-col>
        </b-row>
        <b-row class="mb-1">
            <!-- <b-col>
        <b-card
          :header-html="`<h3>${plan.articleNo}.
          ${plan.title} [${plan.hit}]</h3><div><h6>${plan.userId}</div><div>${plan.writeTime}</h6></div>`"
          class="mb-2"
          border-variant="dark"
          no-body
        >
          <b-card-body class="text-left">
            <div v-html="startDate"></div>
            <div v-html="endDate"></div>
            <div v-html="content"></div>
            <plan-view-map></plan-view-map>
          </b-card-body>
        </b-card>
      </b-col> -->
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
                            <img
                                :src="imgPath.articleTypePlanImgPath"
                                width="30px"
                            />
                            {{ plan.articleNo }}. {{ plan.title }}
                        </h4>
                        <div>
                            <h6>
                                {{ plan.userId }} &bull;
                                <img :src="imgPath.viewImgPath" width="20px" />
                                {{ plan.hit }} &bull;
                                <img :src="imgPath.likeImgPath" width="20px" />
                                {{ plan.like }} &bull;
                                {{ plan.writeTime | timeFormatter }}
                            </h6>
                        </div>
                    </template>
                    <b-card-body class="text-left">
                        <plan-view-map></plan-view-map>
                        <div v-html="content" class="pb-3"></div>
                        <!-- <div v-html="path"></div> -->
                        <b-row v-for="(days, index) in planPath" :key="index">
                            <b-col lg="1">{{ days.day }}일차: </b-col>
                            <b-col
                                class="pr-0"
                                lg="auto"
                                v-for="(path, idx) in days.path"
                                :key="idx"
                            >
                                {{ path.title }} -
                            </b-col>
                        </b-row>
                    </b-card-body>
                    <template #footer>
                        <b-row>
                            <b-col offset-lg="5" lg="1">
                                <!-- <b-card-img :src="imgPath.likeImgPath" alt="like" @click="clickLike" :class="{ 'bg-secondary' : isLike }" /> -->
                                <b-icon
                                    v-if="!isLike"
                                    @click="clickLike"
                                    icon="suit-heart"
                                    font-scale="3"
                                ></b-icon>
                                <b-icon
                                    v-else
                                    @click="clickLike"
                                    icon="suit-heart-fill"
                                    font-scale="3"
                                ></b-icon>
                            </b-col>
                            <b-col lg="1">
                                <!-- <b-card-img :src="imgPath.bookmarkImgPath" alt="bookmark" @click="clickBookmark" :class="{ 'bg-secondary' : isBookmark }" /> -->
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
import { deletePlan } from "@/api/plan";
import { updateLike, toggleBookmark, getArticleInfo } from "@/api/article";
import { mapActions, mapState } from "vuex";
import PlanViewMap from "./PlanViewMap.vue";
import CommentList from "@/components/comment/CommentList.vue";

export default {
    name: "PlanView",
    components: { PlanViewMap, CommentList },
    data() {
        return {
            planPath: [],
            imgPath: {
                articleTypePlanImgPath: require(`@/assets/img/icon/plan.png`),
                viewImgPath: require(`@/assets/img/icon/views.png`),
                likeImgPath: require(`@/assets/img/icon/like.png`),
                bookmarkImgPath: require(`@/assets/img/icon/bookmark.png`),
            },
            isLike: false,
            isBookmark: false,
        };
    },
    computed: {
        ...mapState("planStore", ["plan"]),
        ...mapState("userStore", ["userInfo"]),
        content() {
            if (this.plan.content)
                return this.plan.content.split("\n").join("<br>");
            return "";
        },
        startDate() {
            if (this.plan.startDate) return this.plan.startDate;
            return "";
        },
        endDate() {
            if (this.plan.endDate) return this.plan.endDate;
            return "";
        },
        path() {
            if (this.plan.path) return JSON.parse(this.plan.path);
            return "";
        },
    },
    async created() {
        console.log(33);
        // this.planPath = JSON.stringify(this.plan.path);
        await this.updatePlanInfo(this.$route.params.articleNo);
        this.planPath = JSON.parse(this.plan.path);
        console.log(3);
        console.log(this.$route.params.articleNo);
        const articleInfo = {
            articleNo: this.$route.params.articleNo,
            email: this.userInfo.email,
        };
        await getArticleInfo(
            articleInfo,
            ({ data }) => {
                console.dir(data);
                this.isLike = data.like;
                this.isBookmark = data.bookmark;
            },
            (err) => {
                console.log(err);
            }
        );
        await this.renewComments({ articleNo: this.$route.params.articleNo });
    },
    methods: {
        ...mapActions("planStore", ["updatePlanInfo", "importPlanInfo"]),
        ...mapActions("articleStore", ["renewComments"]),
        moveModifyPlan() {
            this.importPlanInfo(this.plan);

            this.$router.replace({
                name: "plan",
                // params: {
                //   articleNo: this.plan.articleNo,
                // },
            });
        },
        async delPlan() {
            if (confirm("정말 삭제하시겠습니까?")) {
                await deletePlan(
                    this.plan.articleNo,
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
                };
                await updateLike(
                    param,
                    ({ data }) => {
                        this.isLike = true;
                        this.article = data;
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
                articleNo: this.article.articleNo,
                articleType: "article",
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
