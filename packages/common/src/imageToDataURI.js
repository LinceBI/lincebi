import Compressor from 'compressorjs';

import fileToDataURI from './fileToDataURI';

export default async (image, options = {}) => {
	return new Promise((resolve, reject) => {
		/* eslint-disable-next-line no-new */
		new Compressor(image, {
			strict: true,
			checkOrientation: false,
			maxWidth: 512,
			maxHeight: 512,
			quality: 0.4,
			...options,
			success: (result) => resolve(result),
			error: (error) => reject(error),
		});
	}).then((compressedImage) => {
		return fileToDataURI(compressedImage);
	});
};
