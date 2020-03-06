import getFaUnicodeCharacter from './getUnicodeCharacter';

export const prefix = 'fac';
export const iconName = 'tool-stolap';
export const width = 1200;
export const height = 1200;
export const ligatures = [];
export const unicode = getFaUnicodeCharacter();
export const svgPathData =
	'm 487.50242,502.47866 c 517.83058,-268.67128 0,0 517.83058,-268.67128 L 600.73209,-2.304e-5 78.020961,299.39048 v 1.70662 z m 239.88097,13.16538 0.97612,612.43396 145.92962,-83.8683 C 1114.4141,906.21699 1121.247,857.21252 1121.247,584.88419 c 0,-204.551 0.488,-246.72898 0.488,-247.46039 l 0.244,-27.06217 z m -121.77071,175.0508 v 0 L 79.241108,436.65176 c 0,36.3267 -0.244029,80.94271 -0.244029,130.92239 0,293.78303 50.758131,366.43642 237.928731,481.02395 147.14978,89.9635 253.05857,137.0175 257.4511,138.9679 l 28.30742,12.434 3.66044,-2.1942 z';
export const definition = {
	prefix: prefix,
	iconName: iconName,
	icon: [width, height, ligatures, unicode, svgPathData]
};

export { definition as faToolStolap };
