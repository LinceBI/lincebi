import getContextPath from '../getContextPath';

let isInstalled = null;

export default async () => {
	if (isInstalled !== null) {
		return isInstalled;
	}

	const contextPath = await getContextPath();
	const resource = 'content/stdashboard-res/stdashboard.properties';
	const endpoint = `${contextPath}${resource}`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' }
	});

	if (response.status === 200) {
		const content = await response.text();
		const include = 'ERROR_PAGE_TITLE';
		isInstalled = !content.includes(include);
	} else {
		isInstalled = false;
	}

	return isInstalled;
};
