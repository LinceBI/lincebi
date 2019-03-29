<template>
	<div class="NavBar">
		<b-navbar toggleable="lg" variant="light">
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
					<b-nav-form class="nav-element categories-form">
						<b-dropdown class="categories-dropdown" variant="primary" right>
							<template slot="button-content">
								Categories
							</template>
							<b-dropdown-item
								class="category-item"
								v-for="category in categories"
								:key="category.id"
							>
								<b-img
									class="category-img"
									:src="require(`@/assets/img/categories/${category.id}.svg`)"
								></b-img>
								<span class="category-text">{{ category.name }}</span>
							</b-dropdown-item>
						</b-dropdown>
					</b-nav-form>

					<b-nav-form class="nav-element search-form">
						<b-input-group>
							<b-form-input></b-form-input>
							<b-input-group-append>
								<b-button variant="primary">
									<font-awesome-icon :icon="['fas', 'search']" />
								</b-button>
							</b-input-group-append>
						</b-input-group>
					</b-nav-form>

					<b-nav-item-dropdown
						class="nav-element settings-dropdown"
						right
						no-caret
					>
						<template slot="button-content">
							<font-awesome-icon :icon="['fac', 'tool-stadmin']" />
						</template>
						<b-dropdown-item href="#">
							<font-awesome-icon :icon="['fac', 'tool-stprofile']" />
							STProfile
						</b-dropdown-item>
						<b-dropdown-item href="#">
							<font-awesome-icon :icon="['fac', 'tool-stadmin']" />
							STAdmin
						</b-dropdown-item>
						<b-dropdown-divider />
						<b-dropdown-item href="#">
							<font-awesome-icon :icon="['far', 'check-square']" />
							Use descriptions for tooltips
						</b-dropdown-item>
						<b-dropdown-item href="#">
							<font-awesome-icon :icon="['far', 'square']" />
							Show hidden files
						</b-dropdown-item>
					</b-nav-item-dropdown>
				</b-navbar-nav>
			</b-collapse>
		</b-navbar>
	</div>
</template>

<script>
export default {
	name: 'NavBar',
	data: () => ({
		categories: [
			{ id: 'marketing', name: 'Marketing' },
			{ id: 'retail', name: 'Retail' },
			{ id: 'finance', name: 'Finance' },
			{ id: 'ngo', name: 'NGO' },
			{ id: 'gov', name: 'GOV' },
			{ id: 'e-commerce', name: 'e-Commerce' },
			{ id: 'telco', name: 'Telco' },
			{ id: 'utilities', name: 'Utilities' },
			{ id: 'tourism', name: 'Tourism' },
			{ id: 'education', name: 'Education' },
			{ id: 'health', name: 'Health' },
			{ id: 'rrhh', name: 'RRHH' },
			{ id: 'pharma', name: 'Pharma' }
		]
	})
};
</script>

<style scoped lang="scss">
.NavBar {
	.navbar-logo {
		height: rem(48);
	}

	.nav-element {
		margin-left: rem(10);
	}

	.categories-dropdown {
		::v-deep .dropdown-menu {
			width: rem(280) !important;
		}

		.category-item {
			display: inline-block;
			height: rem(40);
			width: 50%;
			padding: rem(10);

			.category-img {
				height: 80%;
				width: auto;
				margin-right: rem(5);
				vertical-align: middle;
			}
		}
	}
}
</style>
