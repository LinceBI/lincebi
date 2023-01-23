<template>
	<li
		class="directory-tree-selector-node"
		tabindex="0"
		@keyup.self.enter="expand()"
		@keyup.self.space="select()"
	>
		<div class="row d-flex align-items-center p-1" :class="{ selected }">
			<font-awesome-icon
				v-if="children.length > 0"
				class="fa-fw"
				:icon="['fas', expanded ? 'folder-open' : 'folder-closed']"
				@click="expand()"
			/>
			<font-awesome-icon v-else class="fa-fw" :icon="['fas', 'folder']" />
			<span class="ml-1 flex-fill" @click="select()">{{ directory.name }}</span>
		</div>
		<ul v-if="expanded" class="children pl-3">
			<directory-tree-selector-node
				v-for="child in children"
				:key="child.path"
				:directory="child"
				:select-event-name="selectEventName"
			/>
		</ul>
	</li>
</template>

<script>
import uniqueId from 'lodash/uniqueId';

import eventBus from '@/eventBus';

export default {
	name: 'DirectoryTreeSelectorNode',
	props: {
		directory: {
			type: Object,
			default: () => ({
				path: '/',
				name: '',
				children: [],
			}),
		},
		selectEventName: {
			type: String,
			default: `directory-tree-selector-select-${uniqueId()}`,
		},
	},
	data() {
		return {
			selected: false,
			expanded: false,
		};
	},
	computed: {
		children() {
			return this.directory.children.filter((c) => c.isFolder);
		},
	},
	created() {
		eventBus.$on(this.selectEventName, (directory) => {
			this.selected = directory.path === this.directory.path;
		});
	},
	methods: {
		select() {
			eventBus.$emit(this.selectEventName, this.directory);
		},
		expand() {
			this.expanded = !this.expanded;
		},
	},
};
</script>

<style lang="scss" scoped>
.directory-tree-selector-node {
	list-style-type: none;

	.row {
		width: 100%;
		cursor: pointer;

		&.selected {
			background-color: lighten(map-get($theme-colors, 'secondary'), 40%);
		}
	}
}
</style>
