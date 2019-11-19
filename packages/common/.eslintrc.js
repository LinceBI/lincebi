module.exports = {
	root: true,
	parserOptions: { parser: 'babel-eslint' },
	extends: ['plugin:vue/recommended', '@vue/prettier/recommended'],
	env: { node: true },
	rules: {
		'no-console': ['warn', { allow: ['warn', 'error'] }],
		'prettier/prettier': ['error', { singleQuote: true, useTabs: true }]
	}
};
