<template>
    <!-- 중앙 content start -->
    <b-container class="bv-example-row mt-3 mb-3 text-center">
        <h3 class="underline-orange">지역별여행지</h3>
        <div>
            <div
                class="alert alert-primary mt-3 text-center fw-bold"
                role="alert"
            >
                전국 관광지 정보
            </div>
            <!-- 관광지 검색 start -->
            <div class="d-flex mb-3">
                <!-- 시도 Select -->
                <b-form-select
                    class="select-location"
                    v-model="searchInfo.sidoCode"
                    :options="sidoList"
                    @change="gugunAPI"
                ></b-form-select>
                <!-- 구군 Select -->
                <b-form-select
                    class="select-location"
                    v-model="searchInfo.gugunCode"
                    :options="gugunList"
                ></b-form-select>
                <!-- 검색어 Input -->
                <b-form-input
                    class="input-search"
                    v-model="searchInfo.query"
                    placeholder="검색어"
                    @keyup.enter="searchAPI"
                ></b-form-input>
                <!-- 검색 Button -->
                <b-button
                    class="button-search"
                    variant="outline-success"
                    @click="searchAPI"
                    >검색</b-button
                >
            </div>
            <b-form-group>
                <b-form-radio-group
                    id="radio-group-content-type"
                    v-model="searchInfo.contentType"
                    :options="contentTypeList"
                    name="radio-options"
                >
                </b-form-radio-group>
            </b-form-group>

            <!-- 관광지 리스트 Start -->
            <b-card-group columns v-if="attractionList.length > 0">
                <b-card
                    v-for="attraction in attractionList"
                    :key="attraction.contentId"
                    :title="
                        attraction.title.length > 12
                            ? `${attraction.title.substr(0, 11)}...`
                            : attraction.title
                    "
                    :img-src="
                        attraction.image2
                            ? attraction.image2
                            : require('@/assets/img/common/noimage.gif')
                    "
                    img-height="230"
                    :img-alt="attraction.title"
                    img-top
                    bg-variant="light"
                    tag="article"
                    class="card-attraction mb-5"
                    @click="showModal(attraction)"
                >
                </b-card>
            </b-card-group>
            <div
                v-if="attractionList.length == 0"
                style="
                    display: flex;
                    height: 500px;
                    align-items: center;
                    justify-content: center;
                "
            >
                새로운 검색어를 입력해주세요!
            </div>
            <!-- 관광지 리스트 End -->
        </div>
        <!-- 관광지 정보 표시 모달 -->
        <trip-info-modal></trip-info-modal>
    </b-container>
    <!-- 중앙 content end -->
</template>

<script>
import { mapActions, mapState } from "vuex";
import TripInfoModal from "../components/tripinfo/TripInfoModal.vue";

export default {
    name: "AppTripInfo",
    components: {
        TripInfoModal,
    },
    async created() {
        await this.getSido();
        this.clear();
    },
    data() {
        return {
            searchInfo: {
                sidoCode: null,
                gugunCode: null,
                contentType: 0,
                query: "",
            },
            map: null,
            markers: [],
            infowindow: null,
        };
    },

    computed: {
        ...mapState(["contentTypeList"]),
        ...mapState("tripInfoStore", [
            "sidoList",
            "gugunList",
            "attractionList",
        ]),
    },
    methods: {
        ...mapActions("tripInfoStore", [
            "getSido",
            "getGugun",
            "getAttraction",
            "select",
            "clear",
        ]),
        ...mapActions("planStore", ["addAttraction"]),
        async gugunAPI() {
            await this.getGugun(this.searchInfo.sidoCode);
            this.searchInfo.gugunCode = null;
        },
        async searchAPI() {
            // 빈 문자열 전달을 막기 위해 검색어에 _ 추가
            let info = {
                ...this.searchInfo,
                query: "_" + this.searchInfo.query,
            };
            await this.getAttraction(info);

            console.log(this.attractionList);
            //   this.removeMarker();
            //   this.displayMarker();
        },

        showModal(attraction) {
            this.select(attraction);
            this.$bvModal.show("modal-tripinfo");
        },

        moveCenter(contentId, mapy, mapx, title, addr1, addr2, image, type) {
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
            this.map.setCenter(new window.kakao.maps.LatLng(mapy, mapx));

            this.attraction = {
                contentId,
                latitude: mapy,
                longitude: mapx,
                title,
                addr: addr1 + addr2,
                image,
                contentTypeId: type,
            };

            if (this.infowindow != null) this.infowindow.close();
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
                </div>
            </div>
              `, // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
                iwPosition = new window.kakao.maps.LatLng(mapy + 0.00125, mapx); // 인포윈도우 표시 위치입니다

            // 인포윈도우를 생성합니다
            this.infowindow = new window.kakao.maps.InfoWindow({
                position: iwPosition,
                content: iwContent,
            });

            // 마커 위에 인포윈도우를 표시합니다. 두번째 파라미터인 marker를 넣어주지 않으면 지도 위에 표시됩니다
            this.infowindow.open(this.map);
        },

        addSchedule() {
            console.log(this.infowindow.content);
            // console.log(attraction);
            this.addAttraction(this.attraction);
        },
    },
};
</script>

<style scoped>
/* .container {
    margin-top: 70px;
    margin-bottom: 70px;
} */
#map {
    width: 100%;
    height: 400px;
}

.select-location {
    width: 200px;
    margin-right: 5px;
}
.input-search {
    /* width: 400px; */
    margin-right: 5px;
}
.button-search {
    width: 150px;
    margin-right: 5px;
    padding: 0px;
}

.card-attraction {
    width: 300px;
    height: 300px;
}

.underline-orange {
    display: inline-block;
    /* background: linear-gradient(180deg, rgba(255, 255, 255, 0) 70%, rgba(231, 149, 27, 0.3) 30%); */
}
</style>
