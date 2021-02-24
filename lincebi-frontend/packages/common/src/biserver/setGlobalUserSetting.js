import fetch from 'unfetch';

import getContextPath from './getContextPath';

import isMocked from '../isMocked';

export default async (key, value = '') => {
	// Simulate action if mocked.
	if (isMocked) {
		try {
			const prefixedKey = `_global_user_setting_${key}`;
			window.sessionStorage.setItem(prefixedKey, value);
			return window.sessionStorage.getItem(prefixedKey);
		} catch (error) {
			console.error(error);
			return null;
		}
	}

	const contextPath = await getContextPath();
	const endpoint = `${contextPath}plugin/global-user-settings/api/${key}`;
	const response = await fetch(endpoint, {
		method: 'POST',
		headers: { 'Content-Type': 'text/plain' },
		body: value,
	});

	if (response.status === 200) {
		const value = await response.text();
		return value === 'null' ? null : value;
	}

	return null;
};
