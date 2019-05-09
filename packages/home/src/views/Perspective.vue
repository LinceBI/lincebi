<template>
	<iframe
		class="perspective"
		ref="mantle"
		:src="`../BridgeHome?${staticSearchParams}`"
		:sandbox="isSanboxed ? sandboxAllowed.join(' ') : false"
	/>
</template>

<script>
import insertIf from '@stratebi/biserver-customization-common/src/insertIf';
import replaceParameter from '@stratebi/biserver-customization-common/src/replaceParameter';
import searchParams from '@stratebi/biserver-customization-common/src/searchParams';
import waitFor from '@stratebi/biserver-customization-common/src/waitFor';

import eventBus from '@/eventBus.js';
import store from '@/store';

export default {
	name: 'Perspective',
	props: { perspective: String },
	data() {
		return {
			staticSearchParams: '',
			// It is recommended to add to this list all properties which are directly or indirectly used.
			mantleWindowProperties: [
				'enableSave',
				'mantle_getPerspectives',
				'mantle_initialized',
				'mantle_setPerspective',
				'openURL'
			],
			// We will leave this disabled for now, as it causes problems with some plugins.
			isSanboxed: false,
			sandboxAllowed: [
				'allow-forms',
				'allow-modals',
				'allow-popups',
				'allow-same-origin',
				'allow-scripts'
			]
		};
	},
	computed: {
		locale() {
			return store.state.locale;
		},
		userSettings() {
			return store.state.userSettings;
		},
		dynamicSearchParams() {
			return searchParams.stringify({
				locale: this.locale,
				...insertIf(this.perspective, { perspective: this.perspective })
			});
		},
		showMenuBar() {
			const key = `${this.namespace}.show_menu_bar`;
			const value = this.userSettings[key] === 'true';
			return value;
		},
		showToolBar() {
			const key = `${this.namespace}.show_tool_bar`;
			const value = this.userSettings[key] === 'true';
			return value;
		}
	},
	created() {
		this.staticSearchParams = this.dynamicSearchParams;

		eventBus.$on('mantle.invoke', this.invokeInMantleWindow);
		eventBus.$on('mantle.perspective.invoke', this.invokeInPerspectiveWindow);
		eventBus.$on('mantle.perspective.reload', this.reloadPerspective);
		eventBus.$on('mantle.perspective.params', this.changePerspectiveParams);

		// Some plugins access these properties using "window.top", so we will expose them.
		this.invokeInMantleWindow(mantleWindow => {
			this.mantleWindowProperties.forEach(prop => {
				window.top[prop] = mantleWindow[prop];
			});
		});

		// Listen to STSearch events.
		this.invokeInMantleWindow(mantleWindow => {
			mantleWindow.addEventListener('stsearch-set-metadata', ({ detail }) => {
				store.commit('setRepositoryFile', detail);
			});
		});
	},
	methods: {
		retrieveMantleWindow() {
			if (this.$refs.mantle && this.$refs.mantle.contentWindow) {
				return this.$refs.mantle.contentWindow;
			}
		},
		async invokeInMantleWindow(fn, reqFns = this.mantleWindowProperties) {
			const mantleWindow = await waitFor(() => {
				const mantleWindow = this.retrieveMantleWindow();
				if (
					typeof mantleWindow !== 'undefined' &&
					reqFns.every(reqFn => reqFn in mantleWindow)
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
		changePerspective(perspective) {
			this.invokeInMantleWindow(mantleWindow => {
				if (!perspective) {
					perspective = mantleWindow.mantle_getPerspectives()[0];
				}
				mantleWindow.mantle_setPerspective(perspective);
				replaceParameter('perspective', perspective, mantleWindow);
			});
		},
		changePerspectiveParams(perspective, params = {}) {
			this.invokeInPerspectiveWindow(perspective, perspectiveWindow => {
				perspectiveWindow.location.search = searchParams.stringify(params);
			});
		},
		changeShowMenuBar(show) {
			this.invokeInMantleWindow(mantleWindow => {
				mantleWindow.document.body.classList.toggle('show-menu-bar', show);
			});
		},
		changeShowToolBar(show) {
			this.invokeInMantleWindow(mantleWindow => {
				mantleWindow.document.body.classList.toggle('show-tool-bar', show);
			});
		}
	},
	watch: {
		perspective(...args) {
			this.changePerspective(...args);
		},
		showMenuBar(...args) {
			this.changeShowMenuBar(...args);
		},
		showToolBar(...args) {
			this.changeShowToolBar(...args);
		},
		locale() {
			this.staticSearchParams = this.dynamicSearchParams;

			// The iframe will reload, so we will need to reapply some changes.
			// But we will wait a few milliseconds to run the code on the next load.
			setTimeout(() => {
				this.changeShowMenuBar(this.showMenuBar);
				this.changeShowToolBar(this.showToolBar);
			}, 500);
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
