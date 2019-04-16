import Vue from 'vue';

import '@/vendor/bootstrap';
import '@/vendor/fontawesome';

import App from '@/App.vue';
import i18n from '@/i18n';

new Vue({
	i18n,
	render: h => h(App)
}).$mount('#app');
