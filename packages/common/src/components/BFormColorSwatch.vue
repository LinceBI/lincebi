<template>
	<b-form-radio-group
		class="color-swatch"
		v-model="color"
		@input="$emit('input', color)"
		buttons
		required
	>
		<b-form-radio
			v-for="color in colors"
			:key="color"
			:value="color"
			:class="{ color: true, transparent: color === 'transparent' }"
			:style="{ backgroundColor: color }"
		></b-form-radio>
	</b-form-radio-group>
</template>

<script>
export default {
	name: 'BFormColorSwatch',
	props: {
		value: {
			type: String,
			default: 'transparent'
		},
		colors: {
			type: Array,
			default() {
				return [
					'transparent',
					'#f44336',
					'#e91e63',
					'#9c27b0',
					'#3f51b5',
					'#2196f3',
					'#03a9f4',
					'#00bcd4',
					'#009688',
					'#4caf50',
					'#8bc34a',
					'#cddc39',
					'#ffeb3b',
					'#ffc107',
					'#ff9800',
					'#ff5722'
				];
			}
		}
	},
	data() {
		return {
			color: this.value
		};
	}
};
</script>

<style scoped lang="scss">
.color-swatch {
	flex-wrap: wrap;

	.color {
		flex-grow: 0;
		margin: 0;
		padding: rem(6) 0;
		width: rem(28);
		height: rem(34);
		border: none;
		border-radius: 0;
		z-index: 0;

		&:hover {
			z-index: 0;
		}

		&.active {
			box-shadow: inset 0 0 0 rem(4) rgba(0, 0, 0, 0.5);
		}

		&.transparent {
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
