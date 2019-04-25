<template>
	<b-carousel class="home-slider" v-if="slides.some(i => i.enabled)" fade>
		<b-carousel-slide
			class="slide-item"
			v-for="(slide, index) in slides.filter(i => i.enabled)"
			:key="index"
			:caption="slide.caption"
			:text="slide.text"
			:img-src="slide.img"
		/>
	</b-carousel>
</template>

<script>
export default {
	name: 'HomeSlider',
	data() {
		return {
			slides: []
		};
	},
	async created() {
		const response = await fetch('./slides/slides.json', {
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});

		if (response.status === 200) {
			this.slides = await response.json();
		}
	}
};
</script>

<style scoped lang="scss">
.home-slider::v-deep {
	text-shadow: 1px 1px 2px #333;

	.carousel-item > img {
		min-height: rem(192);
		object-fit: cover;
	}
}
</style>
