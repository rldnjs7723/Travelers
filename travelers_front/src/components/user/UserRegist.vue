<template>
    <b-container class="bv-example-row mt-3">
        <div class="alert alert-primary mt-3 text-center fw-bold" role="alert">
            회원가입
        </div>
        <b-row>
            <b-col></b-col>
            <b-col
                ><b-card class="mt-3" style="width: 40rem">
                    <b-alert show variant="danger" v-if="validationError"
                        >유효하지 않은 입력을 확인해주세요.</b-alert
                    >
                    <b-alert show variant="danger" v-if="isRegistError"
                        >회원가입 중 에러가 발생했습니다.</b-alert
                    >
                    <b-form class="text-left">
                        <b-form-group
                            label="이메일:"
                            label-for="useremail"
                            :invalid-feedback="emailFeedback"
                            :state="emailValidation"
                        >
                            <b-input-group>
                                <b-form-input
                                    id="useremail"
                                    v-model="user.email"
                                    placeholder="이메일 입력...."
                                    @change="emailCheck"
                                    :state="emailValidation"
                                    required
                                ></b-form-input>

                                <b-input-group-prepend is-text
                                    >@</b-input-group-prepend
                                >
                                <b-form-select
                                    v-model="user.emaildomain"
                                    :options="domains"
                                    @change="emailCheck"
                                    :state="emailValidation"
                                    required
                                ></b-form-select>
                            </b-input-group>
                        </b-form-group>

                        <b-form-group
                            id="password-group"
                            label="비밀번호:"
                            label-for="userpwd"
                            :invalid-feedback="passwordFeedback"
                            :state="passwordValidation"
                        >
                            <b-form-input
                                id="userpwd"
                                type="password"
                                v-model="user.password"
                                placeholder="비밀번호 입력...."
                                :state="passwordValidation"
                                trim
                                required
                            ></b-form-input>
                        </b-form-group>

                        <b-form-group
                            id="pwdconfirm-group"
                            label="비밀번호확인:"
                            label-for="userpwdconfirm"
                            :invalid-feedback="pwdConfirmFeedback"
                            :state="pwdConfirmValidation"
                        >
                            <b-form-input
                                id="userpwdconfirm"
                                type="password"
                                v-model="user.pwdconfirm"
                                placeholder="비밀번호 입력...."
                                :state="pwdConfirmValidation"
                                trim
                                required
                            ></b-form-input>
                        </b-form-group>

                        <b-form-group
                            id="name-group"
                            label="이름:"
                            label-for="username"
                            :state="nameValidation"
                        >
                            <b-form-input
                                id="username"
                                v-model="user.name"
                                placeholder="이름 입력...."
                                :state="nameValidation"
                                trim
                                required
                            ></b-form-input>
                        </b-form-group>

                        <b-form-group
                            id="id-group"
                            label="별명:"
                            label-for="userid"
                            :invalid-feedback="idFeedback"
                            :state="idValidation"
                        >
                            <b-form-input
                                id="userid"
                                v-model="user.id"
                                placeholder="별명 입력...."
                                :state="idValidation"
                                @change="idCheck"
                                trim
                                required
                            ></b-form-input>
                        </b-form-group>

                        <b-form-group
                            id="phone-group"
                            label="연락처:"
                            label-for="userphone"
                            :invalid-feedback="phoneFeedback"
                            :state="phoneValidation"
                        >
                            <b-form-input
                                id="userphone"
                                type="tel"
                                v-model="user.phone"
                                placeholder="-를 뺀 전화번호 11자리"
                                :state="phoneValidation"
                                @change="phoneCheck"
                                trim
                                required
                            ></b-form-input>
                        </b-form-group>

                        <b-button
                            type="submit"
                            variant="outline-success"
                            class="m-1"
                            @click="confirm"
                            >회원가입</b-button
                        >
                        <b-button
                            type="button"
                            variant="outline-primary"
                            class="m-1"
                            @click="movePage"
                            >취소</b-button
                        >
                    </b-form>
                </b-card></b-col
            >
            <b-col></b-col>
        </b-row>
    </b-container>
