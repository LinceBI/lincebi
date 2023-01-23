<template>
	<b-container class="powerbi-view p-4">
		<b-row>
			<b-col md="8" offset-md="2" class="form-container">
				<div class="d-flex flex-column justify-content-center align-items-center p-3">
					<font-awesome-icon
						class="card-icon fa-fw"
						:icon="['fac', 'tool-powerbi']"
						:style="{
							color: '#f4d249',
							fontSize: '4rem',
							marginBottom: '1rem',
						}"
					/>
					<h2 class="font-weight-semibold">{{ $t('tools.powerbi.title') }}</h2>
				</div>
				<b-tabs class="form-tabs" align="center" content-class="mt-3">
					<b-tab :title="$t('tools.powerbi.manual')">
						<h5 class="m-4 text-center font-weight-semibold">
							{{ $t('tools.powerbi.subtitle') }}
						</h5>
						<power-bi-form />
					</b-tab>
					<b-tab :title="$t('tools.powerbi.automatic')" disabled />
				</b-tabs>
			</b-col>
		</b-row>
	</b-container>
</template>

<script>
import PowerBiForm from '@/components/PowerBiForm.vue';

export default {
	name: 'PowerBiView',
	components: {
		PowerBiForm,
	},
};
</script>

<style lang="scss" scoped>
.powerbi-view {
	.form-container {
		background: #ffffff;
		border-top: rem(10) solid map-get($theme-colors, 'tool-powerbi');

		.form-tabs ::v-deep .nav-tabs > .nav-item > .nav-link.active {
			background: #ffffff;
			border-bottom-color: map-get($theme-colors, 'tool-powerbi');
		}
	}
}
</style>
