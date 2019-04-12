export default async () =>
	[
		{
			enabled: true,
			id: 'marketing',
			name: 'Marketing',
			icon: require('@/assets/img/categories/icons/marketing.svg'),
			banner: require('@/assets/img/categories/banners/marketing.png')
		},
		{
			enabled: true,
			id: 'retail',
			name: 'Retail',
			icon: require('@/assets/img/categories/icons/retail.svg'),
			banner: require('@/assets/img/categories/banners/retail.png')
		},
		{
			enabled: true,
			id: 'finance',
			name: 'Finance',
			icon: require('@/assets/img/categories/icons/finance.svg'),
			banner: require('@/assets/img/categories/banners/finance.png')
		},
		{
			enabled: true,
			id: 'ngo',
			name: 'NGO',
			icon: require('@/assets/img/categories/icons/ngo.svg'),
			banner: require('@/assets/img/categories/banners/ngo.png')
		},
		{
			enabled: true,
			id: 'gov',
			name: 'GOV',
			icon: require('@/assets/img/categories/icons/gov.svg'),
			banner: require('@/assets/img/categories/banners/gov.png')
		},
		{
			enabled: true,
			id: 'e-commerce',
			name: 'e-Commerce',
			icon: require('@/assets/img/categories/icons/e-commerce.svg'),
			banner: require('@/assets/img/categories/banners/e-commerce.png')
		},
		{
			enabled: true,
			id: 'telco',
			name: 'Telco',
			icon: require('@/assets/img/categories/icons/telco.svg'),
			banner: require('@/assets/img/categories/banners/telco.png')
		},
		{
			enabled: true,
			id: 'utilities',
			name: 'Utilities',
			icon: require('@/assets/img/categories/icons/utilities.svg'),
			banner: require('@/assets/img/categories/banners/utilities.png')
		},
		{
			enabled: true,
			id: 'tourism',
			name: 'Tourism',
			icon: require('@/assets/img/categories/icons/tourism.svg'),
			banner: require('@/assets/img/categories/banners/tourism.png')
		},
		{
			enabled: true,
			id: 'education',
			name: 'Education',
			icon: require('@/assets/img/categories/icons/education.svg'),
			banner: require('@/assets/img/categories/banners/education.png')
		},
		{
			enabled: true,
			id: 'health',
			name: 'Health',
			icon: require('@/assets/img/categories/icons/health.svg'),
			banner: require('@/assets/img/categories/banners/health.png')
		},
		{
			enabled: true,
			id: 'rrhh',
			name: 'RRHH',
			icon: require('@/assets/img/categories/icons/rrhh.svg'),
			banner: require('@/assets/img/categories/banners/rrhh.png')
		},
		{
			enabled: true,
			id: 'pharma',
			name: 'Pharma',
			icon: require('@/assets/img/categories/icons/pharma.svg'),
			banner: require('@/assets/img/categories/banners/pharma.png')
		}
	].filter(category => category.enabled);
