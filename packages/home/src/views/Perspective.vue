<template>
	<iframe class="perspective" ref="mantle" :src="`../BridgeHome?${search}`" />
</template>

<script>
import isFunction from 'lodash/isFunction';

import insertIf from '@stratebi/biserver-customization-common/src/insertIf';
import searchParams from '@stratebi/biserver-customization-common/src/searchParams';
import waitFor from '@stratebi/biserver-customization-common/src/waitFor';

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
		eventBus.$on('mantle.invoke', this.invokeInMantleWindow);
		eventBus.$on('mantle.perspective.invoke', this.invokeInPerspectiveWindow);
		eventBus.$on('mantle.perspective.reload', this.reloadPerspective);
		eventBus.$on('mantle.perspective.params', this.changePerspectiveParams);
	},
	methods: {
		retrieveMantleWindow() {
			return this.$refs.mantle.contentWindow.mantleWindow;
		},
		async invokeInMantleWindow(fn, reqFns = this.wellKnownMantleFunctions) {
			const mantleWindow = await waitFor(() => {
				const mantleWindow = this.retrieveMantleWindow();
				if (
					typeof mantleWindow !== 'undefined' &&
					reqFns.every(reqFn => isFunction(mantleWindow[reqFn]))
				) {
					return mantleWindow;
				}
			});
			fn.call(mantleWindow, mantleWindow);
		},
		async invokeInPerspectiveWindow(perspective, fn) {
			this.invokeInMantleWindow(async mantleWindow => {
				const perspectiveWindow = await waitFor(() => {
					const perspectiveIframe = mantleWindow.document.querySelector(
						`iframe[id="${perspective}"]`
					);
					if (perspectiveIframe !== null && perspectiveIframe.contentWindow) {
						return perspectiveIframe.contentWindow;
					}
				});
				fn.call(perspectiveWindow, perspectiveWindow);
			});
		},
		reloadPerspective(perspective) {
			this.invokeInPerspectiveWindow(perspective, perspectiveWindow => {
				perspectiveWindow.location.reload();
			});
		},
		changePerspectiveParams(perspective, params = {}) {
			this.invokeInPerspectiveWindow(perspective, perspectiveWindow => {
				perspectiveWindow.location.search = searchParams.stringify(params);
			});
		}
	},
	watch: {
		async perspective(perspective) {
			this.invokeInMantleWindow(mantleWindow => {
				if (!perspective) {
					perspective = mantleWindow.mantle_getPerspectives()[0];
				}
				mantleWindow.mantle_setPerspective(perspective);
			});
		}
	}
};
</script>

<style scoped lang="scss">
.perspective {
	display: block;
	height: 100%;
	width: 100%;
	border: 0;
}
</style>
