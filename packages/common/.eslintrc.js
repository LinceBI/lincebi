module.exports = {
	root: true,
	parserOptions: { parser: 'babel-eslint' },
	extends: ['plugin:vue/essential', '@vue/prettier'],
	env: { node: true },
	rules: {
		'no-console': ['warn', { allow: ['warn', 'error'] }],
		'prettier/prettier': ['error', { singleQuote: true, useTabs: true }]
	}
};
