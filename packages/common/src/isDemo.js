const ENV = process.env.VUE_APP_ENV;
export default ENV === 'demo' ||
	window.location.hostname === 'demo.lincebi.com' ||
	window.location.hostname === 'demo.lincebi.localdomain' ||
	window.location.hostname === 'demo.stratebi.com' ||
	window.location.hostname === 'demo.stratebi.localdomain';
