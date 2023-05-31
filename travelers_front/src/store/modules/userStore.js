import jwtDecode from "jwt-decode";
import store from "@/store";
import router from "@/router";
import {
    login,
    idCheck,
    emailCheck,
    phoneCheck,
    register,
    findById,
    refresh,
    logout,
    update,
    insertAttractionBookmark,
    deleteAttractionBookmark,
} from "@/api/user";

const userStore = {
    namespaced: true,
    state: {
        saveId: localStorage.getItem("saveId"),
        savedEmail: localStorage.getItem("savedEmail"),
        isLogin: false,
        isLoginError: false,
        isRegistError: false,
        isValidEmail: false,
        isValidId: false,
        isValidPhone: false,
        userInfo: null,
        isValidToken: false,
    },
    getters: {
        checkUserInfo: function (state) {
            return state.userInfo;
        },
        checkToken: function (state) {
            return state.isValidToken;
        },
    },
    mutations: {
        SET_SAVE_ID: (state, saveId) => {
            state.saveId = saveId;
            localStorage.setItem("saveId", state.saveId);
        },
        SET_SAVED_EMAIL: (state, savedEmail) => {
            state.savedEmail = savedEmail;
            localStorage.setItem("savedEmail", state.savedEmail);
        },
        SET_IS_LOGIN: (state, isLogin) => {
            state.isLogin = isLogin;
        },
        SET_IS_LOGIN_ERROR: (state, isLoginError) => {
            state.isLoginError = isLoginError;
        },
        SET_IS_REGIST_ERROR: (state, isRegistError) => {
            state.isRegistError = isRegistError;
        },
        SET_IS_VALID_EMAIL: (state, isValidEmail) => {
            state.isValidEmail = isValidEmail;
        },
        SET_IS_VALID_ID: (state, isValidId) => {
            state.isValidId = isValidId;
        },
        SET_IS_VALID_PHONE: (state, isValidPhone) => {
            state.isValidPhone = isValidPhone;
        },
        SET_IS_VALID_TOKEN: (state, isValidToken) => {
            state.isValidToken = isValidToken;
        },
        SET_USER_INFO: (state, userInfo) => {
            // state.isLogin = true;
            state.userInfo = userInfo;
        },
    },
    actions: {
        async registEmailCheck({ commit }, email) {
            await emailCheck(
                email,
                ({ data }) => {
                    // 이메일 중복
                    console.log(data);
                    commit("SET_IS_VALID_EMAIL", true);
                },
                (error) => {
                    console.log(error);
                    commit("SET_IS_VALID_EMAIL", false);
                }
            );
        },

        async registIdCheck({ commit }, id) {
            await idCheck(
                id,
                ({ data }) => {
                    console.log(data);
                    commit("SET_IS_VALID_ID", true);
                },
                (error) => {
                    console.log(error);
                    commit("SET_IS_VALID_ID", false);
                }
            );
        },

        async registPhoneCheck({ commit }, phone) {
            await phoneCheck(
                phone,
                ({ data }) => {
                    console.log(data);
                    commit("SET_IS_VALID_PHONE", true);
                },
                (error) => {
                    console.log(error);
                    commit("SET_IS_VALID_PHONE", false);
                }
            );
        },

        async userRegister({ commit }, user) {
            await register(
                user,
                ({ data }) => {
                    console.log(data);
                    commit("SET_IS_REGIST_ERROR", false);
                },
                (error) => {
                    console.log(error);
                    commit("SET_IS_REGIST_ERROR", true);
                }
            );
        },

        async userConfirm({ commit, dispatch }, user) {
            await login(
                user,
                async ({ data }) => {
                    commit("SET_SAVE_ID", user.saveId);
                    if (user.saveId == "save") {
                        commit("SET_SAVED_EMAIL", user.email);
                    } else {
                        commit("SET_SAVED_EMAIL", "");
                    }

                    console.log(data);
                    let accessToken = data["accessToken"];
                    let refreshToken = data["refreshToken"];
                    // console.log("login success token created!!!! >> ", accessToken, refreshToken);
                    commit("SET_IS_LOGIN", true);
                    commit("SET_IS_LOGIN_ERROR", false);
                    commit("SET_IS_VALID_TOKEN", true);
                    sessionStorage.setItem("access-token", accessToken);
                    sessionStorage.setItem("refresh-token", refreshToken);

                    console.log(user);
                    await dispatch("getUserInfo");
                    router.push({ name: "main" });
                },
                (error) => {
                    console.log(error);
                    commit("SET_IS_LOGIN_ERROR", true);
                }
            );
        },

        async getUserInfo({ commit, dispatch }) {
            let token = sessionStorage.getItem("access-token");
            let decodeToken = jwtDecode(token);
            let email = decodeToken.sub;

            // console.log("2. getUserInfo() decodeToken :: ", decodeToken);
            await findById(
                email,
                ({ data }) => {
                    console.log(data);
                    commit("SET_USER_INFO", data);
                },
                async (error) => {
                    console.log(
                        "getUserInfo() error code [토큰 만료되어 사용 불가능.] ::: ",
                        error.response.status
                    );
                    commit("SET_IS_VALID_TOKEN", false);
                    await dispatch("tokenRegeneration");
                }
            );
        },

        async tokenRegeneration({ commit, state, dispatch }) {
            console.log(
                "토큰 재발급 >> 기존 토큰 정보 : {}",
                sessionStorage.getItem("access-token")
            );
            console.log(state.userInfo);
            await refresh(
                state.userInfo,
                ({ data }) => {
                    let accessToken = data["accessToken"];
                    console.log("재발급 완료 >> 새로운 토큰 : {}", accessToken);
                    sessionStorage.setItem("access-token", accessToken);
                    commit("SET_IS_VALID_TOKEN", true);
                },
                async (error) => {
                    // HttpStatus.UNAUTHORIZE(401) : RefreshToken 기간 만료 >> 다시 로그인!!!!
                    if (error.response.status === 401) {
                        console.log("갱신 실패");
                        // 다시 로그인 전 DB에 저장된 RefreshToken 제거.
                        await dispatch("userLogout");
                    }
                }
            );
        },

        async userLogout({ commit }) {
            commit("SET_USER_INFO", null);
            commit("SET_IS_LOGIN", false);
            let token = sessionStorage.getItem("access-token");
            let decodeToken = jwtDecode(token);
            let email = decodeToken.sub;

            await logout(
                email,
                ({ data }) => {
                    console.log(data);
                },
                (error) => {
                    console.log(error);
                }
            );

            commit("SET_IS_LOGIN", false);
            commit("SET_USER_INFO", null);
            commit("SET_IS_VALID_TOKEN", false);
            sessionStorage.setItem("access-token", "");
            sessionStorage.setItem("refresh-token", "");
        },

        async userUpdate({ dispatch }, user) {
            console.log(user);
            await update(
                user,
                async () => {
                    await dispatch("getUserInfo", user.email);
                    alert("정보 수정이 완료되었습니다.");
                },
                (error) => {
                    console.log(error);
                    alert("비밀번호를 다시 확인해주세요.");
                }
            );
        },

        async insertAttrBookmark({ dispatch }, contentId) {
            await insertAttractionBookmark(
                contentId,
                async (data) => {
                    console.log(data);
                    await dispatch(
                        "getUserInfo",
                        store.getters["userStore/checkUserInfo"].email
                    );
                    store.commit("tripInfoStore/SET_BOOKMARK", true);
                },
                (error) => {
                    console.log(error);
                }
            );
        },

        async deleteAttrBookmark({ dispatch }, contentId) {
            await deleteAttractionBookmark(
                contentId,
                async (data) => {
                    console.log(data);
                    await dispatch(
                        "getUserInfo",
                        store.getters["userStore/checkUserInfo"].email
                    );
                    store.commit("tripInfoStore/SET_BOOKMARK", false);
                },
                (error) => {
                    console.log(error);
                }
            );
        },
    },
};

export default userStore;
