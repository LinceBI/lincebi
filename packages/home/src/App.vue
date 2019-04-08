<template>
	<div class="App">
		<NavBar class="page-navbar" />
		<b-container class="page-container" fluid>
			<b-row class="page-row">
				<b-col class="page-col sidebar">
					<SideBar />
				</b-col>
				<b-col class="page-col content">
					<router-multi-view class="page-router" />
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
	font-family: 'Titillium Web', $system-sans-serif;
	font-size: $em-base + 0px;
}

.App {
	display: flex;
	flex-direction: column;
	height: 100%;

	.page-navbar {
		display: flex;
		flex-direction: row;
		z-index: 1000;
	}

	.page-container {
		display: flex;
		flex-direction: column;
		height: 100%;

		.page-row {
			height: 100%;

			.page-col.sidebar {
				flex: 0 0 rem(50);
				padding: 0;
				z-index: 500;
			}

			.page-col.content {
				padding: 0;
				z-index: 0;
			}
		}

		.page-router {
			height: 100%;
		}
	}
}
</style>
