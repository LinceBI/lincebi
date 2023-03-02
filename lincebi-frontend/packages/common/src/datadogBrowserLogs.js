import { datadogLogs } from '@datadog/browser-logs';

const datadogSite = import.meta.env.VITE_DATADOG_SITE;
const datadogClientToken = import.meta.env.VITE_DATADOG_CLIENT_TOKEN;

if (datadogSite && datadogClientToken) {
	datadogLogs.init({
		site: datadogSite,
		clientToken: datadogClientToken,
		service: 'lincebi',
		version: import.meta.env.VITE_VERSION?.toLowerCase(),
		forwardErrorsToLogs: true,
		sampleRate: 100,
	});
}

export default datadogLogs;
