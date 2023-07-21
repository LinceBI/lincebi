<template>
	<b-container class="superset-view p-4">
		<b-row>
			<b-col md="8" offset-md="2" class="form-container">
				<div class="d-flex flex-column justify-content-center align-items-center p-3">
					<font-awesome-icon
						class="card-icon fa-fw"
						:icon="['fac', 'tool-superset']"
						:style="{
							color: '#484848',
							fontSize: '4rem',
							marginBottom: '1rem',
						}"
					/>
					<h2 class="font-weight-semibold">{{ $t('tools.superset.title') }}</h2>
				</div>
				<h5 class="m-4 text-center font-weight-semibold">
					{{ $t('tools.superset.subtitle') }}
				</h5>
				<superset-form />
			</b-col>
		</b-row>
	</b-container>
</template>

<script>
import SupersetForm from '@/components/SupersetForm.vue';

export default {
	name: 'SupersetView',
	components: {
		SupersetForm,
	},
};
</script>

<style lang="scss" scoped>
.superset-view {
	.form-container {
		background: #ffffff;
		border-top: rem(10) solid map-get($theme-colors, 'tool-superset');
	}
}
</style>
