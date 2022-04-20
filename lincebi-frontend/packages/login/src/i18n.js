import fetch from 'unfetch';

import Vue from 'vue';
import VueI18n from 'vue-i18n';

import ar from '@/locales/ar.json';
import ca from '@/locales/ca.json';
import en from '@/locales/en.json';
import es from '@/locales/es.json';
import ko from '@/locales/ko.json';
import pt from '@/locales/pt.json';

Vue.use(VueI18n);

let locale = process.env.VUE_APP_I18N_LOCALE || navigator.language.slice(0, 2).toLowerCase();
const fallbackLocale = process.env.VUE_APP_I18N_FALLBACK_LOCALE || 'en';
const messages = { ar, ca, en, es, ko, pt };
if (!(locale in messages)) {
	console.warn(`Cannot set locale "${locale}"`);
	locale = fallbackLocale;
}

const i18n = new VueI18n({ locale, fallbackLocale, messages });

Object.keys(i18n.messages).forEach(async (locale) => {
	// Skip aliases.
	if (!/^[a-z]{2}$/.test(locale)) return;

	const response = await fetch(`./locales/${locale}.json`, {
		method: 'GET',
		headers: { 'Content-Type': 'application/json' },
	});
	if (response.status === 200) {
		i18n.mergeLocaleMessage(locale, await response.json());
	}
});

document.documentElement.lang = i18n.messages[i18n.locale]?.__meta__?.lang;
document.documentElement.dir = i18n.messages[i18n.locale]?.__meta__?.dir;

export default i18n;
