<template>
	<div
		ref="container"
		class="node-garden"
		@click="onClick"
		@mousemove="onMousemove"
		@mouseleave="onMouseleave"
	>
		<canvas ref="canvas" />
	</div>
</template>

<script>
/*
 * Based on: https://github.com/pakastin/nodegarden
 */
const defaultTo = (value, defaultValue) => {
	return typeof value === 'undefined' ? defaultValue : value;
};

export class Node {
	constructor(garden) {
		this.garden = garden;
		this.reset();
	}

	render() {
		this.garden.ctx.beginPath();
		this.garden.ctx.arc(this.x, this.y, this.getDiameter(), 0, 2 * Math.PI);
		this.garden.ctx.fill();
	}

	update() {
		this.x += this.vx;
		this.y += this.vy;
		if (
			this.x > this.garden.width + 50 ||
			this.x < -50 ||
			this.y > this.garden.height + 50 ||
			this.y < -50
		) {
			// If node over screen limits - reset to a init position.
			this.reset();
		}
	}

	reset({ x, y, vx, vy, m, c } = {}) {
		this.x = defaultTo(x, Math.random() * this.garden.width);
		this.y = defaultTo(y, Math.random() * this.garden.height);
		this.vx = defaultTo(vx, Math.random() * 0.5 - 0.25);
		this.vy = defaultTo(vy, Math.random() * 0.5 - 0.25);
		this.m = defaultTo(m, Math.random() * 3 + 0.5);
		this.c = defaultTo(c, Math.random() >= 0.5 ? 1 : -1);
	}

	addForce(force, direction) {
		this.vx += (force * direction.x) / this.m;
		this.vy += (force * direction.y) / this.m;
	}

	distanceTo(node) {
		const x = node.x - this.x;
		const y = node.y - this.y;
		const total = Math.sqrt(x ** 2 + y ** 2);
		return { x, y, total };
	}

	squaredDistanceTo(node) {
		return (node.x - this.x) * (node.x - this.x) + (node.y - this.y) * (node.y - this.y);
	}

	collideTo(node) {
		node.vx = (node.m * node.vx) / (this.m + node.m) + (this.m * this.vx) / (this.m + node.m);
		node.vy = (node.m * node.vy) / (this.m + node.m) + (this.m * this.vy) / (this.m + node.m);
		this.reset();
	}

	getDiameter() {
		return this.m;
	}
}

export default {
	name: 'NodeGarden',
	data() {
		return {
			mounted: false,
			width: 0,
			height: 0,
			ctx: null,
			nodes: [],
			mouseNode: null,
			maxNodes: 200,
			colors: {
				node: { r: 0x30, g: 0x30, b: 0x30 },
				repel: { r: 0x30, g: 0x30, b: 0x30 },
				attract: { r: 0x30, g: 0x30, b: 0x30 },
			},
		};
	},
	mounted() {
		this.$nextTick(() => {
			this.mounted = true;
			this.ctx = this.$refs.canvas.getContext('2d');
			this.nodes = [];

			// Add mouse node.
			this.mouseNode = new Node(this);
			this.mouseNode.update = () => {};
			this.mouseNode.reset = () => {};
			this.mouseNode.render = () => {};
			this.mouseNode.m = 15;
			this.mouseNode.c = 1;
			// Move coordinates to unreachable zone.
			this.mouseNode.x = Number.MAX_SAFE_INTEGER;
			this.mouseNode.y = Number.MAX_SAFE_INTEGER;
			this.nodes.unshift(this.mouseNode);

			this.resize();
			this.render();

			window.addEventListener('resize', this.resize);
		});
	},
	beforeDestroy() {
		this.mounted = false;
		window.removeEventListener('resize', this.resize);
	},
	methods: {
		render() {
			if (this.mounted) {
				requestAnimationFrame(this.render);
			}

			// Clear canvas.
			this.ctx.clearRect(0, 0, this.width, this.height);

			// Update links.
			for (let i = 0; i < this.nodes.length - 1; i++) {
				const a = this.nodes[i];

				for (let j = i + 1; j < this.nodes.length; j++) {
					const b = this.nodes[j];

					const attract = a.c !== b.c;

					// Collision: remove smaller or equal, never both of them.
					const squaredDistance = a.squaredDistanceTo(b);
					if (attract && squaredDistance <= (a.m / 2 + b.m / 2) ** 2) {
						if (a.m <= b.m) {
							a.collideTo(b);
						} else {
							b.collideTo(a);
						}
						continue;
					}

					// Calculate gravity force.
					const force = (2 * (a.m * b.m)) / squaredDistance;
					const distance = a.distanceTo(b);
					const direction = {
						x: distance.x / distance.total,
						y: distance.y / distance.total,
					};
					a.addForce((attract ? 1 : -1) * force, direction);
					b.addForce((attract ? 1 : -1) * -force, direction);

					// Draw gravity lines
					const opacity = force * 100;
					if (opacity >= 0.025) {
						let color;
						if (attract) {
							color = this.colors.attract;
						} else {
							color = this.colors.repel;
						}
						this.ctx.beginPath();
						this.ctx.strokeStyle = `rgba(
							${color.r},
							${color.g},
							${color.b},
							${opacity}
						)`;
						this.ctx.moveTo(a.x, a.y);
						this.ctx.lineTo(b.x, b.y);
						this.ctx.stroke();
					}
				}
			}

			// Render and update nodes.
			for (let i = 0; i < this.nodes.length; i++) {
				this.nodes[i].render();
				this.nodes[i].update();
			}
		},
		resize() {
			// If retina screen, scale canvas.
			if (window.devicePixelRatio !== 1) {
				this.$refs.canvas.style.transformOrigin = '0 0';
				this.$refs.canvas.style.transform = `scale(
					${1 / window.devicePixelRatio}
				)`;
			}

			this.width = this.$refs.container.offsetWidth * window.devicePixelRatio;
			this.height = this.$refs.container.offsetHeight * window.devicePixelRatio;
			this.area = this.width * this.height;

			// Calculate nodes needed.
			this.nodes.length = (Math.sqrt(this.area) / 10) | 0;
			if (this.nodes.length > this.maxNodes) {
				this.nodes.length = this.maxNodes;
			}

			// Set canvas size.
			this.$refs.canvas.width = this.width;
			this.$refs.canvas.height = this.height;

			const color = this.colors.node;
			this.ctx.fillStyle = `rgb(${color.r}, ${color.g}, ${color.b})`;

			// Create nodes.
			for (let i = 0; i < this.nodes.length; i++) {
				if (this.nodes[i]) continue;
				this.nodes[i] = new Node(this);
			}
		},
		onClick() {
			this.mouseNode.c *= -1;
		},
		onMousemove(event) {
			this.mouseNode.x = event.pageX * window.devicePixelRatio;
			this.mouseNode.y = event.pageY * window.devicePixelRatio;
		},
		onMouseleave() {
			this.mouseNode.x = Number.MAX_SAFE_INTEGER;
			this.mouseNode.y = Number.MAX_SAFE_INTEGER;
		},
	},
};
</script>
