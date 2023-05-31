<template>
    <!-- 중앙 content start-->
    <section id="board-make" class="pt-3">
        <h3 class="underline-steelblue">나의 여행 계획</h3>
        <div class="container" data-aos="fade-up">
            <div
                class="alert alert-primary mt-3 text-center fw-bold"
                role="alert"
            >
                여행 경로 설정
            </div>
            <b-row>
                <b-col lg="6">
                    <!-- kakao map start -->
                    <plan-map></plan-map>
                    <!-- kakao map end -->
                </b-col>

                <!-- 여행 경로 Draggable Start -->
                <b-col lg="3" class="mt-3 drag-container">
                    <draggable
                        v-for="plan in planList"
                        :key="plan.day"
                        :id="plan.day"
                        data-source="juju"
                        :list="plan.path"
                        class="dragArea list-group"
                        draggable=".item"
                        group="plan"
                        @end="savePlan"
                    >
                        <div
                            class="list-group-item item"
                            v-for="(attraction, index) in plan.path"
                            :key="index"
                        >
                            <div class="icon-container">
                                <div
                                    class="dot"
                                    :style="{
                                        'border-color': `${
                                            colors[plan.day - 1]
                                        }`,
                                        color: `${colors[plan.day - 1]}`,
                                    }"
                                >
                                    {{ index + 1 }}
                                </div>
                            </div>
                            <div class="item-text">{{ attraction.title }}</div>
                        </div>

                        <div
                            slot="header"
                            class="btn-group list-group-item"
                            role="group"
                            aria-label="Basic example"
                            @click="log"
                        >
                            <p
                                class="drag-header-text"
                                :style="{ color: colors[plan.day - 1] }"
                            >
                                {{ plan.day }}일차
                            </p>
                        </div>

                        <div
                            slot="footer"
                            class="btn-group list-group-item list-group-item-icon"
                            role="group"
                            aria-label="Basic example"
                            @click="deletePlanDay(plan.day)"
                        >
                            <b-icon
                                icon="trash"
                                animation="fade"
                                font-scale="2"
                            ></b-icon>
                        </div>
                    </draggable>
                    <b-container
                        class="list-group-item list-group-item-icon"
                        @click="addPlanDay"
                    >
                        <b-row
                            align-v="center"
                            align-h="center"
                            class="list-group"
                        >
                            <b-icon
                                icon="plus-square"
                                animation="fade"
                                font-scale="2"
                            ></b-icon>
                        </b-row>
                    </b-container>
                </b-col>
                <!-- 여행 경로 Draggable End -->

                <!-- 북마크 Draggable Start -->
                <b-col lg="3" class="mt-3 drag-container">
                    <draggable
                        :list="userInfo.attractionBookmark"
                        id="bookmarkList"
                        class="dragArea list-group"
                        draggable=".item"
                        :group="{ name: 'plan', pull: 'clone', put: 'false' }"
                        @end="savePlan"
                    >
                        <div
                            class="list-group-item item"
                            v-for="attraction in userInfo.attractionBookmark"
                            :key="attraction.contentId"
                        >
                            <b-img
                                class="icon icon-container"
                                :src="
                                    contentTypeList[
                                        checkContentTypeMap.get(
                                            attraction.contentTypeId
                                        )
                                    ].image
                                "
                                fluid
                                alt="ContentTypeIcon"
                            ></b-img>
                            <div class="item-text">{{ attraction.title }}</div>
                        </div>

                        <div
                            slot="header"
                            class="btn-group list-group-item"
                            role="group"
                            aria-label="Basic example"
                        >
                            <p class="drag-header-text">북마크</p>
                        </div>
                    </draggable>
                </b-col>
                <!-- 북마크 Draggable End -->
            </b-row>

            <div
                class="alert alert-primary mt-3 text-center fw-bold"
                role="alert"
            >
                여행 상세 설명
            </div>

            <b-card class="text-center mb-3" align="left">
                <b-form id="form-schedule-detail">
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
                                    v-model="pathInfo.title"
                                    type="text"
                                    required
                                    placeholder="제목을 입력해주세요"
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
                                label-align-lg="left"
                            >
                                <b-form-textarea
                                    id="content"
                                    v-model="pathInfo.content"
                                    placeholder="내용을 입력해주세요"
                                    rows="10"
                                    max-rows="15"
                                ></b-form-textarea>
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
                                    v-model="pathInfo.startDate"
                                    class="mb-2"
                                    required
                                />
                            </b-form-group>
                        </b-col>
                    </b-row>

                    <!-- <button
                        type="submit"
                        class="w-10 btn btn-primary mx-1 my-5"
                        id="btn-hotplace-submit"
                    >
                        등록
                    </button> -->

                    <b-row align-h="center">
                        <b-button
                            type="submit"
                            variant="outline-info"
                            size="sm"
                            class="m-1"
                            @click="writeSchedule"
                            >등록</b-button
                        >
                    </b-row>
                </b-form>
            </b-card>
        </div>
    </section>
    <!-- 중앙 content end -->
