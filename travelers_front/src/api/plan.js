import apiInstance from ".";

async function writePlan(pathInfo, success, fail) {
  await apiInstance()
    .post(`/attraction/schedule`, JSON.stringify(pathInfo))
    .then(success)
    .catch(fail);
}

async function getPlan(articleNo, success, fail) {
  console.log(1)
  console.dir(articleNo)
  await apiInstance().get(`/article/${articleNo}/schedule`).then(success).catch(fail);
}

async function modifyPlan(plan, success, fail) {
  // 현재 정상 작동 X
  // 추후 다른 컨트롤러로 수정 예정
  await apiInstance().put(`/article`, JSON.stringify(plan)).then(success).catch(fail);
}

async function deletePlan(articleNo, success, fail) {
  await apiInstance().delete(`/article/${articleNo}/schedule`).then(success).catch(fail);
}

export { writePlan, getPlan, modifyPlan, deletePlan };
