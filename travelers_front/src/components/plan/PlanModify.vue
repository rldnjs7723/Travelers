<template>
    <b-container class="bv-example-row mt-3">
        <b-row>
        <b-col>
            <b-alert show><h3>여행 계획 수정</h3></b-alert>
        </b-col>
        </b-row>
        <plan-input-item type="modify" />
    </b-container>
</template>

<script>
import PlanInputItem from "@/components/plan/item/PlanInputItem";

export default {
    name: "PlanModify",
    components: {
        PlanInputItem,
    },
}; 
</script>

<style></style>
