import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from "vuex-persistedstate";

// Modules
import planStore from "@/store/modules/planStore";
import tripInfoStore from "@/store/modules/tripInfoStore";
import userStore from "@/store/modules/userStore";
import articleStore from "@/store/modules/articleStore";
import hotplaceStore from "@/store/modules/hotplaceStore";
import mapStore from "./modules/mapStore";

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        contentTypeMap: [
            [12, 1],
            [14, 2],
            [15, 3],
            [25, 4],
            [28, 5],
            [32, 6],
            [38, 7],
            [39, 8],
        ],
        contentTypeList: [
            { text: "전체 선택", value: 0 },
            {
                text: "관광지",
                value: 12,
                image: require("@/assets/img/icon/landmark.png"),
            },
            {
                text: "문화시설",
                value: 14,
                image: require("@/assets/img/icon/culture.png"),
            },
            {
                text: "축제공연행사",
                value: 15,
                image: require("@/assets/img/icon/festival.png"),
            },
            {
                text: "여행코스",
                value: 25,
                image: require("@/assets/img/icon/travel.png"),
            },
            {
                text: "레포츠",
                value: 28,
                image: require("@/assets/img/icon/leports.png"),
            },
            {
                text: "숙박",
                value: 32,
                image: require("@/assets/img/icon/hotel.png"),
            },
            {
                text: "쇼핑",
                value: 38,
                image: require("@/assets/img/icon/shopping.png"),
            },
            {
                text: "음식점",
                value: 39,
                image: require("@/assets/img/icon/restaurant.png"),
            },
        ],
        articleMap: [
            [
                "article",
                {
                    image: require(`@/assets/img/icon/article.png`),
                    route: "Articleview",
                },
            ],
            [
                "hotplace",
                {
                    image: require(`@/assets/img/icon/hotplace.png`),
                    route: "Hotplaceview",
                },
            ],
            [
                "schedule",
                {
                    image: require(`@/assets/img/icon/plan.png`),
                    route: "Planview",
                },
            ],
        ],

        colors: [
            "#c356ea",
            "#8ff243",
            "#71aef2",
            "#ea5645",
            "#793079",
            "#7bb8bd",
        ],
    },
    getters: {
        checkContentType: function (state) {
            return state.contentTypeList;
        },
        checkContentTypeMap: function (state) {
            return new Map(state.contentTypeMap);
        },
        checkArticleMap: function (state) {
            return new Map(state.articleMap);
        },
    },
    mutations: {},
    actions: {},
    modules: {
        planStore,
        tripInfoStore,
        userStore,
        articleStore,
        hotplaceStore,
        mapStore,
    },

    plugins: [
        createPersistedState({
            storage: sessionStorage,
        }),
    ],
});
