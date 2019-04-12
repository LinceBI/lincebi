<template>
	<b-nav-text class="nav-bar-welcome">
		<b-link :to="{ name: 'profile' }">
			<b-img class="user-avatar" :src="userSettings.avatar"></b-img>
		</b-link>
		<span class="user-text">{{
			userSettings.name ? `Welcome ${userSettings.name}` : 'Welcome'
		}}</span>
	</b-nav-text>
</template>

<script>
import store from '@/store';

export default {
	name: 'NavBarWelcome',
	computed: {
		userSettings() {
			return store.state.userSettings;
		}
	}
};
</script>

<style scoped lang="scss">
.nav-bar-welcome {
	.user-avatar {
		height: rem(32);
		width: rem(32);
		margin: rem(-6) rem(10) rem(-6) 0;
		border: rem(2) solid rgba(map-get($theme-colors, 'dark'), 0.5);
		border-radius: 50%;
		object-fit: cover;
	}
}
</style>
