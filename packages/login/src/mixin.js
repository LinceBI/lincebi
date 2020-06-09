import Vue from 'vue';

import uniqueId from 'lodash/uniqueId';

export const mixin = {
	data() {
		return {
			uniqueId: uniqueId(),
		};
	},
};

Vue.mixin(mixin);
