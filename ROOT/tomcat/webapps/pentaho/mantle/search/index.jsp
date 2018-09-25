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
	</head>
	<body style="overflow:hidden;margin:0;width:100%;height:100%;">
		<script type="text/javascript">
			//elegir pagina a mostrar segun respuesta de la interfaz js del navegador
			/*try {
				var mobile = StratebiMobile.getFromAndroid();
				var message = 'com.stratebi.mobile';*/
				/*if (message.indexOf(mobile) != -1) {
					//ocultar barra superior
					parent.document.getElementById('pucHeader').style.display = 'none';
					parent.document.getElementById('pucContent').style.top = '0';
					window.location = 'mobile.jsp';
				} */
				
			/*} catch(e) {
				window.location = 'main.jsp';
			}*/

					window.location = 'main.jsp';

		</script>
	</body>
</html>