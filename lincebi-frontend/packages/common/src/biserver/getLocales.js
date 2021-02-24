import getOverlays from './getOverlays';

let localesPromise = null;

const getLocales = async () => {
	const overlays = await getOverlays();

	// English is always installed.
	const locales = new Set(['en']);

	overlays.forEach((_, id) => {
		const idRegex = /\.languagePack_([a-z]{2}(?:_[A-Z]{2})?)$/;
		const found = id.match(idRegex);
		if (found && found.length === 2) {
			locales.add(found[1]);
		}
	});

	return locales;
};

export default async (...args) => {
	if (localesPromise === null) {
		localesPromise = getLocales(...args);
	}
	return localesPromise;
};
