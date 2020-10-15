import fetch from 'unfetch';

import getContextPath from './getContextPath';

let overlaysPromise = null;

const getOverlays = async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/plugin-manager/overlays`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' },
	});

	const overlays = new Map();

	if (response.status === 200) {
		const content = await response.text();

		try {
			const json = JSON.parse(content);
			const xmlParser = new DOMParser();
			json.overlay.forEach((overlay) => {
				const xml = xmlParser.parseFromString(overlay.source, 'text/xml');
				const $commands = xml.querySelectorAll('[command]');
				for (const $command of $commands) {
					let commandId = $command.getAttribute('id');
					let commandValue = $command.getAttribute('command');

					let $parent = $command.parentElement;
					while ($parent !== null) {
						const parentId = $parent.getAttribute('id');
						if (parentId) commandId = `${parentId}.${commandId}`;
						$parent = $parent.parentElement;
					}

					overlays.set(commandId, commandValue);
				}
			});
		} catch (error) {
			console.error(error);
		}
	}

	return overlays;
};

export default async (...args) => {
	if (overlaysPromise === null) {
		overlaysPromise = getOverlays(...args);
	}
	return overlaysPromise;
};
