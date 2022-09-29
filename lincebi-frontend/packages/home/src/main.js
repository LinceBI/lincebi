import '@lincebi/frontend-common/src/nestedFrameDetector';
import '@lincebi/frontend-common/src/googleTagManager';
import '@lincebi/frontend-common/src/datadogBrowserLogs';

import Vue from 'vue';

import '@/vendor/bootstrap';
import '@/vendor/fontawesome';
import '@/vendor/vue-router-multi-view';
import '@/vendor/vue-tour';

import App from '@/App.vue';
import i18n from '@/i18n';
import router from '@/router';
import store from '@/store';
import '@/mixin';

new Vue({
	i18n,
	router,
	store,
	render: (h) => h(App),
}).$mount('#app');
