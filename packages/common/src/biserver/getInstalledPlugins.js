import isStagileInstalled from './plugins/isStagileInstalled';
import isStcardInstalled from './plugins/isStcardInstalled';
import isStdashboardInstalled from './plugins/isStdashboardInstalled';
import isStpivotInstalled from './plugins/isStpivotInstalled';
import isStreportInstalled from './plugins/isStreportInstalled';
import isStsearchInstalled from './plugins/isStsearchInstalled';

let installedPluginsPromise = null;

const getInstalledPlugins = async () => {
	// Each checker implements its own logic to determine if a plugin is installed.
	const installCheckers = [
		['stagile', isStagileInstalled],
		['stcard', isStcardInstalled],
		['stdashboard', isStdashboardInstalled],
		['stpivot', isStpivotInstalled],
		['streport', isStreportInstalled],
		['stsearch', isStsearchInstalled]
	];

	return (await Promise.all(
		installCheckers.map(async ([plugin, isInstalled]) => {
			return (await isInstalled()) ? plugin : undefined;
		})
	)).filter(plugin => typeof plugin === 'string');
};

export default async (useCache = true, ...args) => {
	if (installedPluginsPromise === null || !useCache) {
		installedPluginsPromise = getInstalledPlugins(...args);
	}
	return installedPluginsPromise;
};
