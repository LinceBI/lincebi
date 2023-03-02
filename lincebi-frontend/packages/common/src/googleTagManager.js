const id = import.meta.env.VITE_GTAG_ID;
const dl = '__dataLayer__';

if (typeof window[dl] === 'undefined') {
	window[dl] = [];
}

const gtag = function () {
	window[dl].push(arguments);
};

if (id) {
	gtag('js', new Date());
	gtag('config', id, {
		page_title: 'LinceBI',
		page_location: window.location.origin,
		anonymize_ip: true,
	});

	const script = document.createElement('script');
	script.src = `https://www.googletagmanager.com/gtag/js?id=${id}&l=${dl}`;
	document.head.appendChild(script);
}

export default gtag;
