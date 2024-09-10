<template>
	<b-container class="about-view p-4">
		<b-row class="justify-content-center">
			<b-col cols="6">
				<b-img class="w-100 h-auto mx-auto mb-4" src="@lincebi/frontend-common/src/assets/img/lincebi_vertical.svg" />
			</b-col>
		</b-row>
		<b-row>
			<b-col cols="6" class="text-right">
				<strong>{{ $t('about.lincebiVersion') }}</strong>
			</b-col>
			<b-col cols="6" class="d-flex align-items-end text-left">
				{{ serverInfo.lincebiVersion ?? '' }}
			</b-col>
		</b-row>
		<b-row>
			<b-col cols="6" class="text-right">
				<strong>{{ $t('about.biserverVersion') }}</strong>
			</b-col>
			<b-col cols="6" class="d-flex align-items-end text-left">
				{{ serverInfo.biserverVersion ?? '' }}
			</b-col>
		</b-row>
		<b-row>
			<b-col cols="6" class="text-right">
				<strong>{{ $t('about.instanceId') }}</strong>
			</b-col>
			<b-col cols="6" class="d-flex align-items-end text-left">
				{{ serverInfo.instanceId ?? '' }}
			</b-col>
		</b-row>
	</b-container>
</template>

<script>
import store from '@/store';

export default {
	name: 'AboutView',
	computed: {
		serverInfo() {
			return store.state.serverInfo;
		},
	},
};
</script>

<style scoped lang="scss">
.about-view {
	max-width: toRem(512);
}
</style>
