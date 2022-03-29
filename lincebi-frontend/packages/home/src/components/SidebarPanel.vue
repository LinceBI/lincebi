<template>
	<b-nav :class="{ 'sidebar-panel': true, shadow: true, show: sidebarExpanded }" vertical>
		<sidebar-item v-for="(item, index) in sidebar" :key="index" :item="item" />
	</b-nav>
</template>

<script>
import waitFor from '@lincebi/frontend-common/src/waitFor';

import eventBus from '@/eventBus';
import router from '@/router';
import store from '@/store';

import SidebarItem from '@/components/SidebarItem.vue';

export default {
	name: 'SidebarPanel',
	components: {
		SidebarItem,
	},
	computed: {
		canCreate() {
			return store.state.canCreate;
		},
		canAdminister() {
			return store.state.canAdminister;
		},
		canSchedule() {
			return store.state.canSchedule;
		},
		hasDataAccess() {
			return store.state.hasDataAccess;
		},
		overlays() {
			return store.state.overlays;
		},
		sidebarExpanded: {
			get() {
				return store.state.sidebarExpanded;
			},
			set(sidebarExpanded) {
				store.commit('setSidebarExpanded', sidebarExpanded);
			},
		},
		sidebar() {
			return [
				{
					enabled: true,
					id: 'home',
					name: this.$t('sidebar.home'),
					icon: ['fas', 'house'],
					href: '#/',
				},
				{
					enabled: this.canCreate,
					id: 'screwdriver-wrench',
					name: this.$t('sidebar.tools'),
					icon: ['fas', 'plus'],
					href: '#/new',
				},
				{
					enabled: true,
					id: 'opened',
					name: this.$t('sidebar.opened'),
					icon: ['far', 'window-maximize'],
					href: '#/p/opened.perspective',
				},
				{
					enabled: true,
					id: 'browser',
					name: this.$t('sidebar.browser'),
					icon: ['fas', 'folder-open'],
					href: '#/p/browser.perspective',
				},
				{
					enabled: true,
					id: 'search',
					name: this.$t('sidebar.search'),
					icon: ['fac', 'tool-stsearch'],
					subitems: [
						{
							enabled: true,
							id: 'search-main',
							name: this.$t('sidebar.search'),
							icon: ['fac', 'tool-stsearch'],
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'search.perspective' },
									})
									.catch(() => {});
								eventBus.$emit(
									'mantle-perspective-invoke',
									'search.perspective',
									async (perspectiveWindow) => {
										const STSearch = perspectiveWindow.STSearch;
										// If STSearch has not loaded, no action is required.
										if (STSearch) await STSearch.resetConfig();
									}
								);
							},
						},
						{
							enabled: true,
							id: 'search-favorites',
							name: this.$t('sidebar.favorites'),
							icon: ['fas', 'star'],
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'search.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable(
									'mantle-perspective-invoke',
									'search.perspective',
									async (perspectiveWindow) => {
										const STSearch = await waitFor(() => perspectiveWindow.STSearch);
										await STSearch.applyPreset('favorites');
									}
								);
							},
						},
						{
							enabled: true,
							id: 'search-recents',
							name: this.$t('sidebar.recents'),
							icon: ['far', 'clock'],
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'search.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable(
									'mantle-perspective-invoke',
									'search.perspective',
									async (perspectiveWindow) => {
										const STSearch = await waitFor(() => perspectiveWindow.STSearch);
										await STSearch.applyPreset('recents');
									}
								);
							},
						},
					],
				},
				{
					enabled: this.hasDataAccess,
					id: 'datasources',
					name: this.$t('sidebar.datasources'),
					icon: ['fas', 'database'],
					click: () => {
						router
							.push({
								name: 'perspective',
								params: { perspective: 'browser.perspective' },
							})
							.catch(() => {});
						eventBus.$emitWhenAvailable('mantle-invoke', (mantleWindow) => {
							mantleWindow.executeCommand('ManageDatasourcesCommand');
						});
					},
				},
				{
					enabled: this.canAdminister || this.canSchedule || this.hasDataAccess,
					id: 'administration',
					name: this.$t('sidebar.administration'),
					icon: ['fas', 'screwdriver-wrench'],
					href: '#/administration',
				},
				{
					enabled: true,
					id: 'locales',
					name: this.$t('sidebar.locales'),
					icon: ['fas', 'earth-europe'],
					subitems: [
						{
							enabled: true,
							id: 'locale-arabic',
							name: this.$t('locales.arabic'),
							click: async () => {
								await store.dispatch('updateLocale', 'ar');
								await store.dispatch('fetchRepository');
							},
						},
						{
							enabled: true,
							id: 'locale-catalan',
							name: this.$t('locales.catalan'),
							click: async () => {
								await store.dispatch('updateLocale', 'ca');
								await store.dispatch('fetchRepository');
							},
						},
						{
							enabled: true,
							id: 'locale-english',
							name: this.$t('locales.english'),
							click: async () => {
								await store.dispatch('updateLocale', 'en');
								await store.dispatch('fetchRepository');
							},
						},
						{
							enabled: true,
							id: 'locale-korean',
							name: this.$t('locales.korean'),
							click: async () => {
								await store.dispatch('updateLocale', 'ko');
								await store.dispatch('fetchRepository');
							},
						},
						{
							enabled: true,
							id: 'locale-spanish',
							name: this.$t('locales.spanish'),
							click: async () => {
								await store.dispatch('updateLocale', 'es');
								await store.dispatch('fetchRepository');
							},
						},
						{
							enabled: true,
							id: 'locale-portuguese',
							name: this.$t('locales.portuguese'),
							click: async () => {
								await store.dispatch('updateLocale', 'pt_PT');
								await store.dispatch('fetchRepository');
							},
						},
					],
				},
				{
					enabled: true,
					id: 'logout',
					name: this.$t('sidebar.logout'),
					icon: ['fas', 'right-from-bracket'],
					href: '#/logout',
				},
			];
		},
	},
	created() {
		eventBus.$on('sidebar-show', () => {
			this.sidebarExpanded = true;
		});
		eventBus.$on('sidebar-hide', () => {
			this.sidebarExpanded = false;
		});
	},
};
</script>

<style scoped lang="scss">
.sidebar-panel {
	display: none;
	width: rem(50);

	background-color: map-get($theme-colors, 'primary');

	&.show {
		display: block;
	}
}
</style>
