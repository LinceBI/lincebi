<template>
	<b-nav class="side-bar shadow" vertical>
		<side-bar-item v-for="item in sidebar" :key="item.id" :item="item" />
	</b-nav>
</template>

<script>
import store from '@/store';
import SideBarItem from '@/components/SideBarItem.vue';

export default {
	name: 'SideBar',
	components: {
		SideBarItem
	},
	data() {
		return {
			sidebar: []
		};
	},
	async created() {
		this.sidebar = await store.getters.sidebar;
	}
};
</script>

<style scoped lang="scss">
.side-bar {
	display: block;
	height: 100%;
	width: 100%;
	background-color: map-get($theme-colors, 'primary');
}
</style>
