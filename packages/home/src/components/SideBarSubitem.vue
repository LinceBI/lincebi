<template>
	<b-dropdown-item
		class="side-bar-subitem"
		v-if="item.enabled"
		:to="item.to"
		@click="onClick"
	>
		<font-awesome-icon
			v-if="typeof item.icon !== 'undefined'"
			class="item-icon fa-fw"
			:icon="item.icon"
		/>
		<b-img
			v-else-if="typeof item.img !== 'undefined'"
			class="item-img"
			:src="item.img"
		></b-img>
		<span class="lbl">{{ item.name }}</span>
	</b-dropdown-item>
</template>

<script>
export default {
	name: 'SideBarSubitem',
	components: {},
	props: { item: Object },
	methods: {
		onClick(event) {
			if (typeof this.item.click !== 'undefined') {
				this.item.click.call(this, event);
			}
		}
	}
};
</script>

<style scoped lang="scss">
.side-bar-subitem {
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
