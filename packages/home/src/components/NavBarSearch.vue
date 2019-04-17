<template>
	<b-nav-form
		class="nav-bar-search"
		@submit.prevent="onSubmit"
		@reset.prevent="onReset"
	>
		<b-input-group class="search-input-group">
			<b-form-input
				v-model="searchTerms"
				:placeholder="$t('navbar.search.placeholder')"
			/>
			<b-input-group-append>
				<b-button variant="primary">
					<font-awesome-icon :icon="['fas', 'search']" />
				</b-button>
			</b-input-group-append>
		</b-input-group>
	</b-nav-form>
</template>

<script>
import waitFor from '@stratebi/biserver-customization-common/src/waitFor';

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
			router.push({
				name: 'perspective',
				params: { perspective: 'search.perspective' }
			});
			eventBus.$emitWhen(
				'mantle.perspective.invoke',
				'search.perspective',
				async perspectiveWindow => {
					const STSearch = await waitFor(() => perspectiveWindow.STSearch);
					(await STSearch.resetConfig().doRefresh()).doSearch(this.searchTerms);
					event.target.reset();
				}
			);
		},
		onReset() {
			this.searchTerms = '';
		}
	}
};
</script>

<style scoped lang="scss">
.nav-bar-search {
	.search-input-group {
		width: 100%;
	}
}
</style>
