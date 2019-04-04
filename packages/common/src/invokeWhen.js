import throttle from 'lodash/throttle';

export default (condFunc, invokeFunc, { wait = 100, timeout = 10000 } = {}) => {
	let start = performance.now();
	let tryInvoke = throttle(() => {
		if (performance.now() - start <= timeout) {
			if (condFunc()) invokeFunc();
			else tryInvoke();
		}
	}, wait);

	tryInvoke();
};
