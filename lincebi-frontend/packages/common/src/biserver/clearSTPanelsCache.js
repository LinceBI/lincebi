import getContextPath from './getContextPath';

export default async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}plugin/stpanels/api/cda/cache`;
	const response = await fetch(endpoint, {
		method: 'DELETE',
	});

	return response.status === 200;
};
