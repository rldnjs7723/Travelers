<template>
    <div>
        <b-row>
            <b-col>
                <div class="alert alert-primary mt-3 text-center fw-bold" role="alert">게시글 수정</div>
            </b-col>
        </b-row>
        <article-input-item type="modify" />
    </div>
</template>

<script>
import ArticleInputItem from "@/components/article/item/ArticleInputItem";
// import { modifyArticle } from "@/api/article";

export default {
    name: "ArticleModify",
    components: {
        ArticleInputItem,
    },
}; 
</script>

<style></style>
