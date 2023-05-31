<template>
  <div id="app">
    <header-nav-bar></header-nav-bar>
    <div class="blank"></div>
    <router-view class="main-view" />
    <footer-nav-bar></footer-nav-bar>
  </div>
</template>

<script>
import HeaderNavBar from "./components/HeaderNavBar.vue";
import FooterNavBar from "./components/FooterNavBar.vue";

export default {
  name: "App",
  components: {
    HeaderNavBar,
    FooterNavBar,
  },
};
</script>

<style>
@font-face {
  font-family: "NanumSquareNeo-Variable";
  src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_11-01@1.0/NanumSquareNeo-Variable.woff2")
    format("woff2");
  font-weight: normal;
  font-style: normal;
}

@font-face {
  font-family: "GreatVibes-Regular";
  src: url("@/assets/font/GreatVibes-Regular.ttf") format("truetype");
  font-weight: normal;
  font-style: normal;
}

#app {
  font-family: NanumSquareNeo-Variable, Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

a {
  text-decoration: none;
}

a.router-link-exact-active {
  color: #89bfef;
  /* color: #648099; */
  text-decoration: none;
}
a.router-link-exact-active:hover {
  text-decoration: none;
}

/* .mainView {
  margin-top: 300px;
} */

.blank {
  height: 105px;
}

.main-view {
  min-height: 700px;
}

/* #app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
} */
</style>
