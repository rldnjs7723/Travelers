// contextPath
function getContextPath() {
  var hostIndex = location.href.indexOf(location.host) + location.host.length;
  var contextPath = location.href.substring(hostIndex, location.href.indexOf("/", hostIndex + 1));
  return contextPath;
}

document.getElementById("select-sido").addEventListener("change", function () {
  console.log("시도: " + this.value);
  let gugunUrl = `/attraction/gugun/${this.value}`;
  fetch(gugunUrl, { method: "GET" })
    .then((response) => {
      return response.json();
    })
    .then((data) => {
      let gugunOption = `
                            <option value="0" selected>검색 할 구군 선택</option>
                            `;

      data.forEach((gugun) => {
        gugunOption += `<option value=${gugun.gugunCode}>${gugun.gugunName}</option>`;
      });

      document.getElementById("select-gugun").innerHTML = gugunOption;
    });
});

// 검색 버튼을 누르면..
// 지역, 유형, 검색어 얻기.
// 받은 데이터를 이용하여 화면 구성.
document.getElementById("btn-search").addEventListener("click", () => {
  let sidoCode = document.getElementById("select-sido").value;
  let gugunCode = document.getElementById("select-gugun").value;
  let query = "_" + document.getElementById("search-keyword").value;
  let contentType = document.querySelector(".search-content-class:checked").value;

  let searchUrl = `/attraction/${sidoCode}/${gugunCode}/${query}/${contentType}`;
  if (sidoCode == 0) {
    alert("검색할 시도를 선택해주세요.");
    return;
  } else if (gugunCode == 0) {
    alert("검색할 구군을 선택해주세요.");
    return;
  }

  fetch(searchUrl)
    .then((response) => response.json())
    .then((data) => {
      console.log(data);
      makeList(data);
    });
});

var markers = [];
function makeList(data) {
  document.querySelector("table").setAttribute("style", "display: ;");
  let trips = data;
  removeMarker();
  let positions = []; // marker 배열.
  let tripList = ``;
  if (trips != null) {
    trips.forEach((area) => {
      tripList += `
                <tr onclick="moveCenter(${area.contentId}, ${area.latitude}, ${area.longitude}, '${area.title}', '${area.addr1}', '${area.addr2}', '${area.image1}', ${area.contentTypeId});">
                    <td>
                        ${area.title}
                        <b></b><br>${area.addr1} ${area.addr2}
                    </td>
                </tr>
            `;

      let markerInfo = {
        contentId: area.contentId,
        title: area.title,
        latlng: new kakao.maps.LatLng(area.latitude, area.longitude),
        mapy: area.latitude,
        mapx: area.longitude,
        addr: area.addr1 + " " + area.addr2,
        addr1: area.addr1,
        addr2: area.addr2,
        contentTypeId: area.contentTypeId,
        image: area.image1,
      };
      positions.push(markerInfo);
    });
  }
  displayMarker(positions);
  document.getElementById("trip-list").innerHTML = tripList;
}

// 카카오지도
var mapContainer = document.getElementById("map"), // 지도를 표시할 div
  mapOption = {
    center: new kakao.maps.LatLng(37.500613, 127.036431), // 지도의 중심좌표
    level: 5, // 지도의 확대 레벨
  };

// 지도를 표시할 div와 지도 옵션으로 지도를 생성합니다
var map = new kakao.maps.Map(mapContainer, mapOption);
function displayMarker(positions) {
  // 마커 이미지의 이미지 주소입니다
  var imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";
  for (let i = 0; i < positions.length; i++) {
    // 마커 이미지의 이미지 크기 입니다
    var imageSize = new kakao.maps.Size(24, 35);

    // 마커 이미지를 생성합니다
    var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

    // 마커를 생성합니다
    var marker = new kakao.maps.Marker({
      map: map, // 마커를 표시할 지도
      position: positions[i].latlng, // 마커를 표시할 위치
      title: positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
      image: markerImage, // 마커 이미지
    });

    // 마커에 클릭이벤트를 등록합니다
    kakao.maps.event.addListener(marker, "click", function () {
      moveCenter(
        positions[i].contentId,
        positions[i].mapy,
        positions[i].mapx,
        positions[i].title,
        positions[i].addr1,
        positions[i].addr2,
        positions[i].image,
        positions[i].contentTypeId
      );
    });

    markers.push(marker);
  }
  if (positions.length != 0) {
    map.setCenter(positions[0].latlng);
  }
}

function removeMarker() {
  for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(null);
  }
  markers = [];
}
var infowindow = new kakao.maps.InfoWindow();
function moveCenter(contentId, mapy, mapx, title, addr1, addr2, image, type) {
  let contentType;
  if (type == 12) {
    contentType = " - 관광지";
  } else if (type == 14) {
    contentType = " - 문화시설";
  } else if (type == 15) {
    contentType = " - 축제공연행사";
  } else if (type == 25) {
    contentType = " - 여행코스";
  } else if (type == 28) {
    contentType = " - 레포츠";
  } else if (type == 32) {
    contentType = " - 숙박";
  } else if (type == 38) {
    contentType = " - 쇼핑";
  } else if (type == 39) {
    contentType = " - 음식점";
  }
  map.setCenter(new kakao.maps.LatLng(mapy, mapx));
  infowindow.close();
  var iwContent = `
            <div style="padding:5px; width:auto; height:auto" class="row">
                <img src="${image}" height="100px" class="col-3" alt="관광지 사진" />
                <div class="col-9">
                    <p>
                        <b>
                            ${title}
                        </b>
                        ${contentType}
                    </p>
                    <p>
                        ${addr1} ${addr2}
                    </p>
                    <div class="row">
                    <button
                        id="btn-add-schedule"
                        type="button"
                        class="btn btn-outline-primary me-3 col-4"
                        onclick="addSchedule(${contentId})"
                    >
                        여행 경로 추가
                    </button>
                    
                    <a
                        href="#"
                        data-bs-toggle="modal"
                        data-bs-target="#hotPlaceModal"
                        class="btn btn-outline-danger col-4"
                        >핫플 등록</a>
                    </div>
                </div>
              `, // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
    iwPosition = new kakao.maps.LatLng(mapy + 0.00125, mapx); // 인포윈도우 표시 위치입니다

  // 인포윈도우를 생성합니다
  infowindow = new kakao.maps.InfoWindow({
    position: iwPosition,
    content: iwContent,
  });

  // 마커 위에 인포윈도우를 표시합니다. 두번째 파라미터인 marker를 넣어주지 않으면 지도 위에 표시됩니다
  infowindow.open(map);
}

function addSchedule(contentId) {
  fetch(`/attraction/schedule/${contentId}`, { method: "PUT" })
    .then((response) => {
      if (response.ok === true) {
        return response.json();
      }
      throw new Error(response.json());
    })
    .then((data) => {
      alert(data.msg);
    })
    .catch((error) => alert(error.msg));
}
