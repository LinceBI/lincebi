import fetch from 'unfetch';

import getContextPath from './getContextPath';

let canSchedulePromise = null;

const getCanSchedule = async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/scheduler/canSchedule`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' },
	});

	if (response.status === 200) {
		return (await response.text()) === 'true';
	}

	console.warn('Falling back to default "canSchedule" value');
	return false;
};

export default async (...args) => {
	if (canSchedulePromise === null) {
		canSchedulePromise = getCanSchedule(...args);
	}
	return canSchedulePromise;
};
