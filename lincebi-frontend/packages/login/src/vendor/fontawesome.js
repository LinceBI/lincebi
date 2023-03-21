import Vue from 'vue';

import { config, library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

import { faSignInAlt } from '@fortawesome/free-solid-svg-icons/faSignInAlt';

import '@fortawesome/fontawesome-svg-core/styles.css';

config.autoAddCss = false;

library.add(faSignInAlt);

Vue.component('FontAwesomeIcon', FontAwesomeIcon);
