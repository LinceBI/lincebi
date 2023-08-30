import getGlobalUserSetting from './getGlobalUserSetting';

export default async (keys) => {
	return Object.assign(
		...(await Promise.all(
			keys.map(async (key) => {
				return { [key]: await getGlobalUserSetting(key) };
			}),
		)),
	);
};
