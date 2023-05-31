<template>
    <span class="card-container">
        <b-img
            class="card-container"
            :src="image"
            width="250px"
            height="300px"
            alt="Card Image"
            @click="click"
        />
        <b-form-textarea
            class="text-container"
            id="textarea-plaintext"
            plaintext
            :value="text"
        ></b-form-textarea>
    </span>
</template>

<script>
export default {
    props: ["image", "click", "text"],
};
</script>

<style>
.card-container {
    position: relative;
    margin-right: 20px;
    border-radius: 15px;
    width: 250px;
    height: 300px;
    overflow: hidden;
    cursor: pointer;
}

.text-container {
    position: absolute;
    display: inline-block;
    top: 0px;
    left: 0px;
    color: aliceblue;
    width: 250px;
    height: 300px;
}
</style>
