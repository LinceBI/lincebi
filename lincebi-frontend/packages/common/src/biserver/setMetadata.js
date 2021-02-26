import cloneDeep from 'lodash/cloneDeep';
import fetch from 'unfetch';

import getContextPath from './getContextPath';
import getLocale from './getLocale';
import isMocked from '../isMocked';
import safeJSON from '../safeJSON';
import searchParams from '../searchParams';

export default async (metadata, { locale = getLocale() } = {}) => {
	if (!Array.isArray(metadata)) {
		metadata = [metadata];
	}

	// Simulate action if mocked.
	if (isMocked) {
		return metadata.map((entry) => ({ fullPath: entry.path }));
	}

	// If "locale" is a promise, resolve it.
	if (locale instanceof Promise) {
		locale = await locale;
	}

	if (/^en(?:_[A-Z]{2})?$/.test(locale)) {
		locale = 'default';
	}

	// Clone "metadata" object to avoid mutating the original.
	metadata = cloneDeep(metadata);

	// Transform "metadata" object.
	for await (const child of metadata) {
		// "properties" must be defined.
		if (typeof child.properties === 'undefined') {
			child.properties = {};
		}

		// "properties.tags" must be converted to string.
		if (Array.isArray(child.properties.tags)) {
			const strTags = safeJSON.stringify(child.properties.tags, '[]');
			child.properties.tags = strTags;
		}
	}

	const contextPath = await getContextPath();
	const endpoint = `${contextPath}plugin/lincebi/api/file-metadata/set?${searchParams.stringify(
		{ locale }
	)}`;
	const response = await fetch(endpoint, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(metadata),
	});

	if (response.status === 200) {
		return response.json();
	}

	return null;
};
