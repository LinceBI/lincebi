import getContextPath from './getContextPath';

export default async (locale) => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/mantle/locale`;
	const response = await fetch(endpoint, {
		method: 'POST',
		headers: { 'Content-Type': 'text/plain' },
		body: locale,
	});

	return response.status === 200;
};
