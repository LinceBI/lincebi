export default (key, value) => {
	const url = new URL(location);
	url.searchParams.set(key, value);
	history.replaceState({ key, value }, document.title, url.toString());
};
