// 검색 수행
function searchArticle() {
  let searchForm = null
  let searchData = null

  if (document.getElementById("form-article-search") !== null) {
    searchForm = document.getElementById("form-article-search");

    searchData = {
      keyword: searchForm.querySelector("[name='keyword']").value,
      order: searchForm.querySelector("[name='order']").value,
    };
  }

  if (searchForm == null) {
    searchData = {
      keyword: "",
      order: 2,
    };
  }

  let config = {
    method: "GET",
    // headers: {
    //   "Content-Type": "application/json",
    // },
    // body: JSON.stringify(searchData),
  };

  fetch(`/article?keyword=${searchData.keyword}&order=${searchData.order}`, config)
    .then((response) => {
      console.log(`/article?keyword=${searchData.keyword}&order=${searchData.order}`)
      if (response.ok === true) {
        return response.json();
      }
      throw new Error(response);
    })
    .then((data) => {
      console.log(data);
      location.href = "/article";
    })
    .catch((error) => alert("Error occurred while getting article list"));
}

function writeHotplace() {
  let hotplaceForm = document.getElementById("form-register");

  let hotplaceInfo = {
    title : String,
    content: String,
    contentTypeId : Number,
    visitDate : String,
    image : Blob,
  };
  hotplaceInfo.title = hotplaceForm.querySelector('#title').value;
  if (!hotplaceInfo.title) {
    alert("제목을 입력해주세요!");
    return;
  }

  hotplaceInfo.content = hotplaceForm.querySelector("[name='content']").value;
  if (!hotplaceInfo.content) {
    alert("내용를 입력해주세요!");
    return;
  }

  hotplaceInfo.contentTypeId = hotplaceForm.querySelector("[name='contentTypeId']").value;

  hotplaceInfo.visitDate = hotplaceForm.querySelector("[name='visitDate']").value;
  if (!hotplaceInfo.visitDate) {
    alert("날짜를 입력해주세요!");
    return;
  }

  hotplaceInfo.image = hotplaceForm.querySelector("[name='image']").value;
  if (!hotplaceInfo.image) {
    hotplaceInfo.image="";
  }
 
  let config = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(hotplaceInfo),
  };

  fetch(`/article/hotplace`, config)
    .then((response) => {
      if (response.ok === true) {
        return response;
      }
      throw new Error(response);
    })
    .then((data) => {
      // alert(data.msg);
      location.href = "/article";
    })
    .catch((error) => alert("error.msg"));

}