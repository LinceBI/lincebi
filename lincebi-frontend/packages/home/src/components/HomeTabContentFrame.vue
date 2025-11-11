<template>
	<iframe-keep-alive
		v-if="tab?.name && tab?.data?.src"
		:id="tab?.name"
		class="home-tab-content-frame"
		:src="tab?.data?.src"
		:csp="tab?.data?.csp"
		:allow="tab?.data?.allow"
		:sandbox="tab?.data?.sandbox"
		:referrerpolicy="tab?.data?.referrerpolicy"
	/>
</template>

<script>
import IframeKeepAlive from '@lincebi/frontend-common/src/components/IframeKeepAlive.vue';

export default {
	name: 'HomeTabContentFrame',
	components: {
		IframeKeepAlive,
	},
	model: {
		prop: 'tab',
		event: 'change',
	},
	props: {
		tab: {
			type: Object,
			default: () => null,
		},
	},
};
</script>

<style scoped lang="scss">
.home-tab-content-frame {
	display: block;
	height: 100%;
	width: 100%;
	border: 0;
}
</style>
