import getContextPath from './getContextPath';

let supportedLocalesPromise = null;

const getSupportedLocales = async (expectedLocales = ['en']) => {
	const contextPath = await getContextPath();

	return (await Promise.all(
		expectedLocales.map(async expectedLocale => {
			// English is always supported.
			if (expectedLocale === 'en') {
				return expectedLocale;
			}

			// Check a known language pack resource.
			const resource = `content/languagePack_${expectedLocale}/resources/lang/messages.properties`;
			const endpoint = `${contextPath}${resource}`;
			const response = await fetch(endpoint, {
				method: 'GET',
				headers: { 'Content-Type': 'text/plain' }
			});

			if (response.status === 200) {
				const languagePackText = await response.text();
				const languagePackRegex = /^languagePack\.title=/m;
				if (languagePackRegex.test(languagePackText)) {
					return expectedLocale;
				}
			}
		})
	)).filter(locale => typeof locale === 'string');
};

export default async (...args) => {
	if (supportedLocalesPromise === null) {
		supportedLocalesPromise = getSupportedLocales(...args);
	}
	return supportedLocalesPromise;
};
