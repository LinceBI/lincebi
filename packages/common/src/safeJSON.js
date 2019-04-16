export default {
	parse: (str, defaultValue = null) => {
		try {
			return JSON.parse(str);
		} catch (error) {
			/* eslint-disable no-console */
			console.error(error, str);
		}

		return defaultValue;
	},
	stringify: (obj, defaultValue = null) => {
		try {
			return JSON.stringify(obj);
		} catch (error) {
			/* eslint-disable no-console */
			console.error(error, obj);
		}

		return defaultValue;
	}
};
