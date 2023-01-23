import Vue from 'vue';
import VueI18n from 'vue-i18n';

import ar from '@/locales/ar.json';
import ca from '@/locales/ca.json';
import en from '@/locales/en.json';
import es from '@/locales/es.json';
import ko from '@/locales/ko.json';
import pt from '@/locales/pt.json';

Vue.use(VueI18n);

let locale = process.env.VUE_APP_I18N_LOCALE || navigator.language.replaceAll('-', '_');
const fallbackLocale = process.env.VUE_APP_I18N_FALLBACK_LOCALE || 'en';
const messages = {
	ar,
	ar_AE: ar,
	ar_BH: ar,
	ar_DZ: ar,
	ar_EG: ar,
	ar_IN: ar,
	ar_IQ: ar,
	ar_JO: ar,
	ar_KW: ar,
	ar_LB: ar,
	ar_LY: ar,
	ar_MA: ar,
	ar_OM: ar,
	ar_QA: ar,
	ar_SA: ar,
	ar_SD: ar,
	ar_SS: ar,
	ar_SY: ar,
	ar_TN: ar,
	ar_YE: ar,
	ca,
	ca_AD: ca,
	ca_ES: ca,
	ca_FR: ca,
	ca_IT: ca,
	en,
	en_AG: en,
	en_AU: en,
	en_BW: en,
	en_CA: en,
	en_DK: en,
	en_GB: en,
	en_HK: en,
	en_IE: en,
	en_IL: en,
	en_IN: en,
	en_NG: en,
	en_NZ: en,
	en_PH: en,
	en_SC: en,
	en_SG: en,
	en_US: en,
	en_ZA: en,
	en_ZM: en,
	en_ZW: en,
	es,
	es_AR: es,
	es_BO: es,
	es_CL: es,
	es_CO: es,
	es_CR: es,
	es_CU: es,
	es_DO: es,
	es_EC: es,
	es_ES: es,
	es_GT: es,
	es_HN: es,
	es_MX: es,
	es_NI: es,
	es_PA: es,
	es_PE: es,
	es_PR: es,
	es_PY: es,
	es_SV: es,
	es_US: es,
	es_UY: es,
	es_VE: es,
	ko,
	ko_KR: ko,
	pt,
	pt_BR: pt,
	pt_PT: pt,
};
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
