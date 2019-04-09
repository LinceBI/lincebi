<template>
	<b-form class="Profile py-5 px-2" @submit="onSubmit">
		<b-container>
			<b-row>
				<b-col lg="4">
					<label class="avatar">
						<b-img class="avatar-image" :src="user.avatar"></b-img>
						<input
							class="avatar-input"
							type="file"
							name="avatar"
							accept="image/jpeg, image/png, image/gif"
						/>
						<div class="triangle">
							<div class="edit">
								<font-awesome-icon class="icon" :icon="['fas', 'pencil-alt']" />
							</div>
						</div>
					</label>
				</b-col>
				<b-col lg="8">
					<b-form-group label="Name:">
						<b-form-input
							type="text"
							name="name"
							placeholder="Name..."
							:value="user.name"
						></b-form-input>
					</b-form-group>
					<b-form-group label="Email:">
						<b-form-input
							type="text"
							name="email"
							placeholder="Email..."
							:value="user.email"
						></b-form-input>
					</b-form-group>
					<b-form-group label="Phone:">
						<b-form-input
							type="text"
							name="phone"
							placeholder="Phone..."
							:value="user.phone"
						></b-form-input>
					</b-form-group>
					<b-form-group label="Address:">
						<b-form-input
							type="text"
							name="address"
							placeholder="Address..."
							:value="user.address"
						></b-form-input>
					</b-form-group>
					<b-button type="submit" variant="primary" class="float-right">
						<font-awesome-icon :icon="['fas', 'save']" />
						<span class="lbl">Save</span>
					</b-button>
				</b-col>
			</b-row>
		</b-container>
	</b-form>
</template>

<script>
import imageToDataURI from '@stratebi/biserver-customization-common/src/imageToDataURI';
import store from '@/store';

export default {
	name: 'Profile',
	props: { perspective: String },
	computed: {
		user() {
			return store.state.user;
		}
	},
	methods: {
		async onSubmit(event) {
			event.preventDefault();

			const formData = new FormData(event.target);

			try {
				// User avatar must be converted to a data URI.
				const avatar = formData.get('avatar');
				formData.set('avatar', await imageToDataURI(avatar));
			} catch (error) {
				formData.delete('avatar');
			}

			for (const [key, value] of formData.entries()) {
				store.dispatch('setUserSetting', { key, value });
			}
		}
	}
};
</script>

<style scoped lang="scss">
.Profile {
	.avatar {
		display: block;
		position: relative;
		margin: 0 auto rem(20) auto;
		max-height: rem(256);
		max-width: rem(256);
		cursor: pointer;

		.avatar-image {
			display: block;
			width: 100%;
		}

		.avatar-input {
			display: none;
		}

		.triangle {
			position: absolute;
			top: 0;
			right: 0;
			width: 0;
			height: 0;
			border-style: solid;
			border-width: 0 3em 3em 0;
			border-color: transparent rgba(map-get($theme-colors, 'dark'), 0.6)
				transparent transparent;
			color: $yiq-text-light;

			.edit {
				padding: 0.3em;
				width: 3em;
				height: 3em;
				text-align: right;

				.icon {
					vertical-align: top;
				}
			}
		}
	}
}
</style>
