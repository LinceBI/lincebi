import { initialGlobalUserSettings, initialUserSettings } from '@/userSettings';

export default () => ({
	canCreate: false,
	canAdminister: false,
	canSchedule: false,
	hasDataAccess: false,
	installedPlugins: [],
	supportedLocales: ['en'],
	locale: 'en',
	repository: { path: '/', children: [] },
	globalUserSettings: initialGlobalUserSettings,
	userSettings: initialUserSettings
});
