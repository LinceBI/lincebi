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
		margin: toRem(-6) toRem(5) toRem(-6) toRem(5);
	}

	.user-avatar > .user-avatar-img {
		height: toRem(32);
		width: toRem(32);
		border: toRem(2) solid rgba(map-get($theme-colors, 'dark'), 0.5);
		border-radius: 50%;
		object-fit: cover;
	}
}
</style>
