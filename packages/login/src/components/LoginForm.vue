<template>
	<div class="login-form p-4 rounded">
		<b-form @submit.prevent="onSubmit">
			<b-img
				class="d-block h-auto w-75 mb-4 mx-auto"
				src="@stratebi/biserver-customization-common/src/assets/img/logo.svg"
			/>
			<b-form-group :label="$t('username.label')">
				<b-form-input
					type="text"
					v-model="form.j_username"
					autofocus
					required
				/>
			</b-form-group>
			<b-form-group :label="$t('password.label')">
				<b-form-input type="password" v-model="form.j_password" required />
			</b-form-group>
			<b-form-text v-if="isDemo" class="mb-4" tag="p" text-variant="primary">
				<a href="http://www.stratebi.com/demos" target="_blank" rel="noopener">
					<font-awesome-icon :icon="['fas', 'key']" />
					<span class="lbl">{{ $t('getCredentialsHere') }}</span>
				</a>
			</b-form-text>
			<login-error class="mb-4" />
			<b-button class="float-right" type="submit" variant="primary">
				<font-awesome-icon :icon="['fas', 'sign-in-alt']" />
				<span class="lbl">{{ $t('signIn') }}</span>
			</b-button>
		</b-form>
	</div>
</template>

<script>
import fetch from 'unfetch';

import searchParams from '@stratebi/biserver-customization-common/src/searchParams';

import LoginError from '@/components/LoginError.vue';

export default {
	name: 'LoginForm',
	components: {
		LoginError
	},
	data: () => ({
		form: {
			j_username: '',
			j_password: ''
		}
	}),
	methods: {
		async onSubmit() {
			const response = await fetch('../j_spring_security_check', {
				method: 'POST',
				headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
				body: searchParams.stringify(this.form)
			});

			window.location.replace(response.url);
		}
	}
};
</script>

<style scoped lang="scss">
.login-form {
	background: rgba(map-get($theme-colors, 'light'), 0.85);
}
</style>
