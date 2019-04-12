import Vue from 'vue';
import Vuex from 'vuex';

import blankSvg from '@stratebi/biserver-customization-common/src/blankSvg';
import getUserSetting from '@stratebi/biserver-customization-common/src/biserver/getUserSetting';
import setUserSetting from '@stratebi/biserver-customization-common/src/biserver/setUserSetting';

import categoriesGetter from '@/store/categories';
import languagesGetter from '@/store/languages';
import sidebarGetter from '@/store/sidebar';
import toolsGetter from '@/store/tools';

Vue.use(Vuex);

export default new Vuex.Store({
	strict: process.env.NODE_ENV !== 'production',
	state: () => ({
		userSettings: {
			name: '',
			email: '',
			phone: '',
			address: '',
			avatar: blankSvg,
			MANTLE_SHOW_HIDDEN_FILES: 'false',
			MANTLE_SHOW_DESCRIPTIONS_FOR_TOOLTIPS: 'false'
		}
	}),
	mutations: {
		setUserSetting(state, { key, value }) {
			state.userSettings[key] = value;
		}
	},
	actions: {
		async fetchUserSettings({ commit }, keys) {
			if (!Array.isArray(keys)) keys = [keys];
			for await (let key of keys) {
				const value = await getUserSetting(key);
				if (value !== null) {
					commit('setUserSetting', { key, value });
				}
			}
		},
		async setUserSetting({ commit }, { key, value }) {
			const result = await setUserSetting(key, value);
			if (result !== null) {
				commit('setUserSetting', { key, value });
			}
		}
	},
	getters: {
		categories: categoriesGetter,
		languages: languagesGetter,
		sidebar: sidebarGetter,
		tools: toolsGetter
	}
});
