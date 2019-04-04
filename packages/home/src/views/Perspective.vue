<template>
	<iframe class="Perspective" ref="mantle" :src="`../BridgeHome?${searchParams}`" />
</template>

<script>
import insertIf from '@stratebi/biserver-customization-common/src/insertIf';
import searchParams from '@stratebi/biserver-customization-common/src/searchParams';

export default {
	name: 'Perspective',
	props: { perspective: String },
	data() {
		return {
			searchParams: searchParams.stringify({
				...insertIf(typeof this.perspective !== 'undefined', {perspective: this.perspective})
			})
		};
	},
	watch: {
		perspective(newPerspective, oldPerspective) {
			this.$refs.mantle.contentWindow.mantle_setPerspective(newPerspective);
		}
	}
};
</script>

<style scoped lang="scss">
.Perspective {
	display: block;
	height: 100%;
	width: 100%;
	border: 0;
}
</style>
