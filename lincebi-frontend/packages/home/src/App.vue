<template>
	<div class="app">
		<navbar-panel class="app-navbar" />
		<sidebar-panel class="app-sidebar" />
		<router-multi-view class="app-content" />
		<app-tour v-if="!onboarded" class="app-tour" />
	</div>
</template>

<script>
import uniqBy from 'lodash/uniqBy';

import safeJSON from '@lincebi/frontend-common/src/safeJSON';
import safeWindowTop from '@lincebi/frontend-common/src/safeWindowTop';

import eventBus from '@/eventBus';
import router from '@/router';
import store from '@/store';

import NavbarPanel from '@/components/NavbarPanel.vue';
import SidebarPanel from '@/components/SidebarPanel.vue';
import AppTour from '@/components/AppTour.vue';

export default {
	name: 'App',
	components: {
		NavbarPanel,
		SidebarPanel,
		AppTour,
	},
	computed: {
		onboarded: {
			get() {
				const key = `${this.namespace}.onboarded`;
				const value = store.state.userSettings[key] === 'true';
				return value;
			},
			set(onboarded) {
				const key = `${this.namespace}.onboarded`;
				const value = onboarded ? 'true' : 'false';
				store.dispatch('updateUserSettings', { [key]: value });
			},
		},
	},
	created() {
		// Expose mocked "mantle_setPerspective" method to switch perspective.
		safeWindowTop.mantle_setPerspective = (...args) => {
			router
				.push({
					name: 'perspective',
					params: { perspective: args[0] },
				})
				.catch(() => {});

			eventBus.$emitWhenAvailable('mantle-invoke', (mantleWindow) => {
				mantleWindow.mantle_setPerspective(...args);
			});
		};

		// Expose mocked "mantle_openRepositoryFile" method to switch perspective.
		safeWindowTop.mantle_openRepositoryFile = (...args) => {
			router
				.push({
					name: 'perspective',
					params: { perspective: 'opened.perspective' },
				})
				.catch(() => {});

			eventBus.$emitWhenAvailable('mantle-invoke', (mantleWindow) => {
				mantleWindow.mantle_openRepositoryFile(...args);
			});
		};
	},
	mounted() {
		this.$nextTick(async () => {
			await Promise.all([
				store.dispatch('fetchServerInfo'),
				store.dispatch('fetchAuthenticationProvider'),
				store.dispatch('fetchUserId'),
				store.dispatch('fetchCanCreate'),
				store.dispatch('fetchCanAdminister'),
				store.dispatch('fetchCanSchedule'),
				store.dispatch('fetchHasDataAccess'),
				store.dispatch('fetchPerspectives'),
				store.dispatch('fetchOverlays'),
				store.dispatch('fetchLocale'),
				store.dispatch('fetchGlobalUserSettings'),
				store.dispatch('fetchUserSettings'),
			]);

			store.commit('setSettingsLoaded', true);

			if (!this.onboarded) {
				eventBus.$emit('tour-start');
				eventBus.$on('tour-stopped', () => (this.onboarded = true));
			}

			await store.dispatch('fetchRepository');

			// Check and rename deprecated user settings.
			{
				const gs = store.state.globalUserSettings;
				const us = store.state.userSettings;
				if (us.home !== '' && us.home !== '[]' && us.home !== gs.home) {
					const favorites = safeJSON.parse(us.favorites, []);
					const home = safeJSON.parse(us.home, []);
					await store.dispatch('updateUserSettings', {
						favorites: JSON.stringify(uniqBy([...favorites, ...home], 'fullPath')),
						home: '[]',
					});
				}
				if (gs.global !== '' && gs.global !== '[]') {
					const home = safeJSON.parse(gs.home, []);
					const global = safeJSON.parse(gs.global, []);
					await store.dispatch('updateGlobalUserSettings', {
						home: JSON.stringify(uniqBy([...home, ...global], 'fullPath')),
						global: '[]',
					});
				}
			}
		});
	},
};
</script>

<style lang="scss">
body {
	margin: 0;
	height: 100vh;
	font-family: 'Titillium Web', $system-sans-serif;
	font-size: $em-base-px;
	text-align: inherit;
	background-image: url('@/assets/img/background.jpg');
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
