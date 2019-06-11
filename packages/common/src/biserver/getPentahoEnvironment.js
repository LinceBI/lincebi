import getWebcontextWorker from './getWebcontextWorker';

let pentahoEnvironmentPromise = null;

const defaultPentahoEnvironment = {
	user: { id: '' },
	server: { root: '/pentaho/' }
};

const getPentahoEnvironment = async () => {
	const webcontextWorker = await getWebcontextWorker();

	if (webcontextWorker !== null) {
		webcontextWorker.postMessage('requireCfg.config["pentaho/environment"]');
		return new Promise(resolve => {
			webcontextWorker.onmessage = event => {
				resolve(event.data);
			};
			webcontextWorker.onerror = () => {
				resolve(defaultPentahoEnvironment);
			};
		});
	}

	console.warn('Falling back to default Pentaho environment');
	return defaultPentahoEnvironment;
};

export default async (...args) => {
	if (pentahoEnvironmentPromise === null) {
		pentahoEnvironmentPromise = getPentahoEnvironment(...args);
	}
	return pentahoEnvironmentPromise;
};
