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
		import('@fortawesome/free-solid-svg-icons/faAngleDoubleRight'),
		import('@fortawesome/free-solid-svg-icons/faDatabase'),
		import('@fortawesome/free-solid-svg-icons/faFolder'),
		import('@fortawesome/free-solid-svg-icons/faFolderOpen'),
		import('@fortawesome/free-solid-svg-icons/faGlobeEurope'),
		import('@fortawesome/free-solid-svg-icons/faHome'),
		import('@fortawesome/free-solid-svg-icons/faHourglassHalf'),
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
