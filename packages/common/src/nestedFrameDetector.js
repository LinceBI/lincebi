// Reload page if we find ourselves nested.
// This usually happens if the session expires and we try to load content in an iframe.
try {
	window.self._isLinceBI = true;
	if (window.self !== window.top && window.top._isLinceBI) {
		window.top.location = '../';
	}
} catch (error) {
	// If an exception occurs, it is probably due the same-origin policy.
	// We can safely ignore this exception.
}
