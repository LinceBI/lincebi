<template>
	<b-nav-form class="NavBarSearch">
		<b-input-group class="search-input-group">
			<b-form-input></b-form-input>
			<b-input-group-append>
				<b-button variant="primary">
					<font-awesome-icon :icon="['fas', 'search']" />
				</b-button>
			</b-input-group-append>
		</b-input-group>
	</b-nav-form>
</template>

<script>
export default {
	name: 'NavBarSearch'
};
</script>

<style scoped lang="scss">
.NavBarSearch {
	.search-input-group {
		width: 100%;
	}
}
</style>
