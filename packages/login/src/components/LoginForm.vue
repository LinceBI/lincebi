<template>
	<div class="LoginForm p-4 rounded">
		<b-form @submit="onSubmit">
			<h2 class="text-center">Login</h2>
			<b-form-group label="Username:">
				<b-form-input type="text" v-model="form.j_username" autofocus required />
			</b-form-group>
			<b-form-group label="Password:">
				<b-form-input type="password" v-model="form.j_password" required />
			</b-form-group>
			<LoginError />
			<div class="float-right">
				<b-button type="submit" variant="primary">Sign in</b-button>
			</div>
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
		async onSubmit(event) {
			event.preventDefault();
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
.LoginForm {
	background: rgba(map-get($theme-colors, 'light'), 0.85);
}
</style>
