import getMetadata from './getMetadata';

export default async () => {
	const response = await getMetadata({ fullPath: '/' }, { depth: -1 });

	if (Array.isArray(response) && response.length === 1) {
		return response[0];
	}

	return null;
};
