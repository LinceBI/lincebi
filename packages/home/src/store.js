import Vue from 'vue';
import Vuex from 'vuex';

import mergeWith from 'lodash/mergeWith';

import getCanAdminister from '@stratebi/biserver-customization-common/src/biserver/getCanAdminister';
import getCanCreate from '@stratebi/biserver-customization-common/src/biserver/getCanCreate';
import getInstalledPlugins from '@stratebi/biserver-customization-common/src/biserver/getInstalledPlugins';
import getLocale from '@stratebi/biserver-customization-common/src/biserver/getLocale';
import getRepository from '@stratebi/biserver-customization-common/src/biserver/getRepository';
import getSupportedLocales from '@stratebi/biserver-customization-common/src/biserver/getSupportedLocales';
import getUserSettings from '@stratebi/biserver-customization-common/src/biserver/getUserSettings';
import replaceParameter from '@stratebi/biserver-customization-common/src/replaceParameter';
import setLocale from '@stratebi/biserver-customization-common/src/biserver/setLocale';
import setMetadata from '@stratebi/biserver-customization-common/src/biserver/setMetadata';
import setUserSettings from '@stratebi/biserver-customization-common/src/biserver/setUserSettings';

import { initialUserSettings, defaultUserSettings } from './userSettings';

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
		repository: { path: '/', children: [] },
		userSettings: initialUserSettings
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
		setRepository(state, repository) {
			state.repository = repository;
		},
		setRepositoryFile(state, file) {
			let currentPath = '';
			let currentLocation = state.repository;

			const splittedFilePath = file.path
				.replace(/(.+)\/$/, '$1')
				.match(/\/[^/]*/g);

			for (const filePathFragment of splittedFilePath) {
				currentPath += filePathFragment;
				currentLocation = currentLocation.children.find(
					child => child.path === currentPath
				);
				if (typeof currentLocation === 'undefined') {
					console.error('Cannot update repository file:', file);
					return;
				}
			}

			mergeWith(currentLocation, file, (objValue, srcValue) => {
				if (Array.isArray(objValue)) return srcValue;
				return undefined;
			});
		},
		setUserSettings(state, userSettings) {
			Object.assign(state.userSettings, userSettings);
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
		async fetchRepository({ commit }) {
			const repository = await getRepository();
			commit('setRepository', repository);
		},
		async updateRepositoryFile({ commit }, file) {
			const result = await setMetadata([file]);
			if (result !== null && result.length > 0) {
				commit('setRepositoryFile', file);
			}
		},
		async fetchUserSettings({ commit }) {
			const fetchedUserSettings = await getUserSettings(
				Object.keys(defaultUserSettings)
			);

			Object.entries(fetchedUserSettings).forEach(([key, value]) => {
				if (value === null) {
					fetchedUserSettings[key] = defaultUserSettings[key];
				}
			});

			commit('setUserSettings', fetchedUserSettings);
		},
		async updateUserSettings({ commit }, userSettings) {
			const updatedUserSettings = await setUserSettings(userSettings);
			commit('setUserSettings', updatedUserSettings);
		},
		async resetUserSettings({ commit }) {
			const updatedUserSettings = await setUserSettings(defaultUserSettings);
			commit('setUserSettings', updatedUserSettings);
		}
	},
	getters: {
		flattenedRepository(state) {
			const files = [];

			(function flatten(children) {
				children.forEach(child => {
					if (child.isFolder) {
						flatten(child.children);
					} else {
						files.push(child);
					}
				});
			})(state.repository.children);

			return files;
		}
	}
});
