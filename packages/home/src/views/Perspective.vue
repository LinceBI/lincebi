<template>
	<iframe
		class="Perspective"
		ref="mantle"
		:src="`../BridgeHome?${searchParams}`"
	/>
</template>

<script>
import insertIf from '@stratebi/biserver-customization-common/src/insertIf';
import invokeWhen from '@stratebi/biserver-customization-common/src/invokeWhen';
import searchParams from '@stratebi/biserver-customization-common/src/searchParams';

export default {
	name: 'Perspective',
	props: { perspective: String },
	data() {
		return {
			searchParams: searchParams.stringify({
				...insertIf(typeof this.perspective !== 'undefined', {
					perspective: this.perspective
				})
			})
		};
	},
	methods: {
		retrieveMantleWindow() {
			return this.$refs.mantle.contentWindow.mantleWindow;
		},
		invokeMantleWindowFunction(funcName, ...args) {
			invokeWhen(
				() => {
					let mantleWindow = this.retrieveMantleWindow();
					return mantleWindow && typeof mantleWindow[funcName] !== 'undefined'
				},
				() => {
					let mantleWindow = this.retrieveMantleWindow();
					mantleWindow[funcName](...args);
				}
			);
		}
	},
	watch: {
		perspective(newPerspective) {
			this.invokeMantleWindowFunction('mantle_setPerspective', newPerspective);
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
