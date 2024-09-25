<template>
	<div class="home-view">
		<home-slider class="home-slider" />
		<div class="home-tabs">
			<home-tab-list ref="homeTabList" v-model="tab" :initial-tab="tabName" />
			<home-tab-content-frame v-if="tab?.type === 'frame'" v-model="tab" />
			<home-tab-content-files v-else v-model="tab" />
		</div>
	</div>
</template>

<script>
import router from '@/router';
import store from '@/store';

import HomeSlider from '@/components/HomeSlider.vue';
import HomeTabList from '@/components/HomeTabList.vue';
import HomeTabContentFiles from '@/components/HomeTabContentFiles.vue';
import HomeTabContentFrame from '@/components/HomeTabContentFrame.vue';

export default {
	name: 'HomeView',
	components: {
		HomeSlider,
		HomeTabList,
		HomeTabContentFiles,
		HomeTabContentFrame,
	},
	props: {
		tabName: {
			type: String,
			default: '',
		},
	},
	data() {
		return {
			tab: null,
		};
	},
	watch: {
		tabName(tabName) {
			this.$refs.homeTabList.changeTab(tabName);
		},
		tab(tab) {
			if (tab && this.tabName !== tab.name && store.state.settingsLoaded) {
				router.push({ name: 'home', params: { tabName: tab.name } });
			}
		},
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
