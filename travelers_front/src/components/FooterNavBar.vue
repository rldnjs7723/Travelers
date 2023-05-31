<template>
  <div class="mt-5">
    <b-navbar toggleable="lg" fixed="bottom" sticky type="light" variant="light">
      <!-- 로그인 시 헤더 바 Start -->
      <b-navbar-nav class="link-container">
        <b-nav-item>
          <a class="link">개인정보처리방침</a>
          <a class="link">이용약관</a>
          <a class="link">오시는길</a>
          <a class="link"> &COPY; <span class="logo">JeonNamTour</span></a>
          <a class="link"
            ><b-img
              class="icon"
              :src="require('@/assets/img/icon/github.png')"
              alt="개발자 깃허브"
            ></b-img>
          </a>
          <!-- <a class="link"
            ><b-img
              class="icon"
              :src="require('@/assets/img/icon/github.png')"
              alt="개발자 깃허브"
            ></b-img>
          </a> -->
        </b-nav-item>
      </b-navbar-nav>
      <!-- 로그인 시 헤더 바 End -->
    </b-navbar>
  </div>
</template>

<script>
export default {
  name: "FooterNavBar",
};
</script>

<style scoped>
.navbar {
  width: 80%;
  border-top-left-radius: 25px;
  border-top-right-radius: 25px;
  margin-left: 10%;
  padding: 30px 30px;
}

.logo {
  /* width: 120px; */
  font-family: GreatVibes-Regular;
  font-size: x-large;
  text-decoration: none;
}

.icon {
  height: 25px;
  width: 25px;
}

.link {
  text-decoration: none;
  margin: 0px 15px;
}

.link-container {
  margin: 0 auto;
}

a {
  color: #212121;
  opacity: 0.9;
}

a:hover {
  color: #89bfef;
}
</style>
