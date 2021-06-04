module.exports = {
	root: true,
	env: { node: true },
	extends: ['plugin:vue/recommended', '@vue/prettier/recommended'],
	parserOptions: { parser: '@babel/eslint-parser' },
	rules: {
		'no-console': ['warn', { allow: ['warn', 'error'] }],
	},
};
