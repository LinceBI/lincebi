<template>
	<div class="app">
		<div class="login-container">
			<login-form class="login-form" />
		</div>
		<login-footer class="login-footer" />
		<node-garden class="node-garden" />
	</div>
</template>

<script>
import LoginForm from '@/components/LoginForm.vue';
import LoginFooter from '@/components/LoginFooter.vue';
import NodeGarden from '@/components/NodeGarden.vue';

export default {
	name: 'App',
	components: {
		LoginForm,
		LoginFooter,
		NodeGarden
	}
};
</script>

<style lang="scss">
body {
	margin: 0;
	font-family: 'Titillium Web', $system-sans-serif;
	font-size: $em-base-px;
	background-image: url('~@/assets/img/background.jpg');
	background-size: cover;
	background-repeat: no-repeat;
	background-position: center;
}

.app {
	display: flex;
	flex-grow: 1;
	flex-shrink: 1;
	flex-basis: auto;
	flex-direction: column;
	min-height: 100vh;

	.login-container {
		display: flex;
		flex-grow: 1;
		flex-shrink: 1;
		flex-basis: auto;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: rem(10);

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
	}

	.login-footer {
		display: block;
		padding: rem(10);
		text-align: center;
		z-index: 1;
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
