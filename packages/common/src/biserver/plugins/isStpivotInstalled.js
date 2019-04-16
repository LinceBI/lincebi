import getContextPath from '../getContextPath';

let isStpivotInstalled = null;

export default async () => {
	if (isStpivotInstalled !== null) {
		return isStpivotInstalled;
	}

	const contextPath = await getContextPath();
	const resource = 'stpivot/build.json';
	const endpoint = `${contextPath}${resource}`;
	const response = await fetch(endpoint, { method: 'HEAD' });

	isStpivotInstalled = response.status === 200;

	return isStpivotInstalled;
};
