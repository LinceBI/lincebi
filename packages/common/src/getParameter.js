export default (key, defaultValue = '') => {
	const params = new URLSearchParams(location.search);
	return params.has(key) ? params.get(key) : defaultValue;
};
