<template>
	<b-nav class="side-bar shadow" vertical>
		<side-bar-item v-for="(item, index) in sidebar" :key="index" :item="item" />
	</b-nav>
</template>

<script>
import waitFor from '@stratebi/biserver-customization-common/src/waitFor';

import SideBarItem from '@/components/SideBarItem.vue';

import eventBus from '@/eventBus';
import router from '@/router';
import store from '@/store';

export default {
	name: 'SideBar',
	components: {
		SideBarItem
	},
	computed: {
		sidebar() {
			return [
				{
					enabled: true,
					id: 'home',
					name: this.$t('sidebar.home'),
					icon: ['fas', 'home'],
					subitems: [
						{
							enabled: true,
							id: 'home.perspective',
							name: this.$t('sidebar.home'),
							icon: ['fas', 'home'],
							to: {
								name: 'perspective',
								params: { perspective: 'home.perspective' }
							}
						},
						{
							enabled: true,
							id: 'stadmin.perspective',
							name: this.$t('sidebar.administration'),
							icon: ['fac', 'tool-stadmin'],
							to: {
								name: 'perspective',
								params: { perspective: 'stadmin.perspective' }
							}
						}
					]
				},
				{
					enabled: store.state.canCreate,
					id: 'tools',
					name: this.$t('sidebar.tools'),
					icon: ['fas', 'plus'],
					subitems: [
						{
							enabled: store.state.installedPlugins.includes('stpivot'),
							id: 'tools.stpivot',
							name: 'STPivot',
							icon: ['fac', 'tool-stpivot'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#B0B916',
							click() {
								router.push({
									name: 'perspective',
									params: { perspective: 'opened.perspective' }
								});
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
							enabled: store.state.installedPlugins.includes('stdashboard'),
							id: 'tools.stdashboard',
							name: 'STDashboard',
							icon: ['fac', 'tool-stdashboard'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#2980B9',
							click() {
								router.push({
									name: 'perspective',
									params: { perspective: 'opened.perspective' }
								});
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
							enabled: store.state.installedPlugins.includes('streport'),
							id: 'tools.streport',
							name: 'STReport',
							icon: ['fac', 'tool-streport'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#E67E22',
							click() {
								router.push({
									name: 'perspective',
									params: { perspective: 'opened.perspective' }
								});
								eventBus.$emitWhen('mantle.invoke', mantleWindow => {
									mantleWindow.openURL(
										'STReport',
										'STReport',
										'content/saiku-adhoc/web/index.html?biplugin=true'
									);
								});
							}
						},
						{
							enabled: store.state.installedPlugins.includes('stagile'),
							id: 'tools.stagile',
							name: 'STAgile',
							icon: ['fac', 'tool-stagile'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#B91616',
							click() {
								router.push({
									name: 'perspective',
									params: { perspective: 'opened.perspective' }
								});
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
							enabled: store.state.installedPlugins.includes('stcard'),
							id: 'tools.stcard',
							name: 'STCard',
							icon: ['fac', 'tool-stcard'],
							selectedForeground: '#FFFFFF',
							selectedBackground: '#00796B',
							click() {
								router.push({
									name: 'perspective',
									params: { perspective: 'opened.perspective' }
								});
								eventBus.$emitWhen('mantle.invoke', mantleWindow => {
									mantleWindow.openURL(
										'STCard',
										'STCard',
										'stcard/menu/STCard.jsp'
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
					to: {
						name: 'perspective',
						params: { perspective: 'opened.perspective' }
					}
				},
				{
					enabled: true,
					id: 'browser',
					name: this.$t('sidebar.browser'),
					icon: ['fas', 'folder-open'],
					to: {
						name: 'perspective',
						params: { perspective: 'browser.perspective' }
					}
				},
				{
					enabled: store.state.installedPlugins.includes('stsearch'),
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
								router.push({
									name: 'perspective',
									params: { perspective: 'search.perspective' }
								});
								eventBus.$emit(
									'mantle.perspective.invoke',
									'search.perspective',
									async perspectiveWindow => {
										// En este caso si STSearch no ha cargado,
										// no es necesario realizar ninguna acción.
										const STSearch = perspectiveWindow.STSearch;
										if (STSearch) await STSearch.resetConfig().doRefresh();
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
								router.push({
									name: 'perspective',
									params: { perspective: 'search.perspective' }
								});
								eventBus.$emitWhen(
									'mantle.perspective.invoke',
									'search.perspective',
									async perspectiveWindow => {
										const STSearch = await waitFor(
											() => perspectiveWindow.STSearch
										);
										await STSearch.applyPreset('favorites').doRefresh();
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
								router.push({
									name: 'perspective',
									params: { perspective: 'search.perspective' }
								});
								eventBus.$emitWhen(
									'mantle.perspective.invoke',
									'search.perspective',
									async perspectiveWindow => {
										const STSearch = await waitFor(
											() => perspectiveWindow.STSearch
										);
										await STSearch.applyPreset('recents').doRefresh();
									}
								);
							}
						}
					]
				},
				{
					enabled: store.state.canAdminister,
					id: 'administration',
					name: this.$t('sidebar.administration'),
					icon: ['fas', 'tools'],
					to: { name: 'administration' }
				},
				{
					enabled: store.state.supportedLocales.length > 1,
					id: 'locales',
					name: this.$t('sidebar.locales'),
					icon: ['fas', 'globe-europe'],
					subitems: [
						{
							enabled: store.state.supportedLocales.includes('en'),
							id: 'locales.english',
							name: this.$t('locales.english'),
							img: require('@/assets/img/locales/en.svg'),
							async click() {
								await store.dispatch('updateLocale', 'en');
							}
						},
						{
							enabled: store.state.supportedLocales.includes('es'),
							id: 'locales.spanish',
							name: this.$t('locales.spanish'),
							img: require('@/assets/img/locales/es.svg'),
							async click() {
								await store.dispatch('updateLocale', 'es');
							}
						},
						{
							enabled: store.state.supportedLocales.includes('fr'),
							id: 'locales.french',
							name: this.$t('locales.french'),
							img: require('@/assets/img/locales/fr.svg'),
							async click() {
								await store.dispatch('updateLocale', 'fr');
							}
						},
						{
							enabled: store.state.supportedLocales.includes('de'),
							id: 'locales.german',
							name: this.$t('locales.german'),
							img: require('@/assets/img/locales/de.svg'),
							async click() {
								await store.dispatch('updateLocale', 'de');
							}
						}
					]
				},
				{
					enabled: true,
					id: 'logout',
					name: this.$t('sidebar.logout'),
					icon: ['fas', 'sign-out-alt'],
					to: { name: 'logout' }
				}
			];
		}
	}
};
</script>

<style scoped lang="scss">
.side-bar {
	background-color: map-get($theme-colors, 'primary');
}
</style>
