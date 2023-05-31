import { writePlan, getPlan } from "@/api/plan";

const planStore = {
  namespaced: true,
  state: {
    // 여행 계획에 포함된 관광지 리스트
    planList: [
      {
        day: 1,
        path: [],
      },
    ],
    plan: null,
    planChanged: false,
  },
  getters: {
    checkPlan: function (state) {
      return state.planList;
    },
    checkPlanChanged: function (state) {
      return state.planChanged;
    },
  },
  mutations: {
    SET_PLAN_LIST: (state, planList) => {
      state.planList = planList;
    },
    SET_PLAN: (state, plan) => {
      state.plan = plan;
    },
    SET_PLAN_CHANGED: (state, planChanged) => {
      state.planChanged = planChanged;
    },
  },
  actions: {
    changePlan({ commit }, planList) {
      commit("SET_PLAN_LIST", planList);
      commit("SET_PLAN_CHANGED", true);
    },

    togglePlanChanged({ commit }) {
      commit("SET_PLAN_CHANGED", false);
    },

    async updatePlanInfo({ commit }, articleNo) {
      console.log("gkdl")
      await getPlan(
        articleNo,
        ({ data }) => {
          console.log(data);
          commit("SET_PLAN", data);
        },
        (err) => {
          console.log(err);
        }
      );
    },

    importPlanInfo({ commit }, articlePlan) {
      commit("SET_PLAN_LIST", JSON.parse(articlePlan.path));
    },

    async writeArticle(context, pathInfo) {
      await writePlan(
        pathInfo,
        async ({ data }) => {
          alert(data.msg);
        },
        (error) => {
          console.log(error);
        }
      );
    },
  },
};

export default planStore;
