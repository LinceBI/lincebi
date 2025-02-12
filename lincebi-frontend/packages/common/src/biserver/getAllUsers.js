import getContextPath from './getContextPath';

let allUsersPromise = null;

const getAllUsers = async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}plugin/lincebi/api/permissions/all-users`;
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
	if (allUsersPromise === null) {
		allUsersPromise = getAllUsers(...args);
	}
	return allUsersPromise;
};
