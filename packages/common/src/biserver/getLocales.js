import getPlugins from './getPlugins';

let localesPromise = null;

const getLocales = async () => {
	const registeredPlugins = await getPlugins();

	// English is always installed.
	const locales = new Set(['en']);

	registeredPlugins.forEach((plugin) => {
		if (plugin.startsWith('languagePack_')) {
			locales.add(plugin.replace('languagePack_', ''));
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
