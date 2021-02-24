export default (key, value, context = window) => {
	const url = new URL(context.location);
	url.searchParams.set(key, value);
	const title = context.document.title;
	context.history.replaceState({ key, value }, title, url.toString());
};
