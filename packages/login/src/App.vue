<template>
	<div class="App d-flex align-items-center justify-content-center">
		<LoginForm class="LoginForm" />
	</div>
</template>

<script>
import LoginForm from '@/components/LoginForm.vue';

export default {
	name: 'App',
	components: {
		LoginForm
	}
};
</script>

<style lang="scss">
body {
	margin: 0;
	height: 100vh;
	width: 100vw;
	font-family: 'Titillium Web', $system-sans-serif;
	font-size: $em-base + 0px;
	background-image: url('~@/assets/background.jpg');
	background-size: cover;
	background-repeat: no-repeat;
	background-position: center;
}

.App {
	height: 100%;
	width: 100%;

	.LoginForm {
		margin: 0 rem(10);
		position: relative;
		width: 100%;
		max-width: rem(400);

		@include media-breakpoint-up(md) {
			left: -20%;
		}
	}
}
</style>
