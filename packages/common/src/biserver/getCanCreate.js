import fetch from 'unfetch';

import getContextPath from './getContextPath';

let canCreatePromise = null;
const getCanCreate = async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/repo/files/canCreate`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' }
	});

	if (response.status === 200) {
		return (await response.text()) === 'true';
	}

	console.warn('Falling back to default "canCreate" value');
	return false;
};

export default async (...args) => {
	if (canCreatePromise === null) {
		canCreatePromise = getCanCreate(...args);
	}
	return await canCreatePromise;
};
