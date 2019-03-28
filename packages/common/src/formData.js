import safeJSON from './safeJSON';

export default {
	objectify: (form, defaultValues = {}) => {
		let obj = defaultValues;

		for (let key of form.keys()) {
			if (key.endsWith('[]')) {
				obj[key.substring(0, key.length - 2)] = form.getAll(key);
			} else {
				obj[key] = form.get(key);
			}
		}

		return obj;
	},
	formify: obj => {
		let form = new FormData();

		Object.entries(obj).forEach(entry => {
			let key = entry[0];
			let value = entry[1];

			if (typeof value === 'string') {
				form.set(key, value);
			} else if (Array.isArray(value)) {
				let arrKey = `${key}[]`;
				value.forEach(arrValue => {
					form.append(arrKey, arrValue);
				});
			} else {
				let objValue = safeJSON.stringify(value, value);
				form.append(key, objValue);
			}
		});

		return form;
	}
};
