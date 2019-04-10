<template>
	<b-nav-form class="NavBarCategories" v-show="categories.length > 0">
		<b-dropdown class="categories-dropdown" variant="primary" right>
			<template slot="button-content">
				Categories
			</template>
			<b-dropdown-item
				class="category-item border-collapse"
				v-for="category in categories"
				@click="openCategory(category)"
				:key="category.id"
			>
				<b-img class="category-icon" :src="category.icon"></b-img>
				<span class="category-text">{{ category.name }}</span>
			</b-dropdown-item>
		</b-dropdown>
	</b-nav-form>
</template>

<script>
import eventBus from '@/eventBus';
import router from '@/router';
import store from '@/store';

export default {
	name: 'NavBarCategories',
	data() {
		return {
			categories: []
		};
	},
	async created() {
		this.categories = await store.getters.categories;
	},
	methods: {
		openCategory(category) {
			router.push({
				name: 'perspective',
				params: { perspective: 'search.perspective' }
			});
			eventBus.$emitWhen('mantle.perspective.params', 'search.perspective', {
				preset: 'category',
				'banner-title': category.name,
				'banner-src': `${location.pathname}${category.banner}`,
				'search-terms': category.id
			});
		}
	}
};
</script>

<style scoped lang="scss">
.NavBarCategories {
	.categories-dropdown {
		width: 100%;

		::v-deep .dropdown-menu {
			width: rem(280);
			padding: 0;
			border-top: 0;
			border-left: 0;
		}

		.category-item {
			display: inline-block;
			height: rem(45);
			width: 50%;
			padding: rem(10);
			@include border-collapse();

			.category-icon {
				height: 80%;
				width: auto;
				margin-right: rem(8);
				vertical-align: middle;
			}
		}
	}
}
</style>
