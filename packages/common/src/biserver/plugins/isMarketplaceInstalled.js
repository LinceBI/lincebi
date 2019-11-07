import getPluginPerspectives from '../getPluginPerspectives';

export default async () => {
	const pluginPerspectives = await getPluginPerspectives();

	return pluginPerspectives.includes('marketplace.perspective.osgi');
};
