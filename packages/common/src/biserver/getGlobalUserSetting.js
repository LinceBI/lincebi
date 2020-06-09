import fetch from 'unfetch';

import getContextPath from './getContextPath';

import isMocked from '../isMocked';

export default async (key) => {
	// Simulate action if mocked.
	if (isMocked) {
		try {
			const prefixedKey = `_global_user_setting_${key}`;
			const value = window.sessionStorage.getItem(prefixedKey);
			if (value !== null) return value;
		} catch (error) {
			console.error(error);
		}
	}

	const contextPath = await getContextPath();
	const endpoint = `${contextPath}plugin/global-user-settings/api/${key}`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' },
	});

	if (response.status === 200) {
		const value = await response.text();
		if (value.length > 0 && value !== 'null') {
			return value;
		}
	}

	return null;
};
