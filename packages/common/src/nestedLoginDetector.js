// Reload page if we are not the top window and we are contained in /Home/.
try {
	if (
		window.self !== window.top &&
		/^\/[^/]+\/Home\/$/.test(window.top.location.pathname)
	) {
		window.top.location = '../';
	}
} catch (err) {
	// If an exception occurs, it is probably due the same-origin policy.
	// We can safely ignore this exception.
}
