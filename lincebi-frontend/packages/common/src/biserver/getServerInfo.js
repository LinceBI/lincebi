import getContextPath from './getContextPath';

let serverinfoPromise = null;

const getServerInfo = async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}plugin/lincebi/api/serverinfo/get`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'application/json' },
	});

	if (response.status === 200) {
		return await response.json();
	}

	return {};
};

export default async (...args) => {
	if (serverinfoPromise === null) {
		serverinfoPromise = getServerInfo(...args);
	}
	return serverinfoPromise;
};
