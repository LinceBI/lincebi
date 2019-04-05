<template>
	<b-dropdown-item
		class="SideBarSubitem shadow-sm"
		:to="item.to"
		@click="item.click"
	>
		<font-awesome-icon v-if="item.icon" class="item-icon" :icon="item.icon" />
		<b-img v-else class="item-img" :src="item.img"></b-img>
		<span class="lbl"> {{ item.name }} </span>
	</b-dropdown-item>
</template>

<script>
export default {
	name: 'SideBarSubitem',
	components: {},
	props: { item: Object }
};
</script>

<style scoped lang="scss">
.SideBarSubitem {
	&.dropdown-item {
		display: flex;
		align-items: center;
		height: rem(45);
		background-color: map-get($theme-colors, 'primary');
		color: map-get($theme-colors, 'light');
	}

	&.dropdown-item:hover,
	&.dropdown-item:focus {
		background-color: map-get($theme-colors, 'light');
		color: map-get($theme-colors, 'primary');
	}

	.item-img {
		width: 1em;
		height: auto;
	}
}
</style>
