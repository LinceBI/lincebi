<template>
	<div class="App">
		<NavBar />
		<b-container>
			<router-view />
		</b-container>
	</div>
</template>

<script>
import NavBar from '@/components/NavBar.vue';

export default {
	name: 'App',
	components: {
		NavBar
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
}

.App {
	height: 100%;
	width: 100%;
}
</style>
