<template>
	<b-nav-text class="NavBarWelcome">
		<b-link :to="{ name: 'profile' }">
			<b-img class="user-avatar" :src="user.avatar"></b-img>
		</b-link>
		<span class="user-text">{{
			user.name ? `Welcome ${user.name}` : 'Welcome'
		}}</span>
	</b-nav-text>
</template>

<script>
import store from '@/store';

export default {
	name: 'NavBarWelcome',
	computed: {
		user() {
			return store.state.user;
		}
	}
};
</script>

<style scoped lang="scss">
.NavBarWelcome {
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
