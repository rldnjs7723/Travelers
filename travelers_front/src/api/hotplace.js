import apiInstance from "./index.js";

async function writeHotplace(hotplace, success, fail) {
  await apiInstance().post(`/article/hotplace`, hotplace, { headers: { "Content-Type": "multipart/form-data" } }).then(success).catch(fail);
}

async function getHotplace(articleNo, success, fail) {
  await apiInstance().get(`/article/${articleNo}/hotplace`).then(success).catch(fail);
}

async function modifyHotplace(hotplace, success, fail) {
  await apiInstance().put(`/article/hotplace`, hotplace, { headers: { "Content-Type": "multipart/form-data" } }).then(success).catch(fail);
}

async function deleteHotplace(articleNo, success, fail) {
  await apiInstance().delete(`/article/${articleNo}/hotplace`).then(success).catch(fail);
}

export { writeHotplace, getHotplace, modifyHotplace, deleteHotplace };