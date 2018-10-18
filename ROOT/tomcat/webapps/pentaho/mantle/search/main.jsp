<!DOCTYPE html>
<%@page pageEncoding="UTF-8" %>
<%@page session="true"  
	contentType="text/html; charset=UTF-8"
    language="java"
    import="org.apache.commons.lang.StringUtils,
            org.owasp.encoder.Encode,
            org.pentaho.platform.util.messages.LocaleHelper,
            java.net.URL,
            java.net.URLClassLoader,
            java.util.ArrayList,
            java.util.Iterator,
            java.util.HashMap,
            java.util.LinkedHashMap,
            java.util.List,
            java.util.Locale,
            java.util.Map,
            java.util.ResourceBundle,
            org.pentaho.platform.api.engine.IAuthorizationPolicy,
            org.pentaho.platform.api.engine.IPluginManager,
            org.pentaho.platform.api.usersettings.IUserSettingService,
            org.pentaho.platform.api.usersettings.pojo.IUserSetting,
            org.pentaho.platform.engine.core.system.PentahoSessionHolder,
            org.pentaho.platform.engine.core.system.PentahoSystem,
            org.pentaho.platform.security.policy.rolebased.actions.AdministerSecurityAction,
            org.pentaho.platform.security.policy.rolebased.actions.RepositoryReadAction,
            org.pentaho.platform.security.policy.rolebased.actions.RepositoryCreateAction"%>

<%@ include file="./../fragments/Settings.jspf" %>

