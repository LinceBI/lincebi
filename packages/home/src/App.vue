<template>
	<div class="app">
		<nav-bar class="page-navbar" />
		<div class="page-container">
			<side-bar class="page-sidebar" />
			<div class="page-content">
				<router-multi-view class="page-router" />
			</div>
		</div>
	</div>
</template>

<script>
import generateAvatar from '@stratebi/biserver-customization-common/src/generateAvatar';

import store from '@/store';

import NavBar from '@/components/NavBar.vue';
import SideBar from '@/components/SideBar.vue';

export default {
	name: 'App',
	components: {
		NavBar,
		SideBar
	},
	created() {
		store.dispatch('fetchCanCreate');
		store.dispatch('fetchCanAdminister');
		store.dispatch('fetchInstalledPlugins');
		store.dispatch('fetchSupportedLocales').then(() => {
			store.dispatch('fetchLocale');
		});
		store.dispatch('fetchRepository');
		store
			.dispatch('fetchUserSettings', Object.keys(store.state.userSettings))
			.then(() => {
				if (store.state.userSettings.custom_field_avatar.length === 0) {
					store.commit('setUserSetting', {
						key: 'custom_field_avatar',
						value: generateAvatar(store.state.userSettings.custom_field_name)
					});
				}
			});
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
