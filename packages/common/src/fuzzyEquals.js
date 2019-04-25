export const normalize = string =>
	string
		// Remove leading and trailing whitespace.
		.trim()
		// Transform to lower case.
		.toLowerCase()
		// Decompose combined graphemes into the combination of simple ones.
		.normalize('NFD')
		// Remove diacritics.
		.replace(/[\u0300-\u036f]/g, '')
		// Remove extra whitespace.
		.replace(/\s+/g, ' ');

export default (a, b) => normalize(a) === normalize(b);
