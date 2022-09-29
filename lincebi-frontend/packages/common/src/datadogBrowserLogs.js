import { datadogLogs } from '@datadog/browser-logs';

const datadogSite = process.env.VUE_APP_DATADOG_SITE;
const datadogClientToken = process.env.VUE_APP_DATADOG_CLIENT_TOKEN;

if (datadogSite && datadogClientToken) {
	datadogLogs.init({
		site: datadogSite,
		clientToken: datadogClientToken,
		service: 'lincebi',
		version: process.env.VUE_APP_VERSION?.toLowerCase(),
		forwardErrorsToLogs: true,
		sampleRate: 100,
	});
}

export default datadogLogs;
