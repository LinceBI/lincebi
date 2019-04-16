export default (cond, value) => {
	return cond ? value : Array.isArray(value) ? [] : {};
};
