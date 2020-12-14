export default (() => {
	try {
		if (typeof window.top.location.href !== 'undefined') {
			return window.top;
		}
	} catch (error) {
		// If an exception occurs, it is probably due the same-origin policy.
	}

	return {};
})();
