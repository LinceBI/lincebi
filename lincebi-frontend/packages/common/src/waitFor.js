import invokeWhen from './invokeWhen';

export default async (condFunc, options) => {
	return new Promise((invokeFunc, timeoutFunc) => {
		invokeWhen(condFunc, invokeFunc, timeoutFunc, options);
	});
};
