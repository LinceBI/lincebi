import isCdeInstalled from './plugins/isCdeInstalled';
import isMarketplaceInstalled from './plugins/isMarketplaceInstalled';
import isStagileInstalled from './plugins/isStagileInstalled';
import isStcardInstalled from './plugins/isStcardInstalled';
import isStdashboardInstalled from './plugins/isStdashboardInstalled';
import isStolapInstalled from './plugins/isStolapInstalled';
import isStpivotInstalled from './plugins/isStpivotInstalled';
import isStreportInstalled from './plugins/isStreportInstalled';
import isStsearchInstalled from './plugins/isStsearchInstalled';

let installedPluginsPromise = null;

const getInstalledPlugins = async () => {
	// Each checker implements its own logic to determine if a plugin is installed.
	const installCheckers = [
		['cde', isCdeInstalled],
		['marketplace', isMarketplaceInstalled],
		['stagile', isStagileInstalled],
		['stcard', isStcardInstalled],
		['stdashboard', isStdashboardInstalled],
		['stolap', isStolapInstalled],
		['stpivot', isStpivotInstalled],
		['streport', isStreportInstalled],
		['stsearch', isStsearchInstalled]
	];

	return (
		await Promise.all(
			installCheckers.map(async ([plugin, isInstalled]) => {
				return (await isInstalled()) ? plugin : undefined;
			})
		)
	).filter(plugin => typeof plugin === 'string');
};

export default async (...args) => {
	if (installedPluginsPromise === null) {
		installedPluginsPromise = getInstalledPlugins(...args);
	}
	return installedPluginsPromise;
};
