import Vue from 'vue';
import Router from 'vue-router';

import isDemo from '@stratebi/biserver-customization-common/src/isDemo';
import stopAllWindows from '@stratebi/biserver-customization-common/src/stopAllWindows';

import Administration from '@/views/Administration.vue';
import Home from '@/views/Home.vue';
import Perspective from '@/views/Perspective.vue';
import Profile from '@/views/Profile.vue';

Vue.use(Router);

export default new Router({
	routes: [
		{
			path: '/',
			name: 'root',
			redirect: isDemo
				? { name: 'perspective', params: { perspective: 'sthome.perspective' } }
				: { name: 'home' }
		},
		{
			path: '/home',
			name: 'home',
			component: Home
		},
		{
			path: '/profile',
			name: 'profile',
			component: Profile
		},
		{
			path: '/administration',
			name: 'administration',
			component: Administration
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
				stopAllWindows();
				window.location.href = '../Login';
			}
		},
		{
			path: '/logout',
			name: 'logout',
			beforeEnter() {
				stopAllWindows();
				window.location.href = '../Logout';
			}
		}
	]
});
