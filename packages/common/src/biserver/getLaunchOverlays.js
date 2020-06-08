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

	const launchOverlays = new Map();

	if (response.status === 200) {
		const content = await response.text();

		try {
			const json = JSON.parse(content);
			const xmlParser = new DOMParser();
			json.overlay.forEach((overlay) => {
				const xml = xmlParser.parseFromString(overlay.source, 'text/xml');
				const $button = xml.querySelector('button');
				if ($button.getAttribute('id')) {
					launchOverlays.set(
						$button.getAttribute('id'),
						$button.getAttribute('command')
					);
				}
			});
		} catch (error) {
			console.error(error);
		}
	}

	return launchOverlays;
};

export default async (...args) => {
	if (launchOverlaysPromise === null) {
		launchOverlaysPromise = getLaunchOverlays(...args);
	}
	return launchOverlaysPromise;
};
