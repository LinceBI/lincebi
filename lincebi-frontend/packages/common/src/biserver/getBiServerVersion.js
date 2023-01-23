import getContextPath from './getContextPath';

let localePromise = null;

const getLocale = async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/version/show`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' },
	});

	if (response.status === 200) {
		return (await response.text()).trim();
	}

	return 'Unknown';
};

export default async (...args) => {
	if (localePromise === null) {
		localePromise = getLocale(...args);
	}
	return localePromise;
};
