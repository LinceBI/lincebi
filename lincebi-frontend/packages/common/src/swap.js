export default (arr, i, j) => {
	const len = arr.length;
	if (i >= 0 && i < len && j >= 0 && j < len) {
		[arr[i], arr[j]] = [arr[j], arr[i]];
	}
	return arr;
};
