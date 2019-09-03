module.exports = {
	publicPath: './',
	indexPath: 'index.jsp',
	integrity: true,
	productionSourceMap: false,
	devServer: { historyApiFallback: false, disableHostCheck: true },
	chainWebpack: config => {
		config.optimization.splitChunks({
			cacheGroups: {
				vendor: {
					test: /\/node_modules\//,
					name: 'vendor',
					chunks: 'all',
					enforce: true,
					priority: -20
				},
				bootstrap: {
					test: /\/node_modules\/bootstrap(-vue)?\//,
					name: 'bootstrap',
					chunks: 'all',
					enforce: true,
					priority: -10
				},
				fontawesome: {
					test: /(\/node_modules\/@fortawesome\/)|(\/fontawesome\/)/,
					name: 'fontawesome',
					chunks: 'all',
					enforce: true,
					priority: -10
				}
			}
		});

		// Project relative image URLs for BootstrapVue custom components.
		// See: https://bootstrap-vue.js.org/docs/reference/images/
		config.module
			.rule('vue')
			.use('vue-loader')
			.loader('vue-loader')
			.tap(options => {
				options['transformAssetUrls'] = {
					img: 'src',
					image: 'xlink:href',
					'b-img': 'src',
					'b-img-lazy': ['src', 'blank-src'],
					'b-card': 'img-src',
					'b-card-img': 'img-src',
					'b-card-img-lazy': ['src', 'blank-src'],
					'b-carousel-slide': 'img-src',
					'b-embed': 'src'
				};

				return options;
			});

		// Disable HTML comments removal.
		config.plugin('html').tap(args => {
			if (args.length > 0 && args[0].minify) {
				args[0].minify.removeComments = false;
			}
			return args;
		});
	},
	css: {
		loaderOptions: {
			sass: {
				prependData: `
					$common-font-path: '~@stratebi/biserver-customization-common/src/assets/fonts';
					@import '~@stratebi/biserver-customization-common/src/scss/main';
				`
			}
		}
	}
};
