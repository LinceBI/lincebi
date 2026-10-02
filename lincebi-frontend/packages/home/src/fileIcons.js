import { library as faLibrary } from '@fortawesome/fontawesome-svg-core';

export const getFileIconName = (file) => {
	const faDefs = faLibrary.definitions;
	return faDefs.fac && faDefs.fac[`file-${file.extension}`] ? `file-${file.extension}` : 'file-other';
};

export const getFileColorClass = (file) => {
	return `text-${getFileIconName(file)}`;
};
