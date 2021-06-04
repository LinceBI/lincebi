<template>
	<button
		class="navbar-sidebar-toggle navbar-toggler"
		type="button"
		@click="sidebarExpanded = !sidebarExpanded"
	>
		<font-awesome-icon :icon="['fas', `chevron-${sidebarExpanded ? 'left' : 'right'}`]" />
	</button>
</template>

<script>
import store from '@/store';

export default {
	name: 'NavbarSidebarToggle',
	computed: {
		sidebarExpanded: {
			get() {
				return store.state.sidebarExpanded;
			},
			set(sidebarExpanded) {
				store.commit('setSidebarExpanded', sidebarExpanded);
			},
		},
	},
};
</script>
