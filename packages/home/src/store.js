import Vue from 'vue';
import Vuex from 'vuex';

import get from 'lodash/get';

import getCanAdminister from '@stratebi/biserver-customization-common/src/biserver/getCanAdminister';
import getCanCreate from '@stratebi/biserver-customization-common/src/biserver/getCanCreate';
import getInstalledPlugins from '@stratebi/biserver-customization-common/src/biserver/getInstalledPlugins';
import getLocale from '@stratebi/biserver-customization-common/src/biserver/getLocale';
import getSupportedLocales from '@stratebi/biserver-customization-common/src/biserver/getSupportedLocales';
import getUserSettings from '@stratebi/biserver-customization-common/src/biserver/getUserSettings';
import replaceParameter from '@stratebi/biserver-customization-common/src/replaceParameter';
import setLocale from '@stratebi/biserver-customization-common/src/biserver/setLocale';
import setUserSetting from '@stratebi/biserver-customization-common/src/biserver/setUserSetting';

import i18n from '@/i18n';

Vue.use(Vuex);

export default new Vuex.Store({
	strict: process.env.NODE_ENV !== 'production',
	state: () => ({
		canCreate: false,
		canAdminister: false,
		installedPlugins: [],
		supportedLocales: ['en'],
		locale: 'en',
		userSettings: {
			custom_field_name: '',
			custom_field_email: '',
			custom_field_phone: '',
			custom_field_address: '',
			custom_field_avatar: '',
			custom_field_show_menu_bar: 'false',
			custom_field_show_tool_bar: 'false',
			MANTLE_SHOW_HIDDEN_FILES: 'false',
			MANTLE_SHOW_DESCRIPTIONS_FOR_TOOLTIPS: 'false'
		}
	}),
	mutations: {
		setCanCreate(state, canCreate) {
			state.canCreate = canCreate;
		},
		setCanAdminister(state, canAdminister) {
			state.canAdminister = canAdminister;
		},
		setInstalledPlugins(state, installedPlugins) {
			state.installedPlugins = installedPlugins;
		},
		setSupportedLocales(state, supportedLocales) {
			state.supportedLocales = supportedLocales;
		},
		setLocale(state, locale) {
			if (state.supportedLocales.includes(locale)) {
				state.locale = locale;
				i18n.locale = locale;
				replaceParameter('locale', locale);
			} else {
				console.warn(`Cannot set locale "${locale}"`);
			}
		},
		setUserSetting(state, { key, value }) {
			state.userSettings[key] = value;
		},
		toggleSidebarItem(state, { key, enabled }) {
			const item = get(state.sidebar, key);
			if (typeof item !== 'undefined') {
				item.enabled = enabled;
			} else {
				console.warn(`Cannot toggle item "${key}"`);
			}
		}
	},
	actions: {
		async fetchCanCreate({ commit }) {
			const canCreate = await getCanCreate();
			commit('setCanCreate', canCreate);
		},
		async fetchCanAdminister({ commit }) {
			const canAdminister = await getCanAdminister();
			commit('setCanAdminister', canAdminister);
		},
		async fetchInstalledPlugins({ commit }) {
			const installedPlugins = await getInstalledPlugins();
			commit('setInstalledPlugins', installedPlugins);
		},
		async fetchSupportedLocales({ commit }) {
			const supportedLocales = await getSupportedLocales(i18n.availableLocales);
			commit('setSupportedLocales', supportedLocales);
		},
		async fetchLocale({ commit }) {
			const locale = await getLocale();
			commit('setLocale', locale);
		},
		async setLocale({ commit }, locale) {
			const success = await setLocale(locale);
			if (success) {
				commit('setLocale', locale);
			}
		},
		async fetchUserSettings({ commit }, keys) {
			const userSettings = await getUserSettings(
				Array.isArray(keys) ? keys : [keys]
			);
			Object.entries(userSettings).forEach(([key, value]) => {
				if (value !== null) {
					commit('setUserSetting', { key, value });
				}
			});
		},
		async setUserSetting({ commit }, { key, value }) {
			const result = await setUserSetting(key, value);
			if (result !== null) {
				commit('setUserSetting', { key, value });
			}
		}
	}
});
