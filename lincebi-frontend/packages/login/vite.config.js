import { fileURLToPath, URL } from 'node:url';

import { defineConfig } from 'vite';
import merge from 'lodash/merge';

import viteConfigCommon from '@lincebi/frontend-common/vite.config.js';

export default defineConfig((config) => merge(viteConfigCommon(config), {
	base: config.command === 'serve' ? '/lincebi/Login/' : '',
	resolve: {
		alias: {
			'@/': fileURLToPath(new URL('./src/', import.meta.url))
		},
	},
	server: {
		port: 3031,
	},
	preview: {
		port: 8081,
	},
}));
