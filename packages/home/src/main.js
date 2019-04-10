import Vue from 'vue';

import '@/vendor/bootstrap';
import '@/vendor/fontawesome';
import '@/vendor/vue-notification';
import '@/vendor/vue-router-multi-view';

import App from '@/App.vue';
import router from '@/router';
import store from '@/store';

new Vue({
	router,
	store,
	render: h => h(App)
}).$mount('#app');
