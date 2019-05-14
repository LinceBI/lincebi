import fetch from 'unfetch';

import getContextPath from './getContextPath';

import isDemo from '../isDemo';
import { allowedSettingsInDemo } from './getGlobalUserSetting';

export default async (key, value = '') => {
	// Mock global user settings in demo environment.
	if (isDemo && !allowedSettingsInDemo.has(key)) {
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
		body: value
	});

	if (response.status === 200) {
		const value = await response.text();
		return value === 'null' ? null : value;
	}

	return null;
};
