import getUserSetting from './getUserSetting';

export default async keys => {
	return Object.assign(
		...(await Promise.all(
			keys.map(async key => ({ [key]: await getUserSetting(key) }))
		))
	);
};
