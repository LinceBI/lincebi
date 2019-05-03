import { initialGlobalUserSettings, initialUserSettings } from '@/userSettings';

export default () => ({
	canCreate: false,
	canAdminister: false,
	installedPlugins: [],
	supportedLocales: ['en'],
	locale: 'en',
	repository: { path: '/', children: [] },
	globalUserSettings: initialGlobalUserSettings,
	userSettings: initialUserSettings
});
