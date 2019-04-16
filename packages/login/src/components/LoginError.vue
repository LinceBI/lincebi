<template>
	<b-alert
		class="login-error"
		v-if="showError"
		variant="danger"
		:show="dismissCountDown"
		@dismissed="dismissCountDown = 0"
		@dismiss-count-down="countDownChanged"
	>
		<p>{{ errorMessage }}</p>
		<b-progress
			variant="danger"
			height="4px"
			:max="dismissSecs"
			:value="dismissCountDown"
		></b-progress>
	</b-alert>
</template>

<script>
import getParameter from '@stratebi/biserver-customization-common/src/getParameter';

export default {
	name: 'login-error',
	data() {
		return {
			code: getParameter('login_error', null),
			messages: {
				'0': this.$t('errors.unknownError'),
				'1': this.$t('errors.authenticationFailure'),
				'2': this.$t('errors.sessionReuseDetected')
			},
			dismissSecs: 5,
			dismissCountDown: 0
		};
	},
	computed: {
		showError() {
			return this.code !== null;
		},
		errorMessage() {
			return this.code in this.messages
				? this.messages[this.code]
				: this.messages['0'];
		}
	},
	methods: {
		countDownChanged(dismissCountDown) {
			this.dismissCountDown = dismissCountDown;
		}
	},
	mounted() {
		this.dismissCountDown = this.dismissSecs;
	}
};
</script>
