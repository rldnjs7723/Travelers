<template>
    <b-container class="bv-example-row mt-3">
        <div class="alert alert-primary mt-3 text-center fw-bold" role="alert">
            마이페이지
        </div>

        <b-row class="mt-3">
            <b-col lg="3">
                <user-info-category></user-info-category>
            </b-col>
            <b-col lg="9">
                <router-view></router-view>
            </b-col>
        </b-row>
    </b-container>
</template>

<script>
import { mapState } from "vuex";
import UserInfoCategory from "./UserInfoCategory.vue";

export default {
    components: { UserInfoCategory },
    name: "UserInfo",
    methods: {
        update() {
            this.$router.push({ name: "UserUpdate" });
        },
        withdraw() {
            this.$router.push({ name: "UserDelete" });
        },
    },
    computed: {
        ...mapState("userStore", ["userInfo"]),
    },
};
</script>

<style></style>
