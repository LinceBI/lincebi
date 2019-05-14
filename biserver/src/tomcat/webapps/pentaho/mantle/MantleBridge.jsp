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
			<div id="pucToolBar"></div>
			<div id="pucUserDropDown"></div>
		</div>
		<div id="pucContent"></div>
	</div>
	<script src="webcontext.js?context=mantle"></script>
	<script src="mantle/nativeScripts.js"></script>
	<script src="mantle/mantle.nocache.js"></script>
	<script src="content/data-access/resources/gwt/DatasourceEditor.nocache.js"></script>
	<style>
		body {
			margin: 0;
			min-height: 100vh;
		}

		#pucWrapper {
			position: initial;
			display: flex;
			flex-grow: 1;
			flex-shrink: 1;
			flex-basis: auto;
			flex-direction: column;
			height: 100%;
		}

		#pucWrapper > #pucHeader {
			position: initial;
			display: flex;
			flex-grow: 0;
			flex-shrink: 1;
			flex-basis: auto;
			flex-direction: row;
			flex-wrap: nowrap;
			height: auto;
			min-width: 100vw;
			padding: 0;
			border: 0;
			z-index: 100;
		}

		/* pucMenuBar */
		/* ========== */

		body:not(.show-menu-bar) > #pucWrapper > #pucHeader > #pucMenuBar {
			display: none !important;
		}

		#pucWrapper > #pucHeader > #pucMenuBar {
			position: initial;
			display: flex;
			flex-grow: 0;
			flex-shrink: 1;
			flex-basis: auto;
			flex-direction: row;
			justify-content: flex-start;
			margin: 0;
		}

		#pucWrapper > #pucHeader > #pucMenuBar #mainMenubar {
			position: initial;
		}

		/* pucPerspectives */
		/* =============== */

		#pucWrapper > #pucHeader > #pucPerspectives {
			display: none !important;
		}

		/* pucToolBar */
		/* ========== */

		body:not(.show-tool-bar) > #pucWrapper > #pucHeader > #pucToolBar {
			display: none !important;
		}

		#pucWrapper > #pucHeader > #pucToolBar {
			position: initial;
			display: flex;
			flex-grow: 0;
			flex-shrink: 1;
			flex-basis: auto;
			flex-direction: row;
			justify-content: flex-start;
			margin: 0;
		}

		#pucWrapper > #pucHeader > #pucToolBar,
		#pucWrapper > #pucHeader > #pucToolBar .spacer {
			width: auto !important;
		}

		#pucWrapper > #pucHeader > #pucToolBar #mainToolbar .toolbar-button img {
			margin-top: 2px;
			height: 24px;
			width: 24px;
			background-size: 100%;
		}

		#newButton {
			display: none !important;
		}

		.newToolbarDropdown {
			top: 30px !important;
		}

		/* pucUserDropDown */
		/* =============== */

		#pucWrapper > #pucHeader > #pucUserDropDown {
			display: none !important;
		}

		/* pucContent */
		/* ========== */

		#pucWrapper > #pucContent {
			position: initial;
			display: flex;
			flex-grow: 1;
			flex-shrink: 1;
			flex-basis: auto;
			flex-direction: row;
			height: 100%;
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
