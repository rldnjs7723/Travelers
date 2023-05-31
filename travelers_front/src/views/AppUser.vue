<template>
    <section id="board-share-list" class="pt-3">
        <div class="container" data-aos="fade-up">
            <div class="section-header text-center">
                <h3 class="underline-steelblue">유저 서비스</h3>
                <div class="contour"></div>
                <router-view></router-view>
            </div>
        </div>
    </section>
</template>

<script>
export default {
    name: "AppUser",
};
</script>

<style scoped>
.underline-steelblue {
    display: inline-block;
    /* background: linear-gradient(180deg, rgba(255, 255, 255, 0) 70%, rgba(72, 190, 233, 0.3) 30%); */
}
</style>
