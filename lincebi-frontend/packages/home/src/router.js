import Vue from 'vue';
import Router from 'vue-router';

import stopAllWindows from '@lincebi/frontend-common/src/stopAllWindows';

const AboutView = () => import('@/views/AboutView.vue');
const AdministrationView = () => import('@/views/AdministrationView.vue');
const HomeView = () => import('@/views/HomeView.vue');
const NewView = () => import('@/views/NewView.vue');
const PerspectiveView = () => import('@/views/PerspectiveView.vue');
const ProfileView = () => import('@/views/ProfileView.vue');
const EmbedView = () => import('@/views/EmbedView.vue');
const PowerBiView = () => import('@/views/PowerBiView.vue');
const SupersetView = () => import('@/views/SupersetView.vue');

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
			path: '/profile',
			name: 'profile',
			component: ProfileView,
		},
		{
			path: '/administration',
			name: 'administration',
			component: AdministrationView,
		},
		{
			path: '/about',
			name: 'about',
			component: AboutView,
		},
		{
			path: '/new',
			name: 'new',
			component: NewView,
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
		{
			path: '/t/superset',
			name: 'superset',
			component: SupersetView,
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
	],
});
