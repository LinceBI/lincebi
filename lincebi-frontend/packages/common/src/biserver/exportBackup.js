import getContextPath from './getContextPath';

export default async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/repo/files/backup`;
	window.open(endpoint, '_blank');
};
