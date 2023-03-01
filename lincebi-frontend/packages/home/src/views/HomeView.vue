<template>
	<div class="home-view">
		<home-slider class="home-slider" />
		<div class="home-tabs">
			<home-tab-list :tab.sync="tab" />
			<home-tab-content :tab.sync="tab" />
		</div>
	</div>
</template>

<script>
import HomeSlider from '@/components/HomeSlider.vue';
import HomeTabList from '@/components/HomeTabList.vue';
import HomeTabContent from '@/components/HomeTabContent.vue';

export default {
	name: 'HomeView',
	components: {
		HomeSlider,
		HomeTabList,
		HomeTabContent,
	},
	data() {
		return {
			tab: null,
		};
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