<html lang="<%= effectiveLocale.getLanguage() %>">
<head>
	<meta charset="UTF-8"/>
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>STSearch</title>
	
	<!-- Styles -->
	<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Titillium+Web">
	<link rel="stylesheet" href="../themes/stratebi/vendor/font-awesome/css/font-awesome.min.css">
	<link rel="stylesheet" href="../themes/stratebi/vendor/jquery-ui/css/jquery-ui.min.css">
	<link rel="stylesheet" href="../themes/stratebi/vendor/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="../themes/stratebi/vendor/bootstrap-datepicker/css/bootstrap-datepicker.min.css" />
	<link rel="stylesheet" href="../themes/stratebi/vendor/bootstrap-tagsinput/css/bootstrap-tagsinput.css" />
	<link rel="stylesheet" href="css/home.css">
	<link rel="stylesheet" href="css/main.css">
	<link href="http://aehlke.github.io/tag-it/css/jquery.tagit.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="css/buscador.css">
	
	<!-- Javascript -->
	<script type="text/javascript" src="../themes/stratebi/vendor/jquery/js/jquery-3.3.1.min.js"></script>
	<script type="text/javascript" src="../themes/stratebi/vendor/jquery-ui/js/jquery-ui.min.js"></script>
	<script type="text/javascript" src="../themes/stratebi/vendor/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="../themes/stratebi/vendor/bootstrap-datepicker/js/bootstrap-datepicker.min.js"></script>
	<script type="text/javascript" src="../themes/stratebi/vendor/bootstrap-datepicker/js/locales/bootstrap-datepicker.es.min.js"></script>
	<script type="text/javascript" src="../themes/stratebi/vendor/bootstrap-datepicker/js/locales/bootstrap-datepicker.ca.min.js"></script>
	<script type="text/javascript" src="../themes/stratebi/vendor/bootstrap-tagsinput/js/bootstrap-tagsinput.min.js"></script>
	<script type="text/javascript" src="js/tag-it.min.js"></script>
	<script type="text/javascript" src="js/buscador.js"></script>
	
	<!-- Require Home -->
	<script type="text/javascript">
		$( document ).ready(function() {
			var palabra = $('#buscador', window.parent.document)[0].value;
			accion_buscar(palabra);
			declaracion("<%= effectiveLocale.getLanguage() %>");

			/*
			var iframe = window.top;
			iframe.find("#buscador").on('keyup', function(e) {
				if (e.keyCode == 13) {
					var palabra = $('#buscador', window.parent.document)[0].value;
					accion_buscar(palabra);
				}
			});
			
			buscar( "/public", "/public", "");
			*/
			
			$("#dialog").dialog({
				autoOpen: false
			});
			
			$("#dialog").dialog( "option", "width", "75%");
			$("#dialog").dialog('option', 'position', 'center');
			$("#dialog").parent().css( "display", "none" );	
			$("#dialog").parent().css( "position", "fixed" );

			$('.ui-icon-closethick').on('click', function(e) {
				$( "#dialog" ).parent().fadeOut();
				$('#tags_actuales').empty();    
				$("#modal-tag").val("");
				$("#dialog-bg").css("z-index",-1);$("#dialog-bg").css("opacity",0);
			});

			$('#exit_dialog').on('click', function(e) {
				$( "#dialog" ).parent().fadeOut();
				$("#dialog-bg").css("z-index",-1);$("#dialog-bg").css("opacity",0);
				$('#tags_actuales').empty();    
				$("#modal-tag").val("");
			});

			/*$('#tag-input').tagsinput({
				tagClass: 'element-tag'
			});*/
			
			$("#modal-tag").tagit({maxLength: 3});
			/*autocomplete: {delay: 0, minLength: 2},
			availableTags: ["c++", "java", "php", "javascript","javaasdasdscript","javascripasdasdsadt", "ruby", "python", "c"]*/

			$('#set_info').on('click', function(e) {
				$('#dialog .tagit-choice').remove();    
				set_info("false");
				accion_buscar();
				$("#dialog").parent().fadeOut();
				$("#dialog-bg").css("z-index",-1);$("#dialog-bg").css("opacity",0);
			});

			$('#borrar_tags').on('click', function(e) {
				$('#tags_actuales').empty();    
				$("#modal-tag").val("");
				$('.bootstrap-tagsinput .tag').remove();
				$('#tagit-choice ui-widget-content ui-state-default ui-corner-all tagit-choice-editable').remove();
				set_info("true");
				accion_buscar();
				$("#dialog").parent().fadeOut();
				$("#dialog-bg").css("z-index",-1);$("#dialog-bg").css("opacity",0);
			});

			$('.first_tag_element').on('click', function(e) {
				accion_buscar();
			});

			$('#clear_info').click(function(){
				$('#dialog .tagit-choice').remove();
				$("#dialog").parent().fadeOut();
				$("#dialog-bg").css("z-index",-1);$("#dialog-bg").css("opacity",0);

			});
		});
	</script>

	<style>
	    body {
			padding-bottom: 40px;
			font-family: 'Titillium Web', sans-serif;
		}

		.popup {
			height: 100%;
			width: 100%;
			position: fixed;
			background: rgba(0,0,0,0.5);
			z-index: 110;
			display:table;
			top:0;
		}

		.alert.warning {
			padding: 30px;
			padding-left: 67px;
			border-radius: 5px;
			border-color: #DDD;
			border-width: 1px;
			border-style: solid;
			width: 640px;
		}

		.closebtn {
			margin-left: 15px;
			color: red;
			font-weight: bold;
			float: right;
			font-size: 22px;
			line-height: 20px;
			cursor: pointer;
			transition: 0.3s;
		}

		.closebtn:hover {
			color: black;
		}

		.alert {
			padding: 20px;
			background-color: #f44336;
			color: red;
			opacity: 1;
			transition: opacity 0.6s;
			margin-bottom: 15px;
			display: none;
			position: fixed;
			z-index: 120;
		}

		.column0 img {
			height: 30px;
		}

		a {
			height: 45px;
			margin-top: 5px;
		}

		.resultado_buscar {
			padding: 15px 15px 40px 15px;
			min-height: 140px;
			margin-top: 25px;
		}

		.col-md-12.title,.col-md-10.title {
			border-left: 6px #ccc solid;
			margin-top: 15px;
			margin-bottom: 10px;
			font-weight: bold;
			color: #666;
			background: #f6f6f6;
			padding-top: 5px;
			padding-bottom: 5px;
		}

		#dialog-bg {
			background: rgb(0,0,0);
			width: 100%;
			height: 100vh;
			position: fixed;
			z-index: -1;
			opacity:0;
			transition: opacity 0.3s;
		
		}

		#borrar_tags {
			width: 25px;
			position: absolute;
			right: -10px;
			margin: 10px;
			top: -8px;
			padding: 3px;
		}
	</style>
</head>

