<template>
	<b-form class="profile py-5 px-4" @submit="onSubmit">
		<b-container>
			<b-row>
				<b-col lg="4" class="d-flex justify-content-center align-items-center">
					<label class="avatar">
						<b-img class="avatar-image" :src="userSettings.avatar"></b-img>
						<div class="avatar-overlay">
							<font-awesome-icon class="icon" :icon="['fas', 'pencil-alt']" />
						</div>
						<input
							class="avatar-input"
							type="file"
							name="avatar"
							accept="image/jpeg, image/png, image/gif"
						/>
					</label>
				</b-col>
				<b-col lg="8">
					<b-form-group label="Name:">
						<b-form-input
							type="text"
							name="name"
							placeholder="Name..."
							:value="userSettings.name"
						></b-form-input>
					</b-form-group>
					<b-form-group label="Email:">
						<b-form-input
							type="text"
							name="email"
							placeholder="Email..."
							:value="userSettings.email"
						></b-form-input>
					</b-form-group>
					<b-form-group label="Phone:">
						<b-form-input
							type="text"
							name="phone"
							placeholder="Phone..."
							:value="userSettings.phone"
						></b-form-input>
					</b-form-group>
					<b-form-group label="Address:">
						<b-form-input
							type="text"
							name="address"
							placeholder="Address..."
							:value="userSettings.address"
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
		userSettings() {
			return store.state.userSettings;
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
.profile {
	min-width: rem(256);

	.avatar {
		display: block;
		position: relative;
		margin: 0 auto rem(40) auto;
		height: rem(256);
		width: rem(256);
		cursor: pointer;

		@include media-breakpoint-down(sm) {
			height: rem(192);
			width: rem(192);
		}

		.avatar-image {
			display: block;
			width: 100%;
			height: 100%;
			border: rem(2) solid rgba(map-get($theme-colors, 'dark'), 0.5);
			border-radius: 50%;
			object-fit: cover;
		}

		.avatar-overlay {
			display: flex;
			align-items: center;
			justify-content: center;
			position: absolute;
			top: 0;
			left: 0;
			width: 100%;
			height: 100%;
			font-size: rem(50);
			border-radius: 50%;
			color: rgba(255, 255, 255, 0);
			background-color: rgba(0, 0, 0, 0);
			transition: color 0.1s ease-out, background-color 0.1s ease-out;
		}

		&:hover .avatar-overlay {
			color: rgba(255, 255, 255, 0.8);
			background-color: rgba(0, 0, 0, 0.4);
		}

		.avatar-input {
			display: none;
		}
	}
}
</style>
