<template>
  <b-row class="mb-1">
    <b-col style="text-align: left">
      <b-form @submit="onSubmit" @reset="onReset">
        <b-form-group
          id="contentTypeId-group"
          label="핫플레이스:"
          label-for="contentTypeId"
          description="핫플레이스 유형을 입력하세요."
        >
        <b-form-select v-model="hotplace.contentTypeId" :options="options" />
        </b-form-group>
        <b-form-group
          id="visitDate-group"
          label="방문 날짜:"
          label-for="visitDate"
          description="방문 날짜를 입력하세요."
        >
        <b-form-datepicker id="visitDate" v-model="hotplace.visitDate" class="mb-2"></b-form-datepicker>
        </b-form-group>
        <b-form-group
          id="title-group"
          label="제목:"
          label-for="title"
          description="제목을 입력하세요."
        >
          <b-form-input
            id="title"
            v-model="hotplace.title"
            type="text"
            required
            placeholder="제목 입력..."
          ></b-form-input>
        </b-form-group>
        <b-form-group id="content-group" label="내용:" label-for="content">
          <b-form-textarea
            id="content"
            v-model="hotplace.content"
            placeholder="내용 입력..."
            rows="10"
            max-rows="15"
          ></b-form-textarea>
        </b-form-group>
        <b-button
          type="submit"
          variant="primary"
          class="m-1"
          v-if="this.type === 'register'"
          >등록</b-button
        >
        <b-button type="submit" variant="primary" class="m-1" v-else
          >수정</b-button
        >
        <b-button type="reset" variant="danger" class="m-1">취소</b-button>
      </b-form>
    </b-col>
  </b-row>
</template>

<script>
import { writeHotplace, getHotplace, modifyHotplace } from "@/api/hotplace";
import { mapState } from "vuex";

export default {
  name: "HotplaceWrite",
  data() {
    return {
      hotplace: {
        articleNo: 0,
        userId: '',
        contentTypeId: 0,
        title: '',
        content: '',
        visitDate: '',
      },
      options: [
        { value: 0, text: '관광지 유형을 선택하세요', disabled: true},
        { value: 12, text: '관광지' },
        { value: 14, text: '문화시설' },
        { value: 15, text: '축제공연행사' },
        { value: 25, text: '여행코스' },
        { value: 28, text: '레포츠' },
        { value: 29, text: '음식점' },
        { value: 32, text: '숙박' },
        { value: 38, text: '쇼핑' },
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
        },
        (err) => {
          console.log(err);
        }
      );
    }
  },
  methods: {
    onSubmit(event) {
      event.preventDefault();
      this.hotplace.userId = this.userInfo.id
      this.hotplace.articleType = "hotplace"

      let err = true;
      let msg = "";
      !this.hotplace.visitDate &&
        ((msg = "방문 날짜를 선택해주세요"),
        (err = false),
        this.$refs.visitDate.focus());
      err &&
        !this.hotplace.title &&
        ((msg = "제목을 입력해주세요"), (err = false), this.$refs.title.focus());
      err &&
        !this.hotplace.content &&
        ((msg = "내용을 입력해주세요"),
        (err = false),
        this.$refs.content.focus());

      if (!err) alert(msg);
      else 
        this.type === "register" ? this.registHotplace() : this.modifyHotplace();
    },
    onReset(event) {
      event.preventDefault();
      this.hotplace.contentTypeId = 0;
      this.hotplace.visitDate = "";
      this.hotplace.title = "";
      this.hotplace.content = "";
      this.moveList();
    },
    async registHotplace() {
      await writeHotplace(
        this.hotplace,
        () => {
        },
        (err) => {
          alert(err);
        }
      );
      this.moveList();

    },
    async modifyHotplace() {
      await modifyHotplace(
        this.hotplace,
        () => {
        },
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
