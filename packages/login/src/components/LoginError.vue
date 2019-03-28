<template>
	<div class="LoginError">
		<b-alert variant="danger" dismissible v-model="showAlert">
			{{ messages[code] }}
		</b-alert>
	</div>
</template>

<script>
import { getParameterOrDefault } from '@stratebi/biserver-customization-common/src/helpers';

export default {
	name: 'LoginError',
	data: () => ({
		code: getParameterOrDefault('login_error'),
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
