<template>
	<b-nav-item-dropdown class="NavBarSettings" right no-caret>
		<template slot="button-content">
			<font-awesome-icon :icon="['fac', 'tool-stadmin']" />
			<span class="lbl d-md-none">Options</span>
		</template>
		<b-dropdown-item :to="{ name: 'profile' }">
			<font-awesome-icon :icon="['fac', 'tool-stprofile']" />
			<span class="lbl">Profile</span>
		</b-dropdown-item>
		<b-dropdown-item
			:to="{
				name: 'perspective',
				params: { perspective: 'stadmin.perspective' }
			}"
		>
			<font-awesome-icon :icon="['fac', 'tool-stadmin']" />
			<span class="lbl">Administration</span>
		</b-dropdown-item>
		<b-dropdown-divider />
		<b-dropdown-item
			href="#"
			@click="toggleBoolUserSetting('MANTLE_SHOW_HIDDEN_FILES')"
		>
			<font-awesome-icon
				v-if="getBoolUserSetting('MANTLE_SHOW_HIDDEN_FILES')"
				:icon="['far', 'check-square']"
			/>
			<font-awesome-icon v-else :icon="['far', 'square']" />
			<span class="lbl">Show hidden files</span>
		</b-dropdown-item>
		<b-dropdown-item
			href="#"
			@click="toggleBoolUserSetting('MANTLE_SHOW_DESCRIPTIONS_FOR_TOOLTIPS')"
		>
			<font-awesome-icon
				v-if="getBoolUserSetting('MANTLE_SHOW_DESCRIPTIONS_FOR_TOOLTIPS')"
				:icon="['far', 'check-square']"
			/>
			<font-awesome-icon v-else :icon="['far', 'square']" />
			<span class="lbl">Use descriptions for tooltips</span>
		</b-dropdown-item>
	</b-nav-item-dropdown>
</template>

<script>
import store from '@/store';

export default {
	name: 'NavBarSettings',
	computed: {
		userSettings() {
			return store.state.userSettings;
		}
	},
	methods: {
		getBoolUserSetting(key) {
			return this.userSettings[key] === 'true';
		},
		toggleBoolUserSetting(key) {
			const value = this.userSettings[key] === 'true' ? 'false' : 'true';
			store.dispatch('setUserSetting', { key, value });
		}
	}
};
</script>
