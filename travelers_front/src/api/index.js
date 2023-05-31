import axios from "axios";

// axios 객체 생성
export default function apiInstance(url = process.env.VUE_APP_API_BASE_URL) {
    let instance = axios.create({
        baseURL: url,
        headers: {
            "Content-Type": "application/json;charset=utf-8",
        },
    });
    // 로그인 이후 토큰 전달 자동화
    if (sessionStorage.getItem("access-token"))
        instance.defaults.headers["Authorization"] =
            "Bearer " + sessionStorage.getItem("access-token");

    return instance;
}
