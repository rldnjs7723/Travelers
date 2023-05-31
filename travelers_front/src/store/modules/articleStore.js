import { listArticle, listComment } from "@/api/article";

const articleStore = {
  namespaced: true,
  state: {
    articleList: [],
    comments: [],
  },
  getters: {
  },
  mutations: {
    SET_ARTICLE_LIST: (state, articleList) => {
      state.articleList = articleList
    },
    SET_COMMENT_LIST: (state, comments) => {
      state.comments = comments
    },
  },
  actions: {
    // destructuring 활용
    async articleSearch( { commit }, param) {
      await listArticle(
        param,
        ({ data }) => {
          commit("SET_ARTICLE_LIST", data)
        },
        (err) => {
          alert(err)
        }
      )
    },
    async renewComments({ commit }, param) {
      await listComment(
        param,
        ({ data }) => {
          commit("SET_COMMENT_LIST", data)
        },
        (err) => {
          alert(err)
        }
      )
    }
  },
};

export default articleStore;
