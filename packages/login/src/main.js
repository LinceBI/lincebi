import '@lincebi/biserver-frontend-common/src/nestedFrameDetector';

import Vue from 'vue';

import '@/vendor/bootstrap';
import '@/vendor/fontawesome';

import App from '@/App.vue';
import i18n from '@/i18n';
import '@/mixin';

new Vue({
	i18n,
	render: (h) => h(App),
}).$mount('#app');
