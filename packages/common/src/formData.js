import safeJSON from './safeJSON';

export default {
	objectify: (form, defaultValues = {}) => {
		const obj = defaultValues;

		for (const key of form.keys()) {
			if (key.endsWith('[]')) {
				obj[key.substring(0, key.length - 2)] = form.getAll(key);
			} else {
				obj[key] = form.get(key);
			}
		}

		return obj;
	},
	formify: obj => {
		const form = new FormData();

		Object.entries(obj).forEach(([key, value]) => {
			if (typeof value === 'string') {
				form.set(key, value);
			} else if (Array.isArray(value)) {
				const arrKey = `${key}[]`;
				value.forEach(arrValue => {
					form.append(arrKey, arrValue);
				});
			} else {
				const objValue = safeJSON.stringify(value, value);
				form.append(key, objValue);
			}
		});

		return form;
	}
};
