import safeJSON from '@stratebi/biserver-customization-common/src/safeJSON';

const userSettings = {
	custom_field_name: {
		initial: '',
		default: ''
	},
	custom_field_email: {
		initial: '',
		default: ''
	},
	custom_field_phone: {
		initial: '',
		default: ''
	},
	custom_field_address: {
		initial: '',
		default: ''
	},
	custom_field_avatar: {
		initial: '',
		default: ''
	},
	custom_field_show_menu_bar: {
		initial: 'false',
		default: 'false'
	},
	custom_field_show_tool_bar: {
		initial: 'false',
		default: 'true'
	},
	custom_field_tabs: {
		initial: '[]',
		default: safeJSON.stringify([
			{
				name: 'home.global',
				icon: ['fas', 'globe-europe'],
				type: 'global',
				removable: false,
				translate: true
			},
			{
				name: 'home.home',
				icon: ['fas', 'home'],
				type: 'home',
				removable: false,
				translate: true
			}
		])
	},
	MANTLE_SHOW_HIDDEN_FILES: {
		initial: 'false',
		default: 'false'
	},
	MANTLE_SHOW_DESCRIPTIONS_FOR_TOOLTIPS: {
		initial: 'false',
		default: 'true'
	}
};

export const initialUserSettings = Object.assign(
	...Object.entries(userSettings).map(([key, value]) => ({
		[key]: value.initial
	}))
);

export const defaultUserSettings = Object.assign(
	...Object.entries(userSettings).map(([key, value]) => ({
		[key]: value.default
	}))
);
