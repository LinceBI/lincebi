import safeJSON from '@lincebi/frontend-common/src/safeJSON';

import { namespace } from '@/userSettings';

export const homeTabReplacement = (state) => {
	const value = state.globalUserSettings[`${namespace}.homeTabReplacement`];
	const tab = value ? safeJSON.parse(value, null) : null;
	return tab?.type === 'tag' || tab?.type === 'frame' ? tab : null;
};

export const resolveTab = (_, getters) => (tab) => {
	return tab?.type === 'home' && getters.homeTabReplacement ? { ...tab, ...getters.homeTabReplacement } : tab;
};

export const repositoryMap = (state) => {
	const map = new Map();
	(function flatten(children) {
		children.forEach((child) => {
			if (child.isFolder) {
				map.set(child.path, child);
				flatten(child.children);
			} else {
				map.set(child.path, child);
			}
		});
	})(state.repository.children);
	return map;
};

export const repositoryTags = (_, getters) => {
	const tags = new Set();
	for (var [, file] of getters.repositoryMap) {
		if (Array.isArray(file.properties?.tags)) {
			for (const tag of file.properties.tags) {
				tags.add(tag.value);
			}
		}
	}
	return Array.from(tags).sort();
};
