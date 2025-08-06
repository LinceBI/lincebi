import getFaUnicodeCharacter from './getUnicodeCharacter';

export const prefix = 'fac';
export const iconName = 'home-solid';
export const width = 576;
export const height = 512;
export const ligatures = [];
export const unicode = getFaUnicodeCharacter();
export const svgPathData =
	'm 287.96012,0.1737895 c 0,0 -171.29213,146.7819405 -267.017672,228.9873305 -5.621173,5.23032 -9.743433,17.85773 -9.885458,26.83887 -0.556555,35.19595 27.402459,54.57954 48.836485,54.57954 h 34.23445 v 172.82151 c 0,15.71917 12.377595,28.42517 27.690315,28.42517 h 83.07094 c 15.31272,0 27.69032,-12.73444 27.69032,-28.42517 V 369.70039 h 110.76126 v 113.70065 c 0,15.69073 12.3776,28.42517 27.69032,28.42517 h 83.07119 c 15.31271,0 27.69006,-12.706 27.69006,-28.42517 V 310.57953 h 34.79101 c 27.55533,0 49.94959,-25.5049 48.28018,-54.57954 -0.51374,-8.96753 -4.2643,-21.60855 -10.60545,-26.83887 C 459.19709,146.95573 287.96012,0.1737895 287.96012,0.1737895 Z';
export const definition = {
	prefix,
	iconName,
	icon: [width, height, ligatures, unicode, svgPathData],
};

export { definition as faHomeSolid };
