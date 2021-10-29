<template>
	<div class="home-view">
		<home-slider class="home-slider" />
		<home-tabs class="home-tabs" />
	</div>
</template>

<script>
import HomeSlider from '@/components/HomeSlider.vue';
import HomeTabs from '@/components/HomeTabs.vue';

export default {
	name: 'HomeView',
	components: {
		HomeSlider,
		HomeTabs,
	},
};
</script>

<style scoped lang="scss">
.home-view {
	display: flex;
	flex-grow: 1;
	flex-shrink: 1;
	flex-basis: auto;
	flex-direction: column;

	height: 100%;

	.home-slider {
		display: flex;
		flex-grow: 0;
		flex-shrink: 1;
		flex-basis: auto;
		flex-direction: column;
	}

	.home-tabs {
		display: flex;
		flex-grow: 1;
		flex-shrink: 1;
		flex-basis: auto;
		flex-direction: column;
	}
}
</style>
