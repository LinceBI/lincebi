<template>
	<b-nav :class="{ sidebar: true, shadow: true, show: showSidebar }" vertical>
		<sidebar-item v-for="(item, index) in sidebar" :key="index" :item="item" />
	</b-nav>
</template>

<script>
import waitFor from '@lincebi/biserver-frontend-common/src/waitFor';

import SidebarItem from '@/components/SidebarItem.vue';

import eventBus from '@/eventBus';
import router from '@/router';
import store from '@/store';

export default {
	name: 'Sidebar',
	components: {
		SidebarItem
	},
	computed: {
		showSidebar() {
			return store.state.showSidebar;
		},
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
		installedPlugins() {
			return store.state.installedPlugins;
		},
		supportedLocales() {
			return store.state.supportedLocales;
		},
		sidebar() {
			return [
				{
					enabled: !this.isDemo,
					id: 'home',
					name: this.$t('sidebar.home'),
					icon: ['fas', 'home'],
					href: '#/'
				},
				{
					enabled: this.isDemo,
					id: 'sthome',
					name: this.$t('sidebar.home'),
					icon: ['fas', 'home'],
					subitems: [
						{
							enabled: true,
							id: 'sthome.perspective',
							name: 'STHome',
							icon: ['fas', 'home'],
							href: '#/p/sthome.perspective'
						},
						{
							enabled: true,
							id: 'stadmin.perspective',
							name: 'STAdmin',
							icon: ['fac', 'tool-stadmin'],
							href: '#/p/stadmin.perspective'
						}
					]
				},
				{
					enabled: this.canCreate,
					id: 'tools',
					name: this.$t('sidebar.tools'),
					icon: ['fas', 'plus'],
					subitems: [
						{
							enabled: this.installedPlugins.includes('stpivot'),
							id: 'tools.stpivot',
							name: 'STPivot',
							icon: ['fac', 'tool-stpivot'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#B0B916',
							click() {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' }
									})
									.catch(() => {});
								eventBus.$emitWhen('mantle.invoke', mantleWindow => {
									mantleWindow.openURL(
										'STPivot',
										'STPivot',
										'plugin/jpivot/AnalysisViewService?component=newView'
									);
								});
							}
						},
						{
							enabled: this.installedPlugins.includes('stolap'),
							id: 'tools.stolap',
							name: 'STOlap',
							icon: ['fac', 'tool-stolap'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#993711',
							click() {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' }
									})
									.catch(() => {});
								eventBus.$emitWhen('mantle.invoke', mantleWindow => {
									mantleWindow.openURL(
										'STOlap',
										'STOlap',
										'plugin/stolap/faces/catalog.xhtml'
									);
								});
							}
						},
						{
							enabled: this.installedPlugins.includes('streport'),
							id: 'tools.streport',
							name: 'STReport',
							icon: ['fac', 'tool-streport'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#E67E22',
							click() {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' }
									})
									.catch(() => {});
								eventBus.$emitWhen('mantle.invoke', mantleWindow => {
									mantleWindow.openURL(
										'STReport',
										'STReport',
										'api/repos/saiku-adhoc/adhoc.edit'
									);
								});
							}
						},
						{
							enabled: this.installedPlugins.includes('stdashboard'),
							id: 'tools.stdashboard',
							name: 'STDashboard',
							icon: ['fac', 'tool-stdashboard'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#2980B9',
							click() {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' }
									})
									.catch(() => {});
								eventBus.$emitWhen('mantle.invoke', mantleWindow => {
									mantleWindow.openURL(
										'STDashboard',
										'STDashboard',
										'content/stdashboard?solution=system&path=temp&action=true'
									);
								});
							}
						},
						{
							enabled: this.installedPlugins.includes('stagile'),
							id: 'tools.stagile',
							name: 'STAgile',
							icon: ['fac', 'tool-stagile'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#B91616',
							click() {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' }
									})
									.catch(() => {});
								eventBus.$emitWhen('mantle.invoke', mantleWindow => {
									mantleWindow.openURL(
										'STAgile',
										'STAgile',
										'content/stagile/ui/index.html#!/new'
									);
								});
							}
						},
						{
							enabled: this.installedPlugins.includes('stcard'),
							id: 'tools.stcard',
							name: 'STCard',
							icon: ['fac', 'tool-stcard'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#00796B',
							click() {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' }
									})
									.catch(() => {});
								eventBus.$emitWhen('mantle.invoke', mantleWindow => {
									mantleWindow.openURL(
										'STCard',
										'STCard',
										'stcard/menu/STCard.jsp'
									);
								});
							}
						},
						{
							enabled: this.installedPlugins.includes('cde'),
							id: 'tools.cde',
							name: 'CDE',
							icon: ['fac', 'tool-cde'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#9836E4',
							click() {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' }
									})
									.catch(() => {});
								eventBus.$emitWhen('mantle.invoke', mantleWindow => {
									mantleWindow.openURL(
										'Webdetails CDE',
										'Webdetails CDE',
										'api/repos/wcdf/new'
									);
								});
							}
						}
					]
				},
				{
					enabled: true,
					id: 'opened',
					name: this.$t('sidebar.opened'),
					icon: ['far', 'window-maximize'],
					href: '#/p/opened.perspective'
				},
				{
					enabled: true,
					id: 'browser',
					name: this.$t('sidebar.browser'),
					icon: ['fas', 'folder-open'],
					href: '#/p/browser.perspective'
				},
				{
					enabled: this.installedPlugins.includes('stsearch'),
					id: 'stsearch',
					name: this.$t('sidebar.search'),
					icon: ['fac', 'tool-stsearch'],
					subitems: [
						{
							enabled: true,
							id: 'stsearch.search',
							name: this.$t('sidebar.search'),
							icon: ['fac', 'tool-stsearch'],
							click() {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'search.perspective' }
									})
									.catch(() => {});
								eventBus.$emit(
									'mantle.perspective.invoke',
									'search.perspective',
									async perspectiveWindow => {
										// En este caso si STSearch no ha cargado,
										// no es necesario realizar ninguna acción.
										const STSearch = perspectiveWindow.STSearch;
										if (STSearch) await STSearch.resetConfig();
									}
								);
							}
						},
						{
							enabled: true,
							id: 'stsearch.favorites',
							name: this.$t('sidebar.favorites'),
							icon: ['fas', 'star'],
							click() {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'search.perspective' }
									})
									.catch(() => {});
								eventBus.$emitWhen(
									'mantle.perspective.invoke',
									'search.perspective',
									async perspectiveWindow => {
										const STSearch = await waitFor(
											() => perspectiveWindow.STSearch
										);
										await STSearch.applyPreset('favorites');
									}
								);
							}
						},
						{
							enabled: true,
							id: 'stsearch.recents',
							name: this.$t('sidebar.recents'),
							icon: ['far', 'clock'],
							click() {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'search.perspective' }
									})
									.catch(() => {});
								eventBus.$emitWhen(
									'mantle.perspective.invoke',
									'search.perspective',
									async perspectiveWindow => {
										const STSearch = await waitFor(
											() => perspectiveWindow.STSearch
										);
										await STSearch.applyPreset('recents');
									}
								);
							}
						}
					]
				},
				{
					enabled: this.hasDataAccess,
					id: 'manageDatasources',
					name: this.$t('sidebar.manageDatasources'),
					icon: ['fas', 'database'],
					click() {
						router
							.push({
								name: 'perspective',
								params: { perspective: 'browser.perspective' }
							})
							.catch(() => {});
						eventBus.$emitWhen('mantle.invoke', mantleWindow => {
							mantleWindow.executeCommand('ManageDatasourcesCommand');
						});
					}
				},
				{
					enabled: this.canAdminister || this.canSchedule || this.hasDataAccess,
					id: 'administration',
					name: this.$t('sidebar.administration'),
					icon: ['fas', 'tools'],
					href: '#/administration'
				},
				{
					enabled: this.supportedLocales.length > 1,
					id: 'locales',
					name: this.$t('sidebar.locales'),
					icon: ['fas', 'globe-europe'],
					subitems: [
						{
							enabled: this.supportedLocales.includes('en'),
							id: 'locales.english',
							name: this.$t('locales.english'),
							img: require('@/assets/img/locales/en.svg'),
							async click() {
								await store.dispatch('updateLocale', 'en');
								await store.dispatch('fetchRepository');
							}
						},
						{
							enabled: this.supportedLocales.includes('es'),
							id: 'locales.spanish',
							name: this.$t('locales.spanish'),
							img: require('@/assets/img/locales/es.svg'),
							async click() {
								await store.dispatch('updateLocale', 'es');
								await store.dispatch('fetchRepository');
							}
						},
						{
							enabled: this.supportedLocales.includes('ca'),
							id: 'locales.catalan',
							name: this.$t('locales.catalan'),
							img: require('@/assets/img/locales/ca.svg'),
							async click() {
								await store.dispatch('updateLocale', 'ca');
								await store.dispatch('fetchRepository');
							}
						}
					]
				},
				{
					enabled: true,
					id: 'logout',
					name: this.$t('sidebar.logout'),
					icon: ['fas', 'sign-out-alt'],
					href: '#/logout'
				}
			];
		}
	}
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
