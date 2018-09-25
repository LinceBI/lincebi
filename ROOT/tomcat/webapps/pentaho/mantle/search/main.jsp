<!DOCTYPE html>
<%@ page session="true"  
    contentType="text/html; charset=UTF-8" 
    language="java"
    import="javax.naming.*, java.sql.*, javax.sql.*, java.util.*, java.sql.Connection, java.sql.ResultSet, java.sql.SQLException, java.sql.Connection, java.sql.Statement, java.util.List, org.json.simple.JSONObject, org.json.simple.JSONArray"
    %>
<%@page import="org.pentaho.platform.api.engine.IAuthorizationPolicy" %>
<%@page import="org.pentaho.platform.api.engine.IPluginManager" %>
<%@ page import=" org.pentaho.platform.api.engine.IPentahoSession" %>
<%@ page import=" org.pentaho.platform.web.http.PentahoHttpSessionHelper" %>
<%@page import="org.pentaho.platform.engine.core.system.PentahoSessionHolder" %>
<%@page import="org.pentaho.platform.engine.core.system.PentahoSystem" %>
<%@page import="org.pentaho.platform.security.policy.rolebased.actions.AdministerSecurityAction" %>
<%@page import="org.pentaho.platform.security.policy.rolebased.actions.RepositoryCreateAction" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Locale"%>
<%@page import="javax.servlet.http.HttpServletRequest"%> 
<%
boolean canCreateContent = PentahoSystem.get(IAuthorizationPolicy.class, PentahoSessionHolder.getSession()).isAllowed(RepositoryCreateAction.NAME);
boolean canAdminister = PentahoSystem.get(IAuthorizationPolicy.class, PentahoSessionHolder.getSession()).isAllowed(AdministerSecurityAction.NAME);
List<String> pluginIds = PentahoSystem.get(IPluginManager.class, PentahoSessionHolder.getSession()).getRegisteredPlugins();
IPentahoSession sess = PentahoSessionHolder.getSession();
String usuario =  sess.getName();


%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>LinceBI</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href='http://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700,800' rel='stylesheet' type='text/css'>
  <link rel="stylesheet" href="css/main.css">
  <link rel="stylesheet" href="css/bootstrap.css">
  <link href="css/font-awesome/css/font-awesome.min.css" rel="stylesheet">


  <!-- Le styles -->
  <link href="css/home.css" rel="stylesheet">

  <!-- We need web context for requirejs and css -->
  <script type="text/javascript" src="webcontext.js?context=mantle&cssOnly=true"></script>
  <!--<script language='JavaScript' type='text/javascript' src='http://admin.brightcove.com/js/BrightcoveExperiences.js'></script>-->

  <!-- Avoid 'console' errors in browsers that lack a console. -->
  <script type="text/javascript">
    if (!(window.console && console.log)) {
      (function() {
        var noop = function() {};
        var methods = ['assert', 'debug', 'error', 'info', 'log', 'trace', 'warn'];
        var length = methods.length;
        var console = window.console = {};
        while (length--) {
          console[methods[length]] = noop;
        }
      }());
    }
  </script>
  <link rel="stylesheet" href="css/bootstrap.css">
  <link href='http://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700,800' rel='stylesheet' type='text/css'>
  <link href="css/font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/css/datepicker.min.css" />
<link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/flick/jquery-ui.css">
<link href="http://aehlke.github.io/tag-it/css/jquery.tagit.css" rel="stylesheet" type="text/css">
  <link rel="stylesheet" href="css/buscador.css">

  <script type="text/javascript" src="js/jquery-1.9.1.min.js"></script>
  <script type="text/javascript" src="js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
  <script src="http://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/js/bootstrap-datepicker.min.js"></script>
    <script type="text/javascript" src="js/tag-it.min.js"></script>

  <script type="text/javascript" src="js/buscador.js"></script>
  <!-- Require Home -->
  <script type="text/javascript">


