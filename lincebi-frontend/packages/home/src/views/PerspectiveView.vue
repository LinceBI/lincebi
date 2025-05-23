<template>
	<iframe
		ref="mantle"
		class="perspective-view"
		:src="`../BridgeHome?${staticSearchParams}`"
		:sandbox="isSanboxed ? sandboxAllowed.join(' ') : false"
		@load="handleMantleLoad"
	/>
</template>

<script>
import insertIf from '@lincebi/frontend-common/src/insertIf';
import replaceParameter from '@lincebi/frontend-common/src/replaceParameter';
import safeWindowTop from '@lincebi/frontend-common/src/safeWindowTop';
import searchParams from '@lincebi/frontend-common/src/searchParams';
import waitFor from '@lincebi/frontend-common/src/waitFor';

import eventBus from '@/eventBus.js';
import router from '@/router';
import store from '@/store';

export default {
	name: 'PerspectiveView',
	props: {
		perspective: {
			type: String,
			default: '',
		},
	},
	data() {
		return {
			staticSearchParams: '',
			// It is recommended to add to this list all properties which are directly or indirectly used.
			mantleWindowProperties: [
				'enableSave',
				'executeCommand',
				'HOME_FOLDER',
				// Causes an exception in reportviewer.js from pentaho-platform-plugin-reporting.
				// 'mantle_addHandler',
				'mantle_getPerspectives',
				'mantle_initialized',
				'mantle_openChangePasswordDialog',
				'mantle_openRepositoryFile',
				'mantle_setPerspective',
				'openURL',
			],
			// We will leave this disabled for now, as it causes problems with some plugins.
			isSanboxed: false,
			sandboxAllowed: ['allow-forms', 'allow-modals', 'allow-popups', 'allow-same-origin', 'allow-scripts'],
		};
	},
	computed: {
		locale() {
			return store.state.locale;
		},
		userSettings() {
			return store.state.userSettings;
		},
		isRepositoryLoading() {
			return store.state.isRepositoryLoading;
		},
		repository() {
			return store.state.repository;
		},
		dynamicSearchParams() {
			return searchParams.stringify({
				locale: this.locale,
				...insertIf(this.perspective, { perspective: this.perspective }),
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
		},
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
		},
		isRepositoryLoading(isRepositoryLoading) {
			if (!isRepositoryLoading) {
				// Populate initial STSearch repository.
				this.invokeInMantleWindow((mantleWindow) => {
					mantleWindow.stsearch_initial_repository = this.repository;
				});
			}
		},
	},
	created() {
		this.staticSearchParams = this.dynamicSearchParams;
		eventBus.$on('mantle-invoke', this.invokeInMantleWindow);
		eventBus.$on('mantle-perspective-invoke', this.invokeInPerspectiveWindow);
		eventBus.$on('mantle-perspective-reload', this.reloadPerspective);
		eventBus.$on('mantle-perspective-params', this.changePerspectiveParams);
		eventBus.$on('mantle-home-command', this.runHomeCommand);
	},
	methods: {
		retrieveMantleWindow() {
			if (this.$refs.mantle && this.$refs.mantle.contentWindow) {
				return this.$refs.mantle.contentWindow;
			}
		},
		async invokeInMantleWindow(fn, reqFns = this.mantleWindowProperties) {
			const mantleWindow = await waitFor(() => {
				const mW = this.retrieveMantleWindow();
				if (typeof mW !== 'undefined' && reqFns.every((reqFn) => reqFn in mW)) {
					return mW;
				}
			});
			fn.call(mantleWindow, mantleWindow);
		},
		async invokeInPerspectiveWindow(perspective, fn) {
			this.invokeInMantleWindow(async (mantleWindow) => {
				const perspectiveWindow = await waitFor(() => {
					const perspectiveIframe = mantleWindow.document.querySelector(`iframe[id="${perspective}"]`);
					if (perspectiveIframe !== null && perspectiveIframe.contentWindow) {
						return perspectiveIframe.contentWindow;
					}
				});
				fn.call(perspectiveWindow, perspectiveWindow);
			});
		},
		async runHomeCommand(command) {
			this.invokeInMantleWindow(async (mantleWindow) => {
				// Mock jQuery calls to prevent errors with plugins that try to use it.
				const $ = () => new Proxy({}, { get: () => $ });
				// Mock "Home" and "mantleXulHandler" object so that plugins can use it.
				const Home = { openFile: mantleWindow.openURL };
				const mantleXulHandler = { openUrl: mantleWindow.openURL };
				Function('$', 'Home', 'mantleXulHandler', command)($, Home, mantleXulHandler);
			});
		},
		reloadPerspective(perspective) {
			this.invokeInPerspectiveWindow(perspective, (perspectiveWindow) => {
				perspectiveWindow.location.reload();
			});
		},
		changePerspective(perspective) {
			this.invokeInMantleWindow((mantleWindow) => {
				if (!perspective) {
					perspective = mantleWindow.mantle_getPerspectives()[0];
				}
				mantleWindow.mantle_setPerspective(perspective);
				replaceParameter('perspective', perspective, mantleWindow);
			});
		},
		changePerspectiveParams(perspective, params = {}) {
			this.invokeInPerspectiveWindow(perspective, (perspectiveWindow) => {
				perspectiveWindow.location.search = searchParams.stringify(params);
			});
		},
		changeShowMenuBar(show) {
			this.invokeInMantleWindow((mantleWindow) => {
				mantleWindow.document.body.classList.toggle('show-menu-bar', show);
			});
		},
		changeShowToolBar(show) {
			this.invokeInMantleWindow((mantleWindow) => {
				mantleWindow.document.body.classList.toggle('show-tool-bar', show);
			});
		},
		handleMantleLoad() {
			this.invokeInMantleWindow((mantleWindow) => {
				// Wrap "mantle_setPerspective" method to switch perspective.
				const mantle_setPerspective = mantleWindow.mantle_setPerspective;
				mantleWindow.mantle_setPerspective = (...args) => {
					router
						.push({
							name: 'perspective',
							params: { perspective: args[0] },
						})
						.catch(() => {});

					return mantle_setPerspective(...args);
				};

				// Wrap "mantle_openRepositoryFile" method to switch perspective.
				const mantle_openRepositoryFile = mantleWindow.mantle_openRepositoryFile;
				mantleWindow.mantle_openRepositoryFile = (...args) => {
					router
						.push({
							name: 'perspective',
							params: { perspective: 'opened.perspective' },
						})
						.catch(() => {});

					return mantle_openRepositoryFile(...args);
				};

				// Some plugins access these properties using "window.top", so we will expose them.
				this.mantleWindowProperties.forEach((prop) => {
					safeWindowTop[prop] = mantleWindow[prop];
				});

				// Set menu bar and toolbar state.
				mantleWindow.document.body.classList.toggle('show-menu-bar', this.showMenuBar);
				mantleWindow.document.body.classList.toggle('show-tool-bar', this.showToolBar);

				// Update route when perspective changes.
				mantleWindow.setInterval(() => {
					// Skip if there is no "mantle_getPerspectives" method.
					if (!mantleWindow.mantle_getPerspectives) return;

					const visiblePerspectiveFrame = mantleWindow
						.mantle_getPerspectives()
						.map((p) => mantleWindow.document.getElementById(p))
						.find((f) => f instanceof mantleWindow.HTMLIFrameElement && f.offsetParent !== null);

					let visiblePerspective;
					if (visiblePerspectiveFrame) {
						visiblePerspective = visiblePerspectiveFrame.id;
					} else {
						// Handle "opened.perspective" with a special check.
						const knownId = 'solutionNavigatorAndContentPanel';
						const sncp = mantleWindow.document.getElementById(knownId);
						if (sncp && sncp.offsetParent !== null) {
							visiblePerspective = 'opened.perspective';
						}
					}

					if (visiblePerspective && visiblePerspective !== this.perspective) {
						console.warn(`Perspective updated to ${visiblePerspective}`);
						router
							.push({
								name: 'perspective',
								params: { perspective: visiblePerspective },
							})
							.catch(() => {});
					}
				}, 1000);

				// Redirect to root route when all tabs are closed.
				mantleWindow.mantle_addHandler('GenericEvent', (event) => {
					if (event.eventSubType === 'tabClosing') {
						setTimeout(() => {
							const sel = '.pentaho-tab-deck-panel iframe[id^=frame_]';
							const tab = mantleWindow.document.querySelector(sel);
							if (tab === null) {
								router.push({ name: 'root' }).catch(() => {});
							}
						}, 100);
					}
				});

				// Populate initial STSearch repository.
				if (!this.isRepositoryLoading) {
					mantleWindow.stsearch_initial_repository = this.repository;
				}

				// Listen to STSearch events.
				mantleWindow.addEventListener('stsearch-set-metadata', ({ detail }) => {
					store.commit('setRepositoryFile', detail);
				});
			});
		},
	},
};
</script>

<style scoped lang="scss">
.perspective-view {
	display: block;
	height: 100%;
	width: 100%;
	border: 0;
}
</style>
