<template>
	<div class="tour">
		<v-tour
			name="tour"
			:options="options"
			:steps="steps"
			:callbacks="callbacks"
		></v-tour>
		<div class="v-tour--overlay">
			<div ref="spotlight" class="v-tour--spotlight"></div>
		</div>
	</div>
</template>

<script>
import store from '@/store';

export default {
	name: 'Tour',
	data() {
		return {
			options: {
				highlight: true,
				stopOnTargetNotFound: true,
				labels: {
					buttonSkip: 'Skip tour',
					buttonPrevious: 'Previous',
					buttonNext: 'Next',
					buttonStop: 'Finish',
				},
			},
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
					callback: this.expandNavbar,
					params: {
						placement: 'bottom',
					},
				},
				{
					target: '[data-v-step="search"]',
					content: 'Some text about search',
					callback: this.expandNavbar,
					params: {
						placement: 'bottom',
					},
				},
				{
					target: '[data-v-step="settings"]',
					content: 'Some text about settings',
					callback: this.expandNavbar,
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
					target: '[data-v-step="sidebar-item-home"]',
					content: 'Some text about home',
					callback: this.expandSidebar,
					params: {
						placement: 'right',
					},
				},
				{
					target: '[data-v-step="sidebar-item-tools"]',
					content: 'Some text about tools',
					callback: this.expandSidebar,
					params: {
						placement: 'right',
					},
				},
				{
					target: '[data-v-step="sidebar-item-opened"]',
					content: 'Some text about opened',
					callback: this.expandSidebar,
					params: {
						placement: 'right',
					},
				},
				{
					target: '[data-v-step="sidebar-item-browser"]',
					content: 'Some text about browser',
					callback: this.expandSidebar,
					params: {
						placement: 'right',
					},
				},
				{
					target: '[data-v-step="sidebar-item-stsearch"]',
					content: 'Some text about stsearch',
					callback: this.expandSidebar,
					params: {
						placement: 'right',
					},
				},
				{
					target: '[data-v-step="sidebar-item-manage-datasources"]',
					content: 'Some text about manage datasources',
					callback: this.expandSidebar,
					params: {
						placement: 'right',
					},
				},
				{
					target: '[data-v-step="sidebar-item-administration"]',
					content: 'Some text about administration',
					callback: this.expandSidebar,
					params: {
						placement: 'right',
					},
				},
				{
					target: '[data-v-step="sidebar-item-locales"]',
					content: 'Some text about locales',
					callback: this.expandSidebar,
					params: {
						placement: 'right',
					},
				},
				{
					target: '[data-v-step="sidebar-item-logout"]',
					content: 'Some text about logout',
					callback: this.expandSidebar,
					params: {
						placement: 'right',
					},
				},
			],
			callbacks: {
				onStart: function () {
					// Filter all steps with a non-existent target.
					this.steps = this.steps.filter(
						(step) => document.querySelector(step.target) !== null
					);
					if (this.steps.length > 0) {
						this.step = this.steps[0];
					}
					window.addEventListener('resize', this.resizeSpotlight);
					window.addEventListener('scroll', this.resizeSpotlight, true);
				}.bind(this),
				onStop: function () {
					this.step = null;
					window.removeEventListener('resize', this.resizeSpotlight);
					window.removeEventListener('scroll', this.resizeSpotlight, true);
				}.bind(this),
				onPreviousStep: function (currentStepIndex) {
					const previousStepIndex = currentStepIndex - 1;
					if (previousStepIndex > -1) {
						const previousStep = this.steps[previousStepIndex];
						if (typeof previousStep.callback === 'function') {
							previousStep.callback.call(this, previousStep);
						}
						this.step = previousStep;
					}
				}.bind(this),
				onNextStep: function (currentStepIndex) {
					const nextStepIndex = currentStepIndex + 1;
					if (nextStepIndex < this.steps.length) {
						const nextStep = this.steps[nextStepIndex];
						if (typeof nextStep.callback === 'function') {
							nextStep.callback.call(this, nextStep);
						}
						this.step = nextStep;
					}
				}.bind(this),
			},
		};
	},
	watch: {
		step() {
			this.resizeSpotlight();
		},
	},
	beforeDestroy() {
		window.removeEventListener('resize', this.resizeSpotlight);
		window.removeEventListener('scroll', this.resizeSpotlight, true);
	},
	methods: {
		expandNavbar() {
			store.commit('setNavbarExpanded', true);
		},
		expandSidebar() {
			store.commit('setSidebarExpanded', true);
		},
		resizeSpotlight() {
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
		},
	},
};
</script>

<style lang="scss">
.v-tour__target--highlighted {
	pointer-events: none;
	box-shadow: none;
}

.v-tour--active {
	pointer-events: none;

	.v-tour--overlay {
		display: block;
		position: absolute;
		overflow: hidden;
		inset: 0;
		background-color: rgba(0, 0, 0, 0.5);
		mix-blend-mode: hard-light;
		z-index: 9999;

		.v-tour--spotlight {
			display: block;
			position: absolute;
			background-color: rgb(128, 128, 128, 1);
			border-radius: rem(4);
		}
	}
}
</style>
