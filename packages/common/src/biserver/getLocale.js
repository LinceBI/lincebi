import fetch from 'unfetch';

import getContextPath from './getContextPath';

let localePromise = null;

const getLocale = async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/mantle/locale`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' }
	});

	if (response.status === 200) {
		const localeRegex = /^([a-z]{2})(?:_[A-Z]{2})?$/;
		const localeResult = await response.text();
		const found = localeResult.match(localeRegex);
		if (found && found.length === 2) {
			return found[1];
		}
	}

	console.warn('Falling back to default locale');
	return 'en';
};

export default async (useCache = true, ...args) => {
	if (localePromise === null || !useCache) {
		localePromise = getLocale(...args);
	}
	return localePromise;
};
