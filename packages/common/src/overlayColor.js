import brightness from './brightness';
import hexToRgb from './hexToRgb';

export default (color, light = '#fafafa', dark = '#424242') => {
	if (typeof color === 'string') {
		color = hexToRgb(color);
	}

	return brightness(...color) > 192 ? dark : light;
};
