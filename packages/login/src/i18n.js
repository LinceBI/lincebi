import fetch from 'unfetch';

import Vue from 'vue';
import VueI18n from 'vue-i18n';

import en from '@/locales/en.json';
import es from '@/locales/es.json';
import pt from '@/locales/pt.json';
import ca from '@/locales/ca.json';

Vue.use(VueI18n);

const i18n = new VueI18n({
	locale: process.env.VUE_APP_I18N_LOCALE || navigator.language.slice(0, 2),
	fallbackLocale: process.env.VUE_APP_I18N_FALLBACK_LOCALE || 'en',
	messages: { en, es, pt, ca },
});

Object.keys(i18n.messages).forEach(async (locale) => {
	const response = await fetch(`./locales/${locale}.json`, {
		method: 'GET',
		headers: { 'Content-Type': 'application/json' },
	});
	if (response.status === 200) {
		i18n.mergeLocaleMessage(locale, await response.json());
	}
});

document.documentElement.lang = i18n.locale;

export default i18n;
