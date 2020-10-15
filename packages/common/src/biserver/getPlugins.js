import fetch from 'unfetch';

import getContextPath from './getContextPath';

let pluginsPromise = null;

const getPlugins = async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/plugin-manager/ids`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' },
	});

	const plugins = new Set();

	if (response.status === 200) {
		const content = await response.text();

		try {
			const json = JSON.parse(content);
			json.strings.forEach((p) => {
				plugins.add(p);
			});
		} catch (error) {
			console.error(error);
		}
	}

	return plugins;
};

export default async (...args) => {
	if (pluginsPromise === null) {
		pluginsPromise = getPlugins(...args);
	}
	return pluginsPromise;
};