</template>

<script>
import { mapActions, mapState } from "vuex";
export default {
    name: "UserRegist",
    data() {
        return {
            validationError: false,
            pwdMessage: "",
            domains: [
                { value: null, text: "선택" },
                { value: "@gmail.com", text: "gmail.com" },
                { value: "@naver.com", text: "naver.com" },
                { value: "@kakao.com", text: "kakao.com" },
                { value: "@ssafy.com", text: "ssafy.com" },
            ],
            user: {
                id: "",
                password: "",
                pwdconfirm: "",
                email: "",
                emaildomain: null,
                name: "",
                phone: "",
            },
        };
    },
    computed: {
        ...mapState("userStore", [
            "isValidEmail",
            "isValidId",
            "isValidPhone",
            "isRegistError",
        ]),

        emailValidation() {
            if (this.user.email.length == 0 || !this.user.emaildomain)
                return false;
            return this.isValidEmail;
        },
        emailFeedback() {
            if (this.user.email.length == 0) return "";
            if (!this.user.emaildomain) return "이메일 도메인을 선택해주세요.";
            return "중복된 이메일입니다.";
        },

        passwordValidation() {
            return this.user.password.length >= 8;
        },
        passwordFeedback() {
            if (this.user.password.length == 0) {
                return "";
            }
            return "비밀번호는 최소 8개 이상의 문자로 구성되어야 합니다.";
        },

        pwdConfirmValidation() {
            return (
                this.user.password == this.user.pwdconfirm &&
                this.passwordValidation &&
                this.user.pwdconfirm.length != 0
            );
        },
        pwdConfirmFeedback() {
            if (this.user.pwdconfirm.length == 0) return "";
            if (!this.passwordValidation)
                return "유효한 비밀번호를 입력해주세요.";
            return "입력한 비밀번호가 다릅니다.";
        },

        nameValidation() {
            return this.user.name.length > 0;
        },

        idValidation() {
            if (this.user.id.length == 0) return false;
            return this.isValidId;
        },
        idFeedback() {
            if (this.user.id.length == 0) return "";
            return "중복된 별명입니다.";
        },

        phoneValidation() {
            if (this.user.phone.length != 11) return false;
            else if (this.user.phone.substr(0, 3) !== "010") return false;
            return this.isValidPhone;
        },
        phoneFeedback() {
            if (this.user.phone.length == 0) return "";
            else if (this.user.phone.length != 11)
                return "-를 뺀 전화번호 11자리를 입력해주세요.";
            else if (this.user.phone.substr(0, 3) !== "010")
                return "010으로 시작하는 전화번호를 입력해주세요.";
            return "중복된 연락처입니다.";
        },
    },
    methods: {
        ...mapActions("userStore", [
            "registIdCheck",
            "registEmailCheck",
            "registPhoneCheck",
            "userRegister",
        ]),

        async emailCheck() {
            let useremail = this.user.email + this.user.emaildomain;
            await this.registEmailCheck(useremail);
        },

        async idCheck() {
            await this.registIdCheck(this.user.id);
        },

        async phoneCheck() {
            await this.registPhoneCheck(this.user.phone);
        },

        async confirm(event) {
            if (
                this.user.email &&
                this.user.emaildomain &&
                this.user.password &&
                this.user.name &&
                this.user.id &&
                this.user.phone
            ) {
                event.preventDefault();
            }

            if (
                !this.emailValidation ||
                !this.passwordValidation ||
                !this.pwdConfirmValidation ||
                !this.nameValidation ||
                !this.idValidation ||
                !this.phoneValidation
            ) {
                this.validationError = true;
                return;
            }

            this.validationError = false;
            let info = {
                ...this.user,
                email: this.user.email + this.user.emaildomain,
            };
            console.log(info);
            await this.userRegister(info);

            if (this.isRegistError) {
                alert("회원가입 중 에러가 발생했습니다");
            } else {
                this.$router.push({ name: "main" });
            }
        },
        movePage() {
            this.$router.push({ name: "main" });
        },
    },
};
</script>

<style></style>
