import getContextPath from './getContextPath';

let authenticationProvider = null;

const getAuthenticationProvider = async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/system/authentication-provider`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'application/json' },
	});

	if (response.status === 200) {
		return (await response.json()).authenticationType;
	}

	console.error('Cannot retrieve authentication provider');
	return null;
};

export default async (...args) => {
	if (authenticationProvider === null) {
		authenticationProvider = getAuthenticationProvider(...args);
	}
	return authenticationProvider;
};
