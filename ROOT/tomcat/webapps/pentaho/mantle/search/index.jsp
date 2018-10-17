<!DOCTYPE html>
<html>
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
				var message = 'com.stratebi.mobile';
				if (message.indexOf(mobile) != -1) {
					//ocultar barra superior
					parent.document.getElementById('pucHeader').style.display = 'none';
					parent.document.getElementById('pucContent').style.top = '0';
					window.location = 'mobile.jsp';
				}
			} catch(e) {
				window.location = 'main.jsp';
			}*/

			window.location = 'main.jsp';
		</script>
	</body>
</html>