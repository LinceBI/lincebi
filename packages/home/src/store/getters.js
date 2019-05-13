import safeJSON from '@stratebi/biserver-customization-common/src/safeJSON';

export const repositoryMap = state => {
	const map = new Map();
	(function flatten(children) {
		children.forEach(child => {
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

export const globalFiles = (state, getters) => {
	const files = [];
	const entries = safeJSON.parse(state.globalUserSettings.global, []);
	for (const entry of entries) {
		if (getters.repositoryMap.has(entry.fullPath)) {
			files.push(getters.repositoryMap.get(entry.fullPath));
		}
	}
	return files;
};

export const homeFiles = (state, getters) => {
	const files = [];
	const entries = safeJSON.parse(state.userSettings.home, []);
	for (const entry of entries) {
		if (getters.repositoryMap.has(entry.fullPath)) {
			files.push(getters.repositoryMap.get(entry.fullPath));
		}
	}
	return files;
};
