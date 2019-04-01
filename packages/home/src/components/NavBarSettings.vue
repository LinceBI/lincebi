<template>
	<b-nav-item-dropdown class="NavBarSettings" right no-caret>
		<template slot="button-content">
			<font-awesome-icon :icon="['fac', 'tool-stadmin']" />
		</template>
		<b-dropdown-item href="#">
			<font-awesome-icon :icon="['fac', 'tool-stprofile']" />
			<span class="lbl">STProfile</span>
		</b-dropdown-item>
		<b-dropdown-item href="#">
			<font-awesome-icon :icon="['fac', 'tool-stadmin']" />
			<span class="lbl">STAdmin</span>
		</b-dropdown-item>
		<b-dropdown-divider />
		<b-dropdown-item href="#">
			<font-awesome-icon
				:icon="['far', tooltipDescriptionsEnabled ? 'check-square' : 'square']"
			/>
			<span class="lbl">Use descriptions for tooltips</span>
		</b-dropdown-item>
		<b-dropdown-item href="#">
			<font-awesome-icon
				:icon="['far', showHiddenFilesEnabled ? 'check-square' : 'square']"
			/>
			<span class="lbl">Show hidden files</span>
		</b-dropdown-item>
	</b-nav-item-dropdown>
</template>

<script>
export default {
	name: 'NavBarSettings',
	data: () => ({
		tooltipDescriptionsEnabled: true,
		showHiddenFilesEnabled: false
	})
};
</script>
