import safeJSON from '@stratebi/biserver-customization-common/src/safeJSON';

export const repositoryFiles = state => {
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
};

export const globalFiles = (state, getters) => {
	const paths = new Set(
		safeJSON
			.parse(state.globalUserSettings.global, [])
			.map(entry => entry.fullPath)
	);
	const files = getters.repositoryFiles.filter(file => paths.has(file.path));
	return files;
};

export const homeFiles = (state, getters) => {
	const paths = new Set(
		safeJSON.parse(state.userSettings.home, []).map(entry => entry.fullPath)
	);
	const files = getters.repositoryFiles.filter(file => paths.has(file.path));
	return files;
};
