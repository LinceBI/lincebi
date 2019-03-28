import formData from './formData';
import safeJSON from './safeJSON';

export default {
	parse: (str, defaultValues) => {
		// URLSearchParams and FormData have similar methods.
		return formData.objectify(new URLSearchParams(str), defaultValues);
	},
	stringify: obj => {
		let params = [];

		Object.entries(obj).forEach(entry => {
			let key = entry[0];
			let value = entry[1];

			if (typeof value === 'string') {
				params.push(`${key}=${encodeURIComponent(value)}`);
			} else if (Array.isArray(value)) {
				let arrKey = `${key}[]`;
				value.forEach(arrValue => {
					params.push(`${arrKey}=${encodeURIComponent(arrValue)}`);
				});
			} else {
				let objValue = safeJSON.stringify(value, value);
				params.push(`${key}=${encodeURIComponent(objValue)}`);
			}
		});

		return params.join('&');
	}
};
