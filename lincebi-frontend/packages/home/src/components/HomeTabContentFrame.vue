<template>
	<iframe
		class="home-tab-content-frame"
		:src="tab?.data?.src"
		:csp="tab?.data?.csp"
		:allow="tab?.data?.allow"
		:sandbox="tab?.data?.sandbox"
		:referrerpolicy="tab?.data?.referrerpolicy"
	/>
</template>

<script>
export default {
	name: 'HomeTabContentFrame',
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
