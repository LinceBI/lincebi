/*******************************/
/*******************************/
/** Buscador Reposity Browser **/
/** v3 ***************StrateBI**/
/*******************************/
/*******************************/

/** Configuración de entorno **/

var contexto_pentaho = 'pentaho';
var visor_olap = 'stpivot'; //stpivot - jpivot
var color_principal = '#9DC221';
var color_rotulos = '#1973bc';
var CUSTOM_PROPERTIES_EXT = '.stproperties';

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
var favoritesList;

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

function declaracion(locale) {
	$('#datepicker_desde').datepicker({
		autoclose: true,
		format: 'dd/mm/yyyy',
		language: locale
	});
	$('#datepicker_hasta').datepicker({
		autoclose: true,
		format: 'dd/mm/yyyy',
		language: locale
	});
}

function delete_info(guardar) {
	$.ajax({
		type: 'PUT',
		url: url_ajax,
		contentType: 'application/xml',

		success: function(data) {
			if (guardar == 'true') {
				set_info();
				console.log('Información añadida');
			}
			console.log('Borrado con exito');
		},
		error: function(xhr, ajaxOptions, thrownError) {
			console.log('Error borrado');
		}
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

function getPropertiesId(path) {
	var id = '';

	$.ajax({
		url: '../../api/repo/files/' + path.replace(/\//g, ':') + CUSTOM_PROPERTIES_EXT + '/properties',
		dataType: 'json',
		async: false,
		success: function(r) {
			if (r && r.id) id = r.id;
		}
	});

	return id;
}

function getProperties(path) {
	var properties = {};

	$.ajax({
		url: '../../api/repo/files/' + path.replace(/\//g, ':') + CUSTOM_PROPERTIES_EXT + '/metadata',
		async: false,
		statusCode: {
			200: function() {
				$.ajax({
					url: '../../api/repo/files/' + path.replace(/\//g, ':') + CUSTOM_PROPERTIES_EXT,
					dataType: 'json',
					async: false,
					success: function(data) {
						if (data) properties = data;
					}
				});
			}
		}
	});

	return properties;
}

function deleteProperties(custom) {
	var meta_url = get_meta_url();
	var url_properties = '../../api/repo/files/';
	var data = '';
	var success = false;

	if (custom === true) {
		url_properties += 'deletepermanent';
		data = getPropertiesId(meta_url);
	} else {
		url_properties += meta_url + '/deleteLocale?locale=default';
	}

	if (!custom || (custom && data !== '')) {
		$.ajax({
			type: 'PUT',
			url: url_properties,
			async: false,
			data: data,
			success: function() {
				if (custom) {
					success = true;
				} else {
					success = deleteProperties(true);
				}
			}
		});
	} else {
		success = true;
	}

	return success;
}

function addProperties(title, description, image, tags, custom) {
	var meta_url = get_meta_url();
	var url_properties = '../../api/repo/files/';
	var data;
	var success = false;
	var contentType = 'application/xml';

	if (custom === true) {
		url_properties += get_meta_url() + CUSTOM_PROPERTIES_EXT;
		data = {
			image: image,
			tags: tags
		};
		data = JSON.stringify(data);
		contentType = 'application/json';
	} else {
		url_properties += meta_url + '/localeProperties?locale=default';
		data =
			'<?xml version="1.0" encoding="UTF-8" standalone="yes"?>' +
			'<stringKeyStringValueDtoes>' +
			'<stringKeyStringValueDto>' +
			'<key>file.title</key><value>' +
			title +
			'</value>' +
			'</stringKeyStringValueDto>' +
			'<stringKeyStringValueDto>' +
			'<key>jcr:primaryType</key><value>nt:unstructured</value>' +
			'</stringKeyStringValueDto>' +
			'<stringKeyStringValueDto>' +
			'<key>file.description</key><value>' +
			description +
			'</value>' +
			'</stringKeyStringValueDto>' +
			'</stringKeyStringValueDtoes>'.trim();
	}

	$.ajax({
		type: 'PUT',
		url: url_properties,
		contentType: contentType,
		async: false,
		data: data,
		success: function() {
			if (custom) {
				success = true;
			} else {
				success = addProperties(title, description, image, tags, true);
			}
		}
	});

	return success;
}

function set_info() {
	var success = false;

	// Delete properties and add the new ones
	if (deleteProperties()) {
		var title = $('#modal-titulo').val();
		var description = $('#modal-descripcion').val();
		var image = $('#modal-imagen').val();

		// Tags
		var tags_nuevos = [];
		var tag_element = [];
		var tags = [];
		var tag_element = $('#tags_actuales .element-tag');
		for (i = 0; i < tag_element.length; i++) {
			if (tag_element[i].outerText != '') {
				tags_nuevos.push(tag_element[i].outerText);
			}
		}
		var tags_actuales = get_info();
		tags = arrayUnique(tags_actuales.concat(tags_nuevos));

		success = addProperties(title, description, image, tags);
	}

	if (!success) {
		console.log('error');
		$('#error_msg').fadeIn();
	}
}

function funcion_tags(titulo, descripcion, imagen, ruta, fichero, tags_array) {
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
		if (aux !== '') $('#tags_actuales').prepend('<div class="element-tag">' + aux + '</div>');
	}
	$('#modal-titulo').val(titulo);
	$('#modal-imagen').val(imagen);
	if (imagen != '') {
		document.getElementById('modal-imagen-thumb').src = imagen;
	} else {
		document.getElementById('modal-imagen-thumb').src = '//www.jetstereo.com/images/no_image.png';
	}

	$('#modal-descripcion').val(descripcion);
	$('#ui-id-1').html(titulo);
	$('#modal_ruta').html(ruta);
	$('#modal_archivo').html(fichero);

	var modalPath = ruta + '/' + fichero;
	$('#modal-path').html(modalPath);

	if (checkBox) {
		if (isInHomeGlobalSetting(modalPath) > -1) {
			checkBox.prop('checked', true);
		} else {
			checkBox.prop('checked', false);
		}
	}

	$('#dialog')
		.parent()
		.fadeIn();
	$('#dialog-bg').css('z-index', 100);
	$('#dialog-bg').css('opacity', 0.5);
}

var homeItems = [];

function createHomeGlobalSetting() {
	var path = $('#modal-path').html();
	var title = $('#modal-titulo').val();
	var description = $('#modal-descripcion').val();
	var extension = $('#modal_archivo').html();
	extension = extension.split(/[\s.]+/);
	extension = extension[extension.length - 1];
	var image = $('#modal-imagen').val();
	var tags = [];

	$('#tags_actuales > .element-tag').each(function(index, item) {
		tags.push(item.innerHTML);
	});

	$('ul.tagit > .tagit-choice > .tagit-label').each(function(index, item) {
		tags.push(item.innerHTML);
	});

	var item = {};
	item.path = path;
	item.title = title;
	item.description = description;
	item.extension = extension;
	item.image = image;
	item.tags = tags;

	return item;
}

function updateHomeGlobalSetting() {
	$.ajax({
		type: 'POST',
		data: JSON.stringify(homeItems),
		url: '../../plugin/global-user-settings/api/home-items',
		dataType: 'json',
		async: false,
		success: function() {
			getHomeGlobalSetting();
			return true;
		}
	});

	return false;
}

function setHomeGlobalSetting(item) {
	if (isInHomeGlobalSetting(item['path']) != -1) {
		removeHomeGlobalSetting(item['path']);
	}

	homeItems.push(item);
	updateHomeGlobalSetting();

	return false;
}

function removeHomeGlobalSetting(path) {
	var index = isInHomeGlobalSetting(path);

	if (index > -1) {
		homeItems.splice(index, 1);
		updateHomeGlobalSetting();
	}

	return false;
}

function getHomeGlobalSetting() {
	$.ajax({
		url: '../../api/user-settings/home-items',
		dataType: 'json',
		async: false,
		success: function(r) {
			if (r) homeItems = r;
		}
	});

	return homeItems;
}

function isInHomeGlobalSetting(path) {
	getHomeGlobalSetting();

	for (var i = 0; i < homeItems.length; i++) {
		if (homeItems[i]['path'].localeCompare(path) == 0) {
			return i;
		}
	}

	return -1;
}

function filtro_extension(id) {
	$(id).toggleClass('filtro_activo');
	if ($('#filtro_olap').hasClass('filtro_activo')) {
		$('.buscar_olap').css('display', 'table');
	} else {
		$('.buscar_olap').fadeOut();
	}
	if ($('#filtro_dashboard').hasClass('filtro_activo')) {
		$('.buscar_dashboard').css('display', 'table');
	} else {
		$('.buscar_dashboard').fadeOut();
	}
	if ($('#filtro_report').hasClass('filtro_activo')) {
		$('.buscar_report').css('display', 'table');
	} else {
		$('.buscar_report').fadeOut();
	}
	if ($('#filtro_stagile').hasClass('filtro_activo')) {
		$('.buscar_stagile').css('display', 'table');
	} else {
		$('.buscar_stagile').fadeOut();
	}
	if ($('#filtro_cde').hasClass('filtro_activo')) {
		$('.buscar_cde').css('display', 'table');
	} else {
		$('.buscar_cde').fadeOut();
	}
}

function accion_buscar(tag, onlyTags) {
	if (tag != null) {
		var palabra = tag;
	} else {
		var palabra = $('#buscador').val();
	}

	setHeader(tag, onlyTags);

	if (onlyTags) {
		reset();
	}

	getFavorites();

	var desde = $('#datepicker_desde input').val();
	desde = desde.split(/[\s/]+/);
	desde = desde[1] + '/' + desde[0] + '/' + desde[2];
	desde = new Date(desde);
	desde = desde.getTime();

	var hasta = $('#datepicker_hasta input').val();
	hasta = hasta.split(/[\s/]+/);
	hasta = hasta[1] + '/' + hasta[0] + '/' + hasta[2];
	hasta = new Date(hasta);
	hasta = hasta.getTime() + 86400000;

	$('#repository_browser_buscar').empty();

	if ($('#datepicker_desde input')[0].value != '' || $('#datepicker_hasta input')[0].value != '') {
		if ($('#datepicker_desde input')[0].value == '') {
			desde = 0;
		}
		if ($('#datepicker_hasta input')[0].value == '') {
			hasta = 9999999999999999999;
		}

		var creacion_modificacion = document.getElementById('datepicker_filtro').value;
		buscar(aPath_base, aPath, palabra, desde, hasta, creacion_modificacion, onlyTags);
	} else {
		buscar(aPath_base, aPath, palabra, 0, 9999999999999999999, creacion_modificacion, onlyTags);
	}
}

function reset() {
	aPath_base = '/public/';
	aPath = '/public/';
	$('#repository_browser').toggleClass('mostrar');
	$('#path').toggleClass('mostrar');

	if ($('#filtro_olap').hasClass('filtro_activo') == false) {
		$('#filtro_olap').toggleClass('filtro_activo');
	}
	if ($('#filtro_dashboard').hasClass('filtro_activo') == false) {
		$('#filtro_dashboard').toggleClass('filtro_activo');
	}
	if ($('#filtro_report').hasClass('filtro_activo') == false) {
		$('#filtro_report').toggleClass('filtro_activo');
	}
	browsePath(aPath_base);
}

function reset_filters() {
	window.parent.$('#buscador').val('');
	$('#datepicker_desde').datepicker('setDate', null);
	$('#datepicker_hasta').datepicker('setDate', null);
	accion_buscar('');
}

function actualizar_colores() {
	$('.pestania_repository .fa-refresh').css('color', color_principal);
	$('#filtro_olap,#filtro_dashboard,#filtro_report,#buscador').css('color', color_principal);
	$('h1,h2,h3,h4').css('color', color_rotulos);
}

function debug(aPath_base, aPath, palabra, desde, hasta, creacion_modificacion, fecha_filtro_ms, fecha_creacion, fecha_modificacion) {
	console.log('aPath_base: ' + aPath_base);
	console.log('aPath: ' + aPath);
	console.log('palabra: ' + palabra);
	console.log('desde: ' + desde);
	console.log('hasta: ' + hasta);
	if (desde != undefined) {
		console.log('creacion_modificacion: ' + creacion_modificacion);
		console.log('fecha_filtro_ms: ' + fecha_filtro_ms);
		console.log('fecha_creacion: ' + fecha_creacion);
		console.log('fecha_modificacion: ' + fecha_modificacion);
	}
	console.log('---------------------------------------');
}

function buscar(aPath_base, aPath, palabra, desde, hasta, creacion_modificacion, onlyTags) {
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
					var fecha_filtro_ms = '';
					var fecha_modificacion_ms = parseInt(items[i].lastModifiedDate);
					var fecha_modificacion = new Date(parseInt(fecha_modificacion_ms));
					fecha_modificacion = fecha_modificacion.getDate() + '/' + (fecha_modificacion.getMonth() + 1) + '/' + fecha_modificacion.getFullYear();
					var fecha_creacion_ms = parseInt(items[i].createdDate);
					var fecha_creacion = new Date(parseInt(fecha_creacion_ms));
					fecha_creacion = fecha_creacion.getDate() + '/' + (fecha_creacion.getMonth() + 1) + '/' + fecha_creacion.getFullYear();
					if (creacion_modificacion == 'creacion') {
						fecha_filtro_ms = fecha_creacion_ms;
					} else {
						fecha_filtro_ms = fecha_modificacion_ms;
					}
					if (items[i].folder.toLowerCase() == 'true') {
						buscar(aPath_base, items[i].path, palabra, desde, hasta, creacion_modificacion, onlyTags);
					} else {
						if (items[i].path.includes(aPath_base)) {
							var titulo = items[i].title;
							var description = '';
							if (items[i].description) {
								description = items[i].description;
								description = description.replace(/'/g, '');
							}

							var extension = items[i].name;
							extension = extension.split(/[\s.]+/);
							extension = '.' + extension[extension.length - 1];
							var ruta = items[i].path.replace(items[i].name, '');

							var customProperties = getProperties(items[i].path);
							var image = customProperties && customProperties.image ? customProperties.image : '//www.jetstereo.com/images/no_image.png';

							var tags_array = [];
							var tags = [];
							if (customProperties && customProperties.tags) {
								tags = customProperties.tags;
								tags_array = customProperties.tags;
							}

							tags = arrayUnique(tags);
							for (var n = 0; n < tags_array.length; n++) {
								if (tags[n] != '' && tags[n] != 'undefined') {
									tags[n] = '<span class="element-tag" onClick="accion_buscar(this.innerText)">' + tags[n] + '</span>';
								}
							}
							if (tags_array[0]) {
								icon = 'fa-pencil-square-o';
								tags = tags.toString();
								tags = tags.replace(/,/g, '');
								tags = tags.replace(/<span class="element-tag" onclick="accion_buscar(this.innerText)"> <\/span>/g, '');
							} else {
								icon = 'fa-plus';
							}

							if (palabra == undefined) {
								palabra = '';
							}
							if (
								(titulo.toLowerCase().includes(palabra.toLowerCase()) && !onlyTags) ||
								tags
									.toString()
									.toLowerCase()
									.includes(palabra.toLowerCase()) ||
								(description.toLowerCase().includes(palabra.toLowerCase()) && !onlyTags)
							) {
								if (extension == '.xjpivot') {
									url = '/' + contexto_pentaho + visor_olap + '?solution=&path=' + items[i].path + '&action=' + items[i].name;
									if (fecha_filtro_ms >= desde && fecha_filtro_ms <= hasta) {
										// prettier-ignore
										$('#repository_browser_buscar').append(
											"<div class='col-xs-12 col-md-6 buscar_olap resultado_buscar'>" +
												"<div class='col-md-3 col-lg-2'>" +
													"<img class='thumb_result' src='" + image + "'>" +
												"</div>" +
												"<div class='col-md-9 col-lg-10'>" +
													"<div id='" + aPath.replace(/\//g, ':') + ':' + items[i].name + "'>" +
														"<div data-sort='" + titulo + "' class='col-xs-11' style='padding-left: 0;'>" +
															"<img data-sort='olap' class='type_result' src='../themes/stratebi/images/stpivot_icon_c.png'>" +
															"<a class='titulos' href='#' " +
																"onClick=\"parent.mantle_setPerspective('opened.perspective'); window.parent.openURL('" + titulo + "','" + titulo + "','" + url + '\')"> ' +
																titulo +
															"</a>" +
														"</div>" +
														"<div class='col-xs-1' style='padding-left: 0;'>" +
														"<span onClick='toggleFavorite(this, \"" + items[i].path + "\",\"" + titulo + "\")' class=\"button favorite-button\"><i class=\"fa " + getFavoriteIconState(items[i].path) + " fa-lg\" aria-hidden=\"true\"></i></span>" + 
														"</div>" +
														"<div class='col-md-12 description' style='padding-left: 0;'>" +
															description +
														"</div>" +
														"<div class='col-md-12 tags' style='color: grey;'>" +
															"<i class='fa " + icon + "' aria-hidden='true' " +
																" onClick='funcion_tags(\"" + titulo + "\",\"" + description + "\",\"" + image + "\",\"" + aPath + "\",\"" + items[i].name + "\",\"" + tags_array + "\")'>" +
															"</i>" +
															tags +
														"</div>" +
													"</div>" +
												"</div>" +
											"</div>"
										);
									}
								}
								if (extension == '.std') {
									url = items[i].path;
									url = encodeURIComponent(url);
									url = url.replace(/%2F/g, '%3A');
									url = '/' + contexto_pentaho + '/api/repos/' + url + '/generatedContent';
									if (fecha_filtro_ms >= desde && fecha_filtro_ms <= hasta) {
										// prettier-ignore
										$('#repository_browser_buscar').append(
											"<div class='col-xs-12 col-md-6 buscar_dashboard resultado_buscar'>" +
												"<div class='col-md-3 col-lg-2'>" +
													"<img class='thumb_result' src='" + image + "'>" +
												"</div>" +
												"<div class='col-md-9 col-lg-10'>" +
													"<div id='" + aPath.replace(/\//g, ':') + ':' + items[i].name + "'>" +
														"<div data-sort='" + titulo + "' class='col-xs-11' style='padding-left: 0;'>" +
															"<img data-sort='dashboard' class='type_result' src='../themes/stratebi/images/stdashboard_icon_c.png'>" +
															"<a class='titulos' href='#' " +
																"onClick=\"parent.mantle_setPerspective('opened.perspective'); window.parent.openURL('" + titulo + "','" + titulo + "','" + url + '\')"> ' +
																titulo +
															"</a>" +
														"</div>" +
														"<div class='col-xs-1' style='padding-left: 0;'>" +
														"<span onClick='toggleFavorite(this, \"" + items[i].path + "\",\"" + titulo + "\")' class=\"button favorite-button\"><i class=\"fa " + getFavoriteIconState(items[i].path) + " fa-lg\" aria-hidden=\"true\"></i></span>" + 
														"</div>" +
														"<div class='col-md-12 description' style='padding-left: 0;'>" +
															description +
														"</div>" +
														"<div class='col-md-12 tags' style='color: grey;'>" +
															"<i class='fa " + icon + "' aria-hidden='true' " +
																" onClick='funcion_tags(\"" + titulo + "\",\"" + description + "\",\"" + image + "\",\"" + aPath + "\",\"" + items[i].name + "\",\"" + tags_array + "\")'>" +
															"</i>" +
															tags +
														"</div>" +
													"</div>" +
												"</div>" +
											"</div>"
										);
									}
								}
								if (extension == '.prpt' || extension == '.adhoc') {
									url = items[i].path;
									url = encodeURIComponent(url);
									url = url.replace(/%2F/g, '%3A');
									url = '/' + contexto_pentaho + '/api/repos/' + url + '/generatedContent';
									if (fecha_filtro_ms >= desde && fecha_filtro_ms <= hasta) {
										// prettier-ignore
										$('#repository_browser_buscar').append(
											"<div class='col-xs-12 col-md-6 buscar_report resultado_buscar'>" +
												"<div class='col-md-3 col-lg-2'>" +
													"<img class='thumb_result' src='" + image + "'>" +
												"</div>" +
												"<div class='col-md-9 col-lg-10'>" +
													"<div id='" + aPath.replace(/\//g, ':') + ':' + items[i].name + "'>" +
														"<div data-sort='" + titulo + "' class='col-xs-11' style='padding-left: 0;'>" +
															"<img data-sort='report' class='type_result' src='../themes/stratebi/images/streport_icon_c.png'>" +
															"<a class='titulos' href='#' " +
																"onClick=\"parent.mantle_setPerspective('opened.perspective'); window.parent.openURL('" + titulo + "','" + titulo + "','" + url + '\')"> ' +
																titulo +
															"</a>" +
														"</div>" +
														"<div class='col-xs-1' style='padding-left: 0;'>" +
														"<span onClick='toggleFavorite(this, \"" + items[i].path + "\",\"" + titulo + "\")' class=\"button favorite-button\"><i class=\"fa " + getFavoriteIconState(items[i].path) + " fa-lg\" aria-hidden=\"true\"></i></span>" + 
														"</div>" +
														"<div class='col-md-12 description' style='padding-left: 0;'>" +
															description +
														"</div>" +
														"<div class='col-md-12 tags' style='color: grey;'>" +
															"<i class='fa " + icon + "' aria-hidden='true' " +
																" onClick='funcion_tags(\"" + titulo + "\",\"" + description + "\",\"" + image + "\",\"" + aPath + "\",\"" + items[i].name + "\",\"" + tags_array + "\")'>" +
															"</i>" +
															tags +
														"</div>" +
													"</div>" +
												"</div>" +
											"</div>"
										);
									}
								}
								if (extension == '.sta') {
									url = '/' + contexto_pentaho + '/content/stagile/ui/index.html#!/view/' + items[i].path.replace(/\//g, ':');
									if (fecha_filtro_ms >= desde && fecha_filtro_ms <= hasta) {
										// prettier-ignore
										$('#repository_browser_buscar').append(
											"<div class='col-xs-12 col-md-6 buscar_stagile resultado_buscar'>" +
												"<div class='col-md-3 col-lg-2'>" +
													"<img class='thumb_result' src='" + image + "'>" +
												"</div>" +
												"<div class='col-md-9 col-lg-10'>" +
													"<div id='" + aPath.replace(/\//g, ':') + ':' + items[i].name + "'>" +
														"<div data-sort='" + titulo + "' class='col-xs-11' style='padding-left: 0;'>" +
															"<img data-sort='stagile' class='type_result' src='../themes/stratebi/images/stagile_icon_c.png'>" +
															"<a class='titulos' href='#' " +
																"onClick=\"parent.mantle_setPerspective('opened.perspective'); window.parent.openURL('" + titulo + "','" + titulo + "','" + url + '\')"> ' +
																titulo +
															"</a>" +
														"</div>" +
														"<div class='col-xs-1' style='padding-left: 0;'>" +
														"<span onClick='toggleFavorite(this, \"" + items[i].path + "\",\"" + titulo + "\")' class=\"button favorite-button\"><i class=\"fa " + getFavoriteIconState(items[i].path) + " fa-lg\" aria-hidden=\"true\"></i></span>" + 
														"</div>" +
														"<div class='col-md-12 description' style='padding-left: 0;'>" +
															description +
														"</div>" +
														"<div class='col-md-12 tags' style='color: grey;'>" +
															"<i class='fa " + icon + "' aria-hidden='true' " +
																" onClick='funcion_tags(\"" + titulo + "\",\"" + description + "\",\"" + image + "\",\"" + aPath + "\",\"" + items[i].name + "\",\"" + tags_array + "\")'>" +
															"</i>" +
															tags +
														"</div>" +
													"</div>" +
												"</div>" +
											"</div>"
										);
									}
								}
								if (extension == '.wcdf') {
									url = items[i].path;
									url = encodeURIComponent(url);
									url = url.replace(/%2F/g, '%3A');
									url = '/' + contexto_pentaho + '/api/repos/' + url + '/generatedContent';
									if (fecha_filtro_ms >= desde && fecha_filtro_ms <= hasta) {
										// prettier-ignore
										$('#repository_browser_buscar').append(
											"<div class='col-xs-12 col-md-6 buscar_cde resultado_buscar'>" +
												"<div class='col-md-3 col-lg-2'>" +
													"<img class='thumb_result' src='" + image + "'>" +
												"</div>" +
												"<div class='col-md-9 col-lg-10'>" +
													"<div id='" + aPath.replace(/\//g, ':') + ':' + items[i].name + "'>" +
														"<div data-sort='" + titulo + "' class='col-xs-11' style='padding-left: 0;'>" +
															"<img data-sort='cde' class='type_result' src='../themes/stratebi/images/cde_icon_c.png'>" +
															"<a class='titulos' href='#' " +
																"onClick=\"parent.mantle_setPerspective('opened.perspective'); window.parent.openURL('" + titulo + "','" + titulo + "','" + url + '\')"> ' +
																titulo +
															"</a>" +
														"</div>" +
														"<div class='col-xs-1' style='padding-left: 0;'>" +
														"<span onClick='toggleFavorite(this, \"" + items[i].path + "\",\"" + titulo + "\")' class=\"button favorite-button\"><i class=\"fa " + getFavoriteIconState(items[i].path) + " fa-lg\" aria-hidden=\"true\"></i></span>" + 
														"</div>" +
														"<div class='col-md-12 description' style='padding-left: 0;'>" +
															description +
														"</div>" +
														"<div class='col-md-12 tags' style='color: grey;'>" +
															"<i class='fa " + icon + "' aria-hidden='true' " +
																" onClick='funcion_tags(\"" + titulo + "\",\"" + description + "\",\"" + image + "\",\"" + aPath + "\",\"" + items[i].name + "\",\"" + tags_array + "\")'>" +
															"</i>" +
															tags +
														"</div>" +
													"</div>" +
												"</div>" +
											"</div>"
										);
									}
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

function browsePath(aPath) {
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
				$('#repository_browser').empty();
				var items = r.repositoryFileDto;
				$('#path').empty();
				$('#path').html(
					"<div class='row'><span style='color: #666;border-bottom: solid 1px #ccc;margin: 0px 25px;width: calc(100% - 25px);display: block;padding-left:10px;padding-top: 12px;padding-bottom: 10px;' href='#' > / </span></div>"
				);

				for (var i = 0; i < items.length; i++) {
					if (i == 0 && items[0].path.split('/').length > 3) {
						var _a = $(
							"<div class='row'><a style='color: #666;border-bottom: solid 1px #ccc;margin: 0px 25px;width: calc(100% - 25px);display: block;padding: 0px 10px;padding-top: 10px;' href='#' onclick='return browsePath(\"" +
								items[0].path.replace(/^(\/.*)+(\/.*){2}$/, '$1') +
								"\")'><i style='color: " +
								color_principal +
								";margin-top: 10px;padding-bottom: 10px;font-size: 14px;margin-right: 5px;line-height: 10px;' class='fa fa-reply fa-lg fa-2x' /> " +
								items[0].path.replace(/^(\/public)(\/.*)+(\/.*){1}$/, '$2') +
								'</a></div>'
						);
						$('#repository_browser').append(_a);
						$('#path').html(_a);
						aPath_base = items[0].path.replace(/^(\/public)(\/.*)+(\/.*){1}$/, '$2'); //_a[0].innerText.replace(/^(\/public)(\/.*)+(\/.*){1}$/, "$2");//
					}
					if (i == 0 && items[0].path.split('/').length == 3) {
						aPath_base = '/public';
					}
					if (items[i].folder.toLowerCase() == 'true') {
						var _b = $(
							"<a class='col-lg-4' href='#' style='color:#333;' onclick='return browsePath(\"" +
								items[i].path +
								"\")'><i style='color:" +
								color_principal +
								";margin-top:15px' class='fa fa-folder-open fa-2x' /> " +
								items[i].title +
								'</a></div>'
						);
						$('#repository_browser').append(_b);
					} else {
						var _class = 'cde_icon_c';
						var _class_buscar = 'buscar_cde repository_style';
						if (/^.*\.adhoc$/.test(items[i].path) || /^.*\.prpt$/.test(items[i].path)) {
							_class = 'report_icon_c';
							_class_buscar = 'buscar_report repository_style';
						}
						if (/^.*\.std$/.test(items[i].path)) {
							_class = 'stdashboard_icon_c';
							_class_buscar = 'buscar_dashboard repository_style';
						}
						if (/^.*\.xjpivot$/.test(items[i].path)) {
							_class = 'stpivot_icon_c';
							_class_buscar = 'buscar_olap repository_style';
						}
						if (/^.*\.sta$/.test(items[i].path)) {
							_class = 'stagile_icon_c';
							_class_buscar = 'buscar_stagile repository_style';
						}

						var _c = $(
							"<a class='col-lg-4 " +
								_class_buscar +
								"' href='#' style='color:#333;margin-top: 20px;' onclick='return openSolution(\"" +
								items[i].title +
								'","' +
								new Date().getTime() +
								'","' +
								items[i].path +
								"\")'><img src='../themes/stratebi/images/" +
								_class +
								".png'><span>" +
								items[i].title +
								'</span></a>'
						);
						$('#repository_browser').append(_c);
					}
				}
				filtro_extension(items[i]);
			}
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

function getFavorites() {
	favoritesList = [];

	$.ajax({
		async: false,
		url: '../../api/user-settings/favorites',
		dataType: 'json',
		success: function(response) {
			if (response) {
				favoritesList = response;
			}
		}
	});
}

function getFavoriteIconState(path) {
	for (let i in favoritesList) {
		if (favoritesList[i]['fullPath'] == path) {
			return 'fa-star';
		}
	}

	return 'fa-star-o';
}

function toggleFavorite(favButton, path, title) {
	var icon = favButton.querySelector('i');

	if (icon.classList.contains('fa-star-o')) {
		window.parent.mantle_addFavorite(path, title);
	} else {
		window.parent.mantle_removeFavorite(path);
	}

	icon.classList.toggle('fa-star');
	icon.classList.toggle('fa-star-o');
}

function setHeader(tag, onlyTags) {
	var tagHeader = document.querySelector('#tag-header');

	document.querySelector('#filter-panel').style.display = onlyTags ? 'none' : 'block';
	tagHeader.style.display = onlyTags ? 'flex' : 'none';

	if (onlyTags) {
		var tagValue = tag.toLowerCase().replace(' ', '-');
		var tagClass = tagValue + '-tag';
		var backgroundClass = tagValue + '-background';

		tagHeader.className = '';
		tagHeader.classList.add(backgroundClass);

		var imageNode = tagHeader.querySelector('#tag-header .backgroundBI > img');
		imageNode.title = 'Big Data ' + tag + ' - BI ' + tag;
		imageNode.src = '../themes/stratebi/images/verticales/' + tagValue + '.svg';

		var subtitleNode = tagHeader.querySelector('#tag-header .subtitleBI');
		subtitleNode.textContent = tag;
		subtitleNode.className = '';
		subtitleNode.classList.add('subtitleBI');
		subtitleNode.classList.add(tagClass);
	}
}
