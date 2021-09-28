const merge = require('lodash/merge');
const vueConfigCommon = require('@lincebi/frontend-common/vue.config.js');

module.exports = merge(vueConfigCommon, {
	devServer: {
		port: 8081,
		client: {
			webSocketURL: {
				pathname: '/ws-login',
			},
		},
		webSocketServer: {
			options: {
				path: '/ws-login',
			},
		},
	},
});
