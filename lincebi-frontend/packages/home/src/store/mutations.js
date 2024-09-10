import mergeWith from 'lodash/mergeWith';

import i18n from '@/i18n';

import replaceParameter from '@lincebi/frontend-common/src/replaceParameter';
import safeJSON from '@lincebi/frontend-common/src/safeJSON';

export const setServerInfo = (state, serverInfo) => {
	state.serverInfo = serverInfo;
};

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
		document.documentElement.lang = i18n.messages[locale]?.__meta__?.lang;
		document.documentElement.dir = i18n.messages[locale]?.__meta__?.dir;
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
	let currentFile = state.repository;

	const splitFilePath = file.path.replace(/(.+)\/$/, '$1').match(/\/[^/]*/g);
	for (let i = 0; i < splitFilePath.length; i++) {
		let nextFile = currentFile.children.find((child) => child.path.endsWith(splitFilePath[i]));
		if (typeof nextFile === 'undefined') {
			const isLast = i === splitFilePath.length - 1;
			nextFile = {
				path: splitFilePath.slice(0, i).join(''),
				name: splitFilePath[i].replace(/^\//, ''),
				isFolder: !isLast,
				children: !isLast ? [] : null,
			};
			currentFile.children.push(nextFile);
		}
		currentFile = nextFile;
	}

	// If some file properties differ, update global user settings.
	for (const entry of [{ property: 'isHome', setting: 'home' }]) {
		if (entry.property in file && file[entry.property] !== currentFile[entry.property]) {
			const oldFiles = safeJSON.parse(state.globalUserSettings[entry.setting], []);
			const newFiles = file[entry.property]
				? [...oldFiles, { fullPath: file.path }]
				: oldFiles.filter((entry) => entry.fullPath !== file.path);
			state.globalUserSettings[entry.setting] = safeJSON.stringify(newFiles, '[]');
		}
	}

	// If some file properties differ, update user settings.
	for (const entry of [
		{ property: 'isFavorite', setting: 'favorites' },
		{ property: 'isRecent', setting: 'recent' },
	]) {
		if (entry.property in file && file[entry.property] !== currentFile[entry.property]) {
			const oldFiles = safeJSON.parse(state.userSettings[entry.setting], []);
			const newFiles = file[entry.property]
				? [...oldFiles, { fullPath: file.path }]
				: oldFiles.filter((entry) => entry.fullPath !== file.path);
			state.userSettings[entry.setting] = safeJSON.stringify(newFiles, '[]');
		}
	}

	// Update repository file.
	mergeWith(currentFile, file, (objValue, srcValue) => {
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
