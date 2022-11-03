import Vue from 'vue';

import uniqueId from 'lodash/uniqueId';

import { namespace } from '@/userSettings';

export const mixin = {
	data() {
		return {
			uniqueId: uniqueId(),
			namespace,
		};
	},
};

Vue.mixin(mixin);
