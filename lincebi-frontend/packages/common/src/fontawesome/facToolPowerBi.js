import getFaUnicodeCharacter from './getUnicodeCharacter';

export const prefix = 'fac';
export const iconName = 'tool-powerbi';
export const width = 160;
export const height = 160;
export const ligatures = [];
export const unicode = getFaUnicodeCharacter();
export const svgPathData =
	'm 72.9858,80 c 3.6353,0 6.5822,2.9563 6.5822,6.6032 V 160 H 30.7073 c -3.6353,0 -6.5823,-2.956 -6.5823,-6.603 V 86.6032 C 24.125,82.9563 27.072,80 30.7073,80 Z M 62.8592,39.873 c -3.6353,0 -6.5823,2.9564 -6.5823,6.6032 v 28.4444 h 16.7089 c 6.4316,0 11.6455,5.2305 11.6455,11.6826 V 160 H 111.72 V 46.4762 c 0,-3.6468 -2.947,-6.6032 -6.582,-6.6032 z M 95.5174,0 h 42.2786 c 3.635,0 6.582,2.95634 6.582,6.60317 V 153.397 c 0,3.647 -2.947,6.603 -6.582,6.603 H 116.783 V 46.4762 c 0,-6.4521 -5.214,-11.6825 -11.645,-11.6825 H 88.9351 V 6.60317 C 88.9351,2.95634 91.8821,0 95.5174,0 Z';
export const definition = {
	prefix,
	iconName,
	icon: [width, height, ligatures, unicode, svgPathData],
};

export { definition as faToolPowerBi };
