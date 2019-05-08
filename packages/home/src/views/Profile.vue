<template>
	<b-form class="profile py-5 px-4" @submit.prevent="onSubmit">
		<b-container>
			<b-row>
				<b-col lg="4" class="d-flex justify-content-center align-items-center">
					<label class="avatar">
						<b-img
							class="avatar-image"
							:src="userSettings[`${namespace}.avatar`]"
						/>
						<div :class="{ 'avatar-overlay': true, 'd-none': isDemo }">
							<font-awesome-icon class="icon" :icon="['fas', 'pencil-alt']" />
						</div>
						<input
							class="avatar-input"
							type="file"
							:name="`${namespace}.avatar`"
							accept="image/jpeg, image/png, image/gif"
							@change="onAvatarChange"
							:disabled="isDemo"
						/>
					</label>
				</b-col>
				<b-col lg="8">
					<b-form-group :label="$t('profile.name.label')">
						<b-form-input
							type="text"
							:name="`${namespace}.name`"
							:value="userSettings[`${namespace}.name`]"
							:placeholder="$t('profile.name.placeholder')"
						/>
					</b-form-group>
					<b-form-group :label="$t('profile.email.label')">
						<b-form-input
							type="email"
							:name="`${namespace}.email`"
							:value="userSettings[`${namespace}.email`]"
							:placeholder="$t('profile.email.placeholder')"
						/>
					</b-form-group>
					<b-form-group :label="$t('profile.phone.label')">
						<b-form-input
							type="text"
							:name="`${namespace}.phone`"
							:value="userSettings[`${namespace}.phone`]"
							:placeholder="$t('profile.phone.placeholder')"
						/>
					</b-form-group>
					<b-form-group :label="$t('profile.address.label')">
						<b-form-input
							type="text"
							:name="`${namespace}.address`"
							:value="userSettings[`${namespace}.address`]"
							:placeholder="$t('profile.address.placeholder')"
						/>
					</b-form-group>
					<b-button
						type="submit"
						variant="primary"
						class="float-right"
						:disabled="isDemo"
					>
						<font-awesome-icon :icon="['fas', 'save']" />
						<span class="lbl">{{ $t('profile.save') }}</span>
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
			// Profile update is disabled for demo.
			if (this.isDemo) return;

			const formData = new FormData(event.target);

			try {
				// User avatar must be converted to a data URI.
				const avatar = formData.get(`${this.namespace}.avatar`);
				formData.set(`${this.namespace}.avatar`, await imageToDataURI(avatar));
			} catch (error) {
				formData.delete(`${this.namespace}.avatar`);
			}

			for (const [key, value] of formData.entries()) {
				store.dispatch('updateUserSettings', { [key]: value });
			}
		},
		async onAvatarChange(event) {
			if (event.target.files.length > 0) {
				const avatar = event.target.files[0];
				if (avatar instanceof File && avatar.size > 0) {
					// Note that "commit" is called instead of "dispatch", so the avatar
					// will not be saved on the server until the user submits the form.
					store.commit('setUserSettings', {
						[`${this.namespace}.avatar`]: await imageToDataURI(avatar)
					});
				}
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
			cursor: pointer;
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
