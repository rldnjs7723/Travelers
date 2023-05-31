function moveCenter(lat, lng) {
  map.setCenter(new kakao.maps.LatLng(lat, lng));
}

function deleteSchedule(contentId) {
  fetch(`/attraction/schedule/${contentId}`, { method: "DELETE" })
    .then((response) => {
      if (response.ok === true) {
        return response.json();
      }
      throw new Error(response.json());
    })
    .then((data) => {
      alert(data.msg);
      location.href = "/schedule";
    })
    .catch((error) => alert(error.msg));
}

function getSchedule() {
  let schedule = document.getElementsByClassName("attraction");
  let scheduleInfo = [];
  for (attraction of schedule) {
    console.log(attraction.getElementsByClassName("latitude")[0].textContent);
    console.log(attraction.getElementsByClassName("longitude")[0].textContent);
    let attractionInfo = {
      latitude: attraction.getElementsByClassName("latitude")[0].textContent,
      longitude: attraction.getElementsByClassName("longitude")[0].textContent,
    };
    scheduleInfo.push(attractionInfo);
  }

  return scheduleInfo;
}

function writeSchedule() {
  let scheduleForm = document.getElementById("form-schedule-detail");

  let pathInfo = {}

  pathInfo.startDate = scheduleForm.querySelector('#startDate').value;
  if (!pathInfo.startDate) {
    alert("시작 날짜를 입력해주세요!");
    return;
  }

  pathInfo.endDate = scheduleForm.querySelector('#endDate').value;
  if (!pathInfo.endDate) {
    alert("종료 날짜를 입력해주세요!");
    return;
  }

  pathInfo.title = scheduleForm.querySelector('#title').value;
  if (!pathInfo.title) {
    alert("제목을 입력해주세요!");
    return;
  }

  pathInfo.content = scheduleForm.querySelector('#content').value;
  if (!pathInfo.content) {
    alert("내용를 입력해주세요!");
    return;
  }

  let config = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(pathInfo),
  };

  fetch(`/attraction/schedule`, config)
    .then((response) => {
      if (response.ok === true) {
        return response.json();
      }
      throw new Error(response.json());
    })
    .then((data) => {
      alert(data.msg);
      location.href = "/schedule";
    })
    .catch((error) => alert(error.msg));
}

// 카카오지도
var mapContainer = document.getElementById("schedule-map"), // 지도를 표시할 div
  mapOption = {
    center: new kakao.maps.LatLng(37.500613, 127.036431), // 지도의 중심좌표
    level: 5, // 지도의 확대 레벨
  };

// 지도를 표시할 div와 지도 옵션으로 지도를 생성합니다
var map = new kakao.maps.Map(mapContainer, mapOption);
// 선이 그려지고 있을때 마우스 움직임에 따라 그려질 선 객체 입니다
var scheduleLine = new kakao.maps.Polyline({
  map: map, // 선을 표시할 지도입니다
  path: [], // 선을 구성하는 좌표 배열입니다
  strokeWeight: 3, // 선의 두께입니다
  strokeColor: "#db4040", // 선의 색깔입니다
  strokeOpacity: 1, // 선의 불투명도입니다 0에서 1 사이값이며 0에 가까울수록 투명합니다
  strokeStyle: "solid", // 선의 스타일입니다
});
var distanceOverlay; // 선의 거리정보를 표시할 커스텀오버레이 입니다
var dots = []; // 선이 그려지고 있을때 클릭할 때마다 클릭 지점과 거리를 표시하는 커스텀 오버레이 배열입니다.

// 지도를 재설정할 범위정보를 가지고 있을 LatLngBounds 객체를 생성합니다
var bounds = new kakao.maps.LatLngBounds();

window.addEventListener("load", function () {
  var schedule = getSchedule();

  for (var i = 0; i < schedule.length; i++) {
    let attraction = schedule[i];
    console.log(attraction);

    let location = new kakao.maps.LatLng(attraction.latitude, attraction.longitude);
    // 시작점으로 지도 중심 이동
    if (i == 0) map.setCenter(location);

    // LatLngBounds 객체에 좌표를 추가합니다
    bounds.extend(location);

    // 그려지고 있는 선의 좌표 배열을 얻어옵니다
    var path = scheduleLine.getPath();

    // 좌표 배열에 스케줄에 포함된 위치를 추가합니다
    path.push(location);

    // 다시 선에 좌표 배열을 설정하여 새로운 위치까지 선을 그리도록 설정합니다
    scheduleLine.setPath(path);

    var distance = Math.round(scheduleLine.getLength());
    displayCircleDot(location, distance);
  }

  if (schedule.length != 0) {
    let attraction = schedule[0];
    console.log(attraction);

    let location = new kakao.maps.LatLng(attraction.latitude, attraction.longitude);

    // LatLngBounds 객체에 좌표를 추가합니다
    bounds.extend(location);

    // 그려지고 있는 선의 좌표 배열을 얻어옵니다
    var path = scheduleLine.getPath();

    // 좌표 배열에 스케줄에 포함된 위치를 추가합니다
    path.push(location);

    // 다시 선에 좌표 배열을 설정하여 새로운 위치까지 선을 그리도록 설정합니다
    scheduleLine.setPath(path);

    var distance = Math.round(scheduleLine.getLength());
    showDistance(getTimeHTML(distance), location);
  }

  // LatLngBounds 객체에 추가된 좌표들을 기준으로 지도의 범위를 재설정합니다
  // 이때 지도의 중심좌표와 레벨이 변경될 수 있습니다
  if (schedule.length > 0) map.setBounds(bounds);
});