<body>
    <div id="dialog-bg"></div>
	<div id="mainPanel" class="row">

		<!-- Warning Dialog -->
		<div id="error_msg" class="alert warning">
			<span class="closebtn">&times;</span>
			<strong>No se han actualizado los Tags!</strong> Este usuario no tiene permisos de edici&oacute;n.
		</div>

		<!-- File Metadata -->
		<div id="dialog" title="Dialog Form">

			<!-- First column -->
			<div class="col-md-4 col-xs-12">
				<div style="text-align:center">
					<img id="modal-imagen-thumb" src="https://www.jetstereo.com/images/no_image.png" style="max-height:230px;max-width:300px;margin-top: 15px;">
				</div>
				<div class="row">
					<div class="col-md-10 col-md-offset-1 title" style="margin-bottom: 50px;">
						<span><%= customProperties.getString("tags") %><input id="borrar_tags" value="X" title="<%= customProperties.getString("deleteTags") %>" type="button" style="background: #da6666;"></span>
					</div>            
				</div>            
				<div id="tags_actuales"></div>
			</div>
			
			<!-- Second column -->
			<div class="col-md-8 col-xs-12">
				<form action="" method="post">
					<div class="row hidden-xs hidden-sm"><div class="col-md-6"><div class="col-md-12 title"><%= customProperties.getString("file") %></div><div class="col-md-12" id="modal_archivo"></div></div><div class="col-md-6"><div class="col-md-12 title"><%= customProperties.getString("path") %></div><div class="col-md-12" id="modal_ruta"></div></div></div>
					<div class="row hidden-xs hidden-sm"><div class="col-md-12 title"><%= customProperties.getString("title") %></div><div class="col-md-12"><input id="modal-titulo" name="titulo" type="text"></div></div>
					<div class="row hidden-xs hidden-sm"><div class="col-md-12 title"><%= customProperties.getString("image") %></div><div class="col-md-12"><input id="modal-imagen" name="imagen" type="text"></div></div>
					<div class="row hidden-xs hidden-sm"><div class="col-md-12 title"><%= customProperties.getString("description") %></div><div class="col-md-12"><textarea id="modal-descripcion" name="descripcion" rows="5"></textarea></div></div>
					<div class="row">
						<div class="col-md-12 title"><%= customProperties.getString("addTags") %></div>
						<div class="col-md-12">
							<input id="modal-tag" name="tag" type="text">
						</div>
					</div>
      
					<div class="row" style="border-bottom:0px #ccc solid">
						<div class="col-md-6 col-xs-6"></div>
						<div class="col-md-6 col-xs-6" style="text-align:right; float:right">
							<input id="set_info" value="<%= customProperties.getString("ok") %>" type="button" style="width:100px" >                
							<input id="exit_dialog" value="<%= customProperties.getString("cancel") %>" type="button" style="width:100px">
						</div>
					</div>
				</form>
			</div>
			
		</div>

		<!-- Search Header -->
		<div class="row">
		
			<!-- Title -->
			<div class="col-md-12" style='padding: 0px 30px;'> 
				<h2 class="header"><%= customProperties.getString("results") %></h2>
			</div>
			
			<!-- Filters -->
			<div class="col-md-12">
				<div class="col-md-2 search-filter-box">
					<div id="datepicker_desde" class="input-group date">
						<input type='text' class="form-control" placeholder="<%= customProperties.getString("startDate") %>" style="border: 1px #ccc solid !important;" />
						<span class="input-group-addon"><span class="fa fa-calendar"></span></span>
					</div>
				</div>
				
				<div class="col-md-2 search-filter-box">
					<div id="datepicker_hasta" class="input-group date">
						<input type='text' class="form-control" placeholder="<%= customProperties.getString("endDate") %>" style="border: 1px #ccc solid !important;"/>
						<span class="input-group-addon"><span class="fa fa-calendar"></span></span>
					</div>
				</div>
				
				<div class="col-md-2 search-filter-box">
					<select id="datepicker_filtro" class="input-group">
						<option value="creacion"><%= customProperties.getString("creationDate") %></option>
						<option value="modificacion"><%= customProperties.getString("modificationDate") %></option>
					</select>
				</div>
				
				<div class="col-md-3 search-filter-box">
					<div style="background:#f6f6f6; text-align:center;border: 1px #ccc solid;font-size: 25px;color: #666;width: auto;">
						<img id="filtro_olap" title="<%= customProperties.getString("olapViews") %>" class="fa fa-cube filtro_activo" src="../themes/stratebi/images/stpivot_icon_c.png" onclick="filtro_extension(this);">
						<img id="filtro_report" title="<%= customProperties.getString("reports") %>" class="fa fa-file-o filtro_activo" src="../themes/stratebi/images/streport_icon_c.png" onclick="filtro_extension(this);">
						<img id="filtro_dashboard" title="STDashboard CDM" class="fa fa-pie-chart filtro_activo" src="../themes/stratebi/images/stdashboard_icon_c.png" onclick="filtro_extension(this);">
						<img id="filtro_stagile" title="STAgile CDM" class="fa fa-file-o filtro_activo" src="../themes/stratebi/images/stagile_icon_c.png" onclick="filtro_extension(this);">
						<img id="filtro_cde" title="Custom CDM" class="fa fa-file-o filtro_activo" src="../themes/stratebi/images/cde_icon_c.png" onclick="filtro_extension(this);">
					</div>
				</div>
          
				<div class="col-md-3 search-filter-box">
					<div class="pull-right">
						<span class="search-form-btn input-group-addon" id="reset_filters" onClick="reset_filters();">
							<span class="fa fa-refresh"></span>
						</span>
						<span class="search-form-btn input-group-addon" id="show_repository" onClick="reset();">
							<span class="fa fa-folder"></span>
						</span>
					</div>
				</div>
			</div>
			
		</div>

		<!-- Loading icon -->
		<div id="cargando"></div>

		<!-- Folder selector -->
		<div class="row" style="margin-top:5px;margin-left:initial">  
			<div id="path" class="row container" style="width:100%"> </div>
			<div id="repository_browser" class="row container" style="width:100%"> </div>
			<div id="repository_browser_buscar" class="row container" style="width:100%"> </div>
		</div>
		
	</div>
</body>
</html>

