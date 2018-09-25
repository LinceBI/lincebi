/*******************************/
/*******************************/
/** Buscador Reposity Browser **/
/** v2 ***************StrateBI**/
/*******************************/
/*******************************/

/** Configuración de entorno **/

var contexto_pentaho = 'pentaho';
var visor_olap = 'stpivot'; //stpivot
var color_principal = '#769D49';
var color_rotulos = '#769D49'; //#1973bc

/******************************/

if (visor_olap == 'jpivot') {
	visor_olap = '/plugin/jpivot/Pivot';
}
if (visor_olap == 'stpivot') {
	visor_olap = '/STPivot';
}
var aPath_base = '/';
var aPath = '/';
var loading_var = 0;
//$("#reset_filtros i").style("color","red");

function arrayUnique(array) {
	var a = array.concat();
	for (var i = 0; i < a.length; ++i) {
		for (var j = i + 1; j < a.length; ++j) {
			if (a[i] === a[j]) a.splice(j--, 1);
		}
	}

	return a;
}

function onready() {
	loading_var--;
	if (loading_var == 0) {
		$('#cargando').removeClass('loader');
	}
	filtro_extension();
}

function delete_info(guardar) {
	$.ajax({
		type: 'PUT',
		url: url_ajax,
		contentType: 'application/xml',

		success: function(data) {
			if (guardar == 'true') {
				set_info(meta_url);
				console.log('añado informacion');
			}
			console.log('Borrado con exito');
			//  result = samname;
		},
		error: function(xhr, ajaxOptions, thrownError) {
			console.log('error borrado');
			//console.log(xhr.status);
			//console.log(thrownError);
		}
		//   return samname;
		//also check return false at bottom;
	});
}

function get_info() {
	var tags = $('#modal-tag').val();
	tags = tags.split(',');

	return tags;
}

