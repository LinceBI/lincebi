<template>
	<iframe class="Perspective" ref="mantle" :src="`../BridgeHome?${search}`" />
</template>

<script>
import insertIf from '@stratebi/biserver-customization-common/src/insertIf';
import invokeWhen from '@stratebi/biserver-customization-common/src/invokeWhen';
import searchParams from '@stratebi/biserver-customization-common/src/searchParams';
import eventBus from '@/eventBus.js';

export default {
	name: 'Perspective',
	props: { perspective: String },
	data() {
		return {
			search: searchParams.stringify({
				...insertIf(typeof this.perspective !== 'undefined', {
					perspective: this.perspective
				})
			}),
			wellKnownMantleFunctions: [
				'mantle_getPerspectives',
				'mantle_setPerspective',
				'openURL'
			]
		};
	},
	async created() {
		eventBus.$on('mantle.open-url', tool => {
			this.invokeInMantleWindow(function(window) {
				window.openURL(tool.name, tool.name, tool.url);
			});
		});
	},
	methods: {
		retrieveMantleWindow() {
			return this.$refs.mantle.contentWindow.mantleWindow;
		},
		invokeInMantleWindow(fn, reqFns = this.wellKnownMantleFunctions) {
			invokeWhen(
				() => {
					let mantleWindow = this.retrieveMantleWindow();
					return (
						typeof mantleWindow !== 'undefined' &&
						reqFns.every(reqFn => typeof mantleWindow[reqFn] !== 'undefined')
					);
				},
				() => {
					let mantleWindow = this.retrieveMantleWindow();
					fn.call(mantleWindow, mantleWindow);
				}
			);
		}
	},
	watch: {
		async perspective(perspective) {
			this.invokeInMantleWindow(function(window) {
				if (!perspective) perspective = window.mantle_getPerspectives()[0];
				window.mantle_setPerspective(perspective);
			});
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