// 선이 그려지고 있는 상태일 때 지도를 클릭하면 호출하여
// 클릭 지점에 대한 정보 (동그라미와 클릭 지점까지의 총거리)를 표출하는 함수입니다
function displayCircleDot(position, distance) {
  // 클릭 지점을 표시할 빨간 동그라미 커스텀오버레이를 생성합니다
  let circleOverlay = new kakao.maps.CustomOverlay({
    content: '<span class="dot"></span>',
    position: position,
    zIndex: 1,
  });

  // 지도에 표시합니다
  circleOverlay.setMap(map);

  if (distance > 0) {
    // 클릭한 지점까지의 그려진 선의 총 거리를 표시할 커스텀 오버레이를 생성합니다
    let distanceOverlay = new kakao.maps.CustomOverlay({
      content: '<div class="dotOverlay">거리 <span class="number">' + distance + "</span>m</div>",
      position: position,
      yAnchor: 1,
      zIndex: 2,
    });

    // 지도에 표시합니다
    distanceOverlay.setMap(map);
  }

  // 배열에 추가합니다
  dots.push({ circle: circleOverlay, distance: distanceOverlay });
}

// 마우스 우클릭 하여 선 그리기가 종료됐을 때 호출하여
// 그려진 선의 총거리 정보와 거리에 대한 도보, 자전거 시간을 계산하여
// HTML Content를 만들어 리턴하는 함수입니다
function getTimeHTML(distance) {
  // 도보의 시속은 평균 4km/h 이고 도보의 분속은 67m/min입니다
  var walkkTime = (distance / 67) | 0;
  var walkHour = "",
    walkMin = "";

  // 계산한 도보 시간이 60분 보다 크면 시간으로 표시합니다
  if (walkkTime > 60) {
    walkHour = '<span class="number">' + Math.floor(walkkTime / 60) + "</span>시간 ";
  }
  walkMin = '<span class="number">' + (walkkTime % 60) + "</span>분";

  // 자전거의 평균 시속은 16km/h 이고 이것을 기준으로 자전거의 분속은 267m/min입니다
  var bycicleTime = (distance / 227) | 0;
  var bycicleHour = "",
    bycicleMin = "";

  // 계산한 자전거 시간이 60분 보다 크면 시간으로 표출합니다
  if (bycicleTime > 60) {
    bycicleHour = '<span class="number">' + Math.floor(bycicleTime / 60) + "</span>시간 ";
  }
  bycicleMin = '<span class="number">' + (bycicleTime % 60) + "</span>분";

  // 거리와 도보 시간, 자전거 시간을 가지고 HTML Content를 만들어 리턴합니다
  var content = '<ul class="dotOverlay distanceInfo">';
  content += "    <li>";
  content +=
    '        <span class="label">총거리</span><span class="number">' + distance + "</span>m";
  content += "    </li>";
  content += "    <li>";
  content += '        <span class="label">도보</span>' + walkHour + walkMin;
  content += "    </li>";
  content += "    <li>";
  content += '        <span class="label">자전거</span>' + bycicleHour + bycicleMin;
  content += "    </li>";
  content += "</ul>";

  return content;
}

// 마우스 드래그로 그려지고 있는 선의 총거리 정보를 표시하거
// 마우스 오른쪽 클릭으로 선 그리가 종료됐을 때 선의 정보를 표시하는 커스텀 오버레이를 생성하고 지도에 표시하는 함수입니다
function showDistance(content, position) {
  // 클릭 지점을 표시할 빨간 동그라미 커스텀오버레이를 생성합니다
  let circleOverlay = new kakao.maps.CustomOverlay({
    content: '<span class="dot"></span>',
    position: position,
    zIndex: 1,
  });

  // 지도에 표시합니다
  circleOverlay.setMap(map);

  // 커스텀 오버레이를 생성하고 지도에 표시합니다
  let distanceOverlay = new kakao.maps.CustomOverlay({
    map: map, // 커스텀오버레이를 표시할 지도입니다
    content: content, // 커스텀오버레이에 표시할 내용입니다
    position: position, // 커스텀오버레이를 표시할 위치입니다.
    xAnchor: 0,
    yAnchor: 0,
    zIndex: 3,
  });

  distanceOverlay.setMap(map);
}
