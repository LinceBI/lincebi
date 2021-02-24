export default (a, b, type = String, asc = false) => {
	if (asc) [a, b] = [b, a];
	if (type === String) {
		return a.localeCompare(b);
	} else if (type === Date) {
		return new Date(a) - new Date(b);
	} else if (type === Number) {
		return a - b;
	}
};
