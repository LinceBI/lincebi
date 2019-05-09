import mergeWith from 'lodash/mergeWith';

import i18n from '@/i18n';

import replaceParameter from '@stratebi/biserver-customization-common/src/replaceParameter';
import safeJSON from '@stratebi/biserver-customization-common/src/safeJSON';

export const setCanCreate = (state, canCreate) => {
	state.canCreate = canCreate;
};

export const setCanAdminister = (state, canAdminister) => {
	state.canAdminister = canAdminister;
};

export const setCanSchedule = (state, canSchedule) => {
	state.canSchedule = canSchedule;
};

export const setHasDataAccess = (state, hasDataAccess) => {
	state.hasDataAccess = hasDataAccess;
};

export const setInstalledPlugins = (state, installedPlugins) => {
	state.installedPlugins = installedPlugins;
};

export const setSupportedLocales = (state, supportedLocales) => {
	state.supportedLocales = supportedLocales;
};

export const setLocale = (state, locale) => {
	if (state.supportedLocales.includes(locale)) {
		state.locale = locale;
		i18n.locale = locale;
		replaceParameter('locale', locale);
	} else {
		console.warn(`Cannot set locale "${locale}"`);
	}
};

export const setRepository = (state, repository) => {
	state.repository = repository;
};

export const setRepositoryFile = (state, file) => {
	let currentPath = '';
	let currentLocation = state.repository;

	const splittedFilePath = file.path.replace(/(.+)\/$/, '$1').match(/\/[^/]*/g);

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

	// If "isHomeItem" differs, update "home-items" user setting.
	if (file.isHomeItem !== currentLocation.isHomeItem) {
		const oldHomeItemsStr = state.userSettings['home-items'];
		const oldHomeItems = safeJSON.parse(oldHomeItemsStr, []);
		const newHomeItems = file.isHomeItem
			? [...oldHomeItems, { fullPath: file.path }]
			: oldHomeItems.filter(item => item.fullPath !== file.path);
		const newHomeItemsStr = safeJSON.stringify(newHomeItems, '[]');
		state.userSettings['home-items'] = newHomeItemsStr;
	}

	// If "isGlobalItem" differs, update "global-items" global user setting.
	if (file.isGlobalItem !== currentLocation.isGlobalItem) {
		const oldGlobalItemsStr = state.globalUserSettings['global-items'];
		const oldGlobalItems = safeJSON.parse(oldGlobalItemsStr, []);
		const newGlobalItems = file.isGlobalItem
			? [...oldGlobalItems, { fullPath: file.path }]
			: oldGlobalItems.filter(item => item.fullPath !== file.path);
		const newGlobalItemsStr = safeJSON.stringify(newGlobalItems, '[]');
		state.globalUserSettings['global-items'] = newGlobalItemsStr;
	}

	// Update repository file.
	mergeWith(currentLocation, file, (objValue, srcValue) => {
		if (Array.isArray(objValue)) return srcValue;
		return undefined;
	});
};

export const setGlobalUserSettings = (state, settings) => {
	Object.assign(state.globalUserSettings, settings);
};

export const setUserSettings = (state, settings) => {
	Object.assign(state.userSettings, settings);
};
