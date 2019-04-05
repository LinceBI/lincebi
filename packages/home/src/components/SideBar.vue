<template>
	<b-nav class="SideBar shadow" vertical>
		<SideBarItem v-for="item in sidebar" :key="item.id" :item="item" />
	</b-nav>
</template>

<script>
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
		this.sidebar = await this.$store.getters.sidebar;
	}
};
</script>

<style scoped lang="scss">
.SideBar {
	display: block;
	height: 100%;
	width: 100%;
	background-color: map-get($theme-colors, 'primary');
}
</style>
