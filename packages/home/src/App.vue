<template>
	<div class="app">
		<navbar class="app-navbar" />
		<sidebar class="app-sidebar" />
		<router-multi-view class="app-content" />
		<tour class="app-tour" />
	</div>
</template>

<script>
import store from '@/store';

import Navbar from '@/components/Navbar.vue';
import Sidebar from '@/components/Sidebar.vue';
import Tour from '@/components/Tour.vue';

export default {
	name: 'App',
	components: {
		Navbar,
		Sidebar,
		Tour,
	},
	async mounted() {
		await Promise.all([
			store.dispatch('fetchUserId'),
			store.dispatch('fetchCanCreate'),
			store.dispatch('fetchCanAdminister'),
			store.dispatch('fetchCanSchedule'),
			store.dispatch('fetchHasDataAccess'),
			store.dispatch('fetchInstalledLocales').then(() => {
				store.dispatch('fetchLocale');
			}),
			store.dispatch('fetchInstalledPlugins'),
			store.dispatch('fetchGlobalUserSettings'),
			store.dispatch('fetchUserSettings'),
		]);

		// this.$tours.tour.start();

		await store.dispatch('fetchRepository');
	},
};
</script>

<style lang="scss">
body {
	margin: 0;
	width: 100vw;
	height: 100vh;
	font-family: 'Titillium Web', $system-sans-serif;
	font-size: $em-base-px;
	background-image: url('~@/assets/img/background.jpg');
	background-size: cover;
	background-repeat: no-repeat;
	background-position: center;
	overscroll-behavior-y: contain;
}

.app {
	display: grid;
	grid-template-areas: 'navbar navbar' 'sidebar content';
	grid-template-columns: auto 1fr;
	grid-template-rows: auto 1fr;
	width: 100%;
	height: 100%;
	overflow: auto;

	> .app-navbar {
		grid-area: navbar;
		z-index: 1000;
	}

	> .app-sidebar {
		grid-area: sidebar;
		z-index: 500;
	}

	> .app-content {
		grid-area: content;
		overflow: auto;
		z-index: 0;
	}

	> .app-tour {
		display: none;
	}
}

// Display container when tour is active.
.v-tour--active .app-tour {
	display: block;
}

// Pentaho BI Server injects these elements into "window.top".
.glasspane,
.busy-indicator-container {
	display: none !important;
}
</style>
