import fetch from 'unfetch';

import getContextPath from './getContextPath';

let canAdministerPromise = null;

const getCanAdminister = async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/repo/files/canAdminister`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' },
	});

	if (response.status === 200) {
		return (await response.text()) === 'true';
	}

	console.warn('Falling back to default "canAdminister" value');
	return false;
};

export default async (...args) => {
	if (canAdministerPromise === null) {
		canAdministerPromise = getCanAdminister(...args);
	}
	return canAdministerPromise;
};
