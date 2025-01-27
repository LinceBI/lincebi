import getContextPath from './getContextPath';

export default async (path) => {
	const contextPath = await getContextPath();
	return `${contextPath}api/repos/${path.replaceAll('/', ':')}`;
};
