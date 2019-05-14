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
