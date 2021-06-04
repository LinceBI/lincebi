<template>
	<b-nav :class="{ sidebar: true, shadow: true, show: sidebarExpanded }" vertical>
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
	name: 'Sidebar',
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
		locales() {
			return store.state.locales;
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
					icon: ['fas', 'home'],
					href: '#/',
				},
				{
					enabled: this.canCreate,
					id: 'tools',
					name: this.$t('sidebar.tools'),
					icon: ['fas', 'plus'],
					subitems: [
						{
							enabled: this.overlays.has('launch.stpivot'),
							id: 'tool-stpivot',
							name: 'STPivot',
							icon: ['fac', 'tool-stpivot'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#B0B916',
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable(
									'mantle-home-command',
									this.overlays.get('launch.stpivot')
								);
							},
						},
						{
							enabled: this.overlays.has('launch.launch_new_stolap_analytics'),
							id: 'tool-stolap',
							name: 'STOlap',
							icon: ['fac', 'tool-stolap'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#993711',
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable(
									'mantle-home-command',
									this.overlays.get('launch.launch_new_stolap_analytics')
								);
							},
						},
						{
							enabled: this.overlays.has('launch.launch-new-saiku-adhoc'),
							id: 'tool-streport',
							name: 'STReport',
							icon: ['fac', 'tool-streport'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#E67E22',
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable(
									'mantle-home-command',
									this.overlays.get('launch.launch-new-saiku-adhoc')
								);
							},
						},
						{
							enabled: this.overlays.has('launch.stdashboardButton'),
							id: 'tool-stdashboard',
							name: 'STDashboard',
							icon: ['fac', 'tool-stdashboard'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#2980B9',
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable(
									'mantle-home-command',
									this.overlays.get('launch.stdashboardButton')
								);
							},
						},
						{
							enabled: this.overlays.has('launch.launch-new-stagile'),
							id: 'tool-stagile',
							name: 'STAgile',
							icon: ['fac', 'tool-stagile'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#B91616',
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable(
									'mantle-home-command',
									this.overlays.get('launch.launch-new-stagile')
								);
							},
						},
						{
							enabled: this.overlays.has('launch.button.stcard'),
							id: 'tool-stcard',
							name: 'STCard',
							icon: ['fac', 'tool-stcard'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#00796B',
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable(
									'mantle-home-command',
									this.overlays.get('launch.button.stcard')
								);
							},
						},
						{
							enabled: this.overlays.has('launch.SaikuAnalytics'),
							id: 'tool-saiku',
							name: 'Saiku Analytics',
							icon: ['fac', 'tool-saiku'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#C52120',
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable(
									'mantle-home-command',
									this.overlays.get('launch.SaikuAnalytics')
								);
							},
						},
						{
							enabled: this.overlays.has('launch.launch_new_cde'),
							id: 'tool-cde',
							name: 'CDE',
							icon: ['fac', 'tool-cde'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#9836E4',
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable('mantle-invoke', (mantleWindow) => {
									mantleWindow.openURL('Webdetails CDE', 'Webdetails CDE', 'api/repos/wcdf/new');
								});
							},
						},
						{
							enabled: this.overlays.has('launch.jpivot'),
							id: 'tool-jpivot',
							name: 'JPivot',
							icon: ['fac', 'tool-jpivot'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#00B919',
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable(
									'mantle-home-command',
									this.overlays.get('launch.jpivot')
								);
							},
						},
						{
							enabled: this.overlays.has(
								'startup.repositorySynchronizer.toolsmenu.repositorySynchronizer'
							),
							id: 'tool-repository-synchronizer',
							name: 'Repository Synchronizer',
							icon: ['fac', 'tool-repository-synchronizer'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#4B4B4B',
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable('mantle-invoke', (mantleWindow) => {
									mantleWindow.openURL(
										'Repository Synchronizer',
										'Repository Synchronizer',
										'plugin/repositorySynchronizer/api/default'
									);
								});
							},
						},
					],
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
					icon: ['fas', 'tools'],
					href: '#/administration',
				},
				{
					enabled: this.locales.size > 1,
					id: 'locales',
					name: this.$t('sidebar.locales'),
					icon: ['fas', 'globe-europe'],
					subitems: [
						{
							enabled: this.locales.has('en'),
							id: 'locale-english',
							name: this.$t('locales.english'),
							img: require('@/assets/img/locales/en.svg'),
							click: async () => {
								await store.dispatch('updateLocale', 'en');
								await store.dispatch('fetchRepository');
							},
						},
						{
							enabled: this.locales.has('es'),
							id: 'locale-spanish',
							name: this.$t('locales.spanish'),
							img: require('@/assets/img/locales/es.svg'),
							click: async () => {
								await store.dispatch('updateLocale', 'es');
								await store.dispatch('fetchRepository');
							},
						},
						{
							enabled: this.locales.has('pt_PT'),
							id: 'locale-portuguese',
							name: this.$t('locales.portuguese'),
							img: require('@/assets/img/locales/pt.svg'),
							click: async () => {
								await store.dispatch('updateLocale', 'pt_PT');
								await store.dispatch('fetchRepository');
							},
						},
						{
							enabled: this.locales.has('ca'),
							id: 'locale-catalan',
							name: this.$t('locales.catalan'),
							img: require('@/assets/img/locales/ca.svg'),
							click: async () => {
								await store.dispatch('updateLocale', 'ca');
								await store.dispatch('fetchRepository');
							},
						},
					],
				},
				{
					enabled: true,
					id: 'logout',
					name: this.$t('sidebar.logout'),
					icon: ['fas', 'sign-out-alt'],
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
.sidebar {
	background-color: map-get($theme-colors, 'primary');

	position: relative;
	$sidebar-width: rem(50);
	width: $sidebar-width;
	left: -$sidebar-width;
	margin-right: -$sidebar-width;

	// It's pretty, but also very slow.
	// transition: left 0.35s ease, margin-right 0.35s ease;

	&.show {
		left: 0;
		margin-right: 0;
	}
}
</style>
