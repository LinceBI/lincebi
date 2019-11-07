import getRegisteredPlugins from '../getRegisteredPlugins';

export default async () => {
	const registeredPlugins = await getRegisteredPlugins();
	return registeredPlugins.includes('stdashboard');
};
