<template>
	<b-dropdown-item
		class="side-bar-subitem"
		v-if="item.enabled"
		:to="item.to"
		@click="onClick"
		@mouseover.native="isHover = true"
		@mouseout.native="isHover = false"
		@focus.native="isFocused = true"
		@blur.native="isFocused = false"
		:style="itemStyle"
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
		/>
		<span class="lbl">{{ item.name }}</span>
	</b-dropdown-item>
</template>

<script>
export default {
	name: 'SideBarSubitem',
	components: {},
	props: { item: Object },
	data() {
		return {
			isHover: false,
			isFocused: false
		};
	},
	computed: {
		itemStyle() {
			const style = {};

			if (this.isHover || this.isFocused) {
				style.color = this.item.selectedForeground;
				style.backgroundColor = this.item.selectedBackground;
			} else {
				style.color = this.item.foreground;
				style.backgroundColor = this.item.background;
			}

			return style;
		}
	},
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
	display: flex;
	align-items: center;
	height: rem(45);
	color: map-get($theme-colors, 'light');
	background-color: map-get($theme-colors, 'primary');

	&:hover,
	&:focus {
		color: map-get($theme-colors, 'primary');
		background-color: map-get($theme-colors, 'light');
	}

	.item-img {
		width: 1em;
		height: auto;
	}

	&::v-deep .dropdown-item {
		color: inherit !important;
		background: inherit !important;
	}
}
</style>
