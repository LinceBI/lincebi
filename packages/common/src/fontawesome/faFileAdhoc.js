import getFaUnicodeCharacter from './getUnicodeCharacter';

import { width, height, ligatures, svgPathData } from './faToolStreport';
export { width, height, ligatures, svgPathData };

export const prefix = 'fac';
export const iconName = 'file-adhoc';
export const unicode = getFaUnicodeCharacter();

export const definition = {
	prefix: prefix,
	iconName: iconName,
	icon: [width, height, ligatures, unicode, svgPathData]
};

export { definition as faFileAdhoc };
