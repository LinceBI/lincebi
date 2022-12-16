<template>
	<div v-if="icons.length > 0 || $t('footer').length > 0" class="login-footer">
		<b-img v-for="(icon, index) in icons" :key="index" :item="icon" class="icon" :src="icon" />
		{{ $t('footer', { year: new Date().getFullYear() }) }}
	</div>
</template>

<script>
export default {
	name: 'LoginFooter',
	data: () => ({
		icons: [],
	}),
};
</script>

<style scoped lang="scss">
.login-footer {
	background: rgba(map-get($theme-colors, 'light'), 0.85);

	.icon {
		margin: rem(5);
		height: rem(36);
		width: auto;
	}
}
</style>
