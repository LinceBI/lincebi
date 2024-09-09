<template>
	<b-form-radio-group
		v-model="icon"
		class="icon-swatch"
		button-variant="light"
		buttons
		required
		@input="$emit('input', icon)"
	>
		<b-form-radio v-for="kI in icons" :key="toString(kI)" :value="kI" :class="{ icon: true, none: kI === null }">
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
					{ prefix: 'fas', iconName: 'award' },
					{ prefix: 'fas', iconName: 'book' },
					{ prefix: 'fas', iconName: 'bookmark' },
					{ prefix: 'fas', iconName: 'box-archive' },
					{ prefix: 'fas', iconName: 'briefcase' },
					{ prefix: 'fas', iconName: 'bullhorn' },
					{ prefix: 'fas', iconName: 'calendar' },
					{ prefix: 'fas', iconName: 'cart-shopping' },
					{ prefix: 'fas', iconName: 'chart-line' },
					{ prefix: 'fas', iconName: 'coins' },
					{ prefix: 'fas', iconName: 'comments-dollar' },
					{ prefix: 'fas', iconName: 'diagram-project' },
					{ prefix: 'fas', iconName: 'file' },
					{ prefix: 'fas', iconName: 'filter' },
					{ prefix: 'fas', iconName: 'flask' },
					{ prefix: 'fas', iconName: 'gauge' },
					{ prefix: 'fas', iconName: 'graduation-cap' },
					{ prefix: 'fas', iconName: 'handshake' },
					{ prefix: 'fas', iconName: 'hotel' },
					{ prefix: 'fas', iconName: 'industry' },
					{ prefix: 'fas', iconName: 'landmark' },
					{ prefix: 'fas', iconName: 'lightbulb' },
					{ prefix: 'fas', iconName: 'map-location' },
					{ prefix: 'fas', iconName: 'paperclip' },
					{ prefix: 'fas', iconName: 'rocket' },
					{ prefix: 'fas', iconName: 'scale-balanced' },
					{ prefix: 'fas', iconName: 'shop' },
					{ prefix: 'fas', iconName: 'suitcase-medical' },
					{ prefix: 'fas', iconName: 'truck' },
					{ prefix: 'fas', iconName: 'users' },
					{ prefix: 'fas', iconName: 'warehouse' },
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
	display: flex;
	flex-wrap: wrap;

	.icon {
		flex-grow: 0;
		margin: 0;
		padding: toRem(6) 0;
		width: toRem(28);
		height: toRem(34);
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
			box-shadow: 0 0 0 toRem(2) rgba(0, 0, 0, 0.5);
		}

		&.focus {
			box-shadow: 0 0 0 toRem(2) rgba(0, 0, 0, 0.8);
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
