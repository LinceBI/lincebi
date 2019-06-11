import fetch from 'unfetch';

let webcontextWorkerPromise = null;

const webcontextHeader = `
	const document = {};
	document.write = () => {};
`;

const webcontextFooter = `
	onmessage = event => {
		postMessage(eval(event.data));
	};
`;

const getWebcontextWorker = async () => {
	const endpoint = '../webcontext.js?useFullyQualifiedUrl=false';
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' }
	});

	if (response.status === 200) {
		const webcontextBody = await response.text();
		const webcontextBlob = new Blob(
			[webcontextHeader, webcontextBody, webcontextFooter],
			{ type: 'text/javascript' }
		);
		return new Worker(window.URL.createObjectURL(webcontextBlob));
	}

	console.error('Cannot retrieve webcontext.js');
	return null;
};

export default async (...args) => {
	if (webcontextWorkerPromise === null) {
		webcontextWorkerPromise = getWebcontextWorker(...args);
	}
	return webcontextWorkerPromise;
};
