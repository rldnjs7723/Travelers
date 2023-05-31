import Vue from "vue";
import VueRouter from "vue-router";
import AppMain from "@/views/AppMain.vue";

import store from "@/store";

Vue.use(VueRouter);

// https://v3.router.vuejs.org/kr/guide/advanced/navigation-guards.html
const onlyAuthUser = async (to, from, next) => {
    let checkUserInfo = store.getters["userStore/checkUserInfo"];
    let checkToken = store.getters["userStore/checkToken"];
    let token = sessionStorage.getItem("access-token");
    console.log("로그인 처리 전", checkUserInfo, token);

    if (checkUserInfo != null && token) {
        console.log("토큰 유효성 체크하러 가자!!!!");
        await store.dispatch("userStore/getUserInfo", token);
    }
    checkUserInfo = store.getters["userStore/checkUserInfo"];
    checkToken = store.getters["userStore/checkToken"];
    if (!checkToken || checkUserInfo === null) {
        alert("로그인이 필요한 페이지입니다..");
        router.push({ name: "UserLogin" });
    } else {
        console.log("로그인 했다!!!!!!!!!!!!!.");
        next();
    }
};

const routes = [
    {
        path: "/",
        name: "main",
        component: AppMain,
    },
    {
        path: "/article",
        name: "article",
        component: () =>
            import(/* webpackChunkName: "article" */ "@/views/AppArticle"),
        redirect: "/article/list/common",
        children: [
            {
                path: "list",
                name: "Articlelist",
                beforeEnter: onlyAuthUser,
                component: () =>
                    import(
                        /* webpackChunkName: "article" */ "@/components/article/ArticleList"
                    ),
                redirect: "/article/list/common",
                children: [
                    {
                        path: "common",
                        name: "ArticlelistCommon",
                        beforeEnter: onlyAuthUser,
                        component: () =>
                            import(
                                /* webpackChunkName: "article" */ "@/components/article/ArticleListCommon"
                            ),
                    },
                    {
                        path: "article",
                        name: "ArticlelistArticle",
                        beforeEnter: onlyAuthUser,
                        component: () =>
                            import(
                                /* webpackChunkName: "article" */ "@/components/article/ArticleListArticle"
                            ),
                    },
                    {
                        path: "hotplace",
                        name: "ArticlelistHotplace",
                        beforeEnter: onlyAuthUser,
                        component: () =>
                            import(
                                /* webpackChunkName: "article" */ "@/components/article/ArticleListHotplace"
                            ),
                    },
                    {
                        path: "plan",
                        name: "ArticlelistPlan",
                        beforeEnter: onlyAuthUser,
                        component: () =>
                            import(
                                /* webpackChunkName: "article" */ "@/components/article/ArticleListPlan"
                            ),
                    },
                ],
            },
            {
                path: "write",
                name: "Articlewrite",
                component: () =>
                    import(
                        /* webpackChunkName: "article" */ "@/components/article/ArticleWrite"
                    ),
            },
            {
                path: "view/:articleNo/:articleType",
                name: "Articleview",
                beforeEnter: onlyAuthUser,
                component: () =>
                    import(
                        /* webpackChunkName: "article" */ "@/components/article/ArticleView"
                    ),
            },
            {
                path: "modify",
                name: "Articlemodify",
                beforeEnter: onlyAuthUser,
                component: () =>
                    import(
                        /* webpackChunkName: "article" */ "@/components/article/ArticleModify"
                    ),
            },
            {
                path: "view/:articleNo/plan",
                name: "Planview",
                beforeEnter: onlyAuthUser,
                component: () =>
                    import(
                        /* webpackChunkName: "article" */ "@/components/plan/PlanView"
                    ),
            },
            {
                path: "modify",
                name: "Planmodify",
                beforeEnter: onlyAuthUser,
                component: () =>
                    import(
                        /* webpackChunkName: "article" */ "@/components/plan/PlanModify"
                    ),
            },
        ],
    },
    {
        path: "/hotplace",
        name: "hotplace",
        component: () =>
            import(/* webpackChunkName: "hotplace" */ "@/views/AppHotplace"),
        redirect: "/hotplace/write",
        children: [
            {
                path: "write",
                name: "Hotplacewrite",
                beforeEnter: onlyAuthUser,
                component: () =>
                    import(
                        /* webpackChunkName: "hotplace" */ "@/components/hotplace/HotplaceWrite"
                    ),
            },
            {
                path: "view/:articleNo/hotplace",
                name: "Hotplaceview",
                beforeEnter: onlyAuthUser,
                component: () =>
                    import(
                        /* webpackChunkName: "hotplace" */ "@/components/hotplace/HotplaceView"
                    ),
            },
            {
                path: "modify",
                name: "Hotplacemodify",
                beforeEnter: onlyAuthUser,
                component: () =>
                    import(
                        /* webpackChunkName: "hotplace" */ "@/components/hotplace/HotplaceModify"
                    ),
            },
        ],
    },
    {
        path: "/user",
        name: "user",
        redirect: "/user/info",
        component: () =>
            import(/* webpackChunkName: "user" */ "@/views/AppUser"),
        children: [
            {
                path: "info",
                name: "UserInfo",
                redirect: "/user/info/update",
                beforeEnter: onlyAuthUser,
                component: () =>
                    import(
                        /* webpackChunkName: "user" */ "@/components/user/UserInfo"
                    ),
                children: [
                    {
                        path: "update",
                        name: "UserUpdate",
                        beforeEnter: onlyAuthUser,
                        component: () =>
                            import(
                                /* webpackChunkName: "user" */ "@/components/user/UserInfo/UserUpdate"
                            ),
                    },
                    {
                        path: "bookmark",
                        name: "UserBookmark",
                        beforeEnter: onlyAuthUser,
                        component: () =>
                            import(
                                /* webpackChunkName: "user" */ "@/components/user/UserInfo/UserBookmark"
                            ),
                    },
                    {
                        path: "article",
                        name: "UserArticle",
                        beforeEnter: onlyAuthUser,
                        component: () =>
                            import(
                                /* webpackChunkName: "user" */ "@/components/user/UserInfo/UserArticle"
                            ),
                    },
                ],
            },
            {
                path: "login",
                name: "UserLogin",
                component: () =>
                    import(
                        /* webpackChunkName: "user" */ "@/components/user/UserLogin"
                    ),
            },
            {
                path: "regist",
                name: "UserRegist",
                component: () =>
                    import(
                        /* webpackChunkName: "user" */ "@/components/user/UserRegist"
                    ),
            },
            {
                path: "find",
                name: "UserFind",
                component: () =>
                    import(
                        /* webpackChunkName: "user" */ "@/components/user/UserFind"
                    ),
            },
        ],
    },
    {
        path: "/plan",
        name: "plan",
        beforeEnter: onlyAuthUser,
        component: () =>
            import(/* webpackChunkName: "plan" */ "@/views/AppPlan"),
    },
    {
        path: "/tripinfo",
        name: "tripinfo",
        beforeEnter: onlyAuthUser,
        component: () =>
            import(/* webpackChunkName: "tripinfo" */ "@/views/AppTripInfo"),
    },
    // {
    //   path: '/about',
    //   name: 'about',
    //   // route level code-splitting
    //   // this generates a separate chunk (about.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
    // }
];

const router = new VueRouter({
    mode: "history",
    base: process.env.BASE_URL,
    routes,
});

export default router;
