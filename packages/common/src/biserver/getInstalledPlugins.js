import isStagileInstalled from './plugins/isStagileInstalled';
import isStcardInstalled from './plugins/isStcardInstalled';
import isStdashboardInstalled from './plugins/isStdashboardInstalled';
import isStpivotInstalled from './plugins/isStpivotInstalled';
import isStreportInstalled from './plugins/isStreportInstalled';
import isStsearchInstalled from './plugins/isStsearchInstalled';

let installedPluginsPromise = null;

const getInstalledPlugins = async () => {
	// Each checker makes a HEAD request to a known plugin resource.
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

export default async (...args) => {
	if (installedPluginsPromise === null) {
		installedPluginsPromise = getInstalledPlugins(...args);
	}
	return installedPluginsPromise;
};
