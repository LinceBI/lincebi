<template>
	<b-navbar class="navbar shadow" toggleable="lg" variant="light">
		<navbar-sidebar-toggle />
		<b-navbar-brand class="navbar-logo mr-0 mr-lg-3 p-0" href="#/">
			<b-img
				class="h-100 w-auto"
				src="@lincebi/biserver-customization-common/src/assets/img/logo.svg"
			/>
		</b-navbar-brand>
		<b-navbar-toggle :target="`nav-collapse-${uniqueId}`">
			<template slot="default">
				<font-awesome-icon :icon="['fas', 'bars']" />
			</template>
		</b-navbar-toggle>
		<b-collapse :id="`nav-collapse-${uniqueId}`" is-nav>
			<b-navbar-nav class="nav-section">
				<navbar-welcome class="nav-element" />
			</b-navbar-nav>
			<b-navbar-nav class="nav-section ml-auto">
				<navbar-search
					class="nav-element"
					v-if="installedPlugins.includes('stsearch')"
				/>
				<b-nav-item
					v-if="isDemo"
					class="nav-element"
					href="#/p/stadmin.perspective"
				>
					<font-awesome-icon :icon="['fac', 'tool-stadmin']" />
					<span class="lbl d-lg-none">STAdmin</span>
				</b-nav-item>
				<navbar-settings v-else class="nav-element" />
			</b-navbar-nav>
		</b-collapse>
	</b-navbar>
</template>

<script>
import NavbarSearch from '@/components/NavbarSearch.vue';
import NavbarSettings from '@/components/NavbarSettings.vue';
import NavbarSidebarToggle from '@/components/NavbarSidebarToggle.vue';
import NavbarWelcome from '@/components/NavbarWelcome.vue';

import store from '@/store';

export default {
	name: 'Navbar',
	components: {
		NavbarSearch,
		NavbarSettings,
		NavbarSidebarToggle,
		NavbarWelcome
	},
	computed: {
		installedPlugins() {
			return store.state.installedPlugins;
		}
	}
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
