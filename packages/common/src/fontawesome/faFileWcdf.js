import getFaUnicodeCharacter from './getUnicodeCharacter';

import { width, height, ligatures, svgPathData } from './faToolCde';
export { width, height, ligatures, svgPathData };

export const prefix = 'fac';
export const iconName = 'file-wcdf';
export const unicode = getFaUnicodeCharacter();

export const definition = {
	prefix: prefix,
	iconName: iconName,
	icon: [width, height, ligatures, unicode, svgPathData]
};

export { definition as faFileWcdf };
