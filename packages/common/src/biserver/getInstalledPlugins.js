import isStagileInstalled from './plugins/isStagileInstalled';
import isStcardInstalled from './plugins/isStcardInstalled';
import isStdashboardInstalled from './plugins/isStdashboardInstalled';
import isStpivotInstalled from './plugins/isStpivotInstalled';
import isStreportInstalled from './plugins/isStreportInstalled';
import isStsearchInstalled from './plugins/isStsearchInstalled';

let installedPlugins = null;

export default async () => {
	if (installedPlugins !== null) {
		return installedPlugins;
	}

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

	installedPlugins = installCheckersResults
		.filter(([, isInstalled]) => isInstalled)
		.map(([plugin]) => plugin);

	return installedPlugins;
};
