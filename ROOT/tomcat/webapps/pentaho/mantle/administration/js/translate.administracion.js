//document.write('<script src=properties/languages_.properties></script>');
var idioma = getUrlParameter('locale');
if (idioma != 'en') {
	idioma = 'es';
}

document.write('<script src=properties/languages_' + idioma + '.properties></script>');
function translateAdministracion() {
	// Admin Operations
	$('#mds').html(mdsText);
	$('#biAdministracion').html(biAdministracionText);
	$('#streportStatistics').html(streportStatisticsText);

	// Cache Operations
	$('#refreshSystem').html(refreshSystemText);
	$('#refreshReportingMetadata').html(refreshReportingMetadataText);
	$('#refreshGlobal').html(refreshGlobalText);
	$('#refreshReportingData').html(refreshReportingDataText);
	$('#refreshCDA').html(refreshCDAText);
}

function getUrlParameter(sParam) {
	var sPageURL = decodeURIComponent(window.top.location.search.substring(1)),
		sURLVariables = sPageURL.split('&'),
		sParameterName,
		i;

	for (i = 0; i < sURLVariables.length; i++) {
		sParameterName = sURLVariables[i].split('=');

		if (sParameterName[0] === sParam) {
			return sParameterName[1] === undefined ? 'es' : sParameterName[1].substr(0, 2);
		}
	}
}
