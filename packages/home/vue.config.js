const merge = require('lodash/merge');
const vueConfigCommon = require('@stratebi/biserver-customization-common/vue.config.common.js');

module.exports = merge(vueConfigCommon, {
	devServer: { port: 8082 }
});
