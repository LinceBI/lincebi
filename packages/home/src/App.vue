<template>
	<div class="app">
		<navbar class="page-navbar" />
		<div class="page-container">
			<sidebar class="page-sidebar" />
			<div class="page-content">
				<router-multi-view class="page-router" />
			</div>
		</div>
	</div>
</template>

<script>
import store from '@/store';

import Navbar from '@/components/Navbar.vue';
import Sidebar from '@/components/Sidebar.vue';

export default {
	name: 'App',
	components: {
		Navbar,
		Sidebar
	},
	created() {
		store.dispatch('fetchCanCreate');
		store.dispatch('fetchCanAdminister');
		store.dispatch('fetchCanSchedule');
		store.dispatch('fetchHasDataAccess');
		store.dispatch('fetchInstalledPlugins');
		store.dispatch('fetchSupportedLocales').then(async () => {
			await store.dispatch('fetchLocale');
			await store.dispatch('fetchRepository');
		});
		store.dispatch('fetchGlobalUserSettings');
		store.dispatch('fetchUserSettings');
	}
};
</script>

<style lang="scss">
body {
	display: flex;
	flex-grow: 1;
	flex-shrink: 1;
	flex-basis: auto;
	flex-direction: column;

	margin: 0;
	min-height: 100vh;

	font-family: 'Titillium Web', $system-sans-serif;
	font-size: $em-base + 0px;
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

	> .page-navbar {
		display: flex;
		flex-grow: 0;
		flex-shrink: 1;
		flex-basis: auto;
		flex-direction: row;
		flex-wrap: wrap;

		height: $navbar-height;
		z-index: 1000;
	}

	> .page-container {
		display: flex;
		flex-grow: 1;
		flex-shrink: 1;
		flex-basis: auto;
		flex-direction: row;
		flex-wrap: nowrap;

		> .page-sidebar {
			display: flex;
			flex-grow: 0;
			flex-shrink: 0;
			flex-basis: $sidebar-width;
			flex-direction: column;

			overflow: visible;
			padding: 0;
			z-index: 500;
		}

		> .page-content {
			display: flex;
			flex-grow: 1;
			flex-shrink: 1;
			flex-basis: auto;
			flex-direction: column;

			position: relative;
			overflow: auto;
			padding: 0;
			z-index: 0;

			> .page-router {
				height: 100%;
			}
		}
	}
}

// Pentaho BI Server injects these elements into "window.top".
.glasspane,
.busy-indicator-container {
	display: none !important;
}
</style>
