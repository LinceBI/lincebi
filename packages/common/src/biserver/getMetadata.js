import fetch from 'unfetch';

import getContextPath from './getContextPath';
import safeJSON from '../safeJSON';
import searchParams from '../searchParams';

export default async (paths, { depth = 1 } = {}) => {
	if (!Array.isArray(paths)) {
		paths = [paths];
	}

	const contextPath = await getContextPath();
	const endpoint = `${contextPath}plugin/file-metadata/api/get?${searchParams.stringify(
		{ depth }
	)}`;
	const response = await fetch(endpoint, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(paths)
	});

	if (response.status === 200) {
		const metadata = await response.json();

		// Transform "metadata" object.
		(function transform(children) {
			children.forEach(child => {
				// "properties.tags" must be converted to array.
				if (typeof child.properties.tags !== 'undefined') {
					child.properties.tags = safeJSON.parse(child.properties.tags, []);
				}

				if (child.children) {
					transform(child.children);
				}
			});
		})(metadata);

		return metadata;
	}

	return null;
};
