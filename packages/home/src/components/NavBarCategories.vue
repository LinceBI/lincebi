<template>
	<b-nav-form class="NavBarCategories">
		<b-dropdown class="categories-dropdown" variant="primary" right>
			<template slot="button-content">
				Categories
			</template>
			<b-dropdown-item
				class="category-item border-collapse"
				v-for="category in categories"
				:key="category.id"
			>
				<b-img class="category-img" :src="category.img"></b-img>
				<span class="category-text">{{ category.name }}</span>
			</b-dropdown-item>
		</b-dropdown>
	</b-nav-form>
</template>

<script>
export default {
	name: 'NavBarCategories',
	computed: {
		categories() {
			return this.$store.state.categories;
		}
	}
};
</script>

<style scoped lang="scss">
.NavBarCategories {
	.categories-dropdown {
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

			.category-img {
				height: 80%;
				width: auto;
				margin-right: rem(8);
				vertical-align: middle;
			}
		}
	}
}
</style>
