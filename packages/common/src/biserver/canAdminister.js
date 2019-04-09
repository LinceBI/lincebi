import fetch from 'unfetch';
import getContextPath from './getContextPath';

let canAdminister = null;

export default async () => {
	if (canAdminister !== null) {
		return canAdminister;
	}

	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/repo/files/canAdminister`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' }
	});

	if (response.status === 200) {
		canAdminister = (await response.text()) === 'true';
	} else {
		console.warn('Using predefined "canAdminister" value');
		canAdminister = false;
	}

	return canAdminister;
};
