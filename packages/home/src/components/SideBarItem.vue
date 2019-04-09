<template>
	<b-nav-item-dropdown
		v-if="Array.isArray(item.subitems)"
		class="SideBarItem"
		dropright
		no-caret
	>
		<template slot="button-content">
			<font-awesome-icon v-if="item.icon" class="item-icon fa-fw" :icon="item.icon" />
			<b-img v-else class="item-img" :src="item.img"></b-img>
		</template>
		<SideBarSubitem
			v-for="subitem in item.subitems"
			:key="subitem.id"
			:item="subitem"
		/>
	</b-nav-item-dropdown>
	<b-nav-item v-else class="SideBarItem" :to="item.to" @click="item.click">
		<font-awesome-icon v-if="item.icon" class="item-icon fa-fw" :icon="item.icon" />
		<b-img v-else class="item-img" :src="item.img"></b-img>
	</b-nav-item>
</template>

<script>
import SideBarSubitem from '@/components/SideBarSubitem.vue';

export default {
	name: 'SideBarItem',
	components: {
		SideBarSubitem
	},
	props: { item: Object }
};
</script>

<style scoped lang="scss">
.SideBarItem {
	::v-deep .nav-link {
		padding: rem(8) 0;
		font-size: rem(20);
		text-align: center;
		color: map-get($theme-colors, 'light');
	}

	&::v-deep .nav-link:hover,
	&::v-deep .nav-link:focus,
	&.show::v-deep .nav-link {
		background-color: map-get($theme-colors, 'light');
		color: map-get($theme-colors, 'primary');
	}

	&::v-deep .dropdown-menu {
		margin: 0;
		padding: 0;
		border-left: 0;
	}

	.item-img {
		width: 1em;
		height: auto;
	}
}
</style>
