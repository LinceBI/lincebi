<template>
	<b-navbar class="navbar shadow" toggleable="lg" variant="light">
		<navbar-sidebar-toggle />
		<b-navbar-brand class="navbar-logo mr-0 mr-lg-3 p-0" data-v-step="navbar-welcome" href="#/">
			<b-img class="h-100 w-auto" src="@lincebi/frontend-common/src/assets/img/logo.svg" />
		</b-navbar-brand>
		<b-navbar-toggle :target="`nav-collapse-${uniqueId}`">
			<template slot="default">
				<font-awesome-icon :icon="['fas', 'bars']" />
			</template>
		</b-navbar-toggle>
		<b-collapse :id="`nav-collapse-${uniqueId}`" v-model="navbarExpanded" is-nav>
			<b-navbar-nav class="nav-section">
				<navbar-profile class="nav-element" data-v-step="navbar-profile" />
			</b-navbar-nav>
			<b-navbar-nav class="nav-section ml-auto">
				<navbar-search class="nav-element" data-v-step="navbar-search" />
				<navbar-settings class="nav-element" data-v-step="navbar-settings" />
			</b-navbar-nav>
		</b-collapse>
	</b-navbar>
</template>

<script>
import eventBus from '@/eventBus';
import store from '@/store';

import NavbarSearch from '@/components/NavbarSearch.vue';
import NavbarSettings from '@/components/NavbarSettings.vue';
import NavbarSidebarToggle from '@/components/NavbarSidebarToggle.vue';
import NavbarProfile from '@/components/NavbarProfile.vue';

export default {
	name: 'Navbar',
	components: {
		NavbarSearch,
		NavbarSettings,
		NavbarSidebarToggle,
		NavbarProfile,
	},
	computed: {
		navbarExpanded: {
			get() {
				return store.state.navbarExpanded;
			},
			set(navbarExpanded) {
				store.commit('setNavbarExpanded', navbarExpanded);
			},
		},
	},
	created() {
		eventBus.$on('navbar-show', () => {
			this.navbarExpanded = true;
		});
		eventBus.$on('navbar-hide', () => {
			this.navbarExpanded = false;
		});
	},
};
</script>

<style scoped lang="scss">
.navbar {
	.navbar-logo {
		height: rem(40);
	}

	.nav-element {
		margin: 0 0 0 rem(10);
	}

	@include media-breakpoint-down(md) {
		.nav-element {
			width: 100%;
			margin: rem(10) 0 0 0;
		}
	}
}
</style>
