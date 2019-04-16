import fetch from 'unfetch';

import getContextPath from './getContextPath';

export default async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}plugin/cda/api/clearCache`;
	const response = await fetch(endpoint, {
		method: 'GET'
	});

	return response.status === 200;
};
