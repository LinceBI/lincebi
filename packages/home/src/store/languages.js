export default async () =>
	[
		{
			enabled: true,
			id: 'en_US',
			name: 'English',
			img: require('@/assets/img/flags/uk.svg')
		},
		{
			enabled: true,
			id: 'es_ES',
			name: 'Spanish',
			img: require('@/assets/img/flags/es.svg')
		}
	].filter(language => language.enabled);
