const id = process.env.VUE_APP_GTAG_ID || 'G-FCW6CPLDEL';
const dlName = '__dataLayer__';

if (typeof window[dlName] === 'undefined') {
	window[dlName] = [];
}

export const gtag = function () {
	window[dlName].push(arguments);
};

gtag('js', new Date());
gtag('config', id);

if (process.env.VUE_APP_GTAG_DISABLED !== 'true') {
	const script = document.createElement('script');
	script.src = `https://www.googletagmanager.com/gtag/js?id=${id}&l=${dlName}`;
	document.head.appendChild(script);
}

export default gtag;