function get_meta_url() {
	var meta_url = $('#modal_ruta').html();
	var fichero = $('#modal_archivo').html();
	meta_url = meta_url.replace(/\ /g, '%20');
	meta_url = meta_url.replace(/\//g, ':');

	fichero = fichero.replace(/\ /g, '%20');
	meta_url = meta_url + ':' + fichero;
	return meta_url;
}
function set_info(borrar_locale) {
	var meta_url = get_meta_url();
	var tags_nuevos = [];
	var tag_element = [];
	var tags = [];

	var tag_element = $('#tags_actuales .tag_element');
	console.log('tag_element-2: ');
	console.log(tag_element);

	for (i = 0; i < tag_element.length; i++) {
		if (tag_element[i].outerText != '') {
			tags_nuevos.push(tag_element[i].outerText);
			console.log('tags_nuevos[i]: ' + tag_element[i].outerText);
		}
	}
	var tags_actuales = get_info();
	tags = arrayUnique(tags_actuales.concat(tags_nuevos));

	var url_ajax = '';
	var req_xml = '';
	var titulo = $('#modal-titulo').val();
	var descripcion = $('#modal-descripcion').val();
	if (borrar_locale == 'true') {
		url_ajax = '../../api/repo/files/' + meta_url + '/deleteLocale?locale=default';
		borrar_locale = 'false';
	} else {
		url_ajax = '../../api/repo/files/' + meta_url + '/localeProperties?locale=default';

		req_xml = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?><stringKeyStringValueDtoes>';
		for (i = 0; i < tags.length; i++) {
			req_xml += '<stringKeyStringValueDto><key>tag' + i + '</key><value>' + tags[i] + '</value></stringKeyStringValueDto>';
		}

		req_xml +=
			'<stringKeyStringValueDto><key>title</key><value>' +
			titulo +
			'</value></stringKeyStringValueDto><stringKeyStringValueDto><key>description</key><value>' +
			descripcion +
			'</value></stringKeyStringValueDto></stringKeyStringValueDtoes>';
		borrar_locale = 'end';
	}
	/*if (borrar_locale == "title_desc") {
		url_ajax = "../../../pentaho/api/repo/files/" + meta_url + "/properties";
		req_xml = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?><stringKeyStringValueDtoes><stringKeyStringValueDto><key>title</key><value>hola,soy un titulo</value></stringKeyStringValueDto></stringKeyStringValueDtoes>';

		borrar_locale = "end";
	}*/
	$.ajax({
		type: 'PUT',
		url: url_ajax,
		contentType: 'application/xml',
		data: req_xml, //pass this parameter like this then you will be able to get it there
		success: function() {
			if (borrar_locale == 'false') {
				set_info(borrar_locale);
			}
			/*if (borrar_locale == "title_desc") {
				set_info(borrar_locale);
				console.log("set title");

			}		*/

			console.log('Informacion almacenada con exito');
		},
		error: function() {
			console.log('error');
			$('#error_msg').fadeIn();
		}
		//   return samname;
		//also check return false at bottom;
	});
	/*

		req_xml = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?><stringKeyStringValueDtoes>';

$.ajax({
		type: "PUT",
		url: "../../../pentaho/api/repo/files/" + meta_url + "/title",
		contentType: 'application/xml',
		data: req_xml, //pass this parameter like this then you will be able to get it there
		success: function() {
			if (borrar_locale == "false") {
				set_info(borrar_locale);
			}
			console.log("Informacion almacenada con exito");
		},
		error: function() {
				console.log("error");
			}
			//   return samname;
			//also check return false at bottom;
	});	*/
}

function funcion_tags(titulo, descripcion, ruta, fichero, tags_array) {
	/*var meta_url = $(a).parent().parent().attr('id');
	meta_url = meta_url.replace(/%20/g, " ");
	console.log(meta_url);
	set_info(meta_url);*/
	var aux = '';
	tags_array = tags_array.split(',');
	$('#tags_actuales').empty();
	$('#modal-tag').val('');
	for (i = 0; i < tags_array.length; i++) {
		if (tags_array[i].length > 15) {
			aux = tags_array[i].substring(0, 15);
			aux += '...';
		} else {
			aux = tags_array[i];
		}
		$('#tags_actuales').prepend('<div class="tag_element">' + aux + '</div>');
	}
	//var meta_url = ruta.replace(/\//g, ":");
	$('#modal-titulo').val(titulo);

	$('#modal-descripcion').val(descripcion);
	$('#ui-id-1').html(titulo);
	$('#modal_ruta').html(ruta);
	$('#modal_archivo').html(fichero);

	$('#dialog')
		.parent()
		.fadeIn();
}

function filtro_extension(id) {
	//var ext_fichero = $("#filtro_extension select").val();
	$(id).toggleClass('filtro_activo');
	if ($('#filtro_olap').hasClass('filtro_activo')) {
		$('.buscar_olap').css('display', 'block');
	} else {
		$('.buscar_olap').fadeOut();
		$('.buscar_dashboard').css('display', 'none');
	}
	if ($('#filtro_dashboard').hasClass('filtro_activo')) {
		$('.buscar_dashboard').css('display', 'block');
	} else {
		$('.buscar_dashboard').fadeOut();
		$('.buscar_dashboard').css('display', 'none');
	}
	if ($('#filtro_report').hasClass('filtro_activo')) {
		$('.buscar_report').css('display', 'block');
	} else {
		$('.buscar_report').fadeOut();
		$('.buscar_report').css('display', 'none');
	}
}

function accion_buscar(tag) {
	/*$("#resultado_buscar_olap").html("");
	$("#resultado_buscar_dashboard").html("");
	$("#resultado_buscar_report").html("");*/
	if (tag != null) {
		var palabra = tag;
	} else {
		var palabra = '';
	}

	$('#repository_browser').empty();
	buscar(aPath_base, aPath, palabra);
}

function actualizar_colores() {
	$('.pestania_repository .fa-refresh').css('color', color_principal);
	$('#filtro_olap,#filtro_dashboard,#filtro_report,#buscador').css('color', color_principal);
	$('h1,h2,h3,h4').css('color', color_rotulos);
}

function buscar(aPath_base, aPath, palabra) {
	loading_var++;
	$('#cargando').addClass('loader');
	$.ajax({
		url: '../../api/repo/files/' + aPath.replace(/\//g, ':') + '/children',
		data: {
			depth: 1,
			filter: '*|FILES',
			showHidden: false,
			_: new Date().getTime()
		},
		dataType: 'json',
		success: function(r) {
			if (r && r.repositoryFileDto) {
				var items = r.repositoryFileDto;
				for (var i = 0; i < items.length; i++) {
					if (items[i].folder.toLowerCase() == 'true') {
						buscar(aPath_base, items[i].path, palabra);
					} else {
						if (items[i].path.includes(aPath_base)) {
							var tags_array = [];
							var tags = [];

							var titulo = items[i].title;
							var description = '';
							if (items[i].description) {
								var description = items[i].description;
							}
							var extension = items[i].name;
							extension = extension.split(/[\s.]+/);
							extension = '.' + extension[extension.length - 1];
							var ruta = items[i].path.replace(items[i].name, '');
							var tags = [];
							if (items[i].localePropertiesMapEntries) {
								for (j = 0; j < items[i].localePropertiesMapEntries[0].properties.length; j++) {
									var aux = items[i].localePropertiesMapEntries[0].properties[j].key;
									var aux2 = items[i].localePropertiesMapEntries[0].properties[j].value;

									if (aux.includes('tag')) {
										if (aux2.length > 15) {
											aux2 = aux2.substring(0, 15);
											aux2 += '...';
										}
										tags.push(aux2);
										tags_array.push(aux2);
									}
									if (aux == 'title') {
										titulo = aux2;
									}
									if (aux == 'description') {
										description = aux2;
									}
								}
							}

							tags = arrayUnique(tags);
							for (var n = 0; n < tags_array.length; n++) {
								if (tags[n] != '' && tags[n] != 'undefined') {
									tags[n] = '<span class="tag_element" onClick="accion_buscar(this.innerText)">' + tags[n] + '</span>';
								}
							}
							if (tags_array[0]) {
								icon = 'fa-pencil-square-o';
								/*tags = "<span class='first_tag_element' onClick='accion_buscar(this.innerText)'>" + tags.toString() + "</span>";
							tags = tags.replace(/,/g, "</span><span class='tag_element' onClick='accion_buscar(this.innerText)'>");*/
								tags = tags.toString();
								tags = tags.replace(/,/g, '');
								tags = tags.replace(/<span class="tag_element" onclick="accion_buscar(this.innerText)"> <\/span>/g, '');
							} else {
								icon = 'fa-plus';
							}
							if (
								titulo.toLowerCase().includes(palabra.toLowerCase()) ||
								tags
									.toString()
									.toLowerCase()
									.includes(palabra.toLowerCase()) ||
								description.toLowerCase().includes(palabra.toLowerCase())
							) {
								if (extension == '.xjpivot') {
									url = '/' + contexto_pentaho + visor_olap + '?solution=&path=' + items[i].path + '&action=' + items[i].title + '.xjpivot';
									$('#repository_browser').append(
										"<div class='col-md-6 buscar_olap resultado_buscar'><div class='col-md-2'><img class='thumb_result' src='https://www.jetstereo.com/images/no_image.png'></div><div class='col-md-10'><div  id='" +
											aPath.replace(/\//g, ':') +
											':' +
											items[i].name +
											"' style='margin-left:0px;border-bottom:1px #eee solid'>											<div data-sort='olap' class='col-md-1' style='text-align: center;'><img src='../images/stpivot_icon_c.png'></div><div data-sort='" +
											titulo +
											"' class='col-md-11' style='padding-left: 0;'><a class='titulos' href='#' onClick=\"parent.mantle_setPerspective('opened.perspective'); window.parent.openURL('" +
											titulo +
											"', 'tooltip', '" +
											url +
											'\')"> ' +
											titulo +
											"</a></div><div class='description'>" +
											description +
											"</div><div class='col-md-12 tags' style='color:grey;'><i  onClick='funcion_tags(\"" +
											items[i].title +
											'","' +
											description +
											'","' +
											aPath +
											'","' +
											items[i].name +
											'","' +
											tags_array +
											"\")'class='fa " +
											icon +
											"' aria-hidden='true'></i>" +
											tags +
											'</div></div></div></div>'
									);
								}
								if (extension == '.wcdf' || extension == '.std') {
									url = items[i].path;
									url = encodeURIComponent(url);
									url = url.replace(/%2F/g, '%3A');
									url = '/' + contexto_pentaho + '/api/repos/' + url + '/generatedContent';

									$('#repository_browser').append(
										"<div class='col-md-6 buscar_dashboard resultado_buscar'><div class='col-md-2'><img class='thumb_result' src='https://www.jetstereo.com/images/no_image.png'></div><div class='col-md-10'><div  id='" +
											aPath.replace(/\//g, ':') +
											':' +
											items[i].name +
											"' style='margin-left:0px;border-bottom:1px #eee solid'>											<div data-sort='dashboard' class='col-md-1 style='text-align: center;''><img src='../images/stdashboard_icon_c.png'></div><div data-sort='" +
											titulo +
											"' class='col-md-11' style='padding-left: 0;'><a class='titulos' href='#' onClick=\"parent.mantle_setPerspective('opened.perspective'); window.parent.openURL('" +
											titulo +
											"', 'tooltip', '" +
											url +
											'\')"> ' +
											titulo +
											"</a></div><div class='description'>" +
											description +
											"</div><div class='col-md-12 tags' style='color:grey;'><i  onClick='funcion_tags(\"" +
											items[i].title +
											'","' +
											description +
											'","' +
											aPath +
											'","' +
											items[i].name +
											'","' +
											tags_array +
											"\")'class='fa " +
											icon +
											"' aria-hidden='true'></i>" +
											tags +
											'</div></div></div></div>'
									);
								}
								if (extension == '.prpt' || extension == '.adhoc') {
									url = items[i].path;
									url = encodeURIComponent(url);
									url = url.replace(/%2F/g, '%3A');
									url = '/' + contexto_pentaho + '/api/repos/' + url + '/generatedContent';

									$('#repository_browser').append(
										"<div class='col-md-6 buscar_report resultado_buscar'><div class='col-md-2'><img class='thumb_result' src='https://www.jetstereo.com/images/no_image.png'></div><div class='col-md-10'><div  id='" +
											aPath.replace(/\//g, ':') +
											':' +
											items[i].name +
											"' style='margin-left:0px;border-bottom:1px #eee solid'>											<div data-sort='report' class='col-md-1 style='text-align: center;''><img src='../images/streport_icon_c.png'></div><div data-sort='" +
											titulo +
											"' class='col-md-11' style='padding-left: 0;'><a class='titulos' href='#' onClick=\"parent.mantle_setPerspective('opened.perspective'); window.parent.openURL('" +
											titulo +
											"', 'tooltip', '" +
											url +
											'\')"> ' +
											titulo +
											"</a></div><div class='description'>" +
											description +
											"</div><div class='col-md-12 tags' style='color:grey;'><i  onClick='funcion_tags(\"" +
											items[i].title +
											'","' +
											description +
											'","' +
											aPath +
											'","' +
											items[i].name +
											'","' +
											tags_array +
											"\")'class='fa " +
											icon +
											"' aria-hidden='true'></i>" +
											tags +
											'</div></div></div></div>'
									);
								}
							}
						}
					}
				}
			}
			onready();
		}
	});
	return false;
}

function openSolution(aLabel, aName, aPath) {
	var extension = aPath.substring(aPath.length - 5, aPath.length);

	var url_to_open = 'api/repos/' + aPath.replace(/\//g, '%3A') + '/generatedContent?ts=' + new Date().getTime();

	if (extension === '.prpt') {
		url_to_open = 'api/repos/' + aPath.replace(/\//g, '%3A') + '/viewer?ts=' + new Date().getTime();
		window.open('/pentaho/' + url_to_open, '_blank');
	} else {
		top.mantle_openTab(aLabel, aName, url_to_open);
	}

	return false;
}

var popup_init = false;

function preCreatePopover() {
	if (!popup_init) {
		var tmp = $.fn.popover.Constructor.prototype.show;
		$.fn.popover.Constructor.prototype.show = function() {
			tmp.call(this);
			if (!$('.popover-title').html()) $('.popover-title').hide();
		};
		popup_init = true;
	}
}
