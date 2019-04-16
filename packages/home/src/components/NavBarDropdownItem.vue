<template>
	<b-dropdown-item href="#" :to="to" @click="onClick">
		<font-awesome-icon v-if="enabled" :icon="icon" />
		<font-awesome-icon v-else :icon="iconDisabled" />
		<span class="lbl">{{ text }}</span>
	</b-dropdown-item>
</template>

<script>
export default {
	name: 'NavBarDropdownItem',
	props: {
		text: String,
		to: Object,
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
