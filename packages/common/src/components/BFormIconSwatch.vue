<template>
	<b-form-radio-group
		v-model="icon"
		class="icon-swatch"
		button-variant="light"
		buttons
		required
		@input="$emit('input', icon)"
	>
		<b-form-radio
			v-for="kI in icons"
			:key="toString(kI)"
			:value="kI"
			:class="{ icon: true, none: kI === null }"
		>
			<font-awesome-icon v-if="kI" class="fa-fw" :icon="kI" />
		</b-form-radio>
	</b-form-radio-group>
</template>

<script>
export default {
	name: 'BFormIconSwatch',
	props: {
		value: {
			type: Object,
			default: null,
		},
		icons: {
			type: Array,
			default() {
				return [
					null,
					{ prefix: 'fas', iconName: 'archive' },
					{ prefix: 'fas', iconName: 'balance-scale' },
					{ prefix: 'fas', iconName: 'book' },
					{ prefix: 'fas', iconName: 'briefcase' },
					{ prefix: 'fas', iconName: 'bullhorn' },
					{ prefix: 'fas', iconName: 'calendar' },
					{ prefix: 'fas', iconName: 'chart-bar' },
					{ prefix: 'fas', iconName: 'chart-pie' },
					{ prefix: 'fas', iconName: 'clock' },
					{ prefix: 'fas', iconName: 'coins' },
					{ prefix: 'fas', iconName: 'file-alt' },
					{ prefix: 'fas', iconName: 'filter' },
					{ prefix: 'fas', iconName: 'flask' },
					{ prefix: 'fas', iconName: 'graduation-cap' },
					{ prefix: 'fas', iconName: 'hands-helping' },
					{ prefix: 'fas', iconName: 'hard-hat' },
					{ prefix: 'fas', iconName: 'industry' },
					{ prefix: 'fas', iconName: 'landmark' },
					{ prefix: 'fas', iconName: 'layer-group' },
					{ prefix: 'fas', iconName: 'lightbulb' },
					{ prefix: 'fas', iconName: 'map-marked' },
					{ prefix: 'fas', iconName: 'medkit' },
					{ prefix: 'fas', iconName: 'project-diagram' },
					{ prefix: 'fas', iconName: 'rocket' },
					{ prefix: 'fas', iconName: 'satellite' },
					{ prefix: 'fas', iconName: 'shopping-cart' },
					{ prefix: 'fas', iconName: 'store' },
					{ prefix: 'fas', iconName: 'truck' },
					{ prefix: 'fas', iconName: 'users' },
					{ prefix: 'fas', iconName: 'warehouse' },
					{ prefix: 'fas', iconName: 'wrench' },
				];
			},
		},
	},
	data() {
		return {
			icon: this.value,
		};
	},
	methods: {
		toString: (icon) => {
			return icon !== null ? `${icon.prefix}-${icon.iconName}` : null;
		},
	},
};
</script>

<style scoped lang="scss">
.icon-swatch {
	flex-wrap: wrap;

	.icon {
		flex-grow: 0;
		margin: 0;
		padding: rem(6) 0;
		width: rem(28);
		height: rem(34);
		border: none;
		border-radius: 0;
		z-index: 0;

		background-color: inherit;
		box-shadow: none;

		&:active,
		&.active,
		&.focus {
			z-index: 1;
		}

		&:active,
		&.active {
			background-color: inherit;
			box-shadow: 0 0 0 rem(2) rgba(0, 0, 0, 0.5);
		}

		&.focus {
			box-shadow: 0 0 0 rem(2) rgba(0, 0, 0, 0.8);
		}

		&.none {
			position: relative;

			&::before,
			&::after {
				display: block;
				position: absolute;
				left: 0;
				right: 0;
				top: 0;
				bottom: 0;
				margin: auto;
				width: 80%;
				height: 10%;
				background: #f44336;
				transform: rotate(-45deg);
				content: '';
			}

			&::after {
				transform: rotate(45deg);
			}
		}
	}
}
</style>
