import getContextPath from './getContextPath';

export default async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/repo/files/import`;
	return new Promise((resolve, reject) => {
		const input = document.createElement('input');
		input.type = 'file';
		input.multiple = false;
		input.accept = '.lbix,.zip';
		input.style.display = 'none';
		input.addEventListener(
			'change',
			async (event) => {
				input.remove();
				const formData = new FormData();
				formData.set('fileUpload', event.target.files?.[0]);
				formData.set('fileNameOverride', 'SystemBackup.lbix');
				formData.set('importDir', '/');
				formData.set('overwriteFile', 'true');
				formData.set('overwriteAclPermissions', 'true');
				formData.set('applyAclPermissions', 'true');
				formData.set('retainOwnership', 'true');
				formData.set('charSet', 'UTF-8');
				formData.set('logLevel', 'DEBUG');
				const response = await fetch(endpoint, {
					method: 'POST',
					body: formData,
				});
				if (response.status === 200) {
					resolve(true);
				} else {
					reject({ status: response.status, statusText: response.statusText });
				}
			},
			{ once: true },
		);
		input.addEventListener(
			'cancel',
			() => {
				input.remove();
				resolve(false);
			},
			{ once: true },
		);
		document.body.appendChild(input);
		input.showPicker();
	});
};
