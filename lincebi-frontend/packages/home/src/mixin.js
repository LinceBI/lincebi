import Vue from 'vue';

import uniqueId from 'lodash/uniqueId';

import truncate from '@lincebi/frontend-common/src/truncate';

import { namespace } from '@/userSettings';

export const mixin = {
	data() {
		return {
			uniqueId: uniqueId(),
			namespace,
		};
	},
	methods: {
		truncate,
	},
};

Vue.mixin(mixin);
