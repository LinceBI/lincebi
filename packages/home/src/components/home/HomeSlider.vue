<template>
	<div class="home-slider" v-if="slider.slides.some(i => i.enabled)">
		<b-collapse v-model="slider.expanded">
			<b-carousel
				:interval="slider.interval"
				:indicators="slider.indicators"
				:controls="slider.controls"
				:fade="slider.fade"
			>
				<b-carousel-slide
					v-for="(slide, index) in slider.slides.filter(i => i.enabled)"
					:key="index"
					:caption="slide.caption"
					:text="slide.text"
					:img-src="slide.img"
				/>
			</b-carousel>
		</b-collapse>
		<b-button
			class="p-0 rounded-0"
			size="sm"
			variant="light"
			@click="slider.expanded = !slider.expanded"
		>
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
				slides: []
			}
		};
	},
	async created() {
		const response = await fetch('./slider/slider.json', {
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});

		if (response.status === 200) {
			this.slider = await response.json();
		}
	}
};
</script>

<style scoped lang="scss">
.home-slider::v-deep {
	text-shadow: rem(1) rem(1) rem(2) #333;

	.carousel-item {
		> .carousel-caption {
			position: absolute;
			left: 0;
			bottom: 0;
			max-height: 100%;
			width: 100%;
			padding: rem(20);
			overflow: auto;
		}

		> img {
			display: block;
			margin: 0 auto;
			object-fit: cover;

			height: rem(160);
			max-width: map-get($grid-breakpoints, 'xl');

			@include media-breakpoint-up(sm) {
				height: rem(192);
			}

			@include media-breakpoint-up(lg) {
				height: rem(224);
			}

			@include media-breakpoint-up(xl) {
				height: rem(256);
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
</style>
