import getContextPath from '../getContextPath';

let isStcardInstalled = null;

export default async () => {
	if (isStcardInstalled !== null) {
		return isStcardInstalled;
	}

	const contextPath = await getContextPath();
	const resource = 'stcard/config.properties';
	const endpoint = `${contextPath}${resource}`;
	const response = await fetch(endpoint, { method: 'HEAD' });

	isStcardInstalled = response.status === 200;

	return isStcardInstalled;
};
