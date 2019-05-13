import Vue from 'vue';

import defaultTo from 'lodash/defaultTo';
import uniqueId from 'lodash/uniqueId';

import isProduction from '@stratebi/biserver-customization-common/src/isProduction';
import isDemo from '@stratebi/biserver-customization-common/src/isDemo';

import { namespace } from '@/userSettings';

export const mixin = {
	data() {
		return {
			uniqueId: uniqueId(),
			namespace,
			isProduction,
			isDemo
		};
	},
	methods: {
		defaultTo: (v, d) => defaultTo(v, d),
		defaultToReq: (v, d) => defaultTo(v, require(`@/${d}`))
	}
};

Vue.mixin(mixin);
