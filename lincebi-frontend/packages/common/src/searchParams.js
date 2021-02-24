import formData from './formData';
import safeJSON from './safeJSON';

export default {
	parse: (str, defaultValues) => {
		// URLSearchParams and FormData have similar methods.
		return formData.objectify(new URLSearchParams(str), defaultValues);
	},
	stringify: (obj) => {
		const params = [];

		Object.entries(obj).forEach(([key, value]) => {
			if (typeof value === 'string') {
				params.push(`${key}=${encodeURIComponent(value)}`);
			} else if (Array.isArray(value)) {
				const arrKey = `${key}[]`;
				value.forEach((arrValue) => {
					params.push(`${arrKey}=${encodeURIComponent(arrValue)}`);
				});
			} else {
				const objValue = safeJSON.stringify(value, value);
				params.push(`${key}=${encodeURIComponent(objValue)}`);
			}
		});

		return params.join('&');
	},
};
