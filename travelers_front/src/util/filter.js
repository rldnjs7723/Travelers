import Vue from "vue";

// 게시글 타입 필터
Vue.filter('articleTypeFormatter', (data) => {
  if (data === "hotplace") {
    return "핫플 후기"
  } else if (data === "schedule") {
    return "여행 계획"
  } else return "게시글"
});

// 관광지 유형 코드 필터
Vue.filter('contentTypeFormatter', (data) => {
  if (data == 12) { return "관광지" }
  else if (data == 14) { return "문화시설" }
  else if (data == 16) { return "축제공연행사" }
  else if (data == 25) { return "여행코스" }
  else if (data == 28) { return "레포츠" }
  else if (data == 32) { return "숙박" }
  else if (data == 38) { return "쇼핑" }
  else if (data == 39) { return "음식점" }
});

// 작성 시간 필터
Vue.filter('timeFormatter', (data) => {
  const today = new Date();
  const microWriteTime = new Date(data);
  const diffMSec = (today.getTime() - microWriteTime.getTime()) / 1000;
  if (diffMSec < 60) return ~~diffMSec + "초 전";
  else if (diffMSec < 60 * 60) return ~~(diffMSec / 60) + "분 전";
  else if (diffMSec < 60 * 60 * 24)
    return ~~(diffMSec / (60 * 60)) + "시간 전";
  else if (diffMSec < 60 * 60 * 24 * 30)
    return ~~(diffMSec / (60 * 60 * 24)) + "일 전";
  else if (diffMSec < 60 * 60 * 24 * 30 * 12)
    return ~~(diffMSec / (60 * 60 * 24 * 30)) + "달 전";
  else return ~~(diffMSec / (60 * 60 * 24 * 30)) + "년 전";
});