<template>
	<div class="login-form p-4 rounded">
		<b-form class="d-flex flex-column" @submit.prevent="onSubmit">
			<b-img
				class="d-block h-auto w-75 mb-4 mx-auto"
				src="@lincebi/frontend-common/src/assets/img/lincebi.svg"
			/>
			<b-form-group :label="$t('username.label')">
				<b-form-input v-model="form.j_username" type="text" autofocus required />
			</b-form-group>
			<b-form-group :label="$t('password.label')">
				<b-form-input v-model="form.j_password" type="password" required />
			</b-form-group>
			<login-error class="mb-4" />
			<b-button class="align-self-end" type="submit" variant="primary">
				<font-awesome-icon :icon="['fas', 'sign-in-alt']" />
				<span class="lbl">{{ $t('signIn') }}</span>
			</b-button>
		</b-form>
	</div>
</template>

<script>
import searchParams from '@lincebi/frontend-common/src/searchParams';

import LoginError from '@/components/LoginError.vue';

export default {
	name: 'LoginForm',
	components: {
		LoginError,
	},
	data: () => ({
		form: {
			j_username: '',
			j_password: '',
		},
	}),
	methods: {
		async onSubmit() {
			const response = await fetch('../j_spring_security_check', {
				method: 'POST',
				headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
				body: searchParams.stringify(this.form),
			});

			window.location.replace(response.url);
		},
	},
};
</script>

<style scoped lang="scss">
.login-form {
	background: rgba(map-get($theme-colors, 'light'), 0.85);
}
</style>
