<template>
	<div class="app">
		<navbar class="page-navbar" />
		<sidebar class="page-sidebar" />
		<router-multi-view class="page-content" />
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
		store.dispatch('fetchUserId');
		store.dispatch('fetchCanCreate');
		store.dispatch('fetchCanAdminister');
		store.dispatch('fetchCanSchedule');
		store.dispatch('fetchHasDataAccess');
		store.dispatch('fetchInstalledPlugins');
		store.dispatch('fetchInstalledLocales').then(async () => {
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

	> .page-navbar {
		grid-area: navbar;
		z-index: 1000;
	}

	> .page-sidebar {
		grid-area: sidebar;
		z-index: 500;
	}

	> .page-content {
		grid-area: content;
		overflow: auto;
		z-index: 0;
	}
}

// Pentaho BI Server injects these elements into "window.top".
.glasspane,
.busy-indicator-container {
	display: none !important;
}
</style>
