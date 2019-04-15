import getContextPath from './getContextPath';

let supportedLocales = null;

export default async (expectedLocales = ['en']) => {
	if (supportedLocales !== null) {
		return supportedLocales;
	}

	supportedLocales = expectedLocales.find(l => l === 'en') ? ['en'] : [];

	for await (const expectedLocale of expectedLocales) {
		const contextPath = await getContextPath();
		const resource = `content/languagePack_${expectedLocale}/resources/lang/messages.properties`;
		const endpoint = `${contextPath}${resource}`;
		const response = await fetch(endpoint, {
			method: 'GET',
			headers: { 'Content-Type': 'text/plain' }
		});

		if (response.status === 200) {
			const languagePackRegex = /^languagePack\.title=/m;
			const languagePackText = await response.text();
			if (languagePackRegex.test(languagePackText)) {
				supportedLocales.push(expectedLocale);
			}
		}
	}

	return supportedLocales;
};
