import throttle from 'lodash/throttle';

export default (
	condFunc = () => true,
	invokeFunc = (r) => r,
	timeoutFunc = (r) => r,
	{ wait = 50, timeout = 20000 } = {},
) => {
	const start = performance.now();
	const tryInvoke = throttle(() => {
		const condResult = condFunc();
		if (performance.now() - start <= timeout) {
			if (condResult) invokeFunc(condResult);
			else tryInvoke();
		} else {
			console.warn('[invokeWhen] Timeout:', condFunc);
			timeoutFunc(condResult);
		}
	}, wait);

	tryInvoke();
};
