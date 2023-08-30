import setUserSetting from './setUserSetting';

export default async (userSettings) => {
	return Object.assign(
		...(await Promise.all(
			Object.entries(userSettings).map(async ([key, value]) => {
				return { [key]: await setUserSetting(key, value) };
			}),
		)),
	);
};
