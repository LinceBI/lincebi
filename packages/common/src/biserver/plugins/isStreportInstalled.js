import getRegisteredPlugins from '../getRegisteredPlugins';

let isInstalled = null;

export default async () => {
	if (isInstalled !== null) {
		return isInstalled;
	}

	const registeredPlugins = await getRegisteredPlugins();
	isInstalled = registeredPlugins.includes('saiku-adhoc');

	return isInstalled;
};
