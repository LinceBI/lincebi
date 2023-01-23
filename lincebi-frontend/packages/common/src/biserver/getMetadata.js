import getContextPath from './getContextPath';
import getLocale from './getLocale';
import safeJSON from '../safeJSON';
import searchParams from '../searchParams';

export default async (paths, { locale = getLocale(), depth = 1 } = {}) => {
	if (!Array.isArray(paths)) {
		paths = [paths];
	}

	// If "locale" is a promise, resolve it.
	if (locale instanceof Promise) {
		locale = await locale;
	}

	locale = locale.substring(0, 2);
	if (locale === 'en') {
		locale = 'default';
	}

	const contextPath = await getContextPath();
	const endpoint = `${contextPath}plugin/lincebi/api/file-metadata/get?${searchParams.stringify({
		locale,
		depth,
	})}`;
	const response = await fetch(endpoint, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(paths),
	});

	if (response.status === 200) {
		const metadata = await response.json();

		// Transform "metadata" object.
		(function transform(children) {
			children.forEach((child) => {
				if (child.isFolder) {
					transform(child.children);
				} else if (typeof child.properties.tags !== 'undefined') {
					// "properties.tags" must be converted to array.
					child.properties.tags = safeJSON.parse(child.properties.tags, []);
				}
			});
		})(metadata);

		return metadata;
	}

	return null;
};
