module.exports = {
	root: true,
	env: {
		node: true
	},
	extends: ['plugin:vue/essential', '@vue/prettier'],
	rules: {
		'no-console': process.env.NODE_ENV === 'production' ? 'error' : 'warn',
		'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'warn',
		'prettier/prettier': ['error', { 'singleQuote': true, 'useTabs': true }]
	},
	parserOptions: {
		parser: 'babel-eslint'
	}
};
