<template>
	<b-form-tags v-model="selected" @input="$emit('input', selected)">
		<template #default="{ tags, disabled, addTag, removeTag }">
			<div class="d-flex flex-row align-items-center justify-content-end gap-1">
				<ul v-if="tags.length > 0" class="list-inline d-flex flex-row flex-wrap gap-1 m-0">
					<li v-for="tag in tags" :key="tag" class="list-inline-item m-0">
						<b-form-tag
							:title="tag"
							:disabled="disabled"
							variant="primary"
							class="m-0 px-2 py-1"
							@remove="removeTag(tag)"
						/>
					</li>
				</ul>
				<b-dropdown
					ref="dropdown"
					variant="link"
					class="d-inline flex-grow-1"
					menu-class="w-75 mh-50 overflow-auto"
					toggle-class="d-flex flex-row justify-content-end py-1 px-0 text-dark"
					block
					right
					no-caret
					boundary="viewport"
				>
					<template #button-content>
						<font-awesome-icon class="fa-fw" :icon="['fas', 'caret-down']" />
					</template>
					<b-dropdown-form form-class="px-2 py-1" @submit.stop.prevent="() => {}">
						<b-form-input v-model="search" type="search" size="sm" autocomplete="off" />
					</b-dropdown-form>
					<b-dropdown-divider></b-dropdown-divider>
					<b-dropdown-item-button
						v-for="option in availableOptions"
						:key="option[valueField]"
						button-class="px-2 py-1"
						@click="onOptionClick({ option, addTag })"
					>
						{{ option[textField] }}
					</b-dropdown-item-button>
				</b-dropdown>
			</div>
		</template>
	</b-form-tags>
</template>

<script>
export default {
	name: 'BFixedTagInput',
	props: {
		textField: {
			type: String,
			default: 'text',
		},
		valueField: {
			type: String,
			default: 'value',
		},
		value: {
			type: Array,
			default: () => [],
		},
		options: {
			type: Array,
			default: () => [],
		},
		maxOptions: {
			type: Number,
			default: 100,
		},
	},
	data() {
		return {
			selected: this.value,
			search: '',
		};
	},
	computed: {
		criteria() {
			return this.search.trim().toLowerCase();
		},
		availableOptions() {
			return this.options
				.filter((option) => {
					if (this.criteria.length > 0 && option[this.valueField].toLowerCase().indexOf(this.criteria) === -1) {
						return false;
					}

					if (this.value.indexOf(option[this.valueField]) !== -1) {
						return false;
					}

					return true;
				})
				.slice(0, this.maxOptions);
		},
	},
	watch: {
		value(value) {
			this.selected = value;
		},
	},
	methods: {
		onOptionClick({ option, addTag }) {
			addTag(option[this.valueField]);
			this.search = '';
		},
	},
};
</script>

<style scoped lang="scss"></style>
