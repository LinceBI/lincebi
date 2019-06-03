import Vue from 'vue';

import defaultTo from 'lodash/defaultTo';
import uniqueId from 'lodash/uniqueId';

import isDemo from '@stratebi/biserver-customization-common/src/isDemo';
import isProduction from '@stratebi/biserver-customization-common/src/isProduction';
import isTouchDevice from '@stratebi/biserver-customization-common/src/isTouchDevice';
import overlayColor from '@stratebi/biserver-customization-common/src/overlayColor';
import truncate from '@stratebi/biserver-customization-common/src/truncate';

import { namespace } from '@/userSettings';

export const mixin = {
	data() {
		return {
			uniqueId: uniqueId(),
			namespace,
			isDemo,
			isProduction,
			isTouchDevice
		};
	},
	methods: {
		defaultTo: (v, d) => defaultTo(v, d),
		defaultToReq: (v, d) => defaultTo(v, require(`@/${d}`)),
		overlayColor,
		truncate
	}
};

Vue.mixin(mixin);
