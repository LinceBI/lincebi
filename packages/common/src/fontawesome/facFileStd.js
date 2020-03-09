import { width, height, ligatures, svgPathData } from './facToolStdashboard';
import getFaUnicodeCharacter from './getUnicodeCharacter';

export { width, height, ligatures, svgPathData };
export const prefix = 'fac';
export const iconName = 'file-std';
export const unicode = getFaUnicodeCharacter();
export const definition = {
	prefix,
	iconName,
	icon: [width, height, ligatures, unicode, svgPathData]
};

export { definition as faFileStd };
