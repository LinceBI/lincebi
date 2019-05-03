import getCanAdminister from '@stratebi/biserver-customization-common/src/biserver/getCanAdminister';
import getCanCreate from '@stratebi/biserver-customization-common/src/biserver/getCanCreate';
import getGlobalUserSettings from '@stratebi/biserver-customization-common/src/biserver/getGlobalUserSettings';
import getInstalledPlugins from '@stratebi/biserver-customization-common/src/biserver/getInstalledPlugins';
import getLocale from '@stratebi/biserver-customization-common/src/biserver/getLocale';
import getRepository from '@stratebi/biserver-customization-common/src/biserver/getRepository';
import getSupportedLocales from '@stratebi/biserver-customization-common/src/biserver/getSupportedLocales';
import getUserSettings from '@stratebi/biserver-customization-common/src/biserver/getUserSettings';
import setGlobalUserSettings from '@stratebi/biserver-customization-common/src/biserver/setGlobalUserSettings';
import setLocale from '@stratebi/biserver-customization-common/src/biserver/setLocale';
import setMetadata from '@stratebi/biserver-customization-common/src/biserver/setMetadata';
import setUserSettings from '@stratebi/biserver-customization-common/src/biserver/setUserSettings';

import { defaultGlobalUserSettings, defaultUserSettings } from '@/userSettings';

import i18n from '@/i18n';

export const fetchCanCreate = async ({ commit }) => {
	const canCreate = await getCanCreate();
	commit('setCanCreate', canCreate);
};

export const fetchCanAdminister = async ({ commit }) => {
	const canAdminister = await getCanAdminister();
	commit('setCanAdminister', canAdminister);
};

export const fetchInstalledPlugins = async ({ commit }) => {
	const installedPlugins = await getInstalledPlugins();
	commit('setInstalledPlugins', installedPlugins);
};

export const fetchSupportedLocales = async ({ commit }) => {
	const supportedLocales = await getSupportedLocales(i18n.availableLocales);
	commit('setSupportedLocales', supportedLocales);
};

export const fetchLocale = async ({ commit }) => {
	const locale = await getLocale();
	commit('setLocale', locale);
};

export const updateLocale = async ({ commit }, locale) => {
	const success = await setLocale(locale);
	if (success) {
		commit('setLocale', locale);
	}
};

export const fetchRepository = async ({ commit }) => {
	const repository = await getRepository();
	commit('setRepository', repository);
};

export const updateRepositoryFile = async ({ commit }, file) => {
	const result = await setMetadata([file]);
	if (result !== null && result.length > 0) {
		commit('setRepositoryFile', file);
	}
};

export const fetchUserSettings = async ({ commit }, isGlobal = false) => {
	const getFunc = isGlobal ? getGlobalUserSettings : getUserSettings;
	const defObj = isGlobal ? defaultGlobalUserSettings : defaultUserSettings;
	const fetchedUserSettings = await getFunc(Object.keys(defObj));
	Object.entries(fetchedUserSettings).forEach(([key, value]) => {
		if (value === null) {
			fetchedUserSettings[key] = defObj[key];
		}
	});
	commit('setUserSettings', fetchedUserSettings, isGlobal);
};

export const updateUserSettings = async (
	{ commit },
	userSettings,
	isGlobal = false
) => {
	const setFunc = isGlobal ? setGlobalUserSettings : setUserSettings;
	const updatedUserSettings = await setFunc(userSettings);
	commit('setUserSettings', updatedUserSettings, isGlobal);
};

export const resetUserSettings = async ({ commit }, isGlobal = false) => {
	const setFunc = isGlobal ? setGlobalUserSettings : setUserSettings;
	const defObj = isGlobal ? defaultGlobalUserSettings : defaultUserSettings;
	const updatedUserSettings = await setFunc(defObj);
	commit('setUserSettings', updatedUserSettings, isGlobal);
};
