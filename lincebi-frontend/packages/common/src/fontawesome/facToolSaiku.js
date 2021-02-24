import getFaUnicodeCharacter from './getUnicodeCharacter';

export const prefix = 'fac';
export const iconName = 'tool-saiku';
export const width = 1200;
export const height = 1200;
export const ligatures = [];
export const unicode = getFaUnicodeCharacter();
export const svgPathData =
	'M 0.88307522,628.58636 C 18.10525,961.17651 300.3387,1216.6066 631.20281,1199.1566 962.10689,1181.7016 1216.3847,897.97647 1199.1755,565.38637 1189.9441,387.16626 1104.6471,231.12614 976.52622,127.32615 v 558.33021 c 0,88.76412 -70.8736,227.8201 -173.0817,251.8301 V 29.456161 C 766.5683,16.061162 728.02415,6.2730379 688.32005,0.39003839 V 448.23025 c 0,88.71399 -70.87359,227.78011 -173.08168,251.78011 V 3.842609e-5 C 475.4143,5.696413 436.91011,15.269162 400.1139,28.369161 V 243.28914 c 0,88.75412 -70.9236,227.82011 -173.08169,251.82011 V 124.80915 C 80.359262,242.00914 -9.6364247,426.25925 0.82307522,628.58936';
export const definition = {
	prefix,
	iconName,
	icon: [width, height, ligatures, unicode, svgPathData],
};

export { definition as faToolSaiku };
