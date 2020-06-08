import fetch from 'unfetch';

import getContextPath from './getContextPath';

let perspectivesPromise = null;

const getPerspectives = async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/plugin-manager/perspectives`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' },
	});

	const perspectives = new Set();

	if (response.status === 200) {
		const content = await response.text();

		try {
			const json = JSON.parse(content);
			json.pluginPerspective.forEach((p) => {
				perspectives.add(p.id);
			});
		} catch (error) {
			console.error(error);
		}
	}

	return perspectives;
};

export default async (...args) => {
	if (perspectivesPromise === null) {
		perspectivesPromise = getPerspectives(...args);
	}
	return perspectivesPromise;
};
