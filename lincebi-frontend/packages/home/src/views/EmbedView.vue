<template>
	<b-container class="embed-view p-4">
		<b-row>
			<b-col md="8" offset-md="2" class="form-container">
				<div class="d-flex flex-column justify-content-center align-items-center p-3">
					<font-awesome-icon
						class="card-icon fa-fw"
						:icon="['fac', 'tool-embed']"
						:style="{
							color: '#2ab7d0',
							fontSize: '4rem',
							marginBottom: '1rem',
						}"
					/>
					<h2 class="font-weight-semibold">{{ $t('tools.embed.title') }}</h2>
				</div>
				<h5 class="m-4 text-center font-weight-semibold">
					{{ $t('tools.embed.subtitle') }}
				</h5>
				<embed-form />
			</b-col>
		</b-row>
	</b-container>
</template>

<script>
import EmbedForm from '@/components/EmbedForm.vue';

export default {
	name: 'EmbedView',
	components: {
		EmbedForm,
	},
};
</script>

<style lang="scss" scoped>
.embed-view {
	.form-container {
		background: #ffffff;
		border-top: toRem(10) solid map-get($theme-colors, 'tool-embed');
	}
}
</style>
