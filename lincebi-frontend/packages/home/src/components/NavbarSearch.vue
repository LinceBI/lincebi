<template>
	<b-nav-form
		class="navbar-search"
		form-class="w-100"
		@submit.prevent="onSubmit"
		@reset.prevent="onReset"
	>
		<b-input-group class="w-100">
			<b-input-group-prepend v-if="categories.some((i) => i.enabled)">
				<b-dropdown class="categories-dropdown" variant="primary" no-caret>
					<template slot="button-content">
						<font-awesome-icon :icon="['fas', 'layer-group']" />
						<span class="lbl d-none d-sm-inline">
							{{ $t('navbar.categories') }}
						</span>
					</template>
					<b-dropdown-item
						v-for="(category, index) in categories.filter((i) => i.enabled)"
						:key="index"
						class="category-item border-collapse"
						@click="onCategoryClick(category)"
					>
						<b-img class="category-icon" :src="category.icon" />
						<span class="category-text">{{ category.name }}</span>
					</b-dropdown-item>
				</b-dropdown>
			</b-input-group-prepend>
			<b-form-input v-model="searchTerms" :placeholder="$t('navbar.search.placeholder')" />
			<b-input-group-append>
				<b-button variant="primary" type="submit">
					<font-awesome-icon :icon="['fas', 'magnifying-glass']" />
				</b-button>
			</b-input-group-append>
		</b-input-group>
	</b-nav-form>
</template>

<script>
import waitFor from '@lincebi/frontend-common/src/waitFor';

import eventBus from '@/eventBus';
import router from '@/router';

export default {
	name: 'NavbarSearch',
	data() {
		return {
			searchTerms: '',
			categories: [],
		};
	},
	async created() {
		const response = await fetch('./categories/categories.json', {
			method: 'GET',
			headers: { 'Content-Type': 'application/json' },
		});

		if (response.status === 200) {
			this.categories = await response.json();
		}
	},
	methods: {
		onSubmit(event) {
			router
				.push({
					name: 'perspective',
					params: { perspective: 'search.perspective' },
				})
				.catch(() => {});
			eventBus.$emitWhenAvailable(
				'mantle-perspective-invoke',
				'search.perspective',
				async (perspectiveWindow) => {
					const STSearch = await waitFor(() => perspectiveWindow.STSearch);
					STSearch.resetConfig().doSearch(this.searchTerms).doFocus();
					event.target.reset();
				},
			);
		},
		onReset() {
			this.searchTerms = '';
		},
		onCategoryClick(category) {
			router
				.push({
					name: 'perspective',
					params: { perspective: 'search.perspective' },
				})
				.catch(() => {});
			eventBus.$emitWhenAvailable(
				'mantle-perspective-invoke',
				'search.perspective',
				async (perspectiveWindow) => {
					const STSearch = await waitFor(() => perspectiveWindow.STSearch);
					STSearch.applyPreset('category').applyConfig({
						'banner-title': category.name,
						'banner-src': `${location.pathname}${category.banner}`,
						'search-terms': category.tag,
					});
				},
			);
		},
	},
};
</script>

<style scoped lang="scss">
.navbar-search {
	.categories-dropdown :deep(.dropdown-menu) {
		position: absolute;
		width: rem(350);
		max-width: calc(100vw - rem(30));
		padding: 0;
		border-top: 0;
		border-left: 0;

		.category-item {
			display: inline-block;
			width: 50%;
			height: rem(45);

			.dropdown-item {
				display: flex;
				flex-direction: row;
				width: 100%;
				height: 100%;
				padding: rem(10) rem(5);

				.category-text {
					display: inline-block;
					margin: 0 rem(4);
					overflow: hidden;
					text-overflow: ellipsis;
					vertical-align: middle;
				}

				.category-icon {
					display: inline-block;
					height: 100%;
					width: auto;
					margin: 0 rem(4);
					vertical-align: middle;
				}
			}
		}
	}
}
</style>
