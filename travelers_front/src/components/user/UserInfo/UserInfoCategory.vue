<template>
    <b-container fluid>
        <div class="profile-image">
            <b-img
                :src="
                    userInfo.profileImgInfo[0]
                        ? require(`@/assets/img/springboot/img/${userInfo.profileImgInfo[0].saveFolder}/${userInfo.profileImgInfo[0].saveFile}`)
                        : require(`@/assets/img/common/noimg.png`)
                "
                class="image"
                width="210"
                height="210"
                alt="profile"
            ></b-img>
        </div>
        <div class="link-container">
            <router-link :to="{ name: 'UserUpdate' }"
                >회원 정보 수정</router-link
            >
        </div>
        <div class="link-container">
            <router-link :to="{ name: 'UserBookmark' }"
                >북마크 관리</router-link
            >
        </div>
        <div class="link-container">
            <router-link :to="{ name: 'UserArticle' }">게시글 관리</router-link>
        </div>
    </b-container>
</template>

<script>
import { mapState } from "vuex";

export default {
    name: "UserInfoCategory",
    data() {
        return {
            // profileImage: require("@/assets/img/common/noimg.png"),
        };
    },

    computed: {
        ...mapState("userStore", ["userInfo"]),
    },

    created() {
        // if (this.userInfo.profileImgInfo.length > 0) {
        //     const img = this.userInfo.profileImgInfo[0];
        //     this.profileImage = require(`@/assets/img/springboot/img/${img.saveFolder}/${img.saveFile}`);
        // }
    },
};
</script>

<style scoped>
.profile-image {
    border: 1px solid #ccc;
    border-radius: 5px;
    /* height: 220px; */
    padding: 2px;
}

.image {
    border-radius: 5px;
}
.link-container {
    border: 1px solid #ccc;
    border-radius: 5px;
    padding: 10px 0px;
}
</style>
