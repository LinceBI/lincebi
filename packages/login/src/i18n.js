import Vue from 'vue';
import VueI18n from 'vue-i18n';

import en from '@/locales/en.json';
import es from '@/locales/es.json';

Vue.use(VueI18n);

const i18n = new VueI18n({
	locale: process.env.VUE_APP_I18N_LOCALE || navigator.language.slice(0, 2),
	fallbackLocale: process.env.VUE_APP_I18N_FALLBACK_LOCALE || 'en',
	messages: { en, es }
});

document.documentElement.lang = i18n.locale;

export default i18n;
