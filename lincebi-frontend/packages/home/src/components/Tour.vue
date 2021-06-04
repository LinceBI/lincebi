<template>
	<div
		ref="tour"
		class="tour"
		tabindex="-1"
		@click.stop.prevent
		@focus.stop.prevent
		@focusin.stop.prevent
		@keydown.tab.stop.prevent
	>
		<v-tour
			name="tour"
			:options="options"
			:steps="steps"
			:callbacks="callbacks"
		/>
		<div class="v-tour--overlay">
			<div ref="spotlight" class="v-tour--spotlight"></div>
		</div>
	</div>
</template>

<script>
import defaultsDeep from 'lodash/defaultsDeep';
import throttle from 'lodash/throttle';

import eventBus from '@/eventBus';
import router from '@/router';

const EVENT_OPTS_RESIZE = { passive: true, capture: false };
const EVENT_OPTS_SCROLL = { passive: true, capture: true };

export default {
	name: 'Tour',
	data() {
		return {
			popper: null,
			step: null,
			startCount: 0,
		};
	},
	computed: {
		options() {
			this.startCount;

			return {
				highlight: true,
				stopOnTargetNotFound: true,
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
			this.startCount;

			const stepDefaults = {
				params: {
					placement: 'bottom',
					modifiers: [
						{
							name: 'preventOverflow',
							options: { boundariesElement: 'window' },
						},
					],
					eventsEnabled: false,
					onCreate: this.onCreateStep,
					onUpdate: this.onUpdateStep,
				},
			};

			const steps = [
				{
					target: '[data-v-step="navbar-welcome"]',
					header: { title: this.$t('tour.steps.navbarWelcome.title') },
					content: this.$t('tour.steps.navbarWelcome.content'),
				},
				{
					target: '[data-v-step="navbar-profile"]',
					header: { title: this.$t('tour.steps.navbarProfile.title') },
					content: this.$t('tour.steps.navbarProfile.content'),
					callback: () => this.expandNavbar(),
				},
				{
					target: '[data-v-step="navbar-search"]',
					header: { title: this.$t('tour.steps.navbarSearch.title') },
					content: this.$t('tour.steps.navbarSearch.content'),
					callback: () => this.expandNavbar(),
				},
				{
					target: '[data-v-step="navbar-settings"]',
					header: { title: this.$t('tour.steps.navbarSettings.title') },
					content: this.$t('tour.steps.navbarSettings.content'),
					callback: () => this.expandNavbar(),
				},
				{
					target: '[data-v-step="home-tab-global"]',
					header: { title: this.$t('tour.steps.homeTabGlobal.title') },
					content: this.$t('tour.steps.homeTabGlobal.content'),
					callback: () => this.goPage('home'),
				},
				{
					target: '[data-v-step="home-tab-home"]',
					header: { title: this.$t('tour.steps.homeTabHome.title') },
					content: this.$t('tour.steps.homeTabHome.content'),
					callback: () => this.goPage('home'),
				},
				{
					target: '[data-v-step="home-tab-new"]',
					header: { title: this.$t('tour.steps.homeTabNew.title') },
					content: this.$t('tour.steps.homeTabNew.content'),
					callback: () => this.goPage('home'),
				},
				{
					target: '[data-v-step="sidebar-home"]',
					header: { title: this.$t('tour.steps.sidebarHome.title') },
					content: this.$t('tour.steps.sidebarHome.content'),
					callback: () => this.expandSidebar(),
				},
				{
					target: '[data-v-step="sidebar-tools"]',
					header: { title: this.$t('tour.steps.sidebarTools.title') },
					content: this.$t('tour.steps.sidebarTools.content'),
					callback: () => this.expandSidebar(),
				},
				{
					target: '[data-v-step="sidebar-tool-stpivot"]',
					header: { title: this.$t('tour.steps.sidebarToolStpivot.title') },
					content: this.$t('tour.steps.sidebarToolStpivot.content'),
					callback: () => this.expandSidebarDropdown('tools'),
				},
				{
					target: '[data-v-step="sidebar-tool-stolap"]',
					header: { title: this.$t('tour.steps.sidebarToolStolap.title') },
					content: this.$t('tour.steps.sidebarToolStolap.content'),
					callback: () => this.expandSidebarDropdown('tools'),
				},
				{
					target: '[data-v-step="sidebar-tool-streport"]',
					header: { title: this.$t('tour.steps.sidebarToolStreport.title') },
					content: this.$t('tour.steps.sidebarToolStreport.content'),
					callback: () => this.expandSidebarDropdown('tools'),
				},
				{
					target: '[data-v-step="sidebar-tool-stdashboard"]',
					header: { title: this.$t('tour.steps.sidebarToolStdashboard.title') },
					content: this.$t('tour.steps.sidebarToolStdashboard.content'),
					callback: () => this.expandSidebarDropdown('tools'),
				},
				{
					target: '[data-v-step="sidebar-tool-stagile"]',
					header: { title: this.$t('tour.steps.sidebarToolStagile.title') },
					content: this.$t('tour.steps.sidebarToolStagile.content'),
					callback: () => this.expandSidebarDropdown('tools'),
				},
				{
					target: '[data-v-step="sidebar-tool-stcard"]',
					header: { title: this.$t('tour.steps.sidebarToolStcard.title') },
					content: this.$t('tour.steps.sidebarToolStcard.content'),
					callback: () => this.expandSidebarDropdown('tools'),
				},
				{
					target: '[data-v-step="sidebar-tool-cde"]',
					header: { title: this.$t('tour.steps.sidebarToolCde.title') },
					content: this.$t('tour.steps.sidebarToolCde.content'),
					callback: () => this.expandSidebarDropdown('tools'),
				},
				{
					target: '[data-v-step="sidebar-tool-jpivot"]',
					header: { title: this.$t('tour.steps.sidebarToolJpivot.title') },
					content: this.$t('tour.steps.sidebarToolJpivot.content'),
					callback: () => this.expandSidebarDropdown('tools'),
				},
				{
					target: '[data-v-step="sidebar-opened"]',
					header: { title: this.$t('tour.steps.sidebarOpened.title') },
					content: this.$t('tour.steps.sidebarOpened.content'),
					callback: () => this.expandSidebar(),
				},
				{
					target: '[data-v-step="sidebar-browser"]',
					header: { title: this.$t('tour.steps.sidebarBrowser.title') },
					content: this.$t('tour.steps.sidebarBrowser.content'),
					callback: () => this.expandSidebar(),
				},
				{
					target: '[data-v-step="sidebar-search"]',
					header: { title: this.$t('tour.steps.sidebarSearch.title') },
					content: this.$t('tour.steps.sidebarSearch.content'),
					callback: () => this.expandSidebar(),
				},
				{
					target: '[data-v-step="sidebar-datasources"]',
					header: { title: this.$t('tour.steps.sidebarDatasources.title') },
					content: this.$t('tour.steps.sidebarDatasources.content'),
					callback: () => this.expandSidebar(),
				},
				{
					target: '[data-v-step="sidebar-administration"]',
					header: { title: this.$t('tour.steps.sidebarAdministration.title') },
					content: this.$t('tour.steps.sidebarAdministration.content'),
					callback: () => this.expandSidebar(),
				},
				{
					target: '[data-v-step="sidebar-locales"]',
					header: { title: this.$t('tour.steps.sidebarLocales.title') },
					content: this.$t('tour.steps.sidebarLocales.content'),
					callback: () => this.expandSidebar(),
				},
				{
					target: '[data-v-step="sidebar-logout"]',
					header: { title: this.$t('tour.steps.sidebarLogout.title') },
					content: this.$t('tour.steps.sidebarLogout.content'),
					callback: () => this.expandSidebar(),
				},
			]
				// Filter all steps with a non-existent target.
				.filter((step) => document.querySelector(step.target) !== null)
				// Set default values.
				.map((step) => defaultsDeep(step, stepDefaults));

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
			// Force steps recalculation.
			this.startCount++;

			if (this.steps.length <= 0) return;

			this.popper = null;
			this.step = this.steps[0];
			this.addEventListeners();
			eventBus.$emit('tour-started');
		},
		onStopTour() {
			this.popper = null;
			this.step = null;
			this.removeEventListeners();
			eventBus.$emit('tour-stopped');
		},
		onCreateStep(data) {
			// Store Popper.js instance.
			this.popper = data.instance;
			// This class will be removed on first update
			// (avoids visual glitches on first reposition).
			document.body.classList.add('v-tour--hide-step');
		},
		onUpdateStep() {
			document.body.classList.remove('v-tour--hide-step');
		},
		onPreviousStep(currentStepIndex) {
			const previousStepIndex = currentStepIndex - 1;
			if (previousStepIndex < 0) return;
			this.step = this.steps[previousStepIndex];
			if (typeof this.step.callback === 'function') {
				this.step.callback.call(this);
			}
		},
		onNextStep(currentStepIndex) {
			const nextStepIndex = currentStepIndex + 1;
			if (nextStepIndex >= this.steps.length) return;
			this.step = this.steps[nextStepIndex];
			if (typeof this.step.callback === 'function') {
				this.step.callback.call(this);
			}
		},
		reposition: throttle(
			function () {
				// Update popper position.
				if (this.popper) {
					this.popper.update();
				}

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
							top = rect.top - 4;
							left = rect.left - 4;
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
				if (this.$refs.tour) {
					this.$refs.tour.focus();
				}
			},
			50,
			{ leading: false, trailing: true }
		),
		expandNavbar() {
			eventBus.$emit('navbar-show');
			eventBus.$emit('sidebar-item-hide');
		},
		expandSidebar() {
			eventBus.$emit('sidebar-show');
			eventBus.$emit('sidebar-item-hide');
		},
		expandSidebarDropdown(id) {
			eventBus.$emit('sidebar-show');
			eventBus.$emit('sidebar-item-hide');
			eventBus.$emit(`sidebar-item-${id}-show`);
		},
		goPage(name) {
			router.push({ name }).catch(() => {});
		},
		addEventListeners() {
			window.addEventListener('resize', this.reposition, EVENT_OPTS_RESIZE);
			window.addEventListener('scroll', this.reposition, EVENT_OPTS_SCROLL);
		},
		removeEventListeners() {
			window.removeEventListener('resize', this.reposition, EVENT_OPTS_RESIZE);
			window.removeEventListener('scroll', this.reposition, EVENT_OPTS_SCROLL);
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
		top: 0;
		right: 0;
		bottom: 0;
		left: 0;
		background-color: rgba(0, 0, 0, 0.5);
		mix-blend-mode: hard-light;
		z-index: 9999;

		pointer-events: initial;

		.v-tour--spotlight {
			display: block;
			position: absolute;
			background-color: rgb(128, 128, 128, 1);
			border-radius: rem(4);
		}
	}
}

.v-step__arrow {
	display: none !important;
}
</style>
