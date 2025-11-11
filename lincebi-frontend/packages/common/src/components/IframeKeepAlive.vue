<template>
	<div ref="wrapper" />
</template>

<script>
const iframes = new Map();

export default {
	name: 'IframeKeepAlive',
	inheritAttrs: false,
	props: {
		id: {
			type: String,
			required: true,
		},
		src: {
			type: String,
			required: true,
		},
	},
	data() {
		return {
			iframe: null,
			resizeObserver: null,
		};
	},
	watch: {
		id(newId, oldId) {
			this.unloadIframe(oldId);
			this.loadIframe(newId);
		},
	},
	mounted() {
		this.loadIframe(this.id);
		this.resizeObserver = new ResizeObserver(this.resizeIframe);
		this.resizeObserver.observe(this.$refs.wrapper);
	},
	beforeDestroy() {
		this.unloadIframe(this.id);
		if (this.resizeObserver) {
			this.resizeObserver.disconnect();
		}
	},
	methods: {
		loadIframe(id) {
			const iframe = iframes.get(id);

			if (iframe) {
				this.iframe = iframe;
			} else {
				this.iframe = document.createElement('iframe');
				this.iframe.style.position = 'fixed';
				this.iframe.style.border = '0';
				this.iframe.src = this.src;
				iframes.set(id, this.iframe);
				document.body.appendChild(this.iframe);
			}

			this.iframe.style.display = 'block';

			for (const [key, value] of Object.entries(this.$attrs)) {
				if (typeof value !== "undefined") {
					this.iframe.setAttribute(key, String(value));
				}
			}

			this.resizeIframe();
		},
		unloadIframe(id) {
			const iframe = iframes.get(id);
			if (iframe) {
				iframe.style.display = 'none';
			}
		},
		resizeIframe() {
			if (!this.iframe || !this.$refs.wrapper) {
				return;
			}
			const bounds = this.$refs.wrapper.getBoundingClientRect();
			this.iframe.style.top = `${bounds.top}px`;
			this.iframe.style.left = `${bounds.left}px`;
			this.iframe.style.width = `${bounds.width}px`;
			this.iframe.style.height = `${bounds.height}px`;
		},
	},
};
</script>