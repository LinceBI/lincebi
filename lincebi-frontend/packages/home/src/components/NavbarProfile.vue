<template>
	<b-nav-text class="navbar-profile">
		<b-link class="user-avatar" href="#/profile">
			<b-img class="user-avatar-img" :src="userSettings[`${namespace}.avatar`]" />
		</b-link>
		<span class="user-text">
			{{ userSettings[`${namespace}.name`] || userId }}
		</span>
	</b-nav-text>
</template>

<script>
import store from '@/store';

export default {
	name: 'NavbarProfile',
	computed: {
		userId() {
			return store.state.userId;
		},
		userSettings() {
			return store.state.userSettings;
		},
	},
};
</script>

<style scoped lang="scss">
.navbar-profile {
	.user-avatar,
	.user-text {
		margin: rem(-6) rem(5) rem(-6) rem(5);
	}

	.user-avatar > .user-avatar-img {
		height: rem(32);
		width: rem(32);
		border: rem(2) solid rgba(map-get($theme-colors, 'dark'), 0.5);
		border-radius: 50%;
		object-fit: cover;
	}
}
</style>
