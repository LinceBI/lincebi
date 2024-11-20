<template>
	<div class="home-view">
		<b-collapse v-model="bannerExpanded" class="home-banner">
			<home-slider class="home-slider" />
			<home-motd class="home-motd" />
		</b-collapse>
		<b-button class="home-banner-btn p-0 rounded-0" size="sm" variant="light" @click="bannerExpanded = !bannerExpanded">
			<font-awesome-icon v-if="bannerExpanded" :icon="['fas', 'angle-up']" />
			<font-awesome-icon v-else :icon="['fas', 'angle-down']" />
		</b-button>
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
import HomeMotd from '@/components/HomeMotd.vue';
import HomeTabList from '@/components/HomeTabList.vue';
import HomeTabContentFiles from '@/components/HomeTabContentFiles.vue';
import HomeTabContentFrame from '@/components/HomeTabContentFrame.vue';

export default {
	name: 'HomeView',
	components: {
		HomeSlider,
		HomeMotd,
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
			bannerExpanded: true,
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

	.home-banner {
		&:empty + .home-banner-btn {
			display: none;
		}

		.home-slider {
			display: flex;
			flex-grow: 0;
			flex-shrink: 1;
			flex-basis: auto;
			flex-direction: column;
		}

		.home-motd {
			display: flex;
			padding: toRem(5);
			align-items: center;
			justify-content: center;
			text-align: center;
			font-weight: 600;
			color: map-get($theme-colors, 'dark');
		}
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
