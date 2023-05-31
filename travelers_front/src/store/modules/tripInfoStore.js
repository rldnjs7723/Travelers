import { sido, gugun, search, attractionDetail } from "@/api/tripinfo";
import store from "@/store";

const tripInfoStore = {
  namespaced: true,
  state: {
    sidoList: [
      {
        value: null,
        text: "시도 선택",
        disabled: true,
      },
    ],
    gugunList: [
      {
        value: null,
        text: "구군 선택",
        disabled: true,
      },
    ],
    attractionList: [],
    selectedAttraction: null,
    isBookmark: false,
  },
  getters: {
    checkSelectedAttraction: function (state) {
      return state.selectedAttraction;
    },
    checkBookmark: function (state) {
      return state.isBookmark;
    },
  },
  mutations: {
    SET_SIDO: (state, sidoList) => {
      state.sidoList = sidoList;
    },
    SET_GUGUN: (state, gugunList) => {
      state.gugunList = gugunList;
    },
    SET_ATTRACTION: (state, attractionList) => {
      state.attractionList = attractionList;
    },
    SET_SELECTED_ATTRACTION: (state, attraction) => {
      state.selectedAttraction = attraction;
    },
    SET_BOOKMARK: (state, isBookmark) => {
      state.isBookmark = isBookmark;
    },
  },
  actions: {
    async getSido({ commit }) {
      await sido(
        ({ data }) => {
          let list = [
            {
              value: null,
              text: "시도 선택",
            },
          ];
          data.forEach((info) => {
            list.push({
              value: info.sidoCode,
              text: info.sidoName,
            });
          });
          commit("SET_SIDO", list);
          commit("SET_GUGUN", [
            {
              value: null,
              text: "구군 선택",
            },
          ]);
        },
        (error) => {
          console.log(error);
        }
      );
    },
    async getGugun({ commit }, sidoCode) {
      await gugun(
        sidoCode,
        ({ data }) => {
          let list = [
            {
              value: null,
              text: "구군 선택",
            },
          ];
          data.forEach((info) => {
            list.push({
              value: info.gugunCode,
              text: info.gugunName,
            });
          });

          commit("SET_GUGUN", list);
        },
        (error) => {
          console.log(error);
          commit("SET_GUGUN", [
            {
              value: null,
              text: "구군 선택",
            },
          ]);
        }
      );
    },
    async getAttraction({ commit }, searchInfo) {
      await search(
        searchInfo,
        ({ data }) => {
          commit("SET_ATTRACTION", data);
        },
        (error) => {
          console.log(error);
        }
      );
    },

    async getDetail({ commit }, contentId) {
      await attractionDetail(
        contentId,
        ({ data }) => {
          console.log(data);
          commit("SET_SELECTED_ATTRACTION", data);
        },
        (error) => {
          console.log(error);
        }
      );
    },

    select({ commit }, attraction) {
      console.log("tripInfoStore: select: ", attraction);
      let bookmarkFilter = store.getters["userStore/checkUserInfo"].attractionBookmark.filter(
        ({ contentId }) => contentId == attraction.contentId
      );
      commit("SET_BOOKMARK", bookmarkFilter.length > 0);
      commit("SET_SELECTED_ATTRACTION", attraction);
    },

    clear({ commit }) {
      commit("SET_ATTRACTION", []);
    },
  },
};

export default tripInfoStore;
