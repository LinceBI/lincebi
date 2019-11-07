import fetch from 'unfetch';

import getContextPath from './getContextPath';

let registeredPluginsPromise = null;

const getRegisteredPlugins = async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/mantle/registeredPlugins`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' }
	});

	if (response.status === 200) {
		const content = await response.text();
		return content.replace(/(?:^\[|\]$)/g, '').split(/\s*,\s*/);
	}

	console.warn('Falling back to default "registeredPlugins" value');
	return [];
};

export default async (...args) => {
	if (registeredPluginsPromise === null) {
		registeredPluginsPromise = getRegisteredPlugins(...args);
	}
	return registeredPluginsPromise;
};
