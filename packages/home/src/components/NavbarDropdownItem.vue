<template>
	<b-dropdown-item :href="href" @click="onClick">
		<font-awesome-icon v-if="enabled" :icon="icon" />
		<font-awesome-icon v-else :icon="iconDisabled" />
		<span class="lbl">{{ text }}</span>
	</b-dropdown-item>
</template>

<script>
export default {
	name: 'NavbarDropdownItem',
	props: {
		text: String,
		href: String,
		enabled: { type: Boolean, default: true },
		icon: { type: Array, default: () => ['far', 'check-square'] },
		iconDisabled: { type: Array, default: () => ['far', 'square'] }
	},
	methods: {
		onClick(event) {
			if (typeof this.$listeners.click !== 'undefined') {
				this.$listeners.click.call(this, event);
			}
		}
	}
};
</script>
