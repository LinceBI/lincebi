<template>
	<b-nav-item-dropdown class="nav-bar-settings" right no-caret>
		<template slot="button-content">
			<font-awesome-icon :icon="['fas', 'cog']" />
			<span class="lbl d-lg-none">{{ $t('navbar.settings') }}</span>
		</template>
		<nav-bar-dropdown-item
			:text="$t('navbar.profile')"
			:icon="['fas', 'user']"
			href="#/profile"
		/>
		<b-dropdown-divider />
		<nav-bar-dropdown-item
			:text="$t('navbar.showMenuBar')"
			:enabled="showMenuBar"
			@click="showMenuBar = !showMenuBar"
		/>
		<nav-bar-dropdown-item
			:text="$t('navbar.showToolBar')"
			:enabled="showToolBar"
			@click="showToolBar = !showToolBar"
		/>
		<nav-bar-dropdown-item
			:text="$t('navbar.showHiddenFiles')"
			:enabled="showHiddenFiles"
			@click="showHiddenFiles = !showHiddenFiles"
		/>
		<nav-bar-dropdown-item
			:text="$t('navbar.showDescriptionsForTooltips')"
			:enabled="showDescriptionsForTooltips"
			@click="showDescriptionsForTooltips = !showDescriptionsForTooltips"
		/>
	</b-nav-item-dropdown>
</template>

<script>
import NavBarDropdownItem from '@/components/NavBarDropdownItem.vue';

import store from '@/store';
import eventBus from '@/eventBus';

export default {
	name: 'NavBarSettings',
	components: {
		NavBarDropdownItem
	},
	computed: {
		userSettings() {
			return store.state.userSettings;
		},
		showMenuBar: {
			get() {
				const key = `${this.namespace}.show_menu_bar`;
				const value = this.userSettings[key] === 'true';
				return value;
			},
			set(show) {
				const key = `${this.namespace}.show_menu_bar`;
				const value = show ? 'true' : 'false';
				store.dispatch('updateUserSettings', { [key]: value });
			}
		},
		showToolBar: {
			get() {
				const key = `${this.namespace}.show_tool_bar`;
				const value = this.userSettings[key] === 'true';
				return value;
			},
			set(show) {
				const key = `${this.namespace}.show_tool_bar`;
				const value = show ? 'true' : 'false';
				store.dispatch('updateUserSettings', { [key]: value });
			}
		},
		showHiddenFiles: {
			get() {
				const key = 'MANTLE_SHOW_HIDDEN_FILES';
				const value = this.userSettings[key] === 'true';
				return value;
			},
			set(show) {
				const key = 'MANTLE_SHOW_HIDDEN_FILES';
				const value = show ? 'true' : 'false';
				store.dispatch('updateUserSettings', { [key]: value }).then(() => {
					eventBus.$emitWhen(
						'mantle.perspective.reload',
						'browser.perspective'
					);
				});
			}
		},
		showDescriptionsForTooltips: {
			get() {
				const key = 'MANTLE_SHOW_DESCRIPTIONS_FOR_TOOLTIPS';
				const value = this.userSettings[key] === 'true';
				return value;
			},
			set(show) {
				const key = 'MANTLE_SHOW_DESCRIPTIONS_FOR_TOOLTIPS';
				const value = show ? 'true' : 'false';
				store.dispatch('updateUserSettings', { [key]: value }).then(() => {
					eventBus.$emitWhen(
						'mantle.perspective.reload',
						'browser.perspective'
					);
				});
			}
		}
	}
};
</script>
