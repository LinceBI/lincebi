import cloneDeep from 'lodash/cloneDeep';
import fetch from 'unfetch';

import getContextPath from './getContextPath';
import isDemo from '../isDemo';
import safeJSON from '../safeJSON';
import searchParams from '../searchParams';

export default async (metadata, { locale = 'default' } = {}) => {
	if (!Array.isArray(metadata)) {
		metadata = [metadata];
	}

	// Mock metadata update in demo environment.
	if (isDemo) {
		return metadata.map(entry => ({ fullPath: entry.path }));
	}

	if (/^en(?:_[A-Z]{2})?$/.test(locale)) {
		locale = 'default';
	}

	// Clone "metadata" object to avoid mutating the original.
	metadata = cloneDeep(metadata);

	// Transform "metadata" object.
	for await (const child of metadata) {
		// "properties.tags" must be converted to string.
		if (child.properties && Array.isArray(child.properties.tags)) {
			child.properties.tags = safeJSON.stringify(child.properties.tags, '[]');
		}
	}

	const contextPath = await getContextPath();
	const endpoint = `${contextPath}plugin/file-metadata/api/set?${searchParams.stringify(
		{ locale }
	)}`;
	const response = await fetch(endpoint, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(metadata)
	});

	if (response.status === 200) {
		return response.json();
	}

	return null;
};
