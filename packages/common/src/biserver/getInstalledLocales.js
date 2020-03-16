import getRegisteredPlugins from './getRegisteredPlugins';

let installedLocalesPromise = null;

const getInstalledLocales = async () => {
	const registeredPlugins = await getRegisteredPlugins();

	// English is always installed.
	const installedLocales = ['en'];

	registeredPlugins.forEach(plugin => {
		if (plugin.startsWith('languagePack_')) {
			installedLocales.push(plugin.replace('languagePack_', ''));
		}
	});

	return installedLocales;
};

export default async (...args) => {
	if (installedLocalesPromise === null) {
		installedLocalesPromise = getInstalledLocales(...args);
	}
	return installedLocalesPromise;
};
