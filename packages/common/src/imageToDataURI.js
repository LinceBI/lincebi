import Compressor from 'compressorjs';
import fileToDataURI from './fileToDataURI';

export default async (image, options = {}) => {
	return new Promise((resolve, reject) => {
		new Compressor(image, {
			strict: true,
			checkOrientation: false,
			maxWidth: 256,
			maxHeight: 256,
			quality: 0.4,
			...options,
			success: result => resolve(result),
			error: error => reject(error)
		});
	}).then(compressedImage => {
		return fileToDataURI(compressedImage);
	});
};
