export default (arr, from, to) => {
	const len = arr.length;
	if (from !== to && 0 <= from && from <= len && 0 <= to && to <= len) {
		// Save element
		const element = arr[from];
		// Move element down and shift other elements up
		if (from < to) {
			for (let i = from; i < to; i++) {
				arr[i] = arr[i + 1];
			}
		}
		// Move element up and shift other elements down
		else {
			for (let i = from; i > to; i--) {
				arr[i] = arr[i - 1];
			}
		}
		// Put element
		arr[to] = element;
	}
	return arr;
};
