<template>
	<b-nav-form class="NavBarSearch" @submit="onSubmit" @reset="onReset">
		<b-input-group class="search-input-group">
			<b-form-input
				v-model="searchTerms"
				placeholder="Search..."
			></b-form-input>
			<b-input-group-append>
				<b-button variant="primary">
					<font-awesome-icon :icon="['fas', 'search']" />
				</b-button>
			</b-input-group-append>
		</b-input-group>
	</b-nav-form>
</template>

<script>
import invokeWhen from '@stratebi/biserver-customization-common/src/invokeWhen';

import eventBus from '@/eventBus';
import router from '@/router';

export default {
	name: 'NavBarSearch',
	data() {
		return {
			searchTerms: ''
		};
	},
	methods: {
		onSubmit(event) {
			event.preventDefault();

			router.push({
				name: 'perspective',
				params: { perspective: 'search.perspective' }
			});
			eventBus.$emitWhen(
				'mantle.perspective.invoke',
				'search.perspective',
				perspectiveWindow => {
					invokeWhen(
						() => perspectiveWindow.STSearch,
						STSearch => {
							STSearch.doSearch(this.searchTerms);
							event.target.reset();
						}
					);
				}
			);
		},
		onReset(event) {
			event.preventDefault();

			this.searchTerms = '';
		}
	}
};
</script>

<style scoped lang="scss">
.NavBarSearch {
	.search-input-group {
		width: 100%;
	}
}
</style>
