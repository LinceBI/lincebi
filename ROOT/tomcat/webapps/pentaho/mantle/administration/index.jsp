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

<html lang="<%= effectiveLocale.getLanguage() %>" style="width:100%;height:100%;">
	<head>
		<meta charset="UTF-8"/>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
		<meta name="viewport" content="width=device-width,initial-scale=1">
		<link rel="stylesheet" href="../themes/stratebi/vendor/font-awesome/css/font-awesome.min.css">
		<script type="text/javascript" src="../themes/stratebi/vendor/jquery/js/jquery-3.3.1.min.js"></script>
		<script src="../themes/stratebi/vendor/bootstrap/js/bootstrap.min.js" async></script>
		<style>
			
			html,body {
			  background: transparent;
			  font-family: 'Titillium Web', sans-serif;
			}
			
			body {
			  margin: 20px;
			}
			
			.admin-container {
			  min-width: 250px;
			  max-width: 900px;
			  margin: 0 auto;
			  background: #f5f5f5;
			  border-left: 10px #a4bc81 solid; 
			  box-shadow: 5px 10px 18px #88888855;
			}
			
			.box .box_element {
			  padding: 20px;
			  font-size: 14px !important;
			}
			
			.box i {
			  margin-right: 20px;
			  margin-left:10px;
			  font-size: 15px !important;
			  text-align: center;
			  vertical-align: bottom;
			  width: 15px;
			  height: 15px;
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
			  text-align: left !important;
			  cursor: pointer;
			}
			
			.btn-first {
				border-top: 1px #ccc solid;
			}
			
			.btn:hover {
			  background: #eaeaea;
			  font-weight: bold;
			  color: black !important;
			}
		</style>	
	</head>

	<body style="overflow:auto">
	
		<div class="row admin-container">
			<div class="col-md-5 col-xs-5">
				<div class="box row">
					<% if (hasDataAccess) { %>
						<div class="box_element btn btn-first" onClick="window.parent.executeCommand('ManageDatasourcesCommand')">
							<i class="fa fa-database" aria-hidden="true"></i><span id="mds"><%= customProperties.getString("manageDataSources") %></span>
						</div> 
					<% } %>
					<% if (canAdminister) { %>
						<div class="box_element btn" onClick="window.parent.mantle_setPerspective('admin.perspective')">
							<i class="fa fa-unlock-alt" aria-hidden="true"></i><span id="biAdministracion"><%= customProperties.getString("biAdministration") %></span>
						</div>
					<% } %>					
				</div>
			</div>

			<% if (canAdminister) { %>
				<div class="col-md-5 col-xs-5 col-md-offset-2 col-xs-offset-2">
					<div class="box row">
						<div class="box_element btn" onClick="parent.executeCommand('RefreshSystemSettingsCommand')">
							<i class="fa fa-refresh" aria-hidden="true"></i><span id="refreshSystem"><%= customProperties.getString("refreshSystemSettings") %></span>
						</div>
						<div class="box_element btn" onClick="parent.executeCommand('RefreshMetaDataCommand')">
							<i class="fa fa-refresh" aria-hidden="true"></i><span id="refreshReportingMetadata"><%= customProperties.getString("refreshReportingMetadata") %></span>
						</div>
						<div class="box_element  btn" onClick="parent.executeCommand('ExecuteGlobalActionsCommand')">
							<i class="fa fa-refresh" aria-hidden="true"></i><span id="refreshGlobal"><%= customProperties.getString("refreshGlobalVariables") %></span>
						</div>
						<div class="box_element btn" onClick="parent.executeCommand('PurgeMondrianSchemaCacheCommand')">
							<i class="fa fa-refresh" aria-hidden="true"></i><span id="refreshMondrianSchema"><%= customProperties.getString("refreshMondrianSchema") %></span>
						</div>   
						<div class="box_element btn" onClick="parent.executeCommand('PurgeReportingDataCacheCommand')">
							<i class="fa fa-refresh" aria-hidden="true"></i><span id="refreshReportingData"><%= customProperties.getString("refreshReportingData") %></span>
						</div>            
						<div class="box_element btn" onClick="parent.openURL('CDA Cache', 'CDA Cache', 'plugin/cda/api/clearCache');">
							<i class="fa fa-refresh" aria-hidden="true"></i><span id="refreshCDA"><%= customProperties.getString("refreshCdaCache") %></span>
						</div>                
					</div>
				</div>
			<% } %>	
		</div>
	</body>
</html>