import fetch from 'unfetch';

let contextPath = null;

export default async () => {
	if (contextPath !== null) {
		return contextPath;
	}

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
			contextPath = found[1].replace(/\\/g, '');
			return contextPath;
		}
	}

	console.warn(`Falling back to default context path`);
	contextPath = '/pentaho/';

	return contextPath;
};
