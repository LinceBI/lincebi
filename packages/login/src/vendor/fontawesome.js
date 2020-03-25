import Vue from 'vue';

Vue.component('font-awesome-icon', async () => {
	const { FontAwesomeIcon } = await import('@fortawesome/vue-fontawesome');
	const { library } = await import('@fortawesome/fontawesome-svg-core');

	const icons = [
		/* eslint-disable prettier/prettier */
		import('@fortawesome/free-solid-svg-icons/faKey'),
		import('@fortawesome/free-solid-svg-icons/faSignInAlt')
		/* eslint-enable */
	];

	await Promise.all(
		icons.map(async (icon) => {
			const { definition } = await icon;
			library.add(definition);
		})
	);

	return FontAwesomeIcon;
});
