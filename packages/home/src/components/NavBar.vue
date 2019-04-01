<template>
	<b-navbar class="NavBar shadow-sm" toggleable="lg" variant="light">
		<b-navbar-brand class="navbar-logo" href="#">
			<b-img
				class="h-100"
				src="@/assets/img/stratebi.svg"
				alt="Stratebi"
			></b-img>
		</b-navbar-brand>

		<b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

		<b-collapse id="nav-collapse" is-nav>
			<b-navbar-nav>
				<b-nav-text>Welcome admin</b-nav-text>
			</b-navbar-nav>

			<b-navbar-nav class="ml-auto">
				<NavBarCategories class="nav-element" />
				<NavBarSearch class="nav-element" />
				<NavBarSettings class="nav-element" />
			</b-navbar-nav>
		</b-collapse>
	</b-navbar>
</template>

<script>
import NavBarCategories from '@/components/NavBarCategories.vue';
import NavBarSearch from '@/components/NavBarSearch.vue';
import NavBarSettings from '@/components/NavBarSettings.vue';

export default {
	name: 'NavBar',
	components: {
		NavBarCategories,
		NavBarSearch,
		NavBarSettings
	}
};
</script>

<style scoped lang="scss">
.NavBar {
	.navbar-logo {
		height: rem(40);
		padding: 0;
	}

	.nav-element {
		margin-left: rem(10);
	}
}
</style>
