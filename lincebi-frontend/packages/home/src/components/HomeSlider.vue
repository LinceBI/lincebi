<template>
	<div v-if="slider.slides.some((i) => i.enabled)" class="home-slider">
		<b-collapse v-model="slider.expanded">
			<b-carousel
				:interval="slider.interval"
				:indicators="slider.indicators"
				:controls="slider.controls"
				:fade="slider.fade"
			>
				<b-carousel-slide
					v-for="(slide, index) in slider.slides.filter((i) => i.enabled)"
					:key="index"
					:caption="slide.caption"
					:text="slide.text"
					:img-src="slide.img"
				/>
			</b-carousel>
		</b-collapse>
		<b-button class="p-0 rounded-0" size="sm" variant="light" @click="slider.expanded = !slider.expanded">
			<font-awesome-icon v-if="slider.expanded" :icon="['fas', 'angle-up']" />
			<font-awesome-icon v-else :icon="['fas', 'angle-down']" />
		</b-button>
	</div>
</template>

<script>
export default {
	name: 'HomeSlider',
	data() {
		return {
			slider: {
				expanded: false,
				interval: 5000,
				indicators: false,
				controls: false,
				fade: true,
				slides: [],
			},
		};
	},
	async created() {
		const response = await fetch('./slider/slider.json', {
			method: 'GET',
			headers: { 'Content-Type': 'application/json' },
		});

		if (response.status === 200) {
			this.slider = await response.json();
		}
	},
};
</script>

<style scoped lang="scss">
.home-slider {
	text-shadow: toRem(1) toRem(1) toRem(2) #333;

	:deep() {
		.carousel-item {
			> .carousel-caption {
				position: absolute;
				left: 0;
				bottom: 0;
				max-height: 100%;
				width: 100%;
				padding: toRem(20);
				overflow: auto;
			}

			> img {
				display: block;
				margin: 0 auto;
				object-fit: cover;

				height: toRem(160);
				max-width: map-get($grid-breakpoints, 'xl');

				@include media-breakpoint-up(sm) {
					height: toRem(192);
				}

				@include media-breakpoint-up(lg) {
					height: toRem(224);
				}

				@include media-breakpoint-up(xl) {
					height: toRem(256);
				}
			}
		}

		&,
		.carousel-item {
			background-color: map-get($theme-colors, 'light');
		}

		.carousel-control-prev,
		.carousel-control-next {
			opacity: 0;
		}
	}
}
</style>
