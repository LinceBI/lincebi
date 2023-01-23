export default (filename, filesInFolder) => {
	let duplicated = false;
	for (var i = 0; i < filesInFolder.length; i++) {
		if (filesInFolder[i].name === filename || filesInFolder[i].name === filename + '.url') {
			duplicated = true;
		}
	}
	return duplicated;
};
