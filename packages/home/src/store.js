import Vue from 'vue';
import Vuex from 'vuex';
import router from '@/router';
import eventBus from '@/eventBus';

Vue.use(Vuex);

export default new Vuex.Store({
	state: {
		user: {
			nickname: 'admin',
			fullname: 'Administrator',
			email: 'admin@example.com',
			phone: '+00000000000',
			address: '742 Evergreen Terrace',
			avatar: require('@/assets/img/noavatar.svg')
		},
		settings: {
			tooltipDescriptionsEnabled: true,
			showHiddenFilesEnabled: false
		}
	},
	getters: {
		async languages() {
			return [
				{
					enabled: true,
					id: 'en_US',
					name: 'English',
					img: require('@/assets/img/flags/uk.svg')
				},
				{
					enabled: true,
					id: 'es_ES',
					name: 'Spanish',
					img: require('@/assets/img/flags/es.svg')
				}
			];
		},
		async tools() {
			return [
				{
					enabled: true,
					id: 'stpivot',
					name: 'STPivot',
					url: 'plugin/jpivot/AnalysisViewService?component=newView'
				},
				{
					enabled: true,
					id: 'stdashboard',
					name: 'STDashboard',
					url: 'content/stdashboard?solution=system&path=temp&action=true'
				},
				{
					enabled: true,
					id: 'stcard',
					name: 'STCard',
					url: 'stcard/menu/STCard.jsp'
				},
				{
					enabled: true,
					id: 'streport',
					name: 'STReport',
					url: 'content/saiku-adhoc/web/index.html?biplugin=true'
				},
				{
					enabled: true,
					id: 'stagile',
					name: 'STAgile',
					url: 'content/stagile/ui/index.html#!/new'
				}
			].filter(tool => tool.enabled);
		},
		async categories() {
			return [
				{
					enabled: true,
					id: 'marketing',
					name: 'Marketing',
					img: require('@/assets/img/categories/marketing.svg')
				},
				{
					enabled: true,
					id: 'retail',
					name: 'Retail',
					img: require('@/assets/img/categories/retail.svg')
				},
				{
					enabled: true,
					id: 'finance',
					name: 'Finance',
					img: require('@/assets/img/categories/finance.svg')
				},
				{
					enabled: true,
					id: 'ngo',
					name: 'NGO',
					img: require('@/assets/img/categories/ngo.svg')
				},
				{
					enabled: true,
					id: 'gov',
					name: 'GOV',
					img: require('@/assets/img/categories/gov.svg')
				},
				{
					enabled: true,
					id: 'e-commerce',
					name: 'e-Commerce',
					img: require('@/assets/img/categories/e-commerce.svg')
				},
				{
					id: 'telco',
					name: 'Telco',
					img: require('@/assets/img/categories/telco.svg')
				},
				{
					enabled: true,
					id: 'utilities',
					name: 'Utilities',
					img: require('@/assets/img/categories/utilities.svg')
				},
				{
					enabled: true,
					id: 'tourism',
					name: 'Tourism',
					img: require('@/assets/img/categories/tourism.svg')
				},
				{
					enabled: true,
					id: 'education',
					name: 'Education',
					img: require('@/assets/img/categories/education.svg')
				},
				{
					enabled: true,
					id: 'health',
					name: 'Health',
					img: require('@/assets/img/categories/health.svg')
				},
				{
					enabled: true,
					id: 'rrhh',
					name: 'RRHH',
					img: require('@/assets/img/categories/rrhh.svg')
				},
				{
					enabled: true,
					id: 'pharma',
					name: 'Pharma',
					img: require('@/assets/img/categories/pharma.svg')
				}
			].filter(category => category.enabled);
		},
		async sidebar(state, getters) {
			return [
				{
					enabled: true,
					id: 'home',
					name: 'Home',
					icon: ['fas', 'angle-double-right'],
					subitems: [
						{
							id: 'sthome',
							name: 'STHome',
							icon: ['fac', 'tool-sthome'],
							to: {
								name: 'perspective',
								params: { perspective: 'sthome.perspective' }
							},
							click() {}
						},
						{
							id: 'stadmin',
							name: 'STAdmin',
							icon: ['fac', 'tool-stadmin'],
							to: {
								name: 'perspective',
								params: { perspective: 'stadmin.perspective' }
							},
							click() {}
						}
					]
				},
				{
					enabled: true,
					id: 'tools',
					name: 'Tools',
					icon: ['fas', 'plus'],
					subitems: (await getters.tools).map(tool => ({
						id: tool.id,
						name: tool.name,
						icon: ['fac', `tool-${tool.id}`],
						to: undefined,
						click() {
							router.push({
								name: 'perspective',
								params: { perspective: 'opened.perspective' }
							});
							eventBus.$emit('mantle.open-url', tool);
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
					},
					click() {}
				},
				{
					enabled: true,
					id: 'browser',
					name: 'Browser',
					icon: ['fas', 'folder-open'],
					to: {
						name: 'perspective',
						params: { perspective: 'browser.perspective' }
					},
					click() {}
				},
				{
					enabled: true,
					id: 'stsearch',
					name: 'STSearch',
					icon: ['fac', 'tool-stsearch'],
					to: {
						name: 'perspective',
						params: { perspective: 'stsearch.perspective' }
					},
					click() {}
				},
				{
					enabled: true,
					id: 'favorites',
					name: 'Favorites',
					icon: ['fas', 'star'],
					to: {
						name: 'perspective',
						params: { perspective: 'favorites.perspective' }
					},
					click() {}
				},
				{
					enabled: true,
					id: 'recents',
					name: 'Recents',
					icon: ['far', 'clock'],
					to: {
						name: 'perspective',
						params: { perspective: 'recents.perspective' }
					},
					click() {}
				},
				{
					enabled: true,
					id: 'language',
					name: 'Language',
					icon: ['fas', 'globe-europe'],
					subitems: (await getters.languages).map(lang => ({
						id: lang.id,
						name: lang.name,
						img: lang.img,
						to: { name: 'home', query: { locale: lang.id } },
						click() {}
					}))
				},
				{
					enabled: true,
					id: 'logout',
					name: 'Logout',
					icon: ['fas', 'sign-out-alt'],
					to: { name: 'logout' },
					click() {}
				}
			].filter(item => item.enabled);
		}
	},
	mutations: {},
	actions: {}
});
