<template>
  <b-form @submit="onSubmit" @reset="onReset" b-pa>
    <b-row>
      <b-col lg="6">
        <b-form-group
          id="contentId-group"
          label="관광지:"
          label-for="contentId"
          label-cols-lg="2"
          label-align-lg="left"
        >
          <b-form-select
            v-if="this.type === 'register'"
            id="contentId"
            align-h="start"
            v-model="hotplaceInfo"
            :options="options"
          />
          <b-form-select
            v-if="this.type === 'modify'"
            id="contentId"
            align-h="start"
            v-model="hotplaceInfo"
            :options="options"
            disabled
          />
        </b-form-group>
      </b-col>
      <b-col>
        <star-rating v-model="hotplace.rate" :increment="0.5" :star-size="30" />
      </b-col>
    </b-row>
    <b-row>
      <b-col>
        <b-form-group
          id="thumbNail-group"
          label="썸네일:"
          label-for="thumbNail"
          label-cols-lg="1"
          label-align-lg="left"
        >
          <b-form-file
            v-if="this.type === 'register'"
            id="thumbNail"
            v-model="hotplace.thumbNail"
            :state="Boolean(hotplace.thumbNail)"
            placeholder="파일을 업로드하거나 이곳에 드랍하세요"
            drop-placeholder="이곳에 파일을 드랍하세요"
            accept=".jpg, .png, .gif"
            multiple
            required
          ></b-form-file>
          <b-form-file
            v-if="this.type === 'modify'"
            id="thumbNail"
            :state="Boolean(hotplace.thumbNail)"
            :placeholder="`${hotplace.fileInfos[0].originalFile}`"
            accept=".jpg, .png, .gif"
            disabled
          ></b-form-file>
        </b-form-group>
      </b-col>
    </b-row>
    <b-row>
      <b-col>
        <b-form-group
          id="visitDate-group"
          label="방문 날짜:"
          label-for="visitDate"
          label-cols-lg="1"
          label-align-lg="left"
        >
          <b-form-datepicker
            id="visitDate"
            v-model="hotplace.visitDate"
            class="mb-2"
            required
          />
        </b-form-group>
      </b-col>
    </b-row>
    <b-row>
      <b-col>
        <b-form-group
          id="title-group"
          label="제목:"
          label-for="title"
          label-cols-lg="1"
          label-align-lg="left"
        >
          <b-form-input
            id="title"
            v-model="hotplace.title"
            type="text"
            required
            placeholder="핫 플레이스 이름을 입력해주세요"
          ></b-form-input>
        </b-form-group>
      </b-col>
    </b-row>
    <b-row>
      <b-col>
        <b-form-group 
          id="content-group" 
          label="내용:" 
          label-for="content" 
          label-cols-lg="1"
          label-align-lg="left">
          <b-form-textarea
            id="content"
            v-model="hotplace.content"
            placeholder="핫 플레이스 설명을 입력해주세요"
            rows="10"
            max-rows="15"
            required
          ></b-form-textarea>
        </b-form-group>
      </b-col>
    </b-row>
    <b-row align-h="center">
        <b-button
          type="submit"
          variant="outline-info" 
          size="sm" 
          class="m-1"
          v-if="this.type === 'register'"
          >등록</b-button
        >
        <b-button type="submit" variant="outline-info" size="sm" class="m-1" v-else
          >수정</b-button
        >
        <b-button type="reset" variant="outline-danger" size="sm" class="m-1">취소</b-button>
    </b-row>
  </b-form>
</template>

<script>
import { writeHotplace, getHotplace, modifyHotplace } from "@/api/hotplace";
import { mapState } from "vuex";
import StarRating from 'vue-star-rating'

