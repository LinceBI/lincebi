<template>
	<div class="form">
		<b-form @submit="onSubmit">
			<b-form-group label="Username:" label-for="fieldUsername">
				<b-form-input
					id="fieldUsername"
					type="text"
					v-model="form.username"
					required
				/>
			</b-form-group>

			<b-form-group label="Password:" label-for="fieldPassword">
				<b-form-input
					id="fieldPassword"
					type="text"
					v-model="form.password"
					required
				/>
			</b-form-group>

			<b-button type="submit" variant="primary">Login</b-button>
		</b-form>
	</div>
</template>

<script>
export default {
	name: 'LoginForm',
	data() {
		return {
			form: {
				username: '',
				password: ''
			}
		};
	},
	methods: {
		onSubmit(evt) {
			evt.preventDefault();
			alert(JSON.stringify(this.form));
		}
	}
};
</script>

<style scoped lang="scss">
.form {
	background: rgba(map-get($theme-colors, 'light'), 0.85);
}
</style>
