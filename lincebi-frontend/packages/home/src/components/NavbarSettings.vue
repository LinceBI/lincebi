<template>
	<b-nav-item-dropdown class="navbar-settings" no-caret>
		<template slot="button-content">
			<font-awesome-icon :icon="['fas', 'gear']" />
			<span class="lbl d-lg-none">{{ $t('navbar.settings') }}</span>
		</template>
		<navbar-dropdown-item :text="$t('navbar.profile')" :icon="['fas', 'user']" href="#/profile" />
		<b-dropdown-divider />
		<navbar-dropdown-item :text="$t('navbar.showMenuBar')" :enabled="showMenuBar" @click="showMenuBar = !showMenuBar" />
		<navbar-dropdown-item :text="$t('navbar.showToolBar')" :enabled="showToolBar" @click="showToolBar = !showToolBar" />
		<navbar-dropdown-item
			v-if="canAdminister"
			:text="$t('navbar.showHiddenTabs')"
			:enabled="showHiddenTabs"
			@click="showHiddenTabs = !showHiddenTabs"
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
		<b-dropdown-divider />
		<navbar-dropdown-item :text="$t('navbar.about')" :icon="['fas', 'circle-question']" href="#/about" />
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
		canAdminister() {
			return store.state.canAdminister;
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
		showHiddenTabs: {
			get() {
				const key = `${this.namespace}.show_hidden_tabs`;
				const value = this.userSettings[key] === 'true';
				return value;
			},
			set(show) {
				const key = `${this.namespace}.show_hidden_tabs`;
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
				eventBus.$emitWhenAvailable('mantle-perspective-reload', 'browser.perspective');
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
				eventBus.$emitWhenAvailable('mantle-perspective-reload', 'browser.perspective');
			},
		},
	},
};
</script>

<style scoped lang="scss">
.navbar-settings {
	:deep(.dropdown-menu) {
		left: inherit;
		right: 0;

		[dir='rtl'] & {
			left: 0;
			right: inherit;
		}
	}
}
</style>
