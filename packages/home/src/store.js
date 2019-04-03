import Vue from 'vue';
import Vuex from 'vuex';

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
		},
		categories: [
			{
				id: 'marketing',
				name: 'Marketing',
				img: require('@/assets/img/categories/marketing.svg')
			},
			{
				id: 'retail',
				name: 'Retail',
				img: require('@/assets/img/categories/retail.svg')
			},
			{
				id: 'finance',
				name: 'Finance',
				img: require('@/assets/img/categories/finance.svg')
			},
			{
				id: 'ngo',
				name: 'NGO',
				img: require('@/assets/img/categories/ngo.svg')
			},
			{
				id: 'gov',
				name: 'GOV',
				img: require('@/assets/img/categories/gov.svg')
			},
			{
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
				id: 'utilities',
				name: 'Utilities',
				img: require('@/assets/img/categories/utilities.svg')
			},
			{
				id: 'tourism',
				name: 'Tourism',
				img: require('@/assets/img/categories/tourism.svg')
			},
			{
				id: 'education',
				name: 'Education',
				img: require('@/assets/img/categories/education.svg')
			},
			{
				id: 'health',
				name: 'Health',
				img: require('@/assets/img/categories/health.svg')
			},
			{
				id: 'rrhh',
				name: 'RRHH',
				img: require('@/assets/img/categories/rrhh.svg')
			},
			{
				id: 'pharma',
				name: 'Pharma',
				img: require('@/assets/img/categories/pharma.svg')
			}
		],
		sidebar: [
			{
				id: 'sthome',
				name: 'STHome',
				icon: ['fas', 'angle-double-right'],
				subitems: [
					{
						id: 'sthome',
						name: 'STHome',
						icon: ['fac', 'tool-sthome'],
						perspective: 'sthome.perspective'
					},
					{
						id: 'stadmin',
						name: 'STAdmin',
						icon: ['fac', 'tool-stadmin'],
						perspective: 'stadmin.perspective'
					}
				]
			},
			{
				id: 'tools',
				name: 'Tools',
				icon: ['fas', 'plus'],
				subitems: [
					{
						id: 'stpivot',
						name: 'STPivot',
						icon: ['fac', 'tool-stpivot'],
						perspective: 'opened.perspective'
					},
					{
						id: 'stdashboard',
						name: 'STDashboard',
						icon: ['fac', 'tool-stdashboard'],
						perspective: 'opened.perspective'
					},
					{
						id: 'stcard',
						name: 'STCard',
						icon: ['fac', 'tool-stcard'],
						perspective: 'opened.perspective'
					},
					{
						id: 'streport',
						name: 'STReport',
						icon: ['fac', 'tool-streport'],
						perspective: 'opened.perspective'
					},
					{
						id: 'stagile',
						name: 'STAgile',
						icon: ['fac', 'tool-stagile'],
						perspective: 'opened.perspective'
					}
				]
			},
			{
				id: 'opened',
				name: 'Opened',
				perspective: 'opened.perspective',
				icon: ['far', 'window-maximize']
			},
			{
				id: 'browser',
				name: 'Browser',
				perspective: 'browser.perspective',
				icon: ['fas', 'folder-open']
			},
			{
				id: 'stsearch',
				name: 'STSearch',
				perspective: 'stsearch.perspective',
				icon: ['fac', 'tool-stsearch']
			},
			{
				id: 'favorites',
				name: 'Favorites',
				perspective: 'favorites.perspective',
				icon: ['fas', 'star']
			},
			{
				id: 'recents',
				name: 'Recents',
				perspective: 'recents.perspective',
				icon: ['far', 'clock']
			},
			{
				id: 'language',
				name: 'Language',
				icon: ['fas', 'globe-europe'],
				subitems: [
					{
						id: 'english',
						name: 'English',
						img: require('@/assets/img/flags/uk.svg'),
						link: '?locale=en_US'
					},
					{
						id: 'Spanish',
						name: 'Spanish',
						img: require('@/assets/img/flags/es.svg'),
						link: '?locale=es_ES'
					}
				]
			},
			{
				id: 'logout',
				name: 'Logout',
				icon: ['fas', 'sign-out-alt'],
				link: '../Logout'
			}
		]
	},
	getters: {},
	mutations: {},
	actions: {}
});
