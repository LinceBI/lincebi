import Vue from 'vue';
import '@/vendor/bootstrap';
import '@/vendor/fontawesome';
import App from '@/App.vue';
import router from '@/router';
import store from '@/store';

new Vue({
	router,
	store,
	render: h => h(App)
}).$mount('#app');
