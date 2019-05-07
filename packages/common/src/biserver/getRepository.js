import getMetadata from './getMetadata';

export default async ({ locale = 'default', depth = -1 } = {}) => {
	const response = await getMetadata({ fullPath: '/' }, { locale, depth });

	if (Array.isArray(response) && response.length === 1) {
		return response[0];
	}

	return null;
};
