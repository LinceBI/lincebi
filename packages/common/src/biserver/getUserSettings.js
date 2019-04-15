import getUserSetting from './getUserSetting';

export default async keys =>
	await Promise.all(
		keys.map(key => getUserSetting(key).then(value => ({ key, value })))
	);
