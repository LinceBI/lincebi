<template>
	<b-nav-item-dropdown class="nav-bar-settings" right no-caret>
		<template slot="button-content">
			<font-awesome-icon :icon="['fac', 'tool-stadmin']" />
			<span class="lbl d-lg-none">{{ $t('navbar.settings') }}</span>
		</template>
		<nav-bar-dropdown-item
			:text="$t('navbar.profile')"
			:icon="['fas', 'user']"
			:to="{ name: 'profile' }"
		/>
		<b-dropdown-divider />
		<nav-bar-dropdown-item
			:text="$t('navbar.showMenuBar')"
			:enabled="getUserSetting('custom_field_show_menu_bar')"
			@click="toggleUserSetting('custom_field_show_menu_bar')"
		/>
		<nav-bar-dropdown-item
			:text="$t('navbar.showToolBar')"
			:enabled="getUserSetting('custom_field_show_tool_bar')"
			@click="toggleUserSetting('custom_field_show_tool_bar')"
		/>
		<nav-bar-dropdown-item
			:text="$t('navbar.showHiddenFiles')"
			:enabled="
				getUserSetting('MANTLE_SHOW_HIDDEN_FILES', 'browser.perspective')
			"
			@click="
				toggleUserSetting('MANTLE_SHOW_HIDDEN_FILES', 'browser.perspective')
			"
		/>
		<nav-bar-dropdown-item
			:text="$t('navbar.useDescriptionsForTooltips')"
			:enabled="
				getUserSetting(
					'MANTLE_SHOW_DESCRIPTIONS_FOR_TOOLTIPS',
					'browser.perspective'
				)
			"
			@click="
				toggleUserSetting(
					'MANTLE_SHOW_DESCRIPTIONS_FOR_TOOLTIPS',
					'browser.perspective'
				)
			"
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
		}
	},
	methods: {
		getUserSetting(key) {
			return this.userSettings[key] === 'true';
		},
		async toggleUserSetting(key, perspectiveToReload) {
			const value = this.userSettings[key] === 'true' ? 'false' : 'true';
			await store.dispatch('setUserSetting', { key, value });
			if (perspectiveToReload) {
				eventBus.$emitWhen('mantle.perspective.reload', perspectiveToReload);
			}
		}
	}
};
</script>
