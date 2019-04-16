const isProduction = process.env.NODE_ENV === 'production';

module.exports = {
	root: true,
	env: {
		node: true
	},
	extends: ['plugin:vue/essential', '@vue/prettier'],
	rules: {
		'no-console': [isProduction ? 'error' : 'warn', { allow: ['warn', 'error'] }],
		'no-debugger': [isProduction ? 'error' : 'warn'],
		'prettier/prettier': ['error', { 'singleQuote': true, 'useTabs': true }]
	},
	parserOptions: {
		parser: 'babel-eslint'
	}
};
