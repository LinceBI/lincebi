import Vue from 'vue';
import Router from 'vue-router';
import Profile from '@/views/Profile.vue';
import Perspective from '@/views/Perspective.vue';

Vue.use(Router);

export default new Router({
	routes: [
		{
			path: '/',
			name: 'home',
			redirect: {
				name: 'perspective',
				params: { perspective: 'home.perspective' }
			}
		},
		{
			path: '/profile',
			name: 'profile',
			component: Profile
		},
		{
			path: '/p/:perspective',
			name: 'perspective',
			component: Perspective,
			props: route => ({
				perspective: route.params.perspective
			})
		},
		{
			path: '/login',
			name: 'login',
			beforeEnter() {
				location.href = '../Login';
			}
		},
		{
			path: '/logout',
			name: 'logout',
			beforeEnter() {
				location.href = '../Logout';
			}
		}
	]
});
