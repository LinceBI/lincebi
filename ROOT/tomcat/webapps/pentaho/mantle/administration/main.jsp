<!DOCTYPE html>
<%@page pageEncoding="UTF-8" %>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%@page import="org.owasp.esapi.ESAPI" %>
<%@page import="org.pentaho.platform.util.messages.LocaleHelper" %>
<%@page import="java.net.URL" %>
<%@page import="java.net.URLClassLoader" %>
<%@page import="java.util.Locale" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ResourceBundle" %>
<%@page import="org.pentaho.platform.engine.core.system.PentahoSystem" %>
<%@page import="org.pentaho.platform.api.engine.IPluginManager" %>
<%@page import="org.pentaho.platform.engine.core.system.PentahoSessionHolder" %>

<%
  boolean hasDataAccessPlugin = PentahoSystem.get( IPluginManager.class, PentahoSessionHolder.getSession() ).getRegisteredPlugins().contains( "data-access" );
%>

<%
  Locale effectiveLocale = request.getLocale();
  if ( !StringUtils.isEmpty( request.getParameter( "locale" ) ) ) {
    request.getSession().setAttribute( "locale_override", request.getParameter( "locale" ) );
    LocaleHelper.parseAndSetLocaleOverride( request.getParameter( "locale" ) );
  } else {
    request.getSession().setAttribute( "locale_override", null );
    LocaleHelper.setLocaleOverride( null );
  }

  URLClassLoader loader = new URLClassLoader( new URL[] { application.getResource( "/mantle/messages/" ) } );
  ResourceBundle properties = ResourceBundle.getBundle( "mantleMessages", request.getLocale(), loader );

%>
<html lang="en">
<head>
  <!--contenido del head del index html original-->
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>Administration - OKN</title>
  <meta name="description" content="">
  <meta name="viewport" content="width=device-width">
  <link href='http://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700,800' rel='stylesheet' type='text/css'>
      <link rel="stylesheet" href="../../css/bootstrap.min.css">
      <link rel="stylesheet" href="../../css/font-awesome/css/font-awesome.min.css">
      <script src="../../js/jquery-3.3.1.min.js"></script>
  <style>
         @font-face {
         font-family: 'Roboto';
         src:url('../../fonts/Roboto-Regular.ttf') format('truetype');
         font-weight: normal;
         font-style: normal;
         }
          body#fuente{
         font-family: 'Roboto' !important;
         background: #ccc !important;
         }  
    body {
      padding-top: 40px;
      padding-bottom: 40px;
    }   
    .box{
      border-top:1px #ccc solid;

    }
    .box .box_element{
      padding:20px;
            font-size: 20px !important;

    }
    .box i{
      margin-right: 20px;
            font-size: 30px !important;

    }
  .btn{
    display:block;
    color:#666;
    background: #f6f6f6;
    border-bottom:1px #ccc solid;
    padding:8px;
    text-align: left !important;
    cursor:pointer;
    padding-left: 0px;     
    -webkit-transition: all .5s ease;
    transition: all .5s ease;    
  }
  .btn:hover{
    background:#0cf;
    -webkit-transition: all .5s ease;
    transition: all .5s ease;   
    padding-left: 10px; 
    color:white !important;
  }
  </style>

<script type="text/javascript">

$(document).ready(function() {

});
</script>
</head>

<body style="overflow:auto">

<div class="col-md-10 col-md-offset-1">
<div class="col-md-4 col-md-offset-1">
<h2> Admin Operations</h2>
<div class="box row">
    <!--<div class="box_element  btn" onClick="parent.executeCommand('OpenFileCommand')">
        <i class="fa fa-sign-out" aria-hidden="true" style="float:left;margin-left:10px;margin-top: 3px;"></i><span>Open File</span>
    </div>    -->

    <div class="box_element  btn" onClick="window.top.mantle_setPerspective('schedules.perspective')">
        <i class="fa fa-calendar-check-o" aria-hidden="true" style="float:left;margin-left:10px;margin-top: 3px;"></i><span>Schedules</span>
    </div>    

    <div class="box_element  btn" onClick="window.parent.executeCommand('ManageDatasourcesCommand')">
        <i class="fa fa-database" aria-hidden="true" style="float:left;margin-left:10px;margin-top: 3px;"></i><span>Manage Data Sources</span>
    </div>  

    <div class="box_element  btn" onClick="window.top.mantle_setPerspective('admin.perspective')">
        <i class="fa fa-unlock-alt" aria-hidden="true" style="float:left;margin-left:10px;margin-top: 3px;"></i><span>BI Administration</span>
    </div>    

    <a href="http://pentaho.stratebi.com/pentaho/content/saiku-adhoc/web/statistics.html" target="_blank">
    <div class="box_element  btn">
        <i class="fa fa-file" aria-hidden="true" style="float:left;margin-left:10px;margin-top: 3px;"></i><span>STReport Statistics</span>
    </div>        
    </a>



       

</div>
</div>





<div class="col-md-4 col-md-offset-1">

<h2> Cache Operations</h2>
<div class="box row">
    <div class="box_element btn" onClick="parent.executeCommand('RefreshSystemSettingsCommand')">
        <i class="fa fa-refresh" aria-hidden="true" style="float:left;margin-left:10px;margin-top: 3px;"></i><span>Refresh System Settings</span>
    </div>
    <div class="box_element btn" onClick="parent.executeCommand('RefreshMetaDataCommand')">
        <i class="fa fa-refresh" aria-hidden="true" style="float:left;margin-left:10px;margin-top: 3px;"></i><span>Refresh Reporting Metadata</span>
    </div>
    <div class="box_element  btn" onClick="parent.executeCommand('ExecuteGlobalActionsCommand')">
        <i class="fa fa-refresh" aria-hidden="true" style="float:left;margin-left:10px;margin-top: 3px;"></i><span>Refresh Global Variables</span>
    </div>
    <div class="box_element btn" onClick="parent.executeCommand('PurgeMondrianSchemaCacheCommand')">
        <i class="fa fa-refresh" aria-hidden="true" style="float:left;margin-left:10px;margin-top: 3px;"></i><span>Refresh Reporting Data Cache</span>
    </div>            
    <div class="box_element btn" onClick="parent.executeCommand('PurgeReportingDataCacheCommand')">
        <i class="fa fa-refresh" aria-hidden="true" style="float:left;margin-left:10px;margin-top: 3px;"></i><span>Refresh CDA Cache</span>
    </div>                
</div>
</div>



</div>



</body>
</html>

