import getPentahoEnvironment from './getPentahoEnvironment';

let userIdPromise = null;

const getUserId = async () => {
	const pentahoEnvironment = await getPentahoEnvironment();
	return pentahoEnvironment.user.id;
};

export default async (...args) => {
	if (userIdPromise === null) {
		userIdPromise = getUserId(...args);
	}
	return userIdPromise;
};
