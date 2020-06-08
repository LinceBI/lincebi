import fetch from 'unfetch';

import getContextPath from './getContextPath';

let launchOverlaysPromise = null;

const getLaunchOverlays = async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/plugin-manager/overlays?id=launch`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' },
	});

	if (response.status === 200) {
		const content = await response.text();

		try {
			const json = JSON.parse(content);
			const xmlParser = new DOMParser();
			const overlays = {};
			json.overlay.forEach((overlay) => {
				const xml = xmlParser.parseFromString(overlay.source, 'text/xml');
				const $button = xml.querySelector('button');
				if ($button.getAttribute('id')) {
					overlays[$button.getAttribute('id')] = {
						command: $button.getAttribute('command') || null,
					};
				}
			});
			return overlays;
		} catch (error) {
			console.error(error);
		}
	}

	console.warn('Falling back to default "launchOverlays" value');
	return {};
};

export default async (...args) => {
	if (launchOverlaysPromise === null) {
		launchOverlaysPromise = getLaunchOverlays(...args);
	}
	return launchOverlaysPromise;
};
