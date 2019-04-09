import fetch from 'unfetch';
import getContextPath from './getContextPath';

const allowedResources = new Set([
	'globalActions',
	'metadata',
	'mondrianSchemaCache',
	'reportingDataCache',
	'systemSettings'
]);

export default async resource => {
	if (!allowedResources.has(resource)) {
		return false;
	}

	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/system/refresh/${resource}`;
	const response = await fetch(endpoint, {
		method: 'GET'
	});

	return response.status === 200;
};
