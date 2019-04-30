import jdenticon from 'jdenticon';

const maxCacheSize = 500;
const cache = new Map();

export default (value = '', padding = 0.08) => {
	if (cache.has(value)) {
		return cache.get(value);
	}

	const svg = jdenticon.toSvg(value, 256, padding);
	const dataURI = `data:image/svg+xml;utf8,${encodeURIComponent(svg)}`;
	cache.set(value, dataURI);

	if (cache.size > maxCacheSize) {
		const cacheIterator = cache.entries();
		while (cache.size > maxCacheSize) {
			const pair = cacheIterator.next().value;
			if (pair) cache.delete(pair[0]);
			else break;
		}
	}

	return dataURI;
};