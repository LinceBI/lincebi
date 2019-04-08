<template>
	<b-nav-item-dropdown class="NavBarSettings" right no-caret>
		<template slot="button-content">
			<font-awesome-icon :icon="['fac', 'tool-stadmin']" />
			<span class="lbl d-md-none">Options</span>
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
				:icon="[
					'far',
					settings.tooltipDescriptionsEnabled ? 'check-square' : 'square'
				]"
			/>
			<span class="lbl">Use descriptions for tooltips</span>
		</b-dropdown-item>
		<b-dropdown-item href="#">
			<font-awesome-icon
				:icon="[
					'far',
					settings.showHiddenFilesEnabled ? 'check-square' : 'square'
				]"
			/>
			<span class="lbl">Show hidden files</span>
		</b-dropdown-item>
	</b-nav-item-dropdown>
</template>

<script>
import store from '@/store';

export default {
	name: 'NavBarSettings',
	computed: {
		settings() {
			return store.state.settings;
		}
	}
};
</script>
