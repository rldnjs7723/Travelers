<template>
    <div>
        <b-row>
            <b-col>
                <div class="alert alert-primary mt-3 text-center fw-bold" role="alert">핫 플레이스 리뷰 수정</div>
            </b-col>
        </b-row>
        <hotplace-input-item type="modify" />
    </div>
</template>

<script>
import HotplaceInputItem from "@/components/hotplace/item/HotplaceInputItem";

export default {
    name: "HotplaceModify",
    components: {
        HotplaceInputItem,
    },
}; 
</script>

<style></style>
