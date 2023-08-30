<template>
	<b-container class="about-view p-4">
		<b-row class="justify-content-center">
			<b-col cols="6">
				<b-img
					class="w-100 h-auto mx-auto mb-4"
					src="@lincebi/frontend-common/src/assets/img/lincebi_vertical.svg"
				/>
			</b-col>
		</b-row>
		<b-row>
			<b-col cols="6" class="text-right">
				<strong>{{ $t('about.linceBiVersion') }}</strong>
			</b-col>
			<b-col cols="6" class="d-flex align-items-end text-left">
				{{ linceBiVersion }}
			</b-col>
		</b-row>
		<b-row>
			<b-col cols="6" class="text-right">
				<strong>{{ $t('about.biServerVersion') }}</strong>
			</b-col>
			<b-col cols="6" class="d-flex align-items-end text-left">
				{{ biServerVersion }}
			</b-col>
		</b-row>
	</b-container>
</template>

<script>
import store from '@/store';

export default {
	name: 'AboutView',
	computed: {
		linceBiVersion() {
			return store.state.linceBiVersion;
		},
		biServerVersion() {
			return store.state.biServerVersion;
		},
	},
};
</script>

<style scoped lang="scss">
.about-view {
	max-width: toRem(512);
}
</style>
