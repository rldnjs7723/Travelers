import apiInstance from "./index.js";

async function listArticle(param, success, fail) {
  await apiInstance().get(`/article`, { params: param }).then(success).catch(fail);
  // await api.get(`/article`).then(success).catch(fail);
  // 백에서 pathVariable(params)로 받기
}

async function writeArticle(article, success, fail) {
  await apiInstance().post(`/article`, JSON.stringify(article)).then(success).catch(fail);
}

async function getArticle(article, success, fail) {
  await apiInstance().get(`/article/${article.articleNo}/${article.articleType}`).then(success).catch(fail);
}

async function modifyArticle(article, success, fail) {
  await apiInstance().put(`/article`, JSON.stringify(article)).then(success).catch(fail);
}

async function deleteArticle(article, success, fail) {
  await apiInstance().delete(`/article/${article.articleNo}/${article.articleType}`).then(success).catch(fail);
}

async function listComment(param, success, fail) {
  await apiInstance().get(`/article/comment/${param.articleNo}`, { params: param }).then(success).catch(fail);
}

async function writeComment(comment, success, fail) {
  await apiInstance().post(`/article/comment`, JSON.stringify(comment)).then(success).catch(fail);
}

async function modifyComment(comment, success, fail) {
  console.dir(comment)
  await apiInstance().put(`/article/comment`, JSON.stringify(comment)).then(success).catch(fail);
}

async function deleteComment(param, success, fail) {
  console.dir(param)
  await apiInstance().delete(`/article/comment/`, { params: param }).then(success).catch(fail);
}

async function updateLike(param, success, fail) {
  await apiInstance().put(`/article/like`, JSON.stringify(param)).then(success).catch(fail);
}

async function toggleBookmark(param, success, fail) {
  await apiInstance().put(`/article/bookmark/`, JSON.stringify(param)).then(success).catch(fail);
}

async function getArticleInfo(param, success, fail) {
  await apiInstance().get(`/article/info/`, { params: param }).then(success).catch(fail);
}

async function getArticleBookmark(param, success, fail) {
  await apiInstance().get(`/article/infos/`, { params: param }).then(success).catch(fail);
}

export { listArticle, writeArticle, getArticle, modifyArticle, deleteArticle, listComment, writeComment, modifyComment, deleteComment, updateLike, toggleBookmark, getArticleInfo, getArticleBookmark };