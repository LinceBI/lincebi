import { initialGlobalUserSettings, initialUserSettings } from '@/userSettings';

export default () => ({
	showSidebar: window.matchMedia('(min-device-width: 576px)').matches,
	userId: '',
	canCreate: false,
	canAdminister: false,
	canSchedule: false,
	hasDataAccess: false,
	installedPlugins: [],
	installedLocales: [],
	locale: 'en',
	isRepositoryLoading: true,
	repository: { path: '/', children: [] },
	globalUserSettings: initialGlobalUserSettings,
	userSettings: initialUserSettings,
});
