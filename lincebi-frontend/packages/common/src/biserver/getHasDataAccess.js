import getContextPath from './getContextPath';

let hasDataAccessPromise = null;

const getHasDataAccess = async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}plugin/data-access/api/permissions/hasDataAccess`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' },
	});

	if (response.status === 200) {
		return (await response.text()) === 'true';
	}

	console.warn('Falling back to default "hasDataAccess" value');
	return false;
};

export default async (...args) => {
	if (hasDataAccessPromise === null) {
		hasDataAccessPromise = getHasDataAccess(...args);
	}
	return hasDataAccessPromise;
};
