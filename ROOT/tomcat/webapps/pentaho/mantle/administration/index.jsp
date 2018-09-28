<!DOCTYPE html>
<%@page import="org.pentaho.platform.api.engine.IPluginManager"%>
<%@page import="org.pentaho.platform.security.policy.rolebased.actions.RepositoryCreateAction"%>
<%@page import="org.pentaho.platform.security.policy.rolebased.actions.AdministerSecurityAction"%>
<%@page import="org.pentaho.platform.engine.core.system.PentahoSessionHolder"%>
<%@page import="org.pentaho.platform.api.engine.IAuthorizationPolicy"%>
<%@page import="org.pentaho.platform.engine.core.system.PentahoSystem"%>
<%@page import="org.pentaho.platform.engine.core.system.PentahoSystem"%>
<%@page import="java.util.List"%>

<%
boolean canCreateContent = PentahoSystem.get(IAuthorizationPolicy.class, PentahoSessionHolder.getSession()).isAllowed(RepositoryCreateAction.NAME);
boolean canAdminister = PentahoSystem.get(IAuthorizationPolicy.class, PentahoSessionHolder.getSession()).isAllowed(AdministerSecurityAction.NAME);
List<String> pluginIds = PentahoSystem.get(IPluginManager.class, PentahoSessionHolder.getSession()).getRegisteredPlugins();
%>

<html lang="en" style="width:100%;height:100%;">
	<head>
		<meta charset="UTF-8"/>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
		<meta name="viewport" content="width=device-width,initial-scale=1">
		<link rel="stylesheet" href="../themes/stratebi/vendor/font-awesome/css/font-awesome.min.css">
		<script src="../themes/stratebi/vendor/bootstrap/js/bootstrap.min.js" async></script>
		<script src="../search/js/jquery-1.9.1.min.js"></script>
		<script src="js/translate.administracion.js"></script>
		<style>
			
			html,body {
			  padding-top: 9px;
			  padding-bottom: 40px;
			  background: transparent;
			  font-family: 'Titillium Web', sans-serif;
			}
			
			.box .box_element{
			  padding: 20px;
			  font-size: 14px !important;
			}
			
			.box i {
			  margin-right: 20px;
			  font-size: 15px !important;
			}
				
			.titulo {
			  margin-bottom: 45px;
			  font-weight: 100;
			  font-weight: bold;
			}
				
			.titulo i {
			  margin-left: 49px;
			  margin-right: 38px;
			  font-size: 35px !important;
			}
			
			.btn {
			  display: block;
			  color: #878787;
			  background: #f5f5f5;
			  border-bottom: 1px #ccc solid;
			  padding:8px;
			  text-align: left !important;
			  cursor: pointer;
			  padding-left: 0px;     
			}
			
			.btn:hover {
			  background: #f5f5f5;
			  padding-left: 10px;
			  color: black !important;
			  text-decoration: underline #f5f5f5;
			  border-right: 6px solid #7A9E3F; 
			  /* border-left: 6px solid #7A9E3F; */
			}
		</style>	
	</head>

	<body style="overflow:auto">
	
		<div class="row" style="width: 955px;margin: auto;background: white;border-radius: 5px; background: #f5f5f5; border-left: 10px #7A9E3F solid; box-shadow: 5px 10px 18px #888888;">
			<div class="col-md-5 col-xs-5">
				<div class="box row">
					<!--<div class="box_element  btn" onClick="parent.executeCommand('OpenFileCommand')">
						<i class="fa fa-sign-out" aria-hidden="true" style="float:left;margin-left:10px;margin-top: 3px;"></i><span>Open File</span>
					</div>    -->
					<div class="box_element  btn" style="border-top: 1px #ccc solid;" onClick="window.parent.executeCommand('ManageDatasourcesCommand')">
						<i class="fa fa-database" aria-hidden="true" style="float:left;margin-left:10px;margin-top: 3px;"></i><span id="mds">Manage Data Sources</span>
					</div>  
					<div class="box_element  btn" onClick="window.parent.mantle_setPerspective('admin.perspective')">
						<i class="fa fa-unlock-alt" aria-hidden="true" style="float:left;margin-left:10px;margin-top: 3px;"></i><span id="biAdministracion">BI Administration</span>
					</div>    
				</div>
			</div>

			<div class="col-md-5 col-xs-5 col-md-offset-2 col-xs-offset-2">
				<div class="box row">
					<div class="box_element btn" onClick="parent.executeCommand('RefreshSystemSettingsCommand')">
						<i class="fa fa-refresh" aria-hidden="true" style="float:left;margin-left:10px;margin-top: 3px;"></i><span id="refreshSystem">Refresh System Settings</span>
					</div>
					<div class="box_element btn" onClick="parent.executeCommand('RefreshMetaDataCommand')">
						<i class="fa fa-refresh" aria-hidden="true" style="float:left;margin-left:10px;margin-top: 3px;"></i><span id="refreshReportingMetadata">Refresh Reporting Metadata</span>
					</div>
					<div class="box_element  btn" onClick="parent.executeCommand('ExecuteGlobalActionsCommand')">
						<i class="fa fa-refresh" aria-hidden="true" style="float:left;margin-left:10px;margin-top: 3px;"></i><span id="refreshGlobal">Refresh Global Variables</span>
					</div>
					<div class="box_element btn" onClick="parent.executeCommand('PurgeMondrianSchemaCacheCommand')">
						<i class="fa fa-refresh" aria-hidden="true" style="float:left;margin-left:10px;margin-top: 3px;"></i><span id="refreshReportingData">Refresh Reporting Data Cache</span>
					</div>            
					<div class="box_element btn" onClick="parent.executeCommand('PurgeReportingDataCacheCommand')">
						<i class="fa fa-refresh" aria-hidden="true" style="float:left;margin-left:10px;margin-top: 3px;"></i><span id="refreshCDA">Refresh CDA Cache</span>
					</div>                
				</div>
			</div>
		</div>
		<script type="text/javascript">
			translateAdministracion();
		</script>
	</body>
</html>