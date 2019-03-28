module.exports = {
	publicPath: './',
	indexPath: 'index.jsp',
	integrity: true,
	productionSourceMap: false,
	devServer: { historyApiFallback: false },
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
