module.exports = {
	root: true,
	env: { browser: true, node: true },
	extends: ['plugin:vue/recommended', 'plugin:prettier/recommended'],
	rules: {
		'no-console': ['warn', { allow: ['warn', 'error'] }],
	},
};
