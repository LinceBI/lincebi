<template>
	<div class="app d-flex align-items-center justify-content-center">
		<node-garden class="node-garden" />
		<login-form class="login-form" />
	</div>
</template>

<script>
import LoginForm from '@/components/LoginForm.vue';
import NodeGarden from '@/components/NodeGarden.vue';

export default {
	name: 'App',
	components: {
		LoginForm,
		NodeGarden
	}
};
</script>

<style lang="scss">
body {
	margin: 0;
	height: 100vh;
	font-family: 'Titillium Web', $system-sans-serif;
	font-size: $em-base + 0px;
	background-image: url('~@/assets/img/background.jpg');
	background-size: cover;
	background-repeat: no-repeat;
	background-position: center;
}

.app {
	height: 100%;
	width: 100%;

	.node-garden {
		display: block;
		position: absolute;
		top: 0;
		left: 0;
		height: 100%;
		width: 100%;
	}

	.login-form {
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
