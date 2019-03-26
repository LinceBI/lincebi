const path = require('path');
const root = path.resolve(__dirname, '../../src');

module.exports = {
	publicPath: './',
	indexPath: 'index.jsp',
	devServer: {
		port: 8081,
		historyApiFallback: false
	},
	configureWebpack: {
		resolve: {
			alias: {
				'@@': root
			}
		}
	},
	css: {
		loaderOptions: {
			sass: {
				data: `
					@import '${root}/scss/variables';
					@import '${root}/scss/em';
					@import '${root}/scss/fonts';
					@import '${root}/scss/helpers';
					@import '~bootstrap/scss/bootstrap';
					@import '~bootstrap-vue/src/variables';
					@import '~bootstrap-vue/src/utilities';
					@import '~bootstrap-vue/src/components/index';
				`
			}
		}
	}
};
