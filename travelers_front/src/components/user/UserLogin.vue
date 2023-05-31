<template>
    <!-- 로그인 modal start -->
    <b-container class="bv-example-row mt-3">
        <div class="alert alert-primary mt-3 text-center fw-bold" role="alert">
            로그인
        </div>
        <b-row>
            <b-col></b-col>
            <b-col cols="7">
                <b-card
                    class="text-center mt-3"
                    style="max-width: 40rem"
                    align="left"
                >
                    <b-form class="text-left">
                        <b-alert show variant="danger" v-if="isLoginError"
                            >아이디 또는 비밀번호를 확인하세요.</b-alert
                        >
                        <b-form-group label="이메일:" label-for="useremail">
                            <b-form-input
                                type="email"
                                id="useremail"
                                v-model="user.email"
                                required
                                placeholder="이메일 입력...."
                                @keyup.enter="confirm"
                            ></b-form-input>
                        </b-form-group>
                        <b-form-group label="비밀번호:" label-for="userpwd">
                            <b-form-input
                                type="password"
                                id="userpwd"
                                v-model="user.password"
                                required
                                placeholder="비밀번호 입력...."
                                @keyup.enter="confirm"
                            ></b-form-input>
                        </b-form-group>
                        <b-form-group label-for="checkbox-save">
                            <b-form-checkbox
                                id="checkbox-save"
                                v-model="user.saveId"
                                value="save"
                                unchecked-value="not-save"
                            >
                                아이디 저장
                            </b-form-checkbox>
                        </b-form-group>
                        <b-button
                            type="button"
                            variant="outline-primary"
                            class="m-1"
                            @click="confirm"
                            >로그인</b-button
                        >
                        <b-button
                            type="button"
                            variant="outline-success"
                            class="m-1"
                            @click="moveRegist"
                            >회원가입</b-button
                        >

                        <b-button
                            type="button"
                            variant="outline-info"
                            class="m-1"
                            @click="moveFind"
                            >아이디 / 비밀번호 찾기</b-button
                        >
                    </b-form>
                </b-card>
            </b-col>
            <b-col></b-col>
        </b-row>
    </b-container>
</template>

<script>
import { mapActions, mapState } from "vuex";

export default {
    name: "UserLogin",
    data() {
        return {
            user: {
                email: null,
                password: null,
                saveId: false,
            },
        };
    },
    created() {
        this.user.saveId = this.saveId;
        this.user.email = this.savedEmail;
    },
    computed: {
        ...mapState("userStore", ["saveId", "savedEmail", "isLoginError"]),
    },
    methods: {
        ...mapActions("userStore", ["userConfirm"]),
        async confirm() {
            await this.userConfirm(this.user);
        },
        moveRegist() {
            this.$router.push({ name: "UserRegist" });
        },

        moveFind() {
            this.$router.push({ name: "UserFind" });
        },
    },
};
</script>

<style></style>
