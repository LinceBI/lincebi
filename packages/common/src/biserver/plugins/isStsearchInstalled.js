import getContextPath from '../getContextPath';

let isStsearchInstalled = null;

export default async () => {
	if (isStsearchInstalled !== null) {
		return isStsearchInstalled;
	}

	const contextPath = await getContextPath();
	const resource = 'content/stsearch/resources/html/index.html';
	const endpoint = `${contextPath}${resource}`;
	const response = await fetch(endpoint, { method: 'HEAD' });

	isStsearchInstalled = response.status === 200;

	return isStsearchInstalled;
};
