<template>
	<div
		ref="appTour"
		class="app-tour"
		tabindex="-1"
		@click.stop.prevent
		@focus.stop.prevent
		@focusin.stop.prevent
		@keydown.tab.stop.prevent
	>
		<v-tour name="tour" :options="options" :steps="steps" :callbacks="callbacks" @targetNotFound="onTargetNotFound" />
		<div class="v-tour--overlay">
			<div ref="spotlight" class="v-tour--spotlight"></div>
		</div>
	</div>
</template>

<script>
import throttle from 'lodash/throttle';

import eventBus from '@/eventBus';
import router from '@/router';

export default {
	name: 'AppTour',
	data() {
		return {
			step: null,
		};
	},
	computed: {
		options() {
			// Force recalculation.
			this.step;

			return {
				highlight: true,
				stopOnTargetNotFound: false,
				useKeyboardNavigation: true,
				labels: {
					buttonSkip: this.$t('tour.labels.skip'),
					buttonPrevious: this.$t('tour.labels.previous'),
					buttonNext: this.$t('tour.labels.next'),
					buttonStop: this.$t('tour.labels.stop'),
				},
				enabledButtons: {
					buttonSkip: this.$tours.tour ? this.$tours.tour.isFirst : true,
					buttonPrevious: this.$tours.tour ? !this.$tours.tour.isFirst : false,
					buttonNext: this.$tours.tour ? !this.$tours.tour.isLast : true,
					buttonStop: this.$tours.tour ? this.$tours.tour.isLast : false,
				},
			};
		},
		steps() {
			// Force recalculation.
			this.step;

			const steps = [
				{
					target: '[data-v-step="navbar-welcome"]',
					header: { title: this.$t('tour.steps.navbarWelcome.title') },
					content: this.$t('tour.steps.navbarWelcome.content'),
					before: async () => {
						await router.push({ name: 'home' }).catch(() => {});
					},
				},
				{
					target: '[data-v-step="navbar-profile"]',
					header: { title: this.$t('tour.steps.navbarProfile.title') },
					content: this.$t('tour.steps.navbarProfile.content'),
					before: async () => {
						await router.push({ name: 'home' }).catch(() => {});
						eventBus.$emit('navbar-show');
					},
				},
				{
					target: '[data-v-step="navbar-search"]',
					header: { title: this.$t('tour.steps.navbarSearch.title') },
					content: this.$t('tour.steps.navbarSearch.content'),
					before: async () => {
						await router.push({ name: 'home' }).catch(() => {});
						eventBus.$emit('navbar-show');
					},
				},
				{
					target: '[data-v-step="navbar-settings"]',
					header: { title: this.$t('tour.steps.navbarSettings.title') },
					content: this.$t('tour.steps.navbarSettings.content'),
					before: async () => {
						await router.push({ name: 'home' }).catch(() => {});
						eventBus.$emit('navbar-show');
					},
				},
				{
					target: '[data-v-step="home-tab-home"]',
					header: { title: this.$t('tour.steps.homeTabHome.title') },
					content: this.$t('tour.steps.homeTabHome.content'),
					before: async () => {
						await router.push({ name: 'home' }).catch(() => {});
					},
				},
				{
					target: '[data-v-step="home-tab-favorites"]',
					header: { title: this.$t('tour.steps.homeTabFavorites.title') },
					content: this.$t('tour.steps.homeTabFavorites.content'),
					before: async () => {
						await router.push({ name: 'home' }).catch(() => {});
					},
				},
				{
					target: '[data-v-step="home-tab-new"]',
					header: { title: this.$t('tour.steps.homeTabNew.title') },
					content: this.$t('tour.steps.homeTabNew.content'),
					before: async () => {
						await router.push({ name: 'home' }).catch(() => {});
					},
				},
				{
					target: '[data-v-step="sidebar-home"]',
					header: { title: this.$t('tour.steps.sidebarHome.title') },
					content: this.$t('tour.steps.sidebarHome.content'),
					before: async () => {
						await router.push({ name: 'home' }).catch(() => {});
						eventBus.$emit('sidebar-show');
					},
				},
				{
					target: '[data-v-step="sidebar-tools"]',
					header: { title: this.$t('tour.steps.sidebarTools.title') },
					content: this.$t('tour.steps.sidebarTools.content'),
					before: async () => {
						await router.push({ name: 'home' }).catch(() => {});
						eventBus.$emit('sidebar-show');
					},
				},
				{
					target: '[data-v-step="new-tool-stagile"]',
					header: { title: this.$t('tour.steps.sidebarToolStagile.title') },
					content: this.$t('tour.steps.sidebarToolStagile.content'),
					before: async () => {
						await router.push({ name: 'new' }).catch(() => {});
					},
				},
				{
					target: '[data-v-step="new-tool-stcard"]',
					header: { title: this.$t('tour.steps.sidebarToolStcard.title') },
					content: this.$t('tour.steps.sidebarToolStcard.content'),
					before: async () => {
						await router.push({ name: 'new' }).catch(() => {});
					},
				},
				{
					target: '[data-v-step="new-tool-stdashboard"]',
					header: { title: this.$t('tour.steps.sidebarToolStdashboard.title') },
					content: this.$t('tour.steps.sidebarToolStdashboard.content'),
					before: async () => {
						await router.push({ name: 'new' }).catch(() => {});
					},
				},
				{
					target: '[data-v-step="new-tool-stolap"]',
					header: { title: this.$t('tour.steps.sidebarToolStolap.title') },
					content: this.$t('tour.steps.sidebarToolStolap.content'),
					before: async () => {
						await router.push({ name: 'new' }).catch(() => {});
					},
				},
				{
					target: '[data-v-step="new-tool-stpanels"]',
					header: { title: this.$t('tour.steps.sidebarToolStpanels.title') },
					content: this.$t('tour.steps.sidebarToolStpanels.content'),
					before: async () => {
						await router.push({ name: 'new' }).catch(() => {});
					},
				},
				{
					target: '[data-v-step="new-tool-stpivot"]',
					header: { title: this.$t('tour.steps.sidebarToolStpivot.title') },
					content: this.$t('tour.steps.sidebarToolStpivot.content'),
					before: async () => {
						await router.push({ name: 'new' }).catch(() => {});
					},
				},
				{
					target: '[data-v-step="new-tool-streport"]',
					header: { title: this.$t('tour.steps.sidebarToolStreport.title') },
					content: this.$t('tour.steps.sidebarToolStreport.content'),
					before: async () => {
						await router.push({ name: 'new' }).catch(() => {});
					},
				},
				{
					target: '[data-v-step="new-tool-cde"]',
					header: { title: this.$t('tour.steps.sidebarToolCde.title') },
					content: this.$t('tour.steps.sidebarToolCde.content'),
					before: async () => {
						await router.push({ name: 'new' }).catch(() => {});
					},
				},
				{
					target: '[data-v-step="new-tool-jpivot"]',
					header: { title: this.$t('tour.steps.sidebarToolJpivot.title') },
					content: this.$t('tour.steps.sidebarToolJpivot.content'),
					before: async () => {
						await router.push({ name: 'new' }).catch(() => {});
					},
				},
				{
					target: '[data-v-step="sidebar-opened"]',
					header: { title: this.$t('tour.steps.sidebarOpened.title') },
					content: this.$t('tour.steps.sidebarOpened.content'),
					before: async () => {
						await router.push({ name: 'home' }).catch(() => {});
						eventBus.$emit('sidebar-show');
					},
				},
				{
					target: '[data-v-step="sidebar-browser"]',
					header: { title: this.$t('tour.steps.sidebarBrowser.title') },
					content: this.$t('tour.steps.sidebarBrowser.content'),
					before: async () => {
						await router.push({ name: 'home' }).catch(() => {});
						eventBus.$emit('sidebar-show');
					},
				},
				{
					target: '[data-v-step="sidebar-search"]',
					header: { title: this.$t('tour.steps.sidebarSearch.title') },
					content: this.$t('tour.steps.sidebarSearch.content'),
					before: async () => {
						await router.push({ name: 'home' }).catch(() => {});
						eventBus.$emit('sidebar-show');
					},
				},
				{
					target: '[data-v-step="sidebar-datasources"]',
					header: { title: this.$t('tour.steps.sidebarDatasources.title') },
					content: this.$t('tour.steps.sidebarDatasources.content'),
					before: async () => {
						await router.push({ name: 'home' }).catch(() => {});
						eventBus.$emit('sidebar-show');
					},
				},
				{
					target: '[data-v-step="sidebar-administration"]',
					header: { title: this.$t('tour.steps.sidebarAdministration.title') },
					content: this.$t('tour.steps.sidebarAdministration.content'),
					before: async () => {
						await router.push({ name: 'home' }).catch(() => {});
						eventBus.$emit('sidebar-show');
					},
				},
				{
					target: '[data-v-step="sidebar-locales"]',
					header: { title: this.$t('tour.steps.sidebarLocales.title') },
					content: this.$t('tour.steps.sidebarLocales.content'),
					before: async () => {
						await router.push({ name: 'home' }).catch(() => {});
						eventBus.$emit('sidebar-show');
					},
				},
				{
					target: '[data-v-step="sidebar-logout"]',
					header: { title: this.$t('tour.steps.sidebarLogout.title') },
					content: this.$t('tour.steps.sidebarLogout.content'),
					before: async () => {
						await router.push({ name: 'home' }).catch(() => {});
						eventBus.$emit('sidebar-show');
					},
				},
			];

			for (let i = 0; i < steps.length; i++) {
				const step = steps[i];

				step.__index = i;

				const __params = step.params;
				step.params = {
					enableScrolling: false,
					...__params,
				};

				const __before = step.before;
				step.before = async function () {
					await __before();

					const $target = document.querySelector(this.target);
					if ($target) {
						$target.scrollIntoView({ behavior: 'smooth' });
					} else {
						this.params.highlight = false;
					}
				};
			}

			return steps;
		},
		callbacks() {
			return {
				onStart: this.onStartTour,
				onStop: this.onStopTour,
				onPreviousStep: this.onPreviousStep,
				onNextStep: this.onNextStep,
			};
		},
	},
	watch: {
		step() {
			this.reposition();
		},
	},
	created() {
		eventBus.$on('tour-start', () => this.$tours.tour.start());
		eventBus.$on('tour-stop', () => this.$tours.tour.stop());
	},
	beforeDestroy() {
		this.removeEventListeners();
	},
	methods: {
		onStartTour() {
			this.step = this.steps[0];
			this.addEventListeners();
			eventBus.$emit('tour-started');
		},
		onStopTour() {
			this.step = null;
			this.removeEventListeners();
			eventBus.$emit('tour-stopped');
		},
		onPreviousStep(currentStepIndex) {
			const previousStepIndex = currentStepIndex - 1;
			if (previousStepIndex < 0) return;
			const step = this.steps[previousStepIndex];
			if (document.querySelector(step.target) !== null) {
				this.step = step;
			}
		},
		onNextStep(currentStepIndex) {
			const nextStepIndex = currentStepIndex + 1;
			if (nextStepIndex >= this.steps.length) return;
			const step = this.steps[nextStepIndex];
			if (document.querySelector(step.target) !== null) {
				this.step = step;
			}
		},
		onTargetNotFound(step) {
			if (step.__index - this.step.__index > 0) {
				this.$tours.tour.nextStep();
			} else {
				this.$tours.tour.previousStep();
			}
		},
		reposition: throttle(
			function () {
				// Update spotlight size and position.
				if (this.$refs.spotlight) {
					let top = 0;
					let left = 0;
					let height = 0;
					let width = 0;

					if (this.step) {
						const $target = document.querySelector(this.step.target);
						if ($target !== null) {
							const rect = $target.getBoundingClientRect();
							const win = $target.ownerDocument.defaultView;
							top = rect.top + win.pageYOffset - 4;
							left = rect.left + win.pageXOffset - 4;
							height = rect.height + 8;
							width = rect.width + 8;
						}
					}

					const style = this.$refs.spotlight.style;
					style.top = `${top}px`;
					style.left = `${left}px`;
					style.height = `${height}px`;
					style.width = `${width}px`;
				}

				// Restore focus on tour element.
				if (this.$refs.appTour) {
					this.$refs.appTour.focus();
				}
			},
			50,
			{ leading: false, trailing: true },
		),
		addEventListeners() {
			window.addEventListener('resize', this.reposition, { passive: true, capture: false });
			window.addEventListener('scroll', this.reposition, { passive: true, capture: true });
		},
		removeEventListeners() {
			window.removeEventListener('resize', this.reposition, { passive: true, capture: false });
			window.removeEventListener('scroll', this.reposition, { passive: true, capture: true });
		},
	},
};
</script>

<style lang="scss">
.v-tour__target--highlighted {
	box-shadow: none;
}

.v-tour--active {
	pointer-events: none;

	&.v-tour--hide-step .v-step {
		opacity: 0;
	}

	.v-tour--overlay {
		display: block;
		position: absolute;
		overflow: hidden;
		height: 100vh;
		width: 100vw;
		top: 0;
		left: 0;
		background-color: rgba(0, 0, 0, 0.5);
		mix-blend-mode: hard-light;
		z-index: 9999;

		pointer-events: initial;

		.v-tour--spotlight {
			display: block;
			position: absolute;
			background-color: rgb(128, 128, 128, 1);
			border-radius: toRem(4);
		}
	}
}

.v-step__arrow {
	display: none !important;
}
</style>
