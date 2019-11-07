import fetch from 'unfetch';

import getContextPath from '../getContextPath';

export default async () => {
	const contextPath = await getContextPath();
	const resource = 'stpivot/build.json';
	const endpoint = `${contextPath}${resource}`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' }
	});

	return response.status === 200;
};
