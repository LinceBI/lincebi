import { fileURLToPath, URL } from 'node:url';

import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue2';

const srcDir = fileURLToPath(new URL('./src/', import.meta.url));

export default defineConfig(() => ({
	base: '',
	resolve: {
		alias: {
			'@/': srcDir,
		},
	},
	plugins: [
		vue({
			template: {
				transformAssetUrls: {
					video: ['src', 'poster'],
					source: ['src'],
					img: ['src'],
					image: ['xlink:href'],
					'b-avatar': ['src'],
					'b-img': ['src'],
					'b-img-lazy': ['src', 'blank-src'],
					'b-card': ['img-src'],
					'b-card-img': ['src'],
					'b-card-img-lazy': ['src', 'blank-src'],
					'b-carousel-slide': ['img-src'],
					'b-embed': ['src'],
				},
			},
		}),
	],
	build: {
		sourcemap: 'hidden',
	},
	server: {
		host: '::',
		strictPort: true,
		open: false,
	},
	preview: {
		host: '::',
		strictPort: true,
		open: false,
	},
	css: {
		devSourcemap: true,
		preprocessorOptions: {
			scss: {
				additionalData: `
					$common-src-dir: '${srcDir}';
					@import '@lincebi/frontend-common/src/scss/main';
				`,
			},
		},
	},
}));
