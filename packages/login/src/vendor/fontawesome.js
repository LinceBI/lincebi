import Vue from 'vue';

import '@fortawesome/fontawesome-svg-core/styles.css';

Vue.component('font-awesome-icon', async () => {
	const [{ FontAwesomeIcon }, { config, library }] = await Promise.all([
		/* eslint-disable prettier/prettier */
		import('@fortawesome/vue-fontawesome'),
		import('@fortawesome/fontawesome-svg-core'),
		/* eslint-enable */
	]);

	config.autoAddCss = false;

	const icons = await Promise.all([
		/* eslint-disable prettier/prettier */
		import('@fortawesome/free-solid-svg-icons/faKey'),
		import('@fortawesome/free-solid-svg-icons/faSignInAlt')
		/* eslint-enable */
	]);

	icons.map((icon) => {
		library.add(icon.definition);
	});

	return FontAwesomeIcon;
});
