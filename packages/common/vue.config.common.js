module.exports = {
	publicPath: './',
	indexPath: 'index.jsp',
	integrity: true,
	productionSourceMap: false,
	devServer: { historyApiFallback: false },
	chainWebpack: config => {
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
				}

				return options;
			});
	},
	css: {
		loaderOptions: {
			sass: {
				data: `
					$common-font-path: '~@stratebi/biserver-customization-common/src/assets/fonts';
					@import '~@stratebi/biserver-customization-common/src/scss/main';
					@import '~bootstrap/scss/bootstrap';
					@import '~bootstrap-vue/src/variables';
					@import '~bootstrap-vue/src/utilities';
					@import '~bootstrap-vue/src/components/index';
				`
			}
		}
	}
};