$( document ).ready(function() {
  var palabra = $('#buscador', window.parent.document)[0].value;
  accion_buscar(palabra);
  declaracion();


  /*var iframe = window.top;
  iframe.find("#buscador").on('keyup', function(e) {
  if (e.keyCode == 13) {
      var palabra = $('#buscador', window.parent.document)[0].value;
      accion_buscar(palabra);
      
  }
  });*/
  //buscar( "/public", "/public", "");
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

  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<style>
	    body {
      padding-bottom: 40px;
    }
    .popup{
      height: 100%;
      width: 100%;
      position: fixed;
      background: rgba(0,0,0,0.5);
      z-index: 999;
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
    width: 640px;}

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
.alert{
    padding: 20px;
    background-color: #f44336;
    color: red;
    opacity: 1;
    transition: opacity 0.6s;
    margin-bottom: 15px;
    display: none;
    position: fixed;
    z-index: 9999;
}
.column0 img{
  height:30px;
}
a{
  height: 45px;
  margin-top: 5px;
}
.resultado_buscar{
  padding:15px;
  min-height: 140px;
  margin-top:25px;
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
#borrar_tags{
width: 25px;
    position: absolute;
    right: -10px;
    margin: 10px;
    top: -8px;
    padding: 3px;
    }
  </style>
</head>


<body style="overflow:auto">
      <div id="dialog-bg"></div>

    <div id="mainPanel" class="row">
    <div id="error_msg" class="alert warning">
    <span class="closebtn">&times;</span>
    <strong>No se han actualizado los Tags!</strong> Este usuario no tiene permisos de edici&oacute;n.
  </div>
      <div id="dialog" title="Dialog Form">
      <div class="col-md-4 col-xs-12">
            <div style="text-align:center"><img id="modal-imagen-thumb" src="https://www.jetstereo.com/images/no_image.png" style="max-height:230px;max-width:300px;margin-top: 15px;"></div>
            <div class="row">
              <div class="col-md-10 col-md-offset-1 title" style="margin-bottom: 50px;">
                  <span>Tags<input id="borrar_tags" value="X" title="Borrar Tags" type="button" style="background: #da6666;"></span>
              </div>
                         
            </div>            
          <div id="tags_actuales"></div>
      </div>
      <div class="col-md-8 col-xs-12">
          <form action="" method="post">
          <div class="row hidden-xs hidden-sm"><div class="col-md-6"><div class="col-md-12 title">File</div><div class="col-md-12" id="modal_archivo"></div></div><div class="col-md-6"><div class="col-md-12 title">Path</div><div class="col-md-12" id="modal_ruta"></div></div></div>
          <div class="row hidden-xs hidden-sm"><div class="col-md-12 title">Title</div><div class="col-md-12"><input id="modal-titulo" name="titulo" type="text"></div></div>
          <div class="row hidden-xs hidden-sm"><div class="col-md-12 title">Image</div><div class="col-md-12"><input id="modal-imagen" name="imagen" type="text"></div></div>
          <div class="row hidden-xs hidden-sm"><div class="col-md-12 title">Description</div><div class="col-md-12"><textarea id="modal-descripcion" name="descripcion" rows="5"></textarea></div></div>
          <div class="row">
            <div class="col-md-12 title">Add Tags</div>
            <div class="col-md-12">
                <input id="modal-tag" name="tag" type="text">
            </div>
          </div>
          <div class="row" style="border-bottom:0px #ccc solid">
                <div class="col-md-6 col-xs-6">
                </div>
                <div class="col-md-6 col-xs-6" style="text-align:right; float:right">
                      <input id="set_info" value="Aceptar" type="button" style="width:100px" >                
                      <input id="exit_dialog" value="Cancelar" type="button" style="width:100px">
                </div>
          </div>
          </form>
      </div>

    </div>

        <div class="row">
            <div class="col-md-12"  style='padding: 0px 30px;'> 
              <h2 class="header">Resultados</h2>
            </div>
            <div class="col-md-6">
                    <div id="datepicker_desde" class="col-md-4 input-group date">
                            <input type='text' class="form-control" placeholder="Desde" style="border: 1px #ccc solid !important;" />
                            <span class="input-group-addon">
                                <span class="fa fa-calendar"></span>
                            </span>                         
                    </div>
                    <div id="datepicker_hasta" class="col-md-4 input-group date">
                            <input type='text' class="form-control" placeholder="Hasta" style="border: 1px #ccc solid !important;"/>
                            <span class="input-group-addon">
                                <span class="fa fa-calendar"></span>
                            </span>
                    </div>
                    <div class="col-md-4">
                          <select id="datepicker_filtro" ><option value="creacion">Create date</option><option value="modificacion">Modified date</option></select>
                    </div>
            </div>

            <div class="col-md-4 col-xs-4" style=" background:#f6f6f6; text-align:center;border: 1px #ccc solid;font-size: 25px;color: #666;width: auto;float: right;margin-right: 15px;">
                <img id="filtro_olap" title="Vistas OLAP" class="fa fa-cube filtro_activo" src="../images/stpivot_icon_c.png" onclick="filtro_extension(this);">
                <img id="filtro_report" title="Informes" class="fa fa-file-o filtro_activo" src="../images/streport_icon_c.png" onclick="filtro_extension(this);">
                <img id="filtro_dashboard" title="STDashboard CDM" class="fa fa-pie-chart filtro_activo" src="../images/stdashboard_icon_c.png" onclick="filtro_extension(this);">
                <img id="filtro_stagile" title="STAgile CDM" class="fa fa-file-o filtro_activo" src="../images/stagile_icon_c.png" onclick="filtro_extension(this);">
                <img id="filtro_cde" title="Custom CDM" class="fa fa-file-o filtro_activo" src="../images/cde_icon_c.png" onclick="filtro_extension(this);">
            </div>
            <div class="col-md-1" style="  text-align:right;margin-left: -15px;float:right;">
              <span class="input-group-addon" id="reset_filters" onClick="reset_filters();">
                    <span class="fa fa-refresh"></span>
              </span>
            </div>            
            <div class="col-md-1" style="  text-align:right;float:right;">
              <span class="input-group-addon" id="show_repository" onClick="reset();">
                    <span class="fa fa-folder"></span>
              </span>
            </div>
        </div>

        <div id="cargando"></div>

        <div class="row" style="margin-top:5px;margin-left:initial">  
        
          <div id="path" class="row container" style="width:100%"> </div>
          <div id="repository_browser" class="row container" style="width:100%"> </div>
          <div id="repository_browser_buscar" class="row container" style="width:100%"> </div>
        </div>

                </div>
        </div>





</body>
</html>

