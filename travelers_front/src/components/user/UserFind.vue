<template>
    <b-container class="bv-example-row mt-3">
        <div class="alert alert-primary mt-3 text-center fw-bold" role="alert">
            아이디 / 비밀번호 찾기
        </div>
        <b-row>
            <b-col></b-col>
            <b-col cols="7">
                <b-card class="text-center mt-3" align="left">
                    <b-form class="text-center">
                        <div class="title">아이디 찾기</div>

                        <b-alert
                            show
                            variant="danger"
                            v-if="!findEmailValidation"
                            >해당 연락처로 가입한 계정이 없습니다.</b-alert
                        >
                        <b-alert
                            show
                            variant="success"
                            v-if="findEmailValidation && findEmailResult"
                            >해당 연락처로 가입한 아이디(이메일)은 '{{
                                findEmailResult
                            }}'입니다.</b-alert
                        >

                        <b-form-group
                            id="phone-group"
                            label="연락처:"
                            label-for="userphone"
                            label-cols-lg="2"
                            label-align-lg="left"
                        >
                            <b-form-input
                                id="userphone"
                                type="tel"
                                v-model="user.phone"
                                placeholder="회원가입 시 입력한 연락처를 입력해주세요."
                                trim
                                required
                            ></b-form-input>
                        </b-form-group>

                        <b-button
                            type="submit"
                            variant="outline-primary"
                            class="m-1"
                            @click="getEmail"
                            >아이디 찾기</b-button
                        >
                    </b-form>
                </b-card>
            </b-col>
            <b-col></b-col>
        </b-row>

        <b-row>
            <b-col></b-col>
            <b-col cols="7">
                <b-card class="text-center mt-3" align="left">
                    <b-form class="text-center">
                        <div class="title">비밀번호 찾기</div>

                        <b-alert
                            show
                            variant="danger"
                            v-if="!findPasswordValidation"
                            >해당 정보로 가입한 계정이 없습니다.</b-alert
                        >
                        <b-alert
                            show
                            variant="success"
                            v-if="findPasswordValidation && findPasswordResult"
                            >새로 발급된 비밀번호는 '{{
                                findPasswordResult
                            }}'입니다.</b-alert
                        >

                        <b-form-group
                            id="useremail-group"
                            label="이메일:"
                            label-for="useremail"
                            label-cols-lg="2"
                            label-align-lg="left"
                        >
                            <b-form-input
                                id="useremail"
                                type="email"
                                v-model="user.email"
                                placeholder="이메일을 입력해주세요."
                                trim
                                required
                            ></b-form-input>
                        </b-form-group>

                        <b-form-group
                            id="userphonepwd-group"
                            label="연락처:"
                            label-for="userphonepwd"
                            label-cols-lg="2"
                            label-align-lg="left"
                        >
                            <b-form-input
                                id="userphonepwd"
                                type="tel"
                                v-model="user.phone"
                                placeholder="회원가입 시 입력한 연락처를 입력해주세요."
                                trim
                                required
                            ></b-form-input>
                        </b-form-group>

                        <b-button
                            type="submit"
                            variant="outline-primary"
                            class="m-1"
                            @click="getPassword"
                            >비밀번호 찾기</b-button
                        >
                    </b-form>
                </b-card>
            </b-col>
            <b-col></b-col>
        </b-row>
    </b-container>
</template>

<script>
import { findEmail, findPassword } from "@/api/user";

export default {
    data() {
        return {
            findEmailValidation: true,
            findEmailResult: "",
            findPasswordValidation: true,
            findPasswordResult: "",
            user: {
                email: "",
                phone: "",
                password: null,
            },
        };
    },

    methods: {
        async getEmail(event) {
            if (!this.user.phone) {
                return;
            }

            event.preventDefault();
            await findEmail(
                this.user,
                ({ data }) => {
                    console.log(data);
                    this.findEmailValidation = true;
                    this.findEmailResult = data.msg;
                },
                (error) => {
                    console.log(error);
                    this.findEmailValidation = false;
                    this.findEmailResult = "";
                }
            );
        },

        async getPassword(event) {
            if (!this.user.email || !this.user.phone) {
                return;
            }

            event.preventDefault();
            await findPassword(
                this.user,
                ({ data }) => {
                    console.log(data);
                    this.findPasswordValidation = true;
                    this.findPasswordResult = data.msg;
                },
                (error) => {
                    console.log(error);
                    this.findPasswordValidation = false;
                    this.findPasswordResult = "";
                }
            );
        },
    },
};
</script>

<style scoped>
.title {
    margin-top: 5px;
    margin-bottom: 15px;
    font-size: larger;
}
</style>
