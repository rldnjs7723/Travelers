<template>
    <div>
      <b-row>
        <b-col>
          <div class="alert alert-primary mt-3 text-center fw-bold" role="alert">핫 플레이스 리뷰 작성</div>
        </b-col>
      </b-row>
      <hotplace-input-item type="register" />
    </div>
</template>
  
<script>
  import HotplaceInputItem from "@/components/hotplace/item/HotplaceInputItem";
  
  export default {
    name: 'HotplaceWrite',
    components: {
        HotplaceInputItem,
    },
    data() {
      return {
      };
    },
    created() {},
    methods: {},
  };
</script>
  
<style scoped></style>