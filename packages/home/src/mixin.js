import Vue from 'vue';

import defaultTo from 'lodash/defaultTo';

const mixin = {
	methods: {
		defaultTo: (v, d) => defaultTo(v, d),
		defaultToReq: (v, d) => defaultTo(v, require(`@/${d}`))
	}
};

Vue.mixin(mixin);

export default mixin;
