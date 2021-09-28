const merge = require('lodash/merge');
const vueConfigCommon = require('@lincebi/frontend-common/vue.config.js');

module.exports = merge(vueConfigCommon, {
	devServer: {
		port: 8082,
		client: {
			webSocketURL: {
				pathname: '/ws-home',
			},
		},
		webSocketServer: {
			options: {
				path: '/ws-home',
			},
		},
	},
});
