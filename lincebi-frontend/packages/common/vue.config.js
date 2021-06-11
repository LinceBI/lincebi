module.exports = {
	outputDir: 'build',
	publicPath: '.',
	assetsDir: '',
	indexPath: 'index.jsp',
	integrity: process.env.NODE_ENV === 'production',
	productionSourceMap: false,
	devServer: {
		public: 'localhost:8443',
		historyApiFallback: false,
		disableHostCheck: true,
	},
	chainWebpack: (config) => {
		config.optimization.splitChunks({
			cacheGroups: {
				vendor: {
					test: /\/node_modules\//,
					name: 'vendor',
					chunks: 'all',
					enforce: true,
					priority: -20,
				},
				bootstrap: {
					test: /\/node_modules\/bootstrap(-vue)?\//,
					name: 'bootstrap',
					chunks: 'all',
					enforce: true,
					priority: -10,
				},
				fontawesome: {
					test: /(\/node_modules\/@fortawesome\/)|(\/fontawesome\/)/,
					name: 'fontawesome',
					chunks: 'all',
					enforce: true,
					priority: -10,
				},
			},
		});

		// Use project relative image URLs for BootstrapVue custom components.
		// See: https://bootstrap-vue.js.org/docs/reference/images/
		config.module
			.rule('vue')
			.use('vue-loader')
			.loader('vue-loader')
			.tap((options) => {
				options['transformAssetUrls'] = {
					video: ['src', 'poster'],
					source: 'src',
					img: 'src',
					image: 'xlink:href',
					'b-avatar': 'src',
					'b-img': 'src',
					'b-img-lazy': ['src', 'blank-src'],
					'b-card': 'img-src',
					'b-card-img': 'src',
					'b-card-img-lazy': ['src', 'blank-src'],
					'b-carousel-slide': 'img-src',
					'b-embed': 'src',
				};

				return options;
			});

		// Minify HTML while keeping comments.
		config.plugin('html').tap((args) => {
			if (args.length > 0) {
				args[0].minify = {
					collapseWhitespace: true,
					keepClosingSlash: true,
					removeComments: false,
					removeRedundantAttributes: true,
					removeScriptTypeAttributes: true,
					removeStyleLinkTypeAttributes: true,
					useShortDoctype: true,
				};
			}
			return args;
		});
	},
	css: {
		loaderOptions: {
			sass: {
				additionalData: `
					$common-font-path: '~@lincebi/frontend-common/src/assets/fonts';
					@import '~@lincebi/frontend-common/src/scss/main';
				`,
			},
		},
	},
};
