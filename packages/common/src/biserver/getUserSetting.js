import fetch from 'unfetch';

import getContextPath from './getContextPath';

export default async key => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/user-settings/${key}`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' }
	});

	if (response.status === 200) {
		const value = await response.text();
		return value === 'null' ? null : value;
	}

	return null;
};
