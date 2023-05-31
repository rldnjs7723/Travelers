<template>
  <div id="map" class="mt-3"></div>
</template>

<script>
import { mapActions, mapState, mapGetters } from "vuex";
export default {
  name: "TripInfoMap",
  props: ["attraction"],
  data() {
    return {
      map: null,
      marker: null,
      markerIcon: null,
    };
  },

  mounted() {
    // api 스크립트 소스 불러오기 및 지도 출력
    this.loadScript();
  },
  computed: {
    ...mapState(["contentTypeList"]),
    ...mapState("tripInfoStore", ["iconSource"]),
    ...mapGetters(["checkContentTypeMap"]),
  },
  methods: {
    ...mapActions("mapStore", ["setMap"]),
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
      this.displayMarker();
    },

    // 마커 제거
    removeMarker() {
      if (this.marker) this.marker.setMap(null);
      if (this.markerIcon) this.markerIcon.setMap(null);
    },

    displayMarker() {
      let markerSrc = require("@/assets/img/icon/marker.png");
      let iconSrc =
        this.contentTypeList[this.checkContentTypeMap.get(this.attraction.contentTypeId)].image;

      // 커스텀 오버레이에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
      let content = `
              <div class="marker-icon">
                <img src="${markerSrc}" alt="marker" style="width: 140px; height: 140px;">
                <img src="${iconSrc}" alt="marker" style="position: absolute; width: 45px; height: 45px; top: 23px; left: 48px;">
              </div>
              `;

      // 커스텀 오버레이가 표시될 위치입니다
      let position = new window.kakao.maps.LatLng(
        this.attraction.latitude,
        this.attraction.longitude
      );

      // 커스텀 오버레이를 생성합니다
      this.markerIcon = new window.kakao.maps.CustomOverlay({
        map: this.map,
        position: position,
        content: content,
        yAnchor: 1,
      });

      this.map.setCenter(position);
    },
  },
};
</script>

<style scoped>
#map {
  width: 100%;
  height: 400px;
}
</style>
