import Vue from 'vue';

import { AlertPlugin } from 'bootstrap-vue/esm/components/alert';
import { ButtonPlugin } from 'bootstrap-vue/esm/components/button';
import { FormGroupPlugin } from 'bootstrap-vue/esm/components/form-group';
import { FormInputPlugin } from 'bootstrap-vue/esm/components/form-input';
import { FormPlugin } from 'bootstrap-vue/esm/components/form';
import { ImagePlugin } from 'bootstrap-vue/esm/components/image';
import { LayoutPlugin } from 'bootstrap-vue/esm/components/layout';
import { ProgressPlugin } from 'bootstrap-vue/esm/components/progress';

import 'bootstrap/scss/bootstrap.scss';
import 'bootstrap-v4-rtl/scss/_rtl.scss';
import 'bootstrap-vue/src/index.scss';

Vue.use(AlertPlugin);
Vue.use(ButtonPlugin);
Vue.use(FormGroupPlugin);
Vue.use(FormInputPlugin);
Vue.use(FormPlugin);
Vue.use(ImagePlugin);
Vue.use(LayoutPlugin);
Vue.use(ProgressPlugin);
