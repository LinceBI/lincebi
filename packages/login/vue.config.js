const merge = require('lodash/merge');
const vueConfigCommon = require('@lincebi/biserver-customization-common/vue.config.js');

module.exports = merge(vueConfigCommon, { devServer: { port: 8081 } });
