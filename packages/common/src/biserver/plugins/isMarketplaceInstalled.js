import getPluginPerspectives from '../getPluginPerspectives';

let isInstalled = null;

export default async () => {
	if (isInstalled !== null) {
		return isInstalled;
	}

	const pluginPerspectives = await getPluginPerspectives();
	isInstalled = pluginPerspectives.includes('marketplace.perspective.osgi');

	return isInstalled;
};
