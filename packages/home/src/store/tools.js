export default async () =>
	[
		{
			enabled: true,
			id: 'stpivot',
			name: 'STPivot',
			url: 'plugin/jpivot/AnalysisViewService?component=newView'
		},
		{
			enabled: true,
			id: 'stdashboard',
			name: 'STDashboard',
			url: 'content/stdashboard?solution=system&path=temp&action=true'
		},
		{
			enabled: true,
			id: 'stcard',
			name: 'STCard',
			url: 'stcard/menu/STCard.jsp'
		},
		{
			enabled: true,
			id: 'streport',
			name: 'STReport',
			url: 'content/saiku-adhoc/web/index.html?biplugin=true'
		},
		{
			enabled: true,
			id: 'stagile',
			name: 'STAgile',
			url: 'content/stagile/ui/index.html#!/new'
		}
	].filter(tool => tool.enabled);
