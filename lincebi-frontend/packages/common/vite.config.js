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
		rollupOptions: {
			output: {
				manualChunks: (id) => {
					if (/\/node_modules\//.test(id)) {
						if (/\/lodash\//.test(id)) {
							return 'lodash';
						} else if (/\/@fortawesome\//.test(id)) {
							return 'fontawesome';
						} else if (/\/bootstrap(-vue|-v4-rtl)?\//.test(id)) {
							return 'bootstrap';
						}
						return 'vendor';
					}
				},
			},
		},
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
					@import '${srcDir}/scss/main';
				`,
			},
		},
	},
}));
