import getContextPath from './getContextPath';

let ownRolesPromise = null;

const getOwnRoles = async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}plugin/lincebi/api/permissions/own-roles`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'application/json' },
	});

	if (response.status === 200) {
		return await response.json();
	}

	return [];
};

export default async (...args) => {
	if (ownRolesPromise === null) {
		ownRolesPromise = getOwnRoles(...args);
	}
	return ownRolesPromise;
};
