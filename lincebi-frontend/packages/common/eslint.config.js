import eslintConfigPrettier from 'eslint-config-prettier';
import eslintPluginVue from 'eslint-plugin-vue';
import globals from 'globals';

export default [
	...eslintPluginVue.configs['flat/vue2-recommended'],
	eslintConfigPrettier,
	{
		languageOptions: {
			ecmaVersion: 2024,
			sourceType: 'module',
			globals: globals.browser,
		},
		rules: {
			'no-console': ['error', { allow: ['trace', 'debug', 'info', 'warn', 'error'] }],
		},
		ignores: ['node_modules/**', 'dist/**'],
	},
];
