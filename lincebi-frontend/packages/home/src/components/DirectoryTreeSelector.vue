<template>
	<ul class="directory-tree-selector" :style="{ maxHeight }">
		<directory-tree-selector-node
			v-for="child in root.children"
			:key="child.path"
			:directory="child"
			:select-event-name="selectEventName"
		/>
	</ul>
</template>

<script>
import uniqueId from 'lodash/uniqueId';

import eventBus from '@/eventBus';

import DirectoryTreeSelectorNode from '@/components/DirectoryTreeSelectorNode.vue';

export default {
	name: 'DirectoryTreeSelector',
	components: {
		DirectoryTreeSelectorNode,
	},
	props: {
		value: {
			type: Object,
			default: null,
		},
		root: {
			type: Object,
			default: () => ({
				children: [],
			}),
		},
		maxHeight: {
			type: String,
			default: 'auto',
		},
		selectEventName: {
			type: String,
			default: `directory-tree-selector-select-${uniqueId()}`,
		},
	},
	created() {
		eventBus.$on(this.selectEventName, (directory) => {
			this.$emit('input', directory);
		});
	},
};
</script>

<style lang="scss" scoped>
.directory-tree-selector {
	overflow: auto;
}
</style>
