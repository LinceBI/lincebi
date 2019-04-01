<template>
	<div class="App">
		<NavBar />
		<b-container class="page-container" fluid>
			<b-row class="page-row">
				<b-col class="page-col sidebar">
					<SideBar />
				</b-col>
				<b-col class="page-col content">
					<router-view />
				</b-col>
			</b-row>
		</b-container>
	</div>
</template>

<script>
import NavBar from '@/components/NavBar.vue';
import SideBar from '@/components/SideBar.vue';

export default {
	name: 'App',
	components: {
		NavBar,
		SideBar
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

	.page-container,
	.page-row,
	.page-col {
		height: calc(100vh - #{$nav-link-height} - #{$nav-link-padding-y * 2});
	}

	.page-col.sidebar {
		flex: 0 0 rem(50);
		padding: 0;
	}
}
</style>
