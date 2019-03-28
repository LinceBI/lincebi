module.exports = {
	publicPath: './',
	indexPath: 'index.jsp',
	integrity: true,
	productionSourceMap: false,
	devServer: {
		historyApiFallback: false
	},
	chainWebpack: config => {
		const jsRule = config.module.rule('js');
		jsRule.uses.clear();
		jsRule
			.use('babel-loader')
			.loader('babel-loader')
			.options({
				rootMode: 'upward',
				presets: ['@vue/app']
			});
	},
	css: {
		loaderOptions: {
			sass: {
				data: `
					@import '~@stratebi/biserver-customization-common/src/scss/variables';
					@import '~@stratebi/biserver-customization-common/src/scss/em';
					@import '~@stratebi/biserver-customization-common/src/scss/fonts';
					@import '~@stratebi/biserver-customization-common/src/scss/helpers';
					@import '~bootstrap/scss/bootstrap';
					@import '~bootstrap-vue/src/variables';
					@import '~bootstrap-vue/src/utilities';
					@import '~bootstrap-vue/src/components/index';
				`
			}
		}
	}
};
