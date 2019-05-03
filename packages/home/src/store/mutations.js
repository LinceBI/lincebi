import mergeWith from 'lodash/mergeWith';

import i18n from '@/i18n';

import replaceParameter from '@stratebi/biserver-customization-common/src/replaceParameter';

export const setCanCreate = (state, canCreate) => {
	state.canCreate = canCreate;
};

export const setCanAdminister = (state, canAdminister) => {
	state.canAdminister = canAdminister;
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

	mergeWith(currentLocation, file, (objValue, srcValue) => {
		if (Array.isArray(objValue)) return srcValue;
		return undefined;
	});
};

export const setUserSettings = (state, userSettings, isGlobal = false) => {
	const stateObj = isGlobal ? state.globalUserSettings : state.userSettings;
	Object.assign(stateObj, userSettings);
};
