import getFaUnicodeCharacter from './getUnicodeCharacter';

export const prefix = 'fac';
export const iconName = 'tool-superset';
export const width = 256;
export const height = 256;
export const ligatures = [];
export const unicode = getFaUnicodeCharacter();
export const svgPathData =
	'M190.219 64.458c-21.95 0-42.17 12.349-61.71 33.925-19.202-21.915-39.76-33.925-62.728-33.925C27.751 64.458 0 91.598 0 128.136c0 36.537 27.75 63.338 65.781 63.338 23.375 0 41.49-10.958 61.71-32.806 19.541 21.916 39.421 32.874 62.728 32.874 38.03-.068 65.781-26.767 65.781-63.406 0-36.64-27.75-63.678-65.781-63.678Zm-124.167 88.68c-16.114 0-25.715-10.618-25.715-24.663s9.601-24.969 25.715-24.969c13.57 0 24.664 10.924 36.674 25.647-11.331 13.706-23.307 23.986-36.674 23.986zm123.013 0c-13.366 0-24.663-10.618-36.673-24.663 12.35-14.724 22.968-24.969 36.673-24.969 16.115 0 25.614 11.06 25.614 24.969 0 13.91-9.499 24.664-25.614 24.664z';
export const definition = {
	prefix,
	iconName,
	icon: [width, height, ligatures, unicode, svgPathData],
};

export { definition as faToolSuperset };
