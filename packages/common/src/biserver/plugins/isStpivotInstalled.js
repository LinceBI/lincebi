import fetch from 'unfetch';

import getContextPath from '../getContextPath';
import getRegisteredPlugins from '../getRegisteredPlugins';

export default async () => {
	const registeredPlugins = await getRegisteredPlugins();
	if (registeredPlugins.includes('stpivot')) return true;

	const contextPath = await getContextPath();
	const resource = 'stpivot/build.json';
	const endpoint = `${contextPath}${resource}`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' }
	});

	return response.status === 200;
};
