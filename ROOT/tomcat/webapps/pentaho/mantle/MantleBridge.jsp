<%@page pageEncoding="UTF-8" %>
<%@page language="java" %>
<!doctype html>
<html>

<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" />
	<link rel="icon" href="/pentaho-style/favicon.ico" />
	<style>
		body {
			margin: 0;
		}

		#mantle-iframe {
			display: block;
			height: 100vh;
			width: 100vw;
			border: 0;
		}
	</style>
</head>

<body>
	<iframe id="mantle-iframe" sandbox="allow-forms allow-modals allow-popups allow-same-origin allow-scripts"></iframe>
	<template id="mantle-template">
		<link rel="stylesheet" href="mantle/MantleStyle.css">
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
			#pucHeader { display: none !important; }
			#pucWrapper, #pucContent { position: initial; height: 100vh; width: 100vw; }
		</style>
	</template>
	<script>
		window.addEventListener('DOMContentLoaded', () => {
			let mantleIFrame = document.getElementById('mantle-iframe');
			let mantleTemplate = document.getElementById('mantle-template');
			let mantleWindow = mantleIFrame.contentWindow;

			// Load sandboxed Mantle
			mantleWindow.document.open();
			mantleWindow.document.write(mantleTemplate.innerHTML);
			mantleWindow.document.close();

			// Export Mantle properties
			mantleWindow.addEventListener('load', () => {
				let propRegex = /^(CONTEXT_PATH|FULL_QUALIFIED_URL|mantle_.+)$/;

				for (let propName in mantleWindow) {
					if (propRegex.test(propName)) {
						window[propName] = mantleWindow[propName];
					}
				}

				window.dispatchEvent(new CustomEvent('mantle.loaded', {
					detail: {window: mantleWindow}
				}));
			});

			// Export mantleIFrame
			window.mantleIFrame = mantleIFrame;

			// Set perspective from parameter
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
					}, {context: mantleWindow});
				}
			})();
		});
	</script>
</body>

</html>
