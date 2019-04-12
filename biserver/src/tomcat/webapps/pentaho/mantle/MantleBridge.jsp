<%@page pageEncoding="UTF-8" %>
<%@page language="java" %>
<!doctype html>
<html>

<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=5, user-scalable=yes">
	<title>Bridge</title>
	<link rel="icon" href="/pentaho-style/favicon.ico">
	<link rel="stylesheet" href="mantle/MantleStyle.css">
</head>

<body>
	<div id="pucWrapper">
		<div id="pucHeader">
			<div id="pucMenuBar"></div>
			<div id="pucPerspectives"></div>
			<div id="pucUserDropDown"></div>
		</div>
		<div id="pucToolBar"></div>
		<div id="pucContent"></div>
	</div>
	<script src="webcontext.js?context=mantle"></script>
	<script src="mantle/nativeScripts.js"></script>
	<script src="mantle/mantle.nocache.js"></script>
	<script src="content/data-access/resources/gwt/DatasourceEditor.nocache.js"></script>
	<style>
		body {
			margin: 0;
			height: 100vh;
			width: 100vw;
		}

		#pucHeader {
			display: none;
		}

		#pucWrapper, #pucContent {
			position: initial;
			height: 100%;
			width: 100%;
		}

		#pucToolBar {
			display: block;
			position: absolute;
			top: 0;
			right: 0;
			margin: 0 10px;
			z-index: 10000;
		}

		#pucToolBar,
		#pucToolBar .spacer {
			width: auto !important;
		}

		.newToolbarDropdown {
			top: 40px !important;
			left: auto !important;
			right: 10px !important;
		}
	</style>
	<script>
		(function () {
			let customizeXHRResponse = (customizer, {property = 'responseText', context = window} = {}) => {
				let prototype = context.XMLHttpRequest.prototype;
				let accessor = context.Object.getOwnPropertyDescriptor(prototype, property);
				let definer = get => context.Object.defineProperty(prototype, property, {get, configurable: true});
				definer(function () {
					let accessorResult = accessor.get.call(this);
					let customizerResult = customizer.call(this, accessorResult);
					return typeof customizerResult === 'undefined'
						? accessorResult
						: customizerResult;
				});
			};

			// Set perspective from parameter.
			let searchParams = new URLSearchParams(location.search);
			let perspectiveId = searchParams.get('perspective');
			if (perspectiveId !== null) {
				let maximumPriority = '-10000';
				let responseURLRegex = /\/api\/plugin-manager\/perspectives(\?|$)/;
				customizeXHRResponse(function (result) {
					if (responseURLRegex.test(this.responseURL)) {
						try {
							let parsedResult = JSON.parse(result);
							parsedResult.pluginPerspective.forEach(perspective => {
								if (perspective.id === perspectiveId) {
									perspective.layoutPriority = maximumPriority;
								}
							});
							return JSON.stringify(parsedResult);
						} catch (error) {
							console.error(error);
							return result;
						}
					}
				});
			}
		})();
	</script>
</body>

</html>
