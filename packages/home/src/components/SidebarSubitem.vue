<template>
	<b-link
		v-if="item.enabled"
		class="sidebar-subitem dropdown-item"
		target="_self"
		:href="item.href"
		:style="itemStyle"
		@click="onClick"
		@mouseover="isHover = true"
		@mouseout="isHover = false"
		@focus="isFocused = true"
		@blur="isFocused = false"
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
	</b-link>
</template>

<script>
export default {
	name: 'SidebarSubitem',
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
.sidebar-subitem {
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
}
</style>
