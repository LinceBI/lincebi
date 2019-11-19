<template>
	<div v-if="$t('footer').length > 0" class="login-footer">
		{{ $t('footer', { year: new Date().getFullYear() }) }}
	</div>
</template>

<script>
export default {
	name: 'LoginFooter'
};
</script>

<style scoped lang="scss">
.login-footer {
	background: rgba(map-get($theme-colors, 'light'), 0.85);
}
</style>
