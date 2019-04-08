<template>
	<b-nav-text class="NavBarWelcome">
		<b-img class="user-avatar" :src="user.avatar"></b-img>
		<span class="user-text">Welcome {{ user.fullname }}</span>
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
	}
}
</style>
