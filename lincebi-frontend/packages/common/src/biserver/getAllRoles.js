import getContextPath from './getContextPath';

let allRolesPromise = null;

const getAllRoles = async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}plugin/lincebi/api/permissions/all-roles`;
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
	if (allRolesPromise === null) {
		allRolesPromise = getAllRoles(...args);
	}
	return allRolesPromise;
};
