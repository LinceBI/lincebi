import getUserSetting from './getUserSetting';

export default async keys =>
	await Promise.all(
		keys.map(async key => {
			return { key, value: await getUserSetting(key) };
		})
	);
