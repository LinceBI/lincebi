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
					target: '[data-v-step="welcome"]',
					header: { title: 'Get Started' },
					content: `Discover <strong>LinceBI</strong>!`,
				},
				{
					target: '[data-v-step="profile"]',
					content: 'Some text about profile',
					callback: this.expandNavbar,
				},
				{
					target: '[data-v-step="search"]',
					content: 'Some text about search',
					callback: this.expandNavbar,
				},
				{
					target: '[data-v-step="settings"]',
					content: 'Some text about settings',
					callback: this.expandNavbar,
				},
				{
					target: '[data-v-step="tab-global"]',
					content: 'Some text about global tab',
				},
				{
					target: '[data-v-step="tab-home"]',
					content: 'Some text about home tab',
				},
				{
					target: '[data-v-step="tab-new"]',
					content: 'Some text about new tab',
				},
				{
					target: '[data-v-step="sidebar-item-home"]',
					content: 'Some text about home',
					callback: this.expandSidebar,
				},
				{
					target: '[data-v-step="sidebar-item-tools"]',
					content: 'Some text about tools',
					callback: this.expandSidebar,
				},
				{
					target: '[data-v-step="sidebar-item-opened"]',
					content: 'Some text about opened',
					callback: this.expandSidebar,
				},
				{
					target: '[data-v-step="sidebar-item-browser"]',
					content: 'Some text about browser',
					callback: this.expandSidebar,
				},
				{
					target: '[data-v-step="sidebar-item-stsearch"]',
					content: 'Some text about stsearch',
					callback: this.expandSidebar,
				},
				{
					target: '[data-v-step="sidebar-item-manage-datasources"]',
					content: 'Some text about manage datasources',
					callback: this.expandSidebar,
				},
				{
					target: '[data-v-step="sidebar-item-administration"]',
					content: 'Some text about administration',
					callback: this.expandSidebar,
				},
				{
					target: '[data-v-step="sidebar-item-locales"]',
					content: 'Some text about locales',
					callback: this.expandSidebar,
				},
				{
					target: '[data-v-step="sidebar-item-logout"]',
					content: 'Some text about logout',
					callback: this.expandSidebar,
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
		expandNavbar() {
			store.commit('setNavbarExpanded', true);
		},
		expandSidebar() {
			store.commit('setSidebarExpanded', true);
		},
	},
};
</script>
