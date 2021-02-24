import Vue from 'vue';

import invokeWhen from '@lincebi/frontend-common/src/invokeWhen';

const eventBus = new Vue();

eventBus.$emitWhenAvailable = (eventName, ...args) => {
	invokeWhen(
		() => eventBus._events[eventName],
		() => eventBus.$emit(eventName, ...args)
	);
};

export default eventBus;
