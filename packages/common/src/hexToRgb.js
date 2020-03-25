export default (c) => {
	let r = 0,
		g = 0,
		b = 0;

	if (c.length == 4) {
		r = Number.parseInt(c[1] + c[1], 16);
		g = Number.parseInt(c[2] + c[2], 16);
		b = Number.parseInt(c[3] + c[3], 16);
	} else if (c.length == 7) {
		r = Number.parseInt(c[1] + c[2], 16);
		g = Number.parseInt(c[3] + c[4], 16);
		b = Number.parseInt(c[5] + c[6], 16);
	}

	return [r, g, b];
};
