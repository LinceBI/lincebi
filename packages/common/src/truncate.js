export default (str, maxLength = 30, ellipsis = '...') => {
	if (str.length - ellipsis.length > maxLength) {
		return str.substring(0, maxLength).trimEnd() + ellipsis;
	}

	return str;
};
