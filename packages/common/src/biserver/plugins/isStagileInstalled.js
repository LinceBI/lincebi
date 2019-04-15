import getContextPath from '../getContextPath';

let isStagileInstalled = null;

export default async () => {
	if (isStagileInstalled !== null) {
		return isStagileInstalled;
	}

	const contextPath = await getContextPath();
	const resource = 'content/stagile/ui/index.html';
	const endpoint = `${contextPath}${resource}`;
	const response = await fetch(endpoint, { method: 'HEAD' });

	isStagileInstalled = response.status === 200;

	return isStagileInstalled;
};
