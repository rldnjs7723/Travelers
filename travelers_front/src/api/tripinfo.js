import apiInstance from ".";

async function sido(success, fail) {
  await apiInstance().get(`/attraction`).then(success).catch(fail);
}

async function gugun(sidoCode, success, fail) {
  await apiInstance().get(`/attraction/${sidoCode}`).then(success).catch(fail);
}

async function search(searchInfo, success, fail) {
  await apiInstance()
    .get(
      `/attraction/${searchInfo.sidoCode}/${searchInfo.gugunCode}/${searchInfo.query}/${searchInfo.contentType}`
    )
    .then(success)
    .catch(fail);
}

async function attractionDetail(contentId, success, fail) {
  await apiInstance().get(`/attraction/detail/${contentId}`).then(success).catch(fail);
}

export { sido, gugun, search, attractionDetail };
