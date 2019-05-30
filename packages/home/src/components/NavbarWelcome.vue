<template>
	<b-nav-text class="navbar-welcome">
		<b-link class="user-avatar" href="#/profile">
			<b-img class="user-avatar-img" :src="userSettings[`${namespace}.avatar`]" />
		</b-link>
		<span class="user-text">
			{{ $t('navbar.welcome', { name: userSettings[`${namespace}.name`] }) }}
		</span>
	</b-nav-text>
</template>

<script>
import store from '@/store';

export default {
	name: 'NavbarWelcome',
	computed: {
		userSettings() {
			return store.state.userSettings;
		}
	}
};
</script>

<style scoped lang="scss">
.navbar-welcome {
	.user-avatar {
		margin: rem(-6) rem(10) rem(-6) 0;

		.user-avatar-img {
			height: rem(32);
			width: rem(32);
			border: rem(2) solid rgba(map-get($theme-colors, 'dark'), 0.5);
			border-radius: 50%;
			object-fit: cover;
		}
	}
}
</style>
