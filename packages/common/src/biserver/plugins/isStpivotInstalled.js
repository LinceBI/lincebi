import getContextPath from '../getContextPath';

let isInstalled = null;

export default async () => {
	if (isInstalled !== null) {
		return isInstalled;
	}

	const contextPath = await getContextPath();
	const resource = 'stpivot/build.json';
	const endpoint = `${contextPath}${resource}`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' }
	});

	if (response.status === 200) {
		isInstalled = true;
	} else {
		isInstalled = false;
	}

	return isInstalled;
};
