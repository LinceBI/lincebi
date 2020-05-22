<template>
	<v-tour
		name="tour"
		:options="options"
		:steps="filteredSteps"
		:callbacks="callbacks"
	></v-tour>
</template>

<script>
import store from '@/store';

export default {
	name: 'Tour',
	data() {
		return {
			options: {
				stopOnTargetNotFound: true,
				labels: {
					buttonSkip: 'Skip tour',
					buttonPrevious: 'Previous',
					buttonNext: 'Next',
					buttonStop: 'Finish',
				},
			},
			steps: [
				{
					target: '#navbar-logo',
					header: { title: 'Get Started' },
					content: `Discover <strong>LinceBI</strong>!`,
				},
				{
					target: '#navbar-profile',
					content: 'Some text about profile',
					callback: this.openNavbar,
				},
				{
					target: '#navbar-search',
					content: 'Some text about search',
					callback: this.openNavbar,
				},
				{
					target: '#navbar-settings',
					content: 'Some text about settings',
					callback: this.openNavbar,
				},
				{
					target: '#sidebar-item-home',
					content: 'Some text about home',
					callback: this.openSidebar,
				},
				{
					target: '#sidebar-item-tools',
					content: 'Some text about tools',
					callback: this.openSidebar,
				},
				{
					target: '#sidebar-item-opened',
					content: 'Some text about opened',
					callback: this.openSidebar,
				},
				{
					target: '#sidebar-item-browser',
					content: 'Some text about browser',
					callback: this.openSidebar,
				},
				{
					target: '#sidebar-item-stsearch',
					content: 'Some text about stsearch',
					callback: this.openSidebar,
				},
				{
					target: '#sidebar-item-manage-datasources',
					content: 'Some text about manage datasources',
					callback: this.openSidebar,
				},
				{
					target: '#sidebar-item-administration',
					content: 'Some text about administration',
					callback: this.openSidebar,
				},
				{
					target: '#sidebar-item-locales',
					content: 'Some text about locales',
					callback: this.openSidebar,
				},
				{
					target: '#sidebar-item-logout',
					content: 'Some text about logout',
					callback: this.openSidebar,
				},
			],
			filteredSteps: [],
			callbacks: {
				onStart: function () {
					// Skip all steps with a non-existent target.
					this.filteredSteps = this.steps.filter(
						(step) => document.querySelector(step.target) !== null
					);
				}.bind(this),
				onPreviousStep: function (currentStepIndex) {
					const previousStepIndex = currentStepIndex - 1;
					if (previousStepIndex > -1) {
						const previousStep = this.filteredSteps[previousStepIndex];
						if (typeof previousStep.callback === 'function') {
							previousStep.callback.call(this, previousStep);
						}
					}
				}.bind(this),
				onNextStep: function (currentStepIndex) {
					const nextStepIndex = currentStepIndex + 1;
					if (nextStepIndex < this.filteredSteps.length) {
						const nextStep = this.filteredSteps[nextStepIndex];
						if (typeof nextStep.callback === 'function') {
							nextStep.callback.call(this, nextStep);
						}
					}
				}.bind(this),
			},
		};
	},
	methods: {
		openNavbar() {
			store.commit('setShowNavbar', true);
		},
		openSidebar() {
			store.commit('setShowSidebar', true);
		},
	},
};
</script>
