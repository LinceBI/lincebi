import getContextPath from '../getContextPath';

let isStdashboardInstalled = null;

export default async () => {
	if (isStdashboardInstalled !== null) {
		return isStdashboardInstalled;
	}

	const contextPath = await getContextPath();
	const resource = 'content/stdashboard-res/css/STDashboard.css';
	const endpoint = `${contextPath}${resource}`;
	const response = await fetch(endpoint, { method: 'HEAD' });

	isStdashboardInstalled = response.status === 200;

	return isStdashboardInstalled;
};
