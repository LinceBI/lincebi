import getContextPath from './getContextPath';

export default async (path, content) => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/repo/files/${path.replaceAll('/', ':')}`;
	const response = await fetch(endpoint, {
		method: 'PUT',
		headers: { 'Content-Type': 'text/plain' },
		body: content,
	});

	return response.status === 200;
};
