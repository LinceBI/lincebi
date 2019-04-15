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

	const installCheckersResults = await Promise.all(
		installCheckers.map(([plugin, installChecker]) => {
			return installChecker().then(isInstalled => [plugin, isInstalled]);
		})
	);

	return installCheckersResults
		.filter(([, isInstalled]) => isInstalled)
		.map(([plugin]) => plugin);
};

export default async (...args) => {
	if (installedPluginsPromise === null) {
		installedPluginsPromise = getInstalledPlugins(...args);
	}
	return await installedPluginsPromise;
};
