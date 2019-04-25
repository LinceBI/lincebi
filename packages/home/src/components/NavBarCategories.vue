<template>
	<b-nav-form class="nav-bar-categories" v-if="categories.some(i => i.enabled)">
		<b-dropdown class="categories-dropdown" variant="primary" right>
			<template slot="button-content">
				{{ $t('navbar.categories') }}
			</template>
			<b-dropdown-item
				class="category-item border-collapse"
				v-for="(category, index) in categories.filter(i => i.enabled)"
				:key="index"
				@click="openCategory(category)"
			>
				<b-img class="category-icon" :src="category.icon" />
				<span class="category-text">{{ category.name }}</span>
			</b-dropdown-item>
		</b-dropdown>
	</b-nav-form>
</template>

<script>
import fetch from 'unfetch';

import waitFor from '@stratebi/biserver-customization-common/src/waitFor';

import eventBus from '@/eventBus';
import router from '@/router';

export default {
	name: 'NavBarCategories',
	data() {
		return {
			categories: []
		};
	},
	async created() {
		const response = await fetch('./categories/categories.json', {
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});

		if (response.status === 200) {
			this.categories = await response.json();
		}
	},
	methods: {
		openCategory(category) {
			router.push({
				name: 'perspective',
				params: { perspective: 'search.perspective' }
			});
			eventBus.$emitWhen(
				'mantle.perspective.invoke',
				'search.perspective',
				async perspectiveWindow => {
					const STSearch = await waitFor(() => perspectiveWindow.STSearch);
					await STSearch.applyPreset('category')
						.applyConfig({
							'banner-title': category.name,
							'banner-src': `${location.pathname}${category.banner}`,
							'search-terms': category.tag
						})
						.doRefresh();
				}
			);
		}
	}
};
</script>

<style scoped lang="scss">
.nav-bar-categories {
	width: auto;

	.categories-dropdown {
		&::v-deep .dropdown-menu {
			width: rem(300);
			padding: 0;
			border-top: 0;
			border-left: 0;

			.category-item {
				display: inline-block;
				width: 50%;
				height: rem(45);

				.dropdown-item {
					width: 100%;
					height: 100%;
					padding: rem(10) rem(15);

					.category-icon {
						height: 100%;
						width: auto;
						margin-right: rem(8);
						vertical-align: middle;
					}
				}
			}
		}
	}
}
</style>
