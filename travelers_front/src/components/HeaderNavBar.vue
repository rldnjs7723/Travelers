<template>
    <div>
        <b-navbar
            class="navbar"
            fixed="top"
            toggleable="lg"
            type="light"
            variant="light"
        >
            <b-navbar-brand>
                <router-link class="logo" :to="{ name: 'main' }">
                    Travelers
                </router-link>
            </b-navbar-brand>

            <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

            <b-collapse id="nav-collapse" is-nav>
                <!-- 로그인 시 헤더 바 Start -->
                <b-navbar-nav v-if="isLogin">
                    <b-nav-item>
                        <router-link
                            :to="{ name: 'tripinfo' }"
                            class="m-2 link"
                        >
                            <!-- <b-icon icon="house-fill" animation="fade" font-scale="2"></b-icon> -->
                            지역별여행지
                        </router-link>
                        <router-link :to="{ name: 'plan' }" class="m-2 link">
                            <!-- <b-icon icon="calendar-check" animation="fade" font-scale="2"></b-icon> -->
                            나의여행계획
                        </router-link>
                        <router-link
                            :to="{ name: 'hotplace' }"
                            class="m-2 link"
                        >
                            <!-- <b-icon icon="instagram" animation="fade" font-scale="2"></b-icon> -->
                            핫플자랑하기
                        </router-link>
                        <router-link :to="{ name: 'article' }" class="m-2 link">
                            <!-- <b-icon icon="journal" animation="fade" font-scale="2"></b-icon> -->
                            여행정보공유
                        </router-link>
                    </b-nav-item>
                </b-navbar-nav>

                <b-navbar-nav class="ml-auto" v-if="isLogin">
                    <b-nav-item class="align-self-center" v-if="userInfo">
                        <b-avatar
                            :src="
                                userInfo.profileImgInfo[0]
                                    ? require(`@/assets/img/springboot/img/${userInfo.profileImgInfo[0].saveFolder}/${userInfo.profileImgInfo[0].saveFile}`)
                                    : ``
                            "
                            class="mr-2 avatar"
                        ></b-avatar>
                        <!-- :text="userInfo.id.charAt(0).toUpperCase()" -->
                        <router-link class="avatar" :to="{ name: 'UserInfo' }"
                            >{{ userInfo.name }}({{ userInfo.id }})님
                            환영합니다.</router-link
                        >
                    </b-nav-item>
                    <b-nav-item-dropdown right>
                        <template #button-content>
                            <b-icon icon="people" font-scale="2"></b-icon>
                        </template>

                        <b-dropdown-item>
                            <router-link :to="{ name: 'user' }" class="link">
                                <b-icon icon="person-circle"></b-icon>
                                마이페이지
                            </router-link>
                        </b-dropdown-item>
                        <b-dropdown-item>
                            <!-- <router-link :to="{ name: 'login' }" class="link">
                <b-icon icon="key"></b-icon> 로그아웃
              </router-link> -->
                            <a @click="logout" class="link">
                                <b-icon icon="key"></b-icon>
                                로그아웃</a
                            >
                        </b-dropdown-item>
                    </b-nav-item-dropdown>
                </b-navbar-nav>
                <!-- 로그인 시 헤더 바 End -->

                <!-- 로그아웃 시 헤더 바 Start -->
                <b-navbar-nav class="ml-auto" v-if="!isLogin">
                    <b-nav-item-dropdown right>
                        <template #button-content>
                            <b-icon icon="people" font-scale="2"></b-icon>
                        </template>
                        <b-dropdown-item>
                            <router-link
                                :to="{ name: 'UserRegist' }"
                                class="link"
                            >
                                <b-icon icon="person-circle"></b-icon> 회원가입
                            </router-link>
                        </b-dropdown-item>
                        <b-dropdown-item>
                            <router-link
                                :to="{ name: 'UserLogin' }"
                                class="link"
                            >
                                <b-icon icon="key"></b-icon> 로그인
                            </router-link>
                        </b-dropdown-item>
                    </b-nav-item-dropdown>
                </b-navbar-nav>
                <!-- 로그아웃 시 헤더 바 End -->
            </b-collapse>
        </b-navbar>
    </div>
</template>

<script>
import { mapActions, mapState } from "vuex";

export default {
    name: "HeaderNavBar",
    data() {
        return {
            // profileImg: null,
        };
    },
    created() {
        // this.profileImg = userInfo.profileImg
        // let img = this.userInfo.profileImgInfo[0];
        // this.profileImg = ;
    },
    computed: {
        ...mapState("userStore", ["isLogin", "userInfo"]),
    },
    methods: {
        ...mapActions("userStore", ["userLogout"]),
        ...mapActions("planStore", ["changePlan"]),
        async logout() {
            this.userLogout();
            this.changePlan([
                {
                    day: 1,
                    path: [],
                },
            ]);
            if (this.$route.fullPath != "/") {
                this.$router.push({ name: "main" });
            }
        },
    },
};
</script>

<style scoped>
.navbar {
    width: 80%;
    border-radius: 40px;
    margin-left: 10%;
    margin-top: 15px;
    padding: 10px 30px;
}
.logo {
    /* width: 120px; */
    font-family: GreatVibes-Regular;
    font-size: xx-large;
    text-decoration: none;
}

.link {
    text-decoration: none;
}

.avatar {
    text-decoration: none;
}

a {
    color: #212121;
    opacity: 0.9;
}

a:hover {
    color: #89bfef;
}
</style>
