<template>
	<b-nav-form class="NavBarSearch" @submit="onSubmit">
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
import router from '@/router';
import eventBus from '@/eventBus';

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
			event.target.reset();
			eventBus.$emit('mantle.invoke', () => {
				router.push({
					name: 'perspective',
					params: { perspective: 'search.perspective' }
				});
				eventBus.$emit('mantle.perspective.params', 'search.perspective', {
					'search-terms': this.searchTerms
				});
			});
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
