<template>
	<b-navbar class="nav-bar shadow" toggleable="lg" variant="light">
		<b-navbar-brand
			class="navbar-logo"
			:to="{ name: 'perspective', params: { perspective: 'home.perspective' } }"
		>
			<b-img
				class="h-100 w-auto"
				src="@/assets/img/stratebi.svg"
				alt="Stratebi"
			/>
		</b-navbar-brand>
		<b-navbar-toggle target="nav-collapse" />
		<b-collapse id="nav-collapse" is-nav>
			<b-navbar-nav class="nav-section">
				<nav-bar-welcome class="nav-element" />
			</b-navbar-nav>
			<b-navbar-nav class="nav-section ml-auto">
				<nav-bar-categories
					class="nav-element"
					v-if="installedPlugins.includes('stsearch')"
				/>
				<nav-bar-search
					class="nav-element"
					v-if="installedPlugins.includes('stsearch')"
				/>
				<nav-bar-settings class="nav-element" />
			</b-navbar-nav>
		</b-collapse>
	</b-navbar>
</template>

<script>
import NavBarCategories from '@/components/NavBarCategories.vue';
import NavBarSearch from '@/components/NavBarSearch.vue';
import NavBarSettings from '@/components/NavBarSettings.vue';
import NavBarWelcome from '@/components/NavBarWelcome.vue';

import store from '@/store';

export default {
	name: 'NavBar',
	components: {
		NavBarWelcome,
		NavBarCategories,
		NavBarSearch,
		NavBarSettings
	},
	computed: {
		installedPlugins() {
			return store.state.installedPlugins;
		}
	}
};
</script>

<style scoped lang="scss">
.nav-bar {
	.navbar-logo {
		height: rem(40);
		padding: 0;
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
