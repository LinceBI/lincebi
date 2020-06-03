<template>
	<div class="tour" tabindex="-1" @click.stop @focus.stop @focusin.stop>
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
import throttle from 'lodash/throttle';

import eventBus from '@/eventBus';

const EVENT_OPTS_RESIZE = { passive: true, capture: false };
const EVENT_OPTS_SCROLL = { passive: true, capture: true };

export default {
	name: 'Tour',
	data() {
		return {
			options: {
				highlight: true,
				stopOnTargetNotFound: true,
				useKeyboardNavigation: true,
				labels: {
					buttonSkip: 'Skip tour',
					buttonPrevious: 'Previous',
					buttonNext: 'Next',
					buttonStop: 'Finish',
				},
			},
			popper: null,
			step: null,
			steps: [
				{
					target: '[data-v-step="welcome"]',
					header: { title: 'Get Started' },
					content: `Discover <strong>LinceBI</strong>!`,
					params: {
						placement: 'bottom',
					},
				},
				{
					target: '[data-v-step="profile"]',
					content: 'Some text about profile',
					callback: () => this.expandNavbar(),
					params: {
						placement: 'bottom',
					},
				},
				{
					target: '[data-v-step="search"]',
					content: 'Some text about search',
					callback: () => this.expandNavbar(),
					params: {
						placement: 'bottom',
					},
				},
				{
					target: '[data-v-step="settings"]',
					content: 'Some text about settings',
					callback: () => this.expandNavbar(),
					params: {
						placement: 'bottom',
					},
				},
				{
					target: '[data-v-step="tab-global"]',
					content: 'Some text about global tab',
					params: {
						placement: 'bottom',
						modifiers: {
							preventOverflow: { boundariesElement: 'offsetParent' },
						},
					},
				},
				{
					target: '[data-v-step="tab-home"]',
					content: 'Some text about home tab',
					params: {
						placement: 'bottom',
						modifiers: {
							preventOverflow: { boundariesElement: 'offsetParent' },
						},
					},
				},
				{
					target: '[data-v-step="tab-new"]',
					content: 'Some text about new tab',
					params: {
						placement: 'left',
						modifiers: {
							preventOverflow: { boundariesElement: 'offsetParent' },
						},
					},
				},
				{
					target: '[data-v-step="home"]',
					content: 'Some text about home',
					callback: () => this.expandSidebar(),
					params: {
						placement: 'right',
					},
				},
				{
					target: '[data-v-step="tools"]',
					content: 'Some text about tools',
					callback: () => this.expandSidebar(),
					params: {
						placement: 'right',
					},
				},
				{
					target: '[data-v-step="tool-stpivot"]',
					content: 'Some text about stpivot tool',
					callback: () => this.expandSidebarDropdown('tools'),
					params: {
						placement: 'bottom',
					},
				},
				{
					target: '[data-v-step="tool-stolap"]',
					content: 'Some text about stolap tool',
					callback: () => this.expandSidebarDropdown('tools'),
					params: {
						placement: 'bottom',
					},
				},
				{
					target: '[data-v-step="tool-streport"]',
					content: 'Some text about streport tool',
					callback: () => this.expandSidebarDropdown('tools'),
					params: {
						placement: 'bottom',
					},
				},
				{
					target: '[data-v-step="tool-stdashboard"]',
					content: 'Some text about stdashboard tool',
					callback: () => this.expandSidebarDropdown('tools'),
					params: {
						placement: 'bottom',
					},
				},
				{
					target: '[data-v-step="tool-stagile"]',
					content: 'Some text about stagile tool',
					callback: () => this.expandSidebarDropdown('tools'),
					params: {
						placement: 'bottom',
					},
				},
				{
					target: '[data-v-step="tool-stcard"]',
					content: 'Some text about stcard tool',
					callback: () => this.expandSidebarDropdown('tools'),
					params: {
						placement: 'bottom',
					},
				},
				{
					target: '[data-v-step="tool-cde"]',
					content: 'Some text about cde tool',
					callback: () => this.expandSidebarDropdown('tools'),
					params: {
						placement: 'bottom',
					},
				},
				{
					target: '[data-v-step="opened"]',
					content: 'Some text about opened',
					callback: () => this.expandSidebar(),
					params: {
						placement: 'right',
					},
				},
				{
					target: '[data-v-step="browser"]',
					content: 'Some text about browser',
					callback: () => this.expandSidebar(),
					params: {
						placement: 'right',
					},
				},
				{
					target: '[data-v-step="stsearch"]',
					content: 'Some text about stsearch',
					callback: () => this.expandSidebar(),
					params: {
						placement: 'right',
					},
				},
				{
					target: '[data-v-step="manage-datasources"]',
					content: 'Some text about manage datasources',
					callback: () => this.expandSidebar(),
					params: {
						placement: 'right',
					},
				},
				{
					target: '[data-v-step="administration"]',
					content: 'Some text about administration',
					callback: () => this.expandSidebar(),
					params: {
						placement: 'right',
					},
				},
				{
					target: '[data-v-step="locales"]',
					content: 'Some text about locales',
					callback: () => this.expandSidebar(),
					params: {
						placement: 'right',
					},
				},
				{
					target: '[data-v-step="logout"]',
					content: 'Some text about logout',
					callback: () => this.expandSidebar(),
					params: {
						placement: 'right',
					},
				},
			],
			callbacks: {
				onStart: this.onStart,
				onStop: this.onStop,
				onPreviousStep: this.onPreviousStep,
				onNextStep: this.onNextStep,
			},
		};
	},
	watch: {
		step() {
			this.reposition();
		},
	},
	beforeDestroy() {
		this.popper = null;
		this.step = null;
		this.removeEventListeners();
	},
	methods: {
		onStart() {
			// Filter all steps with a non-existent target.
			this.steps = this.steps.filter(
				(step) => document.querySelector(step.target) !== null
			);
			this.steps.forEach((step) => {
				// Disable Popper.js events, as the positioning will be handled by us.
				step.params.eventsEnabled = false;
				step.params.onCreate = (data) => {
					// Store Popper.js instance.
					this.popper = data.instance;
					// This class will be removed on first update
					// (avoids visual glitches on first reposition).
					document.body.classList.add('v-tour--hide');
				};
				step.params.onUpdate = () => {
					document.body.classList.remove('v-tour--hide');
				};
			});
			if (this.steps.length > 0) {
				this.step = this.steps[0];
			}
			this.addEventListeners();
		},
		onStop() {
			this.popper = null;
			this.step = null;
			this.removeEventListeners();
		},
		onPreviousStep(currentStepIndex) {
			const previousStepIndex = currentStepIndex - 1;
			if (previousStepIndex > -1) {
				const previousStep = this.steps[previousStepIndex];
				if (typeof previousStep.callback === 'function') {
					previousStep.callback.call(this);
				}
				this.step = previousStep;
			}
		},
		onNextStep(currentStepIndex) {
			const nextStepIndex = currentStepIndex + 1;
			if (nextStepIndex < this.steps.length) {
				const nextStep = this.steps[nextStepIndex];
				if (typeof nextStep.callback === 'function') {
					nextStep.callback.call(this);
				}
				this.step = nextStep;
			}
		},
		reposition: throttle(
			function () {
				let top = 0;
				let left = 0;
				let height = 0;
				let width = 0;

				if (this.step !== null) {
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

				if (this.popper !== null) {
					this.popper.update();
				}
			},
			50,
			{ leading: false, trailing: true }
		),
		expandNavbar() {
			eventBus.$emit('navbar.show');
			eventBus.$emit('sidebar.item.hide');
		},
		expandSidebar() {
			eventBus.$emit('sidebar.show');
			eventBus.$emit('sidebar.item.hide');
		},
		expandSidebarDropdown(id) {
			eventBus.$emit('sidebar.show');
			eventBus.$emit('sidebar.item.hide');
			eventBus.$emit(`sidebar.item.${id}.show`);
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

	&.v-tour--hide .v-step {
		opacity: 0;
	}

	.v-tour--overlay {
		display: block;
		position: absolute;
		overflow: hidden;
		inset: 0;
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
</style>
