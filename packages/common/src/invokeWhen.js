import throttle from 'lodash/throttle';

export default (condFunc, invokeFunc, { wait = 100, timeout = 10000 } = {}) => {
	const start = performance.now();
	const tryInvoke = throttle(() => {
		if (performance.now() - start <= timeout) {
			const condResult = condFunc();
			if (condResult) invokeFunc(condResult);
			else tryInvoke();
		}
	}, wait);

	tryInvoke();
};
