<template>
	<div v-if="message" class="home-motd">
		{{ message }}
	</div>
</template>

<script>
import i18n from '@/i18n';

import getCircular from '@lincebi/frontend-common/src/getCircular';

export default {
	name: 'HomeMotd',
	data() {
		return {
			seed: Math.floor(Date.now() / 8.64e7),
			motd: [],
		};
	},
	computed: {
		message() {
			const message = getCircular(this.motd, this.seed);
			// If message is not localised, return early.
			if (typeof message === 'string') return message;
			return (
				message?.[i18n.locale] ??
				message?.[i18n.locale.substring(0, 2)] ??
				message?.[i18n.fallbackLocale] ??
				message?.[i18n.fallbackLocale.substring(0, 2)]
			);
		},
	},
	async created() {
		const response = await fetch('./motd/motd.json', {
			method: 'GET',
			headers: { 'Content-Type': 'application/json' },
		});

		if (response.status === 200) {
			this.motd = await response.json();
		}
	},
};
</script>
