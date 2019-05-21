<template>
	<div class="home-tab-empty">
		<div class="icon">
			<font-awesome-icon class="fa-fw" :icon="icon" :spin="spin" />
		</div>
		<div class="text">
			{{ text }}
		</div>
	</div>
</template>

<script>
export default {
	name: 'HomeTabEmpty',
	props: {
		icon: Array,
		text: {
			type: String,
			default: ''
		},
		spin: {
			type: Boolean,
			default: false
		}
	}
};
</script>

<style scoped lang="scss">
.home-tab-empty {
	display: flex;
	flex-grow: 1;
	flex-shrink: 1;
	flex-basis: auto;
	align-items: center;
	justify-content: center;
	flex-direction: column;

	text-align: center;
	font-weight: bold;
	color: map-get($theme-colors, 'secondary');

	.icon {
		padding: rem(5);
		font-size: rem(80);
	}

	.text {
		padding: rem(5);
		font-size: rem(24);
	}
}
</style>
