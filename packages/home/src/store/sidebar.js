import canAdminister from '@stratebi/biserver-customization-common/src/biserver/canAdminister';
import canCreate from '@stratebi/biserver-customization-common/src/biserver/canCreate';
import waitFor from '@stratebi/biserver-customization-common/src/waitFor';

import router from '@/router';
import eventBus from '@/eventBus';

export default async (state, getters) =>
	[
		{
			enabled: true,
			id: 'sthome',
			name: 'Home',
			icon: ['fac', 'tool-sthome'],
			subitems: [
				{
					id: 'sthome.home',
					name: 'Home',
					icon: ['fac', 'tool-sthome'],
					to: {
						name: 'perspective',
						params: { perspective: 'sthome.perspective' }
					}
				},
				{
					id: 'sthome.administration',
					name: 'Administration',
					icon: ['fac', 'tool-stadmin'],
					to: {
						name: 'perspective',
						params: { perspective: 'stadmin.perspective' }
					}
				}
			]
		},
		{
			enabled: await canCreate(),
			id: 'tools',
			name: 'Tools',
			icon: ['fas', 'plus'],
			subitems: (await getters.tools).map(tool => ({
				id: `tools.${tool.id}`,
				name: tool.name,
				icon: ['fac', `tool-${tool.id}`],
				click() {
					router.push({
						name: 'perspective',
						params: { perspective: 'opened.perspective' }
					});
					eventBus.$emitWhen('mantle.invoke', mantleWindow => {
						mantleWindow.openURL(tool.name, tool.name, tool.url);
					});
				}
			}))
		},
		{
			enabled: true,
			id: 'opened',
			name: 'Opened',
			icon: ['far', 'window-maximize'],
			to: {
				name: 'perspective',
				params: { perspective: 'opened.perspective' }
			}
		},
		{
			enabled: true,
			id: 'browser',
			name: 'Browser',
			icon: ['fas', 'folder-open'],
			to: {
				name: 'perspective',
				params: { perspective: 'browser.perspective' }
			}
		},
		{
			enabled: true,
			id: 'stsearch',
			name: 'Search',
			icon: ['fac', 'tool-stsearch'],
			subitems: [
				{
					id: 'stsearch.search',
					name: 'Search',
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
					id: 'stsearch.favorites',
					name: 'Favorites',
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
					id: 'stsearch.recents',
					name: 'Recents',
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
			enabled: await canAdminister(),
			id: 'administration',
			name: 'Administration',
			icon: ['fas', 'tools'],
			to: { name: 'administration' }
		},
		{
			enabled: true,
			id: 'language',
			name: 'Language',
			icon: ['fas', 'globe-europe'],
			subitems: (await getters.languages).map(lang => ({
				id: `language.${lang.id}`,
				name: lang.name,
				img: lang.img,
				to: { name: 'home', query: { locale: lang.id } }
			}))
		},
		{
			enabled: true,
			id: 'logout',
			name: 'Logout',
			icon: ['fas', 'sign-out-alt'],
			to: { name: 'logout' }
		}
	].filter(item => item.enabled);
