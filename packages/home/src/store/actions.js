import getCanAdminister from '@stratebi/biserver-customization-common/src/biserver/getCanAdminister';
import getCanCreate from '@stratebi/biserver-customization-common/src/biserver/getCanCreate';
import getCanSchedule from '@stratebi/biserver-customization-common/src/biserver/getCanSchedule';
import getGlobalUserSettings from '@stratebi/biserver-customization-common/src/biserver/getGlobalUserSettings';
import getHasDataAccess from '@stratebi/biserver-customization-common/src/biserver/getHasDataAccess';
import getInstalledPlugins from '@stratebi/biserver-customization-common/src/biserver/getInstalledPlugins';
import getLocale from '@stratebi/biserver-customization-common/src/biserver/getLocale';
import getRepository from '@stratebi/biserver-customization-common/src/biserver/getRepository';
import getSupportedLocales from '@stratebi/biserver-customization-common/src/biserver/getSupportedLocales';
import getUserId from '@stratebi/biserver-customization-common/src/biserver/getUserId';
import getUserSettings from '@stratebi/biserver-customization-common/src/biserver/getUserSettings';
import setGlobalUserSettings from '@stratebi/biserver-customization-common/src/biserver/setGlobalUserSettings';
import setLocale from '@stratebi/biserver-customization-common/src/biserver/setLocale';
import setMetadata from '@stratebi/biserver-customization-common/src/biserver/setMetadata';
import setUserSettings from '@stratebi/biserver-customization-common/src/biserver/setUserSettings';

import { defaultGlobalUserSettings, defaultUserSettings } from '@/userSettings';

import i18n from '@/i18n';

export const fetchUserId = async ({ commit }) => {
	const userId = await getUserId(false);
	commit('setUserId', userId);
};

export const fetchCanCreate = async ({ commit }) => {
	const canCreate = await getCanCreate(false);
	commit('setCanCreate', canCreate);
};

export const fetchCanAdminister = async ({ commit }) => {
	const canAdminister = await getCanAdminister(false);
	commit('setCanAdminister', canAdminister);
};

export const fetchCanSchedule = async ({ commit }) => {
	const canSchedule = await getCanSchedule(false);
	commit('setCanSchedule', canSchedule);
};

export const fetchHasDataAccess = async ({ commit }) => {
	const hasDataAccess = await getHasDataAccess(false);
	commit('setHasDataAccess', hasDataAccess);
};

export const fetchInstalledPlugins = async ({ commit }) => {
	const installedPlugins = await getInstalledPlugins(false);
	commit('setInstalledPlugins', installedPlugins);
};

export const fetchSupportedLocales = async ({ commit }) => {
	const availableLocales = i18n.availableLocales;
	const supportedLocales = await getSupportedLocales(false, availableLocales);
	commit('setSupportedLocales', supportedLocales);
};

export const fetchLocale = async ({ commit }) => {
	const locale = await getLocale(false);
	commit('setLocale', locale);
};

export const updateLocale = async ({ commit }, locale) => {
	const success = await setLocale(locale);
	if (success) {
		commit('setLocale', locale);
	}
};

export const fetchRepository = async ({ commit, state }) => {
	commit('setIsRepositoryLoading', true);
	const repository = await getRepository({ locale: state.locale });
	if (repository !== null) {
		commit('setRepository', repository);
	}
	commit('setIsRepositoryLoading', false);
};

export const updateRepositoryFile = async ({ commit, state }, file) => {
	const result = await setMetadata([file], { locale: state.locale });
	if (result !== null && result.length > 0) {
		commit('setRepositoryFile', file);
	}
};

export const fetchGlobalUserSettings = async ({ commit }) => {
	const fetchedSettings = await getGlobalUserSettings(
		Object.keys(defaultGlobalUserSettings)
	);
	Object.entries(fetchedSettings).forEach(([key, value]) => {
		if (value === null) {
			fetchedSettings[key] = defaultGlobalUserSettings[key];
		}
	});
	commit('setGlobalUserSettings', fetchedSettings);
};

export const fetchUserSettings = async ({ commit }) => {
	const fetchedSettings = await getUserSettings(
		Object.keys(defaultUserSettings)
	);
	Object.entries(fetchedSettings).forEach(([key, value]) => {
		if (value === null) {
			fetchedSettings[key] = defaultUserSettings[key];
		}
	});
	commit('setUserSettings', fetchedSettings);
};

export const updateGlobalUserSettings = async ({ commit }, settings) => {
	const updatedSettings = await setGlobalUserSettings(settings);
	commit('setGlobalUserSettings', updatedSettings);
};

export const updateUserSettings = async ({ commit }, settings) => {
	const updatedSettings = await setUserSettings(settings);
	commit('setUserSettings', updatedSettings);
};

export const resetGlobalUserSettings = async ({ commit }) => {
	const updatedSettings = await setGlobalUserSettings(
		defaultGlobalUserSettings
	);
	commit('setGlobalUserSettings', updatedSettings);
};

export const resetUserSettings = async ({ commit }) => {
	const updatedSettings = await setUserSettings(defaultUserSettings);
	commit('setUserSettings', updatedSettings);
};
