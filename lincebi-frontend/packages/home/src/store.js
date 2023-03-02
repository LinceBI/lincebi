import Vue from 'vue';
import Vuex from 'vuex';

import state from '@/store/state';
import * as getters from '@/store/getters';
import * as mutations from '@/store/mutations';
import * as actions from '@/store/actions';

Vue.use(Vuex);

export default new Vuex.Store({
	strict: import.meta.env.NODE_ENV !== 'production',
	state,
	getters,
	mutations,
	actions,
});
