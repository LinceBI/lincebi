export default (initialWindow = window) => {
	return (function next(currentWindow, frames) {
		for (let i = 0; i < currentWindow.frames.length; i++) {
			next(currentWindow.frames[i], frames);
			frames.push(currentWindow.frames[i]);
		}
		return frames;
	})(initialWindow, [initialWindow]);
};
