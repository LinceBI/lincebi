import getPentahoEnvironment from './getPentahoEnvironment';

let contextPathPromise = null;

const getContextPath = async () => {
	const pentahoEnvironment = await getPentahoEnvironment();
	return pentahoEnvironment.server.root;
};

export default async (...args) => {
	if (contextPathPromise === null) {
		contextPathPromise = getContextPath(...args);
	}
	return contextPathPromise;
};
