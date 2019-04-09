import fetch from 'unfetch';
import getContextPath from './getContextPath';

let canCreate = null;

export default async () => {
	if (canCreate !== null) {
		return canCreate;
	}

	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/repo/files/canCreate`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' }
	});

	if (response.status === 200) {
		canCreate = (await response.text()) === 'true';
	} else {
		console.warn('Using predefined "canCreate" value');
		canCreate = false;
	}

	return canCreate;
};
