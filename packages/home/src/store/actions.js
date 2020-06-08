import getCanAdminister from '@lincebi/biserver-frontend-common/src/biserver/getCanAdminister';
import getCanCreate from '@lincebi/biserver-frontend-common/src/biserver/getCanCreate';
import getCanSchedule from '@lincebi/biserver-frontend-common/src/biserver/getCanSchedule';
import getGlobalUserSettings from '@lincebi/biserver-frontend-common/src/biserver/getGlobalUserSettings';
import getHasDataAccess from '@lincebi/biserver-frontend-common/src/biserver/getHasDataAccess';
import getInstalledLocales from '@lincebi/biserver-frontend-common/src/biserver/getInstalledLocales';
import getInstalledPlugins from '@lincebi/biserver-frontend-common/src/biserver/getInstalledPlugins';
import getLaunchOverlays from '@lincebi/biserver-frontend-common/src/biserver/getLaunchOverlays';
import getLocale from '@lincebi/biserver-frontend-common/src/biserver/getLocale';
import getRepository from '@lincebi/biserver-frontend-common/src/biserver/getRepository';
import getUserId from '@lincebi/biserver-frontend-common/src/biserver/getUserId';
import getUserSettings from '@lincebi/biserver-frontend-common/src/biserver/getUserSettings';
import setGlobalUserSettings from '@lincebi/biserver-frontend-common/src/biserver/setGlobalUserSettings';
import setLocale from '@lincebi/biserver-frontend-common/src/biserver/setLocale';
import setMetadata from '@lincebi/biserver-frontend-common/src/biserver/setMetadata';
import setUserSettings from '@lincebi/biserver-frontend-common/src/biserver/setUserSettings';

import { defaultGlobalUserSettings, defaultUserSettings } from '@/userSettings';

export const fetchUserId = async ({ commit }) => {
	const userId = await getUserId();
	commit('setUserId', userId);
};

export const fetchCanCreate = async ({ commit }) => {
	const canCreate = await getCanCreate();
	commit('setCanCreate', canCreate);
};

export const fetchCanAdminister = async ({ commit }) => {
	const canAdminister = await getCanAdminister();
	commit('setCanAdminister', canAdminister);
};

export const fetchCanSchedule = async ({ commit }) => {
	const canSchedule = await getCanSchedule();
	commit('setCanSchedule', canSchedule);
};

export const fetchHasDataAccess = async ({ commit }) => {
	const hasDataAccess = await getHasDataAccess();
	commit('setHasDataAccess', hasDataAccess);
};

export const fetchInstalledPlugins = async ({ commit }) => {
	const installedPlugins = await getInstalledPlugins();
	commit('setInstalledPlugins', installedPlugins);
};

export const fetchInstalledLocales = async ({ commit }) => {
	const installedLocales = await getInstalledLocales();
	commit('setInstalledLocales', installedLocales);
};

export const fetchLocale = async ({ commit }) => {
	const locale = await getLocale();
	commit('setLocale', locale);
};

export const fetchLaunchOverlays = async ({ commit }) => {
	const launchOverlays = await getLaunchOverlays();
	commit('setLaunchOverlays', launchOverlays);
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
