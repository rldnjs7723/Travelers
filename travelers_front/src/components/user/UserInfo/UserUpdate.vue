<template>
    <b-container class="bv-example-row">
        <b-card class="text-center" align="left">
            <div class="title">회원정보 수정</div>
            <b-form class="text-left">
                <b-alert show variant="danger" v-if="validationError">{{
                    feedbackMsg
                }}</b-alert>
                <table class="table">
                    <tbody>
                        <tr>
                            <th width="150px" scope="row">프로필 사진</th>
                            <td>
                                <b-form-file
                                    id="thumbNail"
                                    v-model="user.profileImg"
                                    placeholder="파일을 업로드하거나 이곳에 드랍하세요"
                                    drop-placeholder="이곳에 파일을 드랍하세요"
                                    accept=".jpg, .png, .gif"
                                ></b-form-file>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">이메일</th>
                            <td>{{ user.email }}</td>
                        </tr>
                        <tr>
                            <th scope="row">현재 비밀번호</th>
                            <td>
                                <b-form-input
                                    id="userpwd"
                                    type="password"
                                    v-model="user.password"
                                    placeholder="기존 비밀번호 입력...."
                                    trim
                                    required
                                ></b-form-input>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">새로운 비밀번호</th>
                            <td>
                                <b-form-input
                                    id="usernewpwd"
                                    type="password"
                                    v-model="user.newpassword"
                                    placeholder="새로운 비밀번호 입력...."
                                    :state="passwordValidation"
                                    trim
                                ></b-form-input>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">비밀번호 확인</th>
                            <td>
                                <b-form-input
                                    id="userpwdconfirm"
                                    type="password"
                                    v-model="user.pwdconfirm"
                                    placeholder="새로운 비밀번호 입력...."
                                    :state="pwdConfirmValidation"
                                    trim
                                ></b-form-input>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">별명</th>
                            <td>{{ user.id }}</td>
                        </tr>
                        <tr>
                            <th scope="row">이름</th>
                            <td>{{ user.name }}</td>
                        </tr>
                        <tr>
                            <th scope="row">연락처</th>
                            <td>
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
                            </td>
                        </tr>
                        <tr>
                            <th style="padding: 0px"></th>
                            <td style="padding: 0px"></td>
                        </tr>
                    </tbody>
                </table>
                <div class="text-right button-container">
                    <b-button
                        @click="withdraw"
                        type="submit"
                        size="sm"
                        variant="outline-danger"
                        >회원 탈퇴</b-button
                    >
                    <b-button
                        @click="modify"
                        type="submit"
                        size="sm"
                        class="ml-3"
                        variant="outline-info"
                        >회원정보 수정</b-button
                    >
                </div>
            </b-form>
        </b-card>
    </b-container>
</template>

<script>
import { deleteUser } from "@/api/user";
import { mapActions, mapState } from "vuex";

export default {
    name: "UserUpdate",
    data() {
        return {
            validationError: false,
            feedbackMsg: "",
            user: {
                profileImg: "",
                email: "",
                password: "",
                newpassword: "",
                pwdconfirm: "",
                id: "",
                name: "",
                phone: "",
            },
        };
    },
    created() {
        // if (this.userInfo.profileImgInfo.length > 0) {
        //     const img = this.userInfo.profileImgInfo[0]
        //     this.profileImage = require(`@/assets/img/springboot/img/${img.saveFolder}/${img.saveFile}`);
        // }
        (this.user.profileImg = null), (this.user.email = this.userInfo.email);
        this.user.id = this.userInfo.id;
        this.user.name = this.userInfo.name;
        this.user.phone = this.userInfo.phone;
    },
    computed: {
        ...mapState("userStore", ["userInfo", "isValidPhone"]),

        passwordValidation() {
            return !this.user.newpassword || this.user.newpassword.length >= 8;
        },

        pwdConfirmValidation() {
            return (
                this.user.newpassword == this.user.pwdconfirm &&
                this.passwordValidation
            );
        },

        phoneValidation() {
            if (!this.user.phone || this.user.phone.length != 11) return false;
            else if (this.user.phone.substr(0, 3) !== "010") return false;
            // 기존 전화번호와 동일하면 유효
            else if (this.user.phone == this.userInfo.phone) return true;
            return this.isValidPhone;
        },
    },
    methods: {
        ...mapActions("userStore", [
            "userUpdate",
            "userLogout",
            "registPhoneCheck",
        ]),

        async phoneCheck() {
            await this.registPhoneCheck(this.user.phone);
        },

        async withdraw(event) {
            if (this.user.password) {
                event.preventDefault();
            } else {
                return;
            }

            if (confirm("정말로 탈퇴하시겠습니까?")) {
                await deleteUser(
                    this.user,
                    () => {
                        alert("탈퇴가 완료되었습니다.");
                        this.userLogout();
                        this.$router.push({ name: "main" });
                    },
                    (error) => {
                        console.log(error);
                        alert("비밀번호를 다시 확인해주세요.");
                    }
                );
            }
        },
        async modify(event) {
            if (this.user.password && this.user.phone) {
                event.preventDefault();
            } else {
                return;
            }

            if (!this.passwordValidation || !this.pwdConfirmValidation) {
                this.validationError = true;
                this.feedbackMsg = "유효하지 않은 입력을 확인해주세요.";
                return;
            } else if (!this.phoneValidation) {
                this.validationError = true;
                if (!this.isValidPhone) {
                    this.feedbackMsg = "중복된 연락처입니다.";
                } else {
                    this.feedbackMsg =
                        "-를 뺀 010으로 시작하는 전화번호 11자리를 입력해주세요.";
                }

                return;
            }
            const formData = new FormData();
            formData.append("profileImg", this.user.profileImg);
            formData.append("email", this.user.email);
            formData.append("password", this.user.password);
            formData.append("newpassword", this.user.newpassword);
            // formData.append("pwdconfirm", this.user.pwdconfirm);
            formData.append("id", this.user.id);
            formData.append("name", this.user.name);
            formData.append("phone", this.user.phone);

            for (const value of formData.values()) {
                console.log(value);
            }

            await this.userUpdate(this.user);
        },

        // async confirm() {
        //   await this.userConfirm(this.user);
        //   this.$router.push({ name: "main" });
        // },
    },
};
</script>

<style scoped>
* {
    font-weight: 600;
    font-size: medium;
}

.title {
    margin-top: 5px;
    margin-bottom: 25px;
    font-size: larger;
}
th,
td {
    padding: 20px 10px;
    vertical-align: middle;
}

.button-container {
    margin-right: 10px;
}
</style>
