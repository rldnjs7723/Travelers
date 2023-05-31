<template>
  <b-modal size="lg" id="modal-tripinfo" :static="true" :lazy="true">
    <!-- Modal Header Start -->
    <template #modal-header="{}">
      <h3>
        {{ selectedAttraction.title }}

        <b-img
          class="icon"
          :src="contentTypeList[checkContentTypeMap.get(selectedAttraction.contentTypeId)].image"
          fluid
          alt="ContentTypeIcon"
        ></b-img>
      </h3>
      <b-icon
        v-if="!isBookmark"
        icon="bookmark"
        font-scale="2"
        @click="insertAttractionBookmark(selectedAttraction.contentId)"
      ></b-icon>
      <b-icon
        v-if="isBookmark"
        icon="bookmark-fill"
        font-scale="2"
        @click="deleteAttractionBookmark(selectedAttraction.contentId)"
      ></b-icon>
    </template>
    <!-- Modal Header End -->

    <!-- Modal Content Start -->
    <h5>이미지</h5>
    <b-img
      v-if="selectedAttraction && selectedAttraction.image1"
      :src="selectedAttraction.image1"
      fluid
      alt="image1"
    ></b-img>

    <h5>지도</h5>
    <trip-info-map :attraction="selectedAttraction"></trip-info-map>

    <h5>주소</h5>
    <p class="my-4" v-if="selectedAttraction">
      {{ selectedAttraction.addr1 + " " + selectedAttraction.addr2 }}
    </p>

    <h5 v-if="selectedAttraction && selectedAttraction.tel">전화번호</h5>
    <p class="my-4" v-if="selectedAttraction && selectedAttraction.tel">
      {{ selectedAttraction.tel }}
    </p>

    <h5>설명</h5>
    <p class="my-4" v-if="selectedAttraction">{{ selectedAttraction.overview }}</p>

    <!-- Modal Content End -->

    <!-- Modal Footer Start -->
    <template #modal-footer><div></div> </template>
    <!-- Modal Footer End -->
  </b-modal>
</template>

<script>
import { mapActions, mapState, mapGetters } from "vuex";
import TripInfoMap from "./TripInfoMap.vue";
export default {
  components: { TripInfoMap },
  computed: {
    ...mapState(["contentTypeList"]),
    ...mapState("tripInfoStore", ["selectedAttraction", "isBookmark"]),
    ...mapGetters(["checkContentTypeMap"]),
  },
  methods: {
    ...mapActions("userStore", ["insertAttrBookmark", "deleteAttrBookmark"]),
    async insertAttractionBookmark(contentId) {
      await this.insertAttrBookmark(contentId);
    },
    async deleteAttractionBookmark(contentId) {
      await this.deleteAttrBookmark(contentId);
    },
  },
};
</script>

<style scoped>
.icon {
  width: 40px;
  height: 40px;
}

h5 {
  margin: 10px;
}
</style>
