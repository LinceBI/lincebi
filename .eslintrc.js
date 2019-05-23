const isProduction = process.env.NODE_ENV === 'production';

module.exports = {
	root: true,
	parserOptions: { parser: 'babel-eslint' },
	extends: ['plugin:vue/essential', '@vue/prettier'],
	env: { node: true },
	rules: {
		'no-console': [isProduction ? 'error' : 'warn', { allow: ['warn', 'error'] }],
		'prettier/prettier': ['error', { singleQuote: true, useTabs: true }]
	}
};
