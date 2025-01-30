import getContextPath from './getContextPath';

export default async (path, outputTarget = 'pageable/text') => {
	const contextPath = await getContextPath();

	const reserveIdEndpoint = `${contextPath}plugin/reporting/api/jobs/reserveId`;
	const reserveIdResponse = await fetch(reserveIdEndpoint, {
		method: 'POST',
	});

	let reservedId;
	if (reserveIdResponse.status === 200) {
		({ reservedId } = await reserveIdResponse.json());
	} else {
		return null;
	}

	const reportJobEndpoint = `${contextPath}api/repos/${path.replaceAll('/', ':')}/reportjob`;
	const reportJobResponse = await fetch(reportJobEndpoint, {
		method: 'POST',
		headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		body: new URLSearchParams({
			ts: Date.now(),
			renderMode: 'REPORT',
			reservedId: reservedId,
			showParameters: 'false',
			'output-target': outputTarget,
			'accepted-page': '0',
			'query-limit': '0',
			'maximum-query-limit': '0',
		}),
	});

	let uuid;
	if (reportJobResponse.status === 200) {
		({ uuid } = await reportJobResponse.json());
	} else {
		return null;
	}

	while (true) {
		const statusEndpoint = `${contextPath}plugin/reporting/api/jobs/${uuid}/status`;
		const statusResponse = await fetch(statusEndpoint, {
			method: 'GET',
		});

		let status;
		if (statusResponse.status === 200) {
			({ status } = await statusResponse.json());
		} else {
			status = 'FAILED';
		}

		if (status === 'FINISHED') {
			return `${contextPath}plugin/reporting/api/jobs/${uuid}/content`;
		} else if (status !== 'WORKING') {
			return null;
		}

		await new Promise((resolve) => setTimeout(resolve, 1000));
	}
};
