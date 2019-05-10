import fetch from 'unfetch';

import getContextPath from './getContextPath';
import isDemo from '../isDemo';

const ALLOWED_SETTINGS_IN_DEMO = new Set(['global']);

export default async (key, value = '') => {
	// Some global user settings are mocked for demo.
	if (isDemo && !ALLOWED_SETTINGS_IN_DEMO.has(key)) {
		return value;
	}

	const contextPath = await getContextPath();
	const endpoint = `${contextPath}plugin/global-user-settings/api/${key}`;
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
