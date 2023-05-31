<template>
    <section>
        <b-container class="main-intro" align="left">
            <div class="title">나만의 여행 계획 만들기</div>
            <b-card-group deck>
                <b-card
                    title="지역별 관광지 검색"
                    :img-src="require('@/assets/img/hotplace/busan_2.png')"
                    img-height="250px"
                    img-alt="지역별 여행지"
                    img-top
                    title-tag="h5"
                    bg-variant="light"
                    tag="article"
                    class="card card-attraction mb-5"
                    @click="moveTripInfo"
                >
                    <b-card-text>
                        관심 있는 관광지를 찾아서 북마크에 등록해보세요!
                    </b-card-text>
                </b-card>
                <b-card
                    title="여행 계획 공유"
                    :img-src="require('@/assets/img/route/route_2.png')"
                    img-height="250px"
                    img-width="250px"
                    img-alt="나의 여행 계획"
                    img-top
                    title-tag="h5"
                    bg-variant="light"
                    tag="article"
                    class="card card-attraction mb-5"
                    @click="movePlan"
                >
                    <b-card-text>
                        북마크에 등록한 관광지로 여행 계획을 설계하고
                        공유해보세요!
                    </b-card-text>
                </b-card>
                <b-card
                    title="핫플레이스 리뷰 작성"
                    :img-src="require('@/assets/img/hotplace/busan_4.png')"
                    img-height="250px"
                    img-width="250px"
                    img-alt="핫플 자랑하기"
                    img-top
                    title-tag="h5"
                    bg-variant="light"
                    tag="article"
                    class="card card-attraction mb-5"
                    @click="moveHotplace"
                >
                    <b-card-text>
                        관광지에 방문한 경험을 공유해주세요!
                    </b-card-text>
                </b-card>
                <b-card
                    title="여행 정보 수집"
                    :img-src="
                        require('@/assets/img/common/main-background.jpg')
                    "
                    img-height="250px"
                    img-width="250px"
                    img-alt="여행정보공유"
                    img-top
                    title-tag="h5"
                    bg-variant="light"
                    tag="article"
                    class="card card-attraction mb-5"
                    @click="moveArticle"
                >
                    <b-card-text>
                        사용자들이 공유한 여행 계획과 핫플레이스 리뷰를
                        확인하세요!
                    </b-card-text>
                </b-card>
            </b-card-group>
        </b-container>
    </section>
</template>

<script>
export default {
    components: {},
    methods: {
        moveTripInfo() {
            this.$router.push({ name: "tripinfo" });
        },
        movePlan() {
            this.$router.push({ name: "plan" });
        },
        moveHotplace() {
            this.$router.push({ name: "hotplace" });
        },
        moveArticle() {
            this.$router.push({ name: "article" });
        },
    },
};
</script>

<style scoped>
.main-intro {
    padding-top: 50px;
}

.contour {
    content: "";
    display: block;
    width: 150px;
    height: 5px;
    border-radius: 10px;
    background-color: #89bfef;
    /* background-color: cornflowerblue; */
    margin: 20px auto;
}

.title {
    font-size: 30px;
    font-weight: bold;
    margin-bottom: 15px;
}

.card {
    max-width: 280px;
    /* margin: 20px; */
    cursor: pointer;
}
</style>