</template>

<script>
import draggable from "vuedraggable";
import PlanMap from "../components/plan/PlanMap.vue";
import { mapActions, mapGetters, mapState } from "vuex";

export default {
    name: "AppPlan",
    components: {
        PlanMap,
        draggable,
    },
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
            temp: [],
            drag: false,
        };
    },

    computed: {
        ...mapState(["colors", "contentTypeList"]),
        ...mapState("planStore", ["planList"]),
        ...mapState("userStore", ["userInfo"]),
        ...mapGetters(["checkContentTypeMap"]),
        dragOptions() {
            return {
                animation: 200,
                group: "description",
                disabled: false,
                ghostClass: "ghost",
            };
        },
    },

    methods: {
        ...mapActions("planStore", [
            "changePlan",
            "togglePlanChanged",
            "writeArticle",
        ]),

        log() {
            console.log("test");
        },

        savePlan(data) {
            // console.log(data);
            // console.log(data.to.id == data.from.id);
            // 요소 삭제
            if (
                data.to.id != "bookmarkList" &&
                data.to.id == data.from.id &&
                data.oldIndex == data.newIndex
            ) {
                this.planList[data.to.id - 1].path.splice(data.oldIndex - 1, 1);
            }

            this.changePlan(this.planList);
        },

        addPlanDay() {
            let plans = this.planList;
            plans.push({
                day: plans.length + 1,
                path: [],
            });

            this.changePlan(plans);
        },

        deletePlanDay(day) {
            let plans = this.planList;
            plans = plans.filter((plan) => plan.day != day);
            plans.forEach((plan, index) => {
                plan.day = index + 1;
            });

            this.changePlan(plans);
        },

        async writeSchedule(event) {
            this.pathInfo.userId = this.userInfo.id;
            const contentIdList = [];

            if (this.pathInfo.title) {
                event.preventDefault();
            } else {
                return;
            }

            if (!this.pathInfo.startDate) {
                alert("시작 날짜를 지정해주세요.");
                event.preventDefault();
                return;
            }

            let totalCount = 0;
            for (let plan of this.planList) {
                if (plan.path.length == 0) {
                    alert(`${plan.day}일차의 계획이 비어있습니다.`);
                    return;
                }
                let curr = {
                    ...plan,
                    path: plan.path.map(({ contentId }) => {
                        totalCount++;
                        return contentId;
                    }),
                };
                contentIdList.push(curr);
            }
            this.pathInfo.path = JSON.stringify(contentIdList);

            if (totalCount == 0) {
                alert("여행 경로에 적어도 하나의 관광지를 추가해야 합니다.");
                return;
            }

            // 종료 날짜 계산
            let endDate = new Date(this.pathInfo.startDate);
            endDate.setDate(endDate.getDate() + contentIdList.length - 1);
            this.pathInfo.endDate = `${endDate.getFullYear()}-${
                endDate.getMonth() + 1
            }-${endDate.getDate()}`;

            await this.writeArticle(this.pathInfo).then(() =>
                this.$router.push({ name: "Articlelist" })
            );
        },
    },
};
</script>

<style scoped>
.underline-steelblue {
    display: inline-block;
    /* background: linear-gradient(180deg, rgba(255, 255, 255, 0) 70%, rgba(72, 190, 233, 0.3) 30%); */
}

.icon-container {
    margin-right: 10px;
}

.icon {
    width: 25px;
    height: 25px;
}

.item {
    display: flex;
    align-items: center;
}

.item-text {
    margin: auto;
}

.drag-container {
    max-height: 600px;
    overflow: scroll;
    -ms-overflow-style: none; /* IE and Edge */
    scrollbar-width: none; /* Firefox */

    margin-bottom: 20px;
}

.drag-container::-webkit-scrollbar {
    display: none;
}

.flip-list-move {
    transition: transform 0.5s;
}

.no-move {
    transition: transform 0s;
}

.ghost {
    opacity: 0.5;
    background: #c8ebfb;
}

.list-group-item {
    cursor: move;
    border-radius: 5px;
}

.list-group-item-icon {
    cursor: pointer;
}

.drag-header-text {
    margin-top: 15px;
}
</style>
