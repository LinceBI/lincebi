<template>
	<div class="login-error">
		<b-alert variant="danger" dismissible v-model="showAlert">
			{{ messages[code] }}
		</b-alert>
	</div>
</template>

<script>
import getParameter from '@stratebi/biserver-customization-common/src/getParameter';

export default {
	name: 'login-error',
	data: () => ({
		code: getParameter('login_error'),
		messages: {
			// authenticationFailure
			'1': 'A login error occurred. Please try again.',
			// sessionReuseDetected
			'2': 'A login error occurred. Please try again.'
		}
	}),
	computed: {
		showAlert: function() {
			return this.code in this.messages;
		}
	}
};
</script>
