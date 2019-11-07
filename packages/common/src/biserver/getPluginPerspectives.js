import fetch from 'unfetch';

import getContextPath from './getContextPath';

let pluginPerspectivesPromise = null;

const getPluginPerspectives = async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/plugin-manager/perspectives`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' }
	});

	if (response.status === 200) {
		const content = await response.text();

		try {
			const json = JSON.parse(content);
			return json.pluginPerspective.map(p => p.id);
		} catch (error) {
			console.error(error);
		}
	}

	console.warn('Falling back to default "pluginPerspectives" value');
	return [];
};

export default async (...args) => {
	if (pluginPerspectivesPromise === null) {
		pluginPerspectivesPromise = getPluginPerspectives(...args);
	}
	return pluginPerspectivesPromise;
};
