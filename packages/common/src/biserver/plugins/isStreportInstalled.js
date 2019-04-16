import getContextPath from '../getContextPath';

let isStreportInstalled = null;

export default async () => {
	if (isStreportInstalled !== null) {
		return isStreportInstalled;
	}

	const contextPath = await getContextPath();
	const resource = 'content/saiku-adhoc/web/js/adhoc/version.json';
	const endpoint = `${contextPath}${resource}`;
	const response = await fetch(endpoint, { method: 'HEAD' });

	isStreportInstalled = response.status === 200;

	return isStreportInstalled;
};
