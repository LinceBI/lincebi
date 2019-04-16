import Vue from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { library } from '@fortawesome/fontawesome-svg-core';

Vue.component('font-awesome-icon', FontAwesomeIcon);

import { faKey, faSignInAlt } from '@fortawesome/free-solid-svg-icons';

library.add(faKey, faSignInAlt);
