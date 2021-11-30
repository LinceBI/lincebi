import getFaUnicodeCharacter from './getUnicodeCharacter';

export const prefix = 'fac';
export const iconName = 'tool-stpanels';
export const width = 1200;
export const height = 1200;
export const ligatures = [];
export const unicode = getFaUnicodeCharacter();
export const svgPathData =
	'M 799.582,1201.0803 V 667.42388 h 400.408 v 400.52382 h 0.01 v 133.1412 z M 934.07038,1067.9477 H 1065.5011 V 801.68507 H 934.07038 Z M 665.86703,667.45739 V 1200 H 0.01797635 l 7.529e-5,-399.68767 H 0 L 5.99e-6,667.44883 Z M 532.02079,800.31233 H 133.86456 V 1066.0205 H 532.02079 Z M 534.134,532.54347 V 8.4580095e-7 L 1199.983,0 V 399.68853 H 1200 v 132.8635 z M 667.98014,399.68853 H 1066.1361 V 133.98121 H 667.98014 Z M 0,533.65636 V 1e-6 L 400.40749,0 v 400.52379 h 0.0102 V 533.66495 Z M 134.48822,400.52379 H 265.91881 V 134.26119 H 134.48822 Z';
export const definition = {
	prefix,
	iconName,
	icon: [width, height, ligatures, unicode, svgPathData],
};

export { definition as faToolStpanels };
