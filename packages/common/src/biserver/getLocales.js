import getPlugins from './getPlugins';

let localesPromise = null;

const getLocales = async () => {
	const registeredPlugins = await getPlugins();

	// English is always installed.
	const locales = new Set(['en']);

	registeredPlugins.forEach((plugin) => {
		const pluginRegex = /^languagePack_([a-z]{2}(?:_[A-Z]{2})?)$/;
		const found = plugin.match(pluginRegex);
		if (found && found.length === 2) {
			locales.add(found[1]);
		}
	});

	return locales;
};

export default async (...args) => {
	if (localesPromise === null) {
		localesPromise = getLocales(...args);
	}
	return localesPromise;
};
