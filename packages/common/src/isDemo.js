const ENV = process.env.VUE_APP_ENV;
const DEMO_HOSTNAME = process.env.VUE_APP_DEMO_HOSTNAME || 'demo.stratebi.com';
export default ENV === 'demo' || DEMO_HOSTNAME === window.location.hostname;
