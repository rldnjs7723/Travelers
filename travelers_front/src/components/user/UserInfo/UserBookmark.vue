<template>
    <b-container fluid>
        <h4 class="title">관광지 북마크</h4>
        <b-card class="text-center mt-3 mb-3" align="left">
            <div
                v-if="
                    userInfo.attractionBookmark &&
                    userInfo.attractionBookmark.length > 0
                "
            >
                <icon-text-item
                    v-for="attraction in userInfo.attractionBookmark"
                    :key="attraction.contentId"
                    :img="
                        contentTypeList[
                            checkContentTypeMap.get(attraction.contentTypeId)
                        ].image
                    "
                    :text="attraction.title"
                    :click="showModal"
                    :clickParam="attraction"
                ></icon-text-item>
            </div>
            <div
                v-if="
                    !userInfo.attractionBookmark ||
                    userInfo.attractionBookmark.length == 0
                "
            >
                북마크한 항목이 없습니다.
            </div>
        </b-card>

        <h4 class="title">게시글 북마크</h4>
        <b-card class="text-center mt-3 mb-3" align="left">
            <div v-if="articleBookmark && articleBookmark.length > 0">
                <icon-text-item
                    v-for="article in articleBookmark"
                    :key="article.articleNo"
                    :img="checkArticleMap.get(article.articleType).image"
                    :text="article.articleNo"
                    :click="moveArticle"
                    :clickParam="article"
                ></icon-text-item>
            </div>
            <div v-if="!articleBookmark || articleBookmark.length == 0">
                북마크한 항목이 없습니다.
            </div>
        </b-card>

        <!-- 관광지 정보 표시 모달 -->
        <trip-info-modal />
    </b-container>
</template>

<script>
import { mapActions, mapGetters, mapState } from "vuex";
import { getArticleBookmark } from "@/api/article";
import IconTextItem from "@/components/common/item/IconTextItem.vue";
import TripInfoModal from "@/components/tripinfo/TripInfoModal.vue";

export default {
    components: { IconTextItem, TripInfoModal },
    name: "UserBookmark",
    data() {
        return {
            articleBookmark: [],
        };
    },

    async created() {
        await getArticleBookmark(
            { email: this.userInfo.email },
            ({ data }) => {
                // console.log(data);
                this.articleBookmark = data;
            },
            (error) => {
                console.log(error);
            }
        );
    },
    computed: {
        ...mapState("userStore", ["userInfo"]),
        ...mapState(["contentTypeList"]),
        ...mapGetters(["checkContentTypeMap", "checkArticleMap"]),
    },
    methods: {
        ...mapActions("tripInfoStore", ["select"]),
        showModal(attraction) {
            this.select(attraction);
            this.$bvModal.show("modal-tripinfo");
        },

        moveArticle(article) {
            console.log(article);
            this.$router.push({
                name: this.checkArticleMap.get(article.articleType).route,
                params: {
                    articleNo: article.articleNo,
                },
            });
        },
    },
};
</script>

<style scoped>
.title {
    margin-top: 10px;
    margin-bottom: 5px;
}
</style>
