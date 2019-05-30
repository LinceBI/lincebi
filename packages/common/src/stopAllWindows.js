import getAllWindows from './getAllWindows';

export default (initialWindow = window) => {
	getAllWindows(initialWindow).forEach(currentWindow => {
		try {
			currentWindow.stop();
		} catch (error) {
			/* Ignore cross-origin exceptions */
		}
	});
};
