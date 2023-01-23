import Vue from 'vue';
import Router from 'vue-router';

import stopAllWindows from '@lincebi/frontend-common/src/stopAllWindows';

import AboutView from '@/views/AboutView.vue';
import AdministrationView from '@/views/AdministrationView.vue';
import HomeView from '@/views/HomeView.vue';
import NewView from '@/views/NewView.vue';
import PerspectiveView from '@/views/PerspectiveView.vue';
import ProfileView from '@/views/ProfileView.vue';
import EmbedView from '@/views/EmbedView.vue';
import PowerBiView from '@/views/PowerBiView.vue';

Vue.use(Router);

export default new Router({
	routes: [
		{
			path: '/',
			name: 'root',
			redirect: { name: 'home' },
		},
		{
			path: '/home',
			name: 'home',
			component: HomeView,
		},
		{
			path: '/new',
			name: 'new',
			component: NewView,
		},
		{
			path: '/profile',
			name: 'profile',
			component: ProfileView,
		},
		{
			path: '/about',
			name: 'about',
			component: AboutView,
		},
		{
			path: '/administration',
			name: 'administration',
			component: AdministrationView,
		},
		{
			path: '/p/:perspective',
			name: 'perspective',
			component: PerspectiveView,
			props: (route) => ({
				perspective: route.params.perspective,
			}),
		},
		{
			path: '/login',
			name: 'login',
			beforeEnter() {
				stopAllWindows();
				window.location.href = '../Login';
			},
		},
		{
			path: '/logout',
			name: 'logout',
			beforeEnter() {
				stopAllWindows();
				window.location.href = '../Logout';
			},
		},
		{
			path: '/t/embed',
			name: 'embed',
			component: EmbedView,
		},
		{
			path: '/t/powerbi',
			name: 'powerbi',
			component: PowerBiView,
		},
	],
});
