export default {
	parse: (str, defaultValue = null) => {
		try {
			return JSON.parse(str);
		} catch (error) {
			console.error(error, str);
		}

		return defaultValue;
	},
	stringify: (obj, defaultValue = null) => {
		try {
			return JSON.stringify(obj);
		} catch (error) {
			console.error(error, obj);
		}

		return defaultValue;
	}
};