export default {
  name: "HotplaceInputItem",
  components: {
    StarRating,
  },
  data() {
    return {
      hotplace: {
        articleNo: 0,
        userId: "",
        contentId: 0,
        contentTypeId: 0,
        title: "",
        content: "",
        visitDate: "",
        thumbNail: null,
        rate: 0,
      },
      hotplaceInfo: null,
      options: [
        { value: null, text: "관광지 선택하세요", disabled: true },
        // { value: 12, text: "관광지" },
        // { value: 14, text: "문화시설" },
        // { value: 15, text: "축제공연행사" },
        // { value: 25, text: "여행코스" },
        // { value: 28, text: "레포츠" },
        // { value: 29, text: "음식점" },
        // { value: 32, text: "숙박" },
        // { value: 38, text: "쇼핑" },
      ],
    };
  },
  computed: {
    ...mapState("userStore", ["userInfo"]),
  },
  props: {
    type: { type: String },
  },
  created() {
    if (this.type === "modify") {
      getHotplace(
        this.$route.params.articleNo,
        ({ data }) => {
          this.hotplace = data;
          this.hotplace.rate /= 2
          this.hotplaceInfo = {
            contentId: this.hotplace.contentId,
            contentTypeId: this.hotplace.contentTypeId,
          }
        },
        (err) => {
          console.log(err);
        }
      );
    }
    this.userInfo.attractionBookmark.map(({contentId, contentTypeId, title}) => {
      return {
        value: { contentId, contentTypeId },
        text: title,
      }
    }).forEach((data) => this.options.push(data))
  },
  methods: {
    onSubmit(event) {
      event.preventDefault();
      this.hotplace.userId = this.userInfo.id;
      this.hotplace.articleType = "hotplace";
      if (this.hotplaceInfo !== null) {
        this.hotplace.contentId = this.hotplaceInfo.contentId
        this.hotplace.contentTypeId = this.hotplaceInfo.contentTypeId
      }

      let err = true;
      let msg = "";
      !this.hotplace.visitDate &&
        ((msg = "방문 날짜를 선택해주세요"),
        (err = false));
      if (this.type === 'register') {
        err && 
          !this.hotplace.contentId &&
          ((msg = "관광지를 선택해주세요"),
          (err = false));
      }
      err &&
        !this.hotplace.title &&
        ((msg = "제목을 입력해주세요"),
        (err = false));
      err &&
        !this.hotplace.content &&
        ((msg = "내용을 입력해주세요"),
        (err = false));
      err &&
        !this.hotplace.rate &&
        ((msg = "별점을 입력해주세요"),
        (err = false));
      if (this.type === 'register') {
        err &&  
          (this.hotplace.thumbNail.size > 10 * 1024 * 1024) &&
          ((msg = "파일 용량을 확인해주세요(10MB)"),
          (err = false))
      }

      if (!err) alert(msg);
      else {
        const formData = new FormData()
        formData.append("articleNo", this.hotplace.articleNo);
        formData.append("userId", this.hotplace.userId);
        formData.append("articleType", "hotplace");
        formData.append("contentId", this.hotplace.contentId);
        formData.append("contentTypeId", this.hotplace.contentTypeId);
        formData.append("title", this.hotplace.title);
        formData.append("content", this.hotplace.content);
        formData.append("visitDate", this.hotplace.visitDate);
        formData.append("rate", this.hotplace.rate * 2);
        // formData.append("thumbNail", this.hotplace.thumbNail);
        // Array.from(this.hotplace.thumbNail)
        if (this.type === 'register') {
          this.hotplace.thumbNail.forEach( (el) => {
            formData.append("thumbNail", el);
          });
        }

        // FormData의 key 확인
        // @ts-ignore
        for (const key of formData.keys()) {
          console.log(key);
        }
        // FormData의 value 확인
        // @ts-ignore
        for (const value of formData.values()) {
          console.log(value);
        }
        console.log(formData);

        this.type === "register"
          ? this.registHotplace(formData)
          : this.modifyHotplace(formData);
      }

    },
    onReset(event) {
      if (confirm("정말 취소하시겠습니까?")) {
        event.preventDefault();
        this.hotplace.contentTypeId = 0;
        this.hotplace.visitDate = "";
        this.hotplace.title = "";
        this.hotplace.content = "";
        this.moveList();
      }
    },
    async registHotplace(formData) {
      console.dir(formData)
      await writeHotplace(
        formData,
        () => {},
        (err) => {
          alert(err);
        }
      );
      this.moveList();
    },
    async modifyHotplace(formData) {
      await modifyHotplace(
        formData,
        () => {},
        (err) => {
          alert(err);
        }
      );
      this.moveList();
    },
    moveList() {
      this.$router.push({ name: "article" });
    },
  },
};
</script>

<style></style>
