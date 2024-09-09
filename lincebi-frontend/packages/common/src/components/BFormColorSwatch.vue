<template>
	<b-form-radio-group v-model="color" class="color-swatch" buttons required @input="$emit('input', color)">
		<b-form-radio
			v-for="kC in colors"
			:key="kC"
			:value="kC"
			:class="{ color: true, none: kC === null }"
			:style="{ backgroundColor: kC }"
		></b-form-radio>
	</b-form-radio-group>
</template>

<script>
export default {
	name: 'BFormColorSwatch',
	props: {
		value: {
			type: String,
			default: null,
		},
		colors: {
			type: Array,
			default() {
				return [
					null,
					'#e53935',
					'#d81b60',
					'#8e24aa',
					'#5e35b1',
					'#3949ab',
					'#1e88e5',
					'#039be5',
					'#00acc1',
					'#00897b',
					'#43a047',
					'#7cb342',
					'#c0ca33',
					'#ffb300',
					'#fb8c00',
					'#f4511e',
				];
			},
		},
	},
	data() {
		return {
			color: this.value,
		};
	},
};
</script>

<style scoped lang="scss">
.color-swatch {
	display: flex;
	flex-wrap: wrap;

	.color {
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
