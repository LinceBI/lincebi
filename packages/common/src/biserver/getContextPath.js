import fetch from 'unfetch';

let contextPathPromise = null;

const getContextPath = async () => {
	const endpoint = '../webcontext.js?useFullyQualifiedUrl=false';
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' }
	});

	if (response.status === 200) {
		// Horrible way to get the CONTEXT_PATH.
		// TODO: find a more robust approach.
		const contextPathRegex = /^var\sCONTEXT_PATH\s=\s"(.*)";$/m;
		const webcontextText = await response.text();
		const found = webcontextText.match(contextPathRegex);
		if (found && found.length === 2) {
			return found[1].replace(/\\/g, '');
		}
	}

	console.warn('Falling back to default context path');
	return '/pentaho/';
};

export default async (useCache = true, ...args) => {
	if (contextPathPromise === null || !useCache) {
		contextPathPromise = getContextPath(...args);
	}
	return contextPathPromise;
};
