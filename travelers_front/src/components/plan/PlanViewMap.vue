<template>
  <div id="map" class="mt-3"></div>
</template>

<script>
import { mapState } from "vuex";

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
    ...mapState("planStore", ["plan"]),
  },
  methods: {
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
      let plans = JSON.parse(this.plan.path);
      this.displayLine(plans);
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

      // 배열에 추가합니다
      this.dots.push(circleOverlay);
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
