<template>
	<b-container class="additional-info w-100 text-reset" :class="{ [`border-${variant}`]: true }">
		<b-row align-h="center" class="title bg-light">
			<b-col>
				<font-awesome-icon :class="{ 'card-icon': true, 'fa-fw': true, [`text-${variant}`]: true }" :icon="icon" />
				<span class="lbl font-weight-semibold">{{ title }}</span>
			</b-col>
		</b-row>
		<b-row align-h="center" class="body bg-light">
			<b-col>
				<slot></slot>
			</b-col>
		</b-row>
	</b-container>
</template>

<script>
export default {
	name: 'AdditionalInfo',
	props: {
		icon: {
			type: Array,
			default: () => ['fas', 'circle-info'],
		},
		variant: {
			type: String,
			default: 'info',
		},
		title: {
			type: String,
			default: '',
		},
	},
};
</script>

<style lang="scss" scoped>
.additional-info {
	border-left: toRem(5) solid;

	.title {
		padding-top: toRem(15);
	}

	.body {
		padding: toRem(15) 0 toRem(15) 0;
		height: auto;
	}
}
</style>
