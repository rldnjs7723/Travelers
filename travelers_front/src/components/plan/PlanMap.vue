<template>
  <div id="map" class="mt-3"></div>
</template>

<script>
import { mapActions, mapGetters, mapState } from "vuex";

export default {
  data() {
    return {
      pathInfo: {
        articleType: "schedule",
        userId: "",
        startDate: "",
        endDate: "",
        title: "",
        content: "",
        path: "",
      },
      map: null,
      planLines: [],
      distanceOverlay: null,
      dots: [],
      bounds: null,
    };
  },
  mounted() {
    // api 스크립트 소스 불러오기 및 지도 출력
    this.loadScript();
  },
  computed: {
    ...mapState(["colors"]),
    ...mapState("planStore", ["planList"]),
    ...mapGetters("planStore", ["checkPlanChanged"]),
  },

  watch: {
    checkPlanChanged(value) {
      // console.log(value);
      if (value) {
        this.displayLine(this.planList);
        this.togglePlanChanged();
      }
    },
  },

  methods: {
    ...mapActions("planStore", ["togglePlanChanged"]),
    // api 불러오기
    loadScript() {
      const script = document.createElement("script");
      script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${process.env.VUE_APP_KAKAO_API_KEY}&libraries=services,clusterer,drawing&autoload=false`;
      script.onload = () => window.kakao.maps.load(this.loadMap);

      document.head.appendChild(script);
    },
    // 맵 출력하기
    loadMap() {
      const container = document.getElementById("map");
      const options = {
        center: new window.kakao.maps.LatLng(37.500613, 127.036431), // 지도의 중심좌표
        level: 5, // 지도의 확대 레벨
      };

      this.map = new window.kakao.maps.Map(container, options);
      this.displayLine(this.planList);
    },

    removeLines() {
      for (let i = 0; i < this.planLines.length; i++) {
        this.planLines[i].setMap(null);
      }

      for (let i = 0; i < this.dots.length; i++) {
        this.dots[i].setMap(null);
      }
    },

    displayLine(plans) {
      this.removeLines();

      // 지도를 재설정할 범위정보를 가지고 있을 LatLngBounds 객체를 생성합니다
      this.bounds = new window.kakao.maps.LatLngBounds();
      let totalCount = 0;

      for (let planIdx = 0; planIdx < plans.length; planIdx++) {
        this.planLines[planIdx] = new window.kakao.maps.Polyline({
          map: this.map, // 선을 표시할 지도입니다
          path: [], // 선을 구성하는 좌표 배열입니다
          strokeWeight: 3, // 선의 두께입니다
          strokeColor: this.colors[planIdx], // 선의 색깔입니다
          strokeOpacity: 1, // 선의 불투명도입니다 0에서 1 사이값이며 0에 가까울수록 투명합니다
          strokeStyle: "solid", // 선의 스타일입니다
        });

        let plan = plans[planIdx];
        totalCount += plan.path.length;
        for (var i = 0; i < plan.path.length; i++) {
          let attraction = plan.path[i];

          let location = new window.kakao.maps.LatLng(attraction.latitude, attraction.longitude);
          // 시작점으로 지도 중심 이동
          //   if (i == 0) this.map.setCenter(location);

          // LatLngBounds 객체에 좌표를 추가합니다
          this.bounds.extend(location);

          // 그려지고 있는 선의 좌표 배열을 얻어옵니다
          let path = this.planLines[planIdx].getPath();

          // 좌표 배열에 스케줄에 포함된 위치를 추가합니다
          path.push(location);

          // 다시 선에 좌표 배열을 설정하여 새로운 위치까지 선을 그리도록 설정합니다
          this.planLines[planIdx].setPath(path);

          // let distance = Math.round(this.planLines[planIdx].getLength());
          this.displayCircleDot(location, i + 1, this.colors[planIdx]);
        }
      }

      // LatLngBounds 객체에 추가된 좌표들을 기준으로 지도의 범위를 재설정합니다
      // 이때 지도의 중심좌표와 레벨이 변경될 수 있습니다
      if (totalCount > 0) this.map.setBounds(this.bounds);
    },

    moveCenter(lat, lng) {
      this.map.setCenter(new window.kakao.maps.LatLng(lat, lng));
    },

    // 선이 그려지고 있는 상태일 때 지도를 클릭하면 호출하여
    // 클릭 지점에 대한 정보 (동그라미와 클릭 지점까지의 총거리)를 표출하는 함수입니다
    displayCircleDot(position, index, color) {
      // 클릭 지점을 표시할 빨간 동그라미 커스텀오버레이를 생성합니다
      let circleOverlay = new window.kakao.maps.CustomOverlay({
        content: `<div class="dot" style="border-color: ${color}; color: ${color}">${index}</div>`,
        position: position,
        zIndex: 1,
      });

      // 지도에 표시합니다
      circleOverlay.setMap(this.map);

      // if (distance > 0) {
      //   // 클릭한 지점까지의 그려진 선의 총 거리를 표시할 커스텀 오버레이를 생성합니다
      //   let distanceOverlay = new window.kakao.maps.CustomOverlay({
      //     content:
      //       '<div class="dotOverlay">거리 <span class="number">' + distance + "</span>m</div>",
      //     position: position,
      //     yAnchor: 1,
      //     zIndex: 2,
      //   });

      //   // 지도에 표시합니다
      //   distanceOverlay.setMap(this.map);
      // }

      // 배열에 추가합니다
      this.dots.push(circleOverlay);
    },

    // 마우스 우클릭 하여 선 그리기가 종료됐을 때 호출하여
    // 그려진 선의 총거리 정보와 거리에 대한 도보, 자전거 시간을 계산하여
    // HTML Content를 만들어 리턴하는 함수입니다
    getTimeHTML(distance) {
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
    },

    // 마우스 드래그로 그려지고 있는 선의 총거리 정보를 표시하거
    // 마우스 오른쪽 클릭으로 선 그리가 종료됐을 때 선의 정보를 표시하는 커스텀 오버레이를 생성하고 지도에 표시하는 함수입니다
    showDistance(content, position) {
      // 클릭 지점을 표시할 빨간 동그라미 커스텀오버레이를 생성합니다
      let circleOverlay = new window.kakao.maps.CustomOverlay({
        content: '<span class="dot"></span>',
        position: position,
        zIndex: 1,
      });

      // 지도에 표시합니다
      circleOverlay.setMap(this.map);

      // 커스텀 오버레이를 생성하고 지도에 표시합니다
      let distanceOverlay = new window.kakao.maps.CustomOverlay({
        map: this.map, // 커스텀오버레이를 표시할 지도입니다
        content: content, // 커스텀오버레이에 표시할 내용입니다
        position: position, // 커스텀오버레이를 표시할 위치입니다.
        xAnchor: 0,
        yAnchor: 0,
        zIndex: 3,
      });

      distanceOverlay.setMap(this.map);
    },
  },
};
</script>

<style>
#map {
  width: 100%;
  height: 600px;
}

.info-window {
  background-color: lavenderblush;
  width: 250px;
}

.dot {
  overflow: hidden;
  /* float: left; */
  width: 30px;
  height: 30px;
  background-color: white;
  border-radius: 20px;
  border: 3px solid #ccc;
  text-align: center;
  /* background: url("https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/mini_circle.png"); */
}

.dotOverlay {
  position: relative;
  bottom: 10px;
  border-radius: 6px;
  border: 1px solid #ccc;
  border-bottom: 2px solid #ddd;
  float: left;
  font-size: 12px;
  padding: 5px;
  background: #fff;
}

.dotOverlay:nth-of-type(n) {
  border: 0;
  box-shadow: 0px 1px 2px #888;
}

.number {
  font-weight: bold;
  color: #ee6152;
}

.dotOverlay:after {
  content: "";
  position: absolute;
  margin-left: -6px;
  left: 50%;
  bottom: -8px;
  width: 11px;
  height: 8px;
  background: url("https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/vertex_white_small.png");
}

.distanceInfo {
  position: relative;
  top: 5px;
  left: 5px;
  list-style: none;
  margin: 0;
}

.distanceInfo .label {
  display: inline-block;
  width: 50px;
}

.distanceInfo:after {
  content: none;
}
</style>
