import Vue from 'vue';

import defaultTo from 'lodash/defaultTo';

import { namespace } from '@/userSettings';

export const mixin = {
	data() {
		return {
			namespace
		};
	},
	methods: {
		defaultTo: (v, d) => defaultTo(v, d),
		defaultToReq: (v, d) => defaultTo(v, require(`@/${d}`))
	}
};

Vue.mixin(mixin);
