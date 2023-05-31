<template>
    <b-container fluid>
        <h4 class="title">게시글 목록</h4>
        <b-card class="text-center mt-3 mb-3" align="left">
            <div v-if="articleList && articleList.length > 0">
                <icon-text-item
                    v-for="article in articleList"
                    :key="article.articleNo"
                    :img="checkArticleMap.get(article.articleType).image"
                    :text="article.title"
                    :click="moveArticle"
                    :clickParam="article"
                ></icon-text-item>
            </div>
            <div v-if="!articleList || articleList.length == 0">
                작성한 게시글이 없습니다.
            </div>
        </b-card>
    </b-container>
</template>

<script>
import { mapActions, mapGetters, mapState } from "vuex";
import IconTextItem from "@/components/common/item/IconTextItem.vue";

export default {
    components: { IconTextItem },
    name: "UserArticle",
    async created() {
        let param = {
            pg: 1,
            spp: 50,
            key: "userId",
            word: this.userInfo.id,
        };
        await this.articleSearch(param);
    },
    computed: {
        ...mapState("userStore", ["userInfo"]),
        ...mapState("articleStore", ["articleList"]),
        ...mapGetters(["checkArticleMap"]),
    },
    methods: {
        ...mapActions("articleStore", ["articleSearch"]),

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
