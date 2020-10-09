<template>
	<b-nav-item-dropdown class="navbar-settings" right no-caret>
		<template slot="button-content">
			<font-awesome-icon :icon="['fas', 'cog']" />
			<span class="lbl d-lg-none">{{ $t('navbar.settings') }}</span>
		</template>
		<navbar-dropdown-item
			:text="$t('navbar.profile')"
			:icon="['fas', 'user']"
			href="#/profile"
		/>
		<b-dropdown-divider />
		<navbar-dropdown-item
			:text="$t('navbar.showMenuBar')"
			:enabled="showMenuBar"
			@click="showMenuBar = !showMenuBar"
		/>
		<navbar-dropdown-item
			:text="$t('navbar.showToolBar')"
			:enabled="showToolBar"
			@click="showToolBar = !showToolBar"
		/>
		<navbar-dropdown-item
			:text="$t('navbar.showHiddenFiles')"
			:enabled="showHiddenFiles"
			@click="showHiddenFiles = !showHiddenFiles"
		/>
		<navbar-dropdown-item
			:text="$t('navbar.showDescriptionsForTooltips')"
			:enabled="showDescriptionsForTooltips"
			@click="showDescriptionsForTooltips = !showDescriptionsForTooltips"
		/>
	</b-nav-item-dropdown>
</template>

<script>
import eventBus from '@/eventBus';
import store from '@/store';

import NavbarDropdownItem from '@/components/NavbarDropdownItem.vue';

export default {
	name: 'NavbarSettings',
	components: {
		NavbarDropdownItem,
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
			},
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
			},
		},
		showHiddenFiles: {
			get() {
				const key = 'MANTLE_SHOW_HIDDEN_FILES';
				const value = this.userSettings[key] === 'true';
				return value;
			},
			async set(show) {
				const key = 'MANTLE_SHOW_HIDDEN_FILES';
				const value = show ? 'true' : 'false';
				await store.dispatch('updateUserSettings', { [key]: value });
				eventBus.$emitWhenAvailable(
					'mantle-perspective-reload',
					'browser.perspective'
				);
			},
		},
		showDescriptionsForTooltips: {
			get() {
				const key = 'MANTLE_SHOW_DESCRIPTIONS_FOR_TOOLTIPS';
				const value = this.userSettings[key] === 'true';
				return value;
			},
			async set(show) {
				const key = 'MANTLE_SHOW_DESCRIPTIONS_FOR_TOOLTIPS';
				const value = show ? 'true' : 'false';
				store.dispatch('updateUserSettings', { [key]: value });
				eventBus.$emitWhenAvailable(
					'mantle-perspective-reload',
					'browser.perspective'
				);
			},
		},
	},
};
</script>
