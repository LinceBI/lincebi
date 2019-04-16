import Vue from 'vue';

Vue.component('font-awesome-icon', async () => {
	const {
		FontAwesomeIcon
	} = await import(/* webpackChunkName: "fa" */ '@fortawesome/vue-fontawesome');

	const {
		library
	} = await import(/* webpackChunkName: "fa" */ '@fortawesome/fontawesome-svg-core');

	const iconImports = [
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-regular-svg-icons/faCheckSquare'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-regular-svg-icons/faClock'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-regular-svg-icons/faFile'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-regular-svg-icons/faFileAlt'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-regular-svg-icons/faSquare'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-regular-svg-icons/faWindowMaximize'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-solid-svg-icons/faAngleDoubleRight'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-solid-svg-icons/faDatabase'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-solid-svg-icons/faFolder'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-solid-svg-icons/faFolderOpen'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-solid-svg-icons/faGlobeEurope'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-solid-svg-icons/faHome'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-solid-svg-icons/faHourglassHalf'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-solid-svg-icons/faLock'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-solid-svg-icons/faPencilAlt'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-solid-svg-icons/faPlus'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-solid-svg-icons/faSave'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-solid-svg-icons/faSearch'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-solid-svg-icons/faSignOutAlt'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-solid-svg-icons/faSlidersH'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-solid-svg-icons/faStar'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-solid-svg-icons/faStore'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-solid-svg-icons/faSync'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-solid-svg-icons/faTable'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-solid-svg-icons/faTimes'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-solid-svg-icons/faTools'),
		import(/* webpackChunkName: "fa" */ '@fortawesome/free-solid-svg-icons/faUser'),
		import(/* webpackChunkName: "fa" */ '@stratebi/biserver-customization-common/src/fac/faToolStagile'),
		import(/* webpackChunkName: "fa" */ '@stratebi/biserver-customization-common/src/fac/faToolStcard'),
		import(/* webpackChunkName: "fa" */ '@stratebi/biserver-customization-common/src/fac/faToolStdashboard'),
		import(/* webpackChunkName: "fa" */ '@stratebi/biserver-customization-common/src/fac/faToolStpivot'),
		import(/* webpackChunkName: "fa" */ '@stratebi/biserver-customization-common/src/fac/faToolStreport'),
		import(/* webpackChunkName: "fa" */ '@stratebi/biserver-customization-common/src/fac/faToolStsearch')
	];

	const icons = await Promise.all(iconImports);
	icons.forEach(({ definition }) => library.add(definition));

	return FontAwesomeIcon;
});

Vue.component(
	'font-awesome-layers',
	import(/* webpackChunkName: "fa" */ '@fortawesome/vue-fontawesome')
);

Vue.component(
	'font-awesome-layers-text',
	import(/* webpackChunkName: "fa" */ '@fortawesome/vue-fontawesome')
);
