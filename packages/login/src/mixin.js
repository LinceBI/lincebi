import Vue from 'vue';

import uniqueId from 'lodash/uniqueId';

import isDemo from '@lincebi/biserver-customization-common/src/isDemo';
import isProduction from '@lincebi/biserver-customization-common/src/isProduction';

export const mixin = {
	data() {
		return {
			uniqueId: uniqueId(),
			isDemo,
			isProduction
		};
	}
};

Vue.mixin(mixin);
