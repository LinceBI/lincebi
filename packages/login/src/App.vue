<template>
	<div class="app d-flex align-items-center justify-content-center">
		<login-form class="login-form" />
		<node-garden class="node-garden" />
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
	padding: rem(10);
	height: 100vh;
	font-family: 'Titillium Web', $system-sans-serif;
	font-size: $em-base-px;
	background-image: url('~@/assets/img/background.jpg');
	background-size: cover;
	background-repeat: no-repeat;
	background-position: center;
}

.app {
	display: flex;
	position: relative;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	min-height: 100%;

	.login-form {
		display: block;
		position: relative;
		width: 100%;
		max-width: rem(400);
		z-index: 1;

		@include media-breakpoint-up(md) {
			left: -20%;
		}
	}

	.node-garden {
		display: block;
		position: absolute;
		top: 0;
		left: 0;
		height: 100%;
		width: 100%;
		overflow: hidden;
		z-index: 0;
	}
}
</style>
