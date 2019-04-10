<template>
	<notifications class="BNotifications" classes="alert shadow" :max="3" />
</template>

<script>
export default {
	name: 'BNotifications'
};
</script>

<style scoped lang="scss">
.BNotifications {
	position: absolute;
	z-index: 10000;

	::v-deep .notification-wrapper {
		padding: 0 rem(10);

		&:last-child {
			padding-bottom: rem(20);
		}

		.vue-notification-template.alert {
			&.success {
				@include alert-variant(
					theme-color-level('success', $alert-bg-level),
					theme-color-level('success', $alert-border-level),
					theme-color-level('success', $alert-color-level)
				);
			}

			&.warn {
				@include alert-variant(
					theme-color-level('warning', $alert-bg-level),
					theme-color-level('warning', $alert-border-level),
					theme-color-level('warning', $alert-color-level)
				);
			}

			&.error {
				@include alert-variant(
					theme-color-level('danger', $alert-bg-level),
					theme-color-level('danger', $alert-border-level),
					theme-color-level('danger', $alert-color-level)
				);
			}
		}
	}
}
</style>
