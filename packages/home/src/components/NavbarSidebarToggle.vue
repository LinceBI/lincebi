<template>
	<button
		class="navbar-sidebar-toggle navbar-toggler"
		type="button"
		@click="showSidebar = !showSidebar"
	>
		<font-awesome-icon
			:icon="['fas', `chevron-${showSidebar ? 'left' : 'right'}`]"
		/>
	</button>
</template>

<script>
import store from '@/store';

export default {
	name: 'NavbarSidebarToggle',
	computed: {
		showSidebar: {
			get() {
				return store.state.showSidebar;
			},
			set(showSidebar) {
				store.commit('setShowSidebar', showSidebar);
			}
		}
	}
};
</script>

<style scoped lang="scss">
.navbar-sidebar-toggle {
}
</style>
