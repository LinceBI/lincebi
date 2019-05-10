import fetch from 'unfetch';

import getContextPath from './getContextPath';
import isDemo from '../isDemo';

const ALLOWED_SETTINGS_IN_DEMO = new Set(['home', 'favorites', 'recent']);

export default async (key, value = '') => {
	// Some user settings are mocked for demo.
	if (isDemo && !ALLOWED_SETTINGS_IN_DEMO.has(key)) {
		return value;
	}

	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/user-settings/${key}`;
	const response = await fetch(endpoint, {
		method: 'POST',
		headers: { 'Content-Type': 'text/plain' },
		body: value
	});

	if (response.status === 200) {
		const value = await response.text();
		return value === 'null' ? null : value;
	}

	return null;
};
