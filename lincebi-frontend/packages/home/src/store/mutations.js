import mergeWith from 'lodash/mergeWith';

import i18n from '@/i18n';

import replaceParameter from '@lincebi/frontend-common/src/replaceParameter';
import safeJSON from '@lincebi/frontend-common/src/safeJSON';

export const setUserId = (state, userId) => {
	state.userId = userId;
};

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

export const setPerspectives = (state, perspectives) => {
	state.perspectives = perspectives;
};

export const setOverlays = (state, overlays) => {
	state.overlays = overlays;
};

export const setLocale = (state, locale) => {
	if (i18n.availableLocales.includes(locale)) {
		state.locale = locale;
		i18n.locale = locale;
		document.documentElement.lang = i18n.messages[locale].__meta__.lang;
		document.documentElement.dir = i18n.messages[locale].__meta__.dir;
		replaceParameter('locale', locale);
	} else {
		console.warn(`Cannot set locale "${locale}"`);
	}
};

export const setIsRepositoryLoading = (state, isRepositoryLoading) => {
	state.isRepositoryLoading = isRepositoryLoading;
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
		currentLocation = currentLocation.children.find((child) => child.path === currentPath);
		if (typeof currentLocation === 'undefined') {
			console.error('Cannot update repository file:', file);
			return;
		}
	}

	// If "isHome" differs, update "home" user setting.
	if (file.isHome !== currentLocation.isHome) {
		const oldHomeFiles = safeJSON.parse(state.userSettings.home, []);
		const newHomeFiles = file.isHome
			? [...oldHomeFiles, { fullPath: file.path }]
			: oldHomeFiles.filter((entry) => entry.fullPath !== file.path);
		state.userSettings.home = safeJSON.stringify(newHomeFiles, '[]');
	}

	// If "isGlobal" differs, update "global" global user setting.
	if (file.isGlobal !== currentLocation.isGlobal) {
		const oldGlobalFiles = safeJSON.parse(state.globalUserSettings.global, []);
		const newGlobalFiles = file.isGlobal
			? [...oldGlobalFiles, { fullPath: file.path }]
			: oldGlobalFiles.filter((entry) => entry.fullPath !== file.path);
		state.globalUserSettings.global = safeJSON.stringify(newGlobalFiles, '[]');
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

export const setNavbarExpanded = (state, navbarExpanded) => {
	if (!window.matchMedia('(min-width: 992px)').matches) {
		state.navbarExpanded = navbarExpanded;
	}
};

export const setSidebarExpanded = (state, sidebarExpanded) => {
	state.sidebarExpanded = sidebarExpanded;
};
