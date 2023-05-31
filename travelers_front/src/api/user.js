import apiInstance from ".";

const api = apiInstance();

// 로그인
async function login(user, success, fail) {
    await api
        .post(`/user/login`, JSON.stringify(user))
        .then(success)
        .catch(fail);
}
// 아이디 찾기
async function findEmail(user, success, fail) {
    await api.get(`/user/find/${user.phone}`).then(success).catch(fail);
}

// 비밀번호 찾기
async function findPassword(user, success, fail) {
    await api
        .get(`/user/find/${user.email}/${user.phone}`)
        .then(success)
        .catch(fail);
}

// 회원 가입 아이디 중복 체크
async function idCheck(id, success, fail) {
    await api.get(`/user/idCheck/${id}`).then(success).catch(fail);
}
// 회원 가입 이메일 중복 체크
async function emailCheck(email, success, fail) {
    await api.get(`/user/emailCheck/${email}`).then(success).catch(fail);
}
// 회원 가입 연락처 중복 체크
async function phoneCheck(phone, success, fail) {
    await api.get(`/user/phoneCheck/${phone}`).then(success).catch(fail);
}

// 회원 가입
async function register(user, success, fail) {
    await api.post(`/user`, JSON.stringify(user)).then(success).catch(fail);
}

async function findById(email, success, fail) {
    // api.defaults.headers["Authorization"] = "Bearer " + sessionStorage.getItem("access-token");
    // await api.get(`/user/${email}`).then(success).catch(fail);
    await apiInstance().get(`/user/${email}`).then(success).catch(fail);
}

async function refresh(user, success, fail) {
    let api = apiInstance();
    api.defaults.headers["refreshToken"] =
        sessionStorage.getItem("refresh-token"); //axios header에 refresh-token 셋팅
    await api
        .post(`/user/refresh`, JSON.stringify(user))
        .then(success)
        .catch(fail);
}

async function logout(email, success, fail) {
    await apiInstance().get(`/user/logout/${email}`).then(success).catch(fail);
}

async function update(user, success, fail) {
    await apiInstance()
        .put(`/user`, user, {
            headers: { "Content-Type": "multipart/form-data" },
        })
        .then(success)
        .catch(fail);
}

async function deleteUser(user, success, fail) {
    await apiInstance()
        .post(`/user/delete`, JSON.stringify(user))
        .then(success)
        .catch(fail);
}

async function insertAttractionBookmark(contentId, success, fail) {
    await apiInstance()
        .put(`/user/attraction/${contentId}`)
        .then(success)
        .catch(fail);
}

async function deleteAttractionBookmark(contentId, success, fail) {
    await apiInstance()
        .delete(`/user/attraction/${contentId}`)
        .then(success)
        .catch(fail);
}

export {
    login,
    findEmail,
    findPassword,
    idCheck,
    emailCheck,
    phoneCheck,
    register,
    findById,
    refresh,
    logout,
    update,
    deleteUser,
    insertAttractionBookmark,
    deleteAttractionBookmark,
};
