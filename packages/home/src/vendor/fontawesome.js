import Vue from 'vue';

Vue.component('font-awesome-icon', async () => {
	const { FontAwesomeIcon } = await import('@fortawesome/vue-fontawesome');
	const { library } = await import('@fortawesome/fontawesome-svg-core');

	const icons = [
		import('@fortawesome/free-regular-svg-icons/faCheckSquare'),
		import('@fortawesome/free-regular-svg-icons/faClock'),
		import('@fortawesome/free-regular-svg-icons/faFile'),
		import('@fortawesome/free-regular-svg-icons/faFileAlt'),
		import('@fortawesome/free-regular-svg-icons/faSquare'),
		import('@fortawesome/free-regular-svg-icons/faWindowMaximize'),
		import('@fortawesome/free-regular-svg-icons/faWindowRestore'),
		import('@fortawesome/free-solid-svg-icons/faAngleDoubleRight'),
		import('@fortawesome/free-solid-svg-icons/faDatabase'),
		import('@fortawesome/free-solid-svg-icons/faEdit'),
		import('@fortawesome/free-solid-svg-icons/faExternalLinkAlt'),
		import('@fortawesome/free-solid-svg-icons/faFolder'),
		import('@fortawesome/free-solid-svg-icons/faFolderOpen'),
		import('@fortawesome/free-solid-svg-icons/faGlobeEurope'),
		import('@fortawesome/free-solid-svg-icons/faHome'),
		import('@fortawesome/free-solid-svg-icons/faHourglassHalf'),
		import('@fortawesome/free-solid-svg-icons/faLink'),
		import('@fortawesome/free-solid-svg-icons/faLock'),
		import('@fortawesome/free-solid-svg-icons/faPencilAlt'),
		import('@fortawesome/free-solid-svg-icons/faPlus'),
		import('@fortawesome/free-solid-svg-icons/faSave'),
		import('@fortawesome/free-solid-svg-icons/faSearch'),
		import('@fortawesome/free-solid-svg-icons/faSignOutAlt'),
		import('@fortawesome/free-solid-svg-icons/faSlidersH'),
		import('@fortawesome/free-solid-svg-icons/faStar'),
		import('@fortawesome/free-solid-svg-icons/faStore'),
		import('@fortawesome/free-solid-svg-icons/faSync'),
		import('@fortawesome/free-solid-svg-icons/faTable'),
		import('@fortawesome/free-solid-svg-icons/faTimes'),
		import('@fortawesome/free-solid-svg-icons/faTools'),
		import('@fortawesome/free-solid-svg-icons/faUser'),
		import('@stratebi/biserver-customization-common/src/fontawesome/faFileAdhoc'),
		import('@stratebi/biserver-customization-common/src/fontawesome/faFileCda'),
		import('@stratebi/biserver-customization-common/src/fontawesome/faFileCdfde'),
		import('@stratebi/biserver-customization-common/src/fontawesome/faFilePrpt'),
		import('@stratebi/biserver-customization-common/src/fontawesome/faFileSta'),
		import('@stratebi/biserver-customization-common/src/fontawesome/faFileStd'),
		import('@stratebi/biserver-customization-common/src/fontawesome/faFileUrl'),
		import('@stratebi/biserver-customization-common/src/fontawesome/faFileWcdf'),
		import('@stratebi/biserver-customization-common/src/fontawesome/faFileXaction'),
		import('@stratebi/biserver-customization-common/src/fontawesome/faFileXcdf'),
		import('@stratebi/biserver-customization-common/src/fontawesome/faFileXjpivot'),
		import('@stratebi/biserver-customization-common/src/fontawesome/faFileXml'),
		import('@stratebi/biserver-customization-common/src/fontawesome/faToolCde'),
		import('@stratebi/biserver-customization-common/src/fontawesome/faToolStadmin'),
		import('@stratebi/biserver-customization-common/src/fontawesome/faToolStagile'),
		import('@stratebi/biserver-customization-common/src/fontawesome/faToolStcard'),
		import('@stratebi/biserver-customization-common/src/fontawesome/faToolStdashboard'),
		import('@stratebi/biserver-customization-common/src/fontawesome/faToolStpivot'),
		import('@stratebi/biserver-customization-common/src/fontawesome/faToolStreport'),
		import('@stratebi/biserver-customization-common/src/fontawesome/faToolStsearch')
	];

	await Promise.all(
		icons.map(async icon => {
			const { definition } = await icon;
			library.add(definition);
		})
	);

	return FontAwesomeIcon;
});
