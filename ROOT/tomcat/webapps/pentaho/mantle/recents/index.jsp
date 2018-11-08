<!DOCTYPE html>
<%@page pageEncoding="UTF-8" %>
<%@page session="true"  
	contentType="text/html; charset=UTF-8"
    language="java"
    import="org.apache.commons.lang.StringUtils,
            org.owasp.encoder.Encode,
            org.pentaho.platform.util.messages.LocaleHelper,
            java.io.Serializable,
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
            org.pentaho.platform.security.policy.rolebased.actions.RepositoryCreateAction,
            org.pentaho.platform.api.repository2.unified.IUnifiedRepository,
            org.pentaho.platform.api.repository2.unified.RepositoryRequest,
            org.pentaho.platform.api.repository2.unified.RepositoryFile,
            com.google.gson.*" %>

<%@ include file="./../fragments/Settings.jspf" %>

<html lang="<%= effectiveLocale.getLanguage() %>">
<head>
	<meta charset="UTF-8"/>
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<title>Favorites</title>
	
	<!-- Styles -->
	<link rel="stylesheet" href="//fonts.googleapis.com/css?family=Titillium+Web">
	<link rel="stylesheet" href="../themes/stratebi/vendor/font-awesome/css/font-awesome.min.css">
	<link rel="stylesheet" href="../themes/stratebi/vendor/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/recents.css" />
	
	<!-- Javascript -->
	<script type="text/javascript" src="../themes/stratebi/vendor/jquery/js/jquery-3.3.1.min.js"></script>
	<script type="text/javascript" src="../themes/stratebi/vendor/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/recents.js"></script>
    <script type="text/javascript">
        document.addEventListener("DOMContentLoaded", () => {
            init();
        });
    </script>
</head>

<body>

    <div id="main-container">

        <!-- Header -->
        <div id="header" class="container-fluid">
			<div class="row">
				<div class="col-xs-12">
					<h2 class="section-title"><%= customProperties.getString("recents") %></h2>
				</div>
			</div>
		</div>
		
        <!-- Results -->
        <div id="results-container" class="container-fluid">
            <div class="row">
                <div class="col-xs-12">
                    <div class="panel panel-default">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-10 flex-column flex-column-vertical"><h4><%= customProperties.getString("results") %></h4></div>
								<div class="col-xs-2 flex-column">
									<button class="btn btn-primary" id="reload" onclick="populateRecents()">
										<span class="fa fa-refresh"></span>
									</button>
								</div>
							</div>
						</div>
						<div id="recents-panel" class="list-group">
						</div>
					</div>
                </div>
            </div>
		</div>
		
	</div>

    <!-- Item Template -->
    <template id="item-template">
		<div href="#" class="list-group-item">
			<div class="row">
                <div class="col-xs-10">
					<a href="#" class="list-group-item-heading item-title"></a>
                </div>
				<div class="col-xs-2 flex-column">
					<span class="button favorite-button">
                        <i class="fa fa-lg" aria-hidden="true"></i>
                    </span>
				</div>
            </div>
		</div>
    </template>

</body>
</html>
