<template>
	<div id="app">
		<LoginForm />
	</div>
</template>

<script>
import LoginForm from './components/LoginForm.vue';

export default {
	name: 'app',
	components: {
		LoginForm
	}
};
</script>

<style lang="scss">
#app {
	height: 100vh;
	width: 100vw;
	font-family: 'Titillium Web', $system-sans-serif;
	font-size: $em-base + 0px;
	background-image: url('./assets/background.jpg');
	background-size: cover;
	background-repeat: no-repeat;
	background-position: center;
}
</style>
