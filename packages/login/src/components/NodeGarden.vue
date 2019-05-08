<template>
	<div
		class="node-garden"
		ref="container"
		@mousemove="onMousemove"
		@mouseleave="onMouseleave"
		@mousedown="onMousedown"
		@mouseup="onMouseup"
	>
		<canvas ref="canvas" />
	</div>
</template>

<script>
import defaultTo from 'lodash/defaultTo';

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
			// If node over screen limits - reset to a init position
			this.reset();
		}
	}

	reset({ x, y, vx, vy, m } = {}) {
		this.x = defaultTo(x, Math.random() * this.garden.width);
		this.y = defaultTo(y, Math.random() * this.garden.height);
		this.vx = defaultTo(vx, Math.random() * 0.5 - 0.25);
		this.vy = defaultTo(vy, Math.random() * 0.5 - 0.25);
		this.m = defaultTo(m, Math.random() * 2.5 + 0.5);
	}

	addForce(force, direction) {
		this.vx += (force * direction.x) / this.m;
		this.vy += (force * direction.y) / this.m;
	}

	distanceTo(node) {
		let x = node.x - this.x;
		let y = node.y - this.y;
		let total = Math.sqrt(x ** 2 + y ** 2);
		return { x, y, total };
	}

	squaredDistanceTo(node) {
		return (
			(node.x - this.x) * (node.x - this.x) +
			(node.y - this.y) * (node.y - this.y)
		);
	}

	collideTo(node) {
		node.vx =
			(node.m * node.vx) / (this.m + node.m) +
			(this.m * this.vx) / (this.m + node.m);
		node.vy =
			(node.m * node.vy) / (this.m + node.m) +
			(this.m * this.vy) / (this.m + node.m);
		this.reset();
	}

	getDiameter() {
		return this.m;
	}
}

export default {
	name: 'node-garden',
	props: {
		color: {
			type: Object,
			default() {
				return { r: 0, g: 0, b: 0, a: 0.5 };
			}
		}
	},
	data() {
		return {
			mounted: false,
			width: 0,
			height: 0,
			ctx: null,
			nodes: [],
			mouseNode: null
		};
	},
	methods: {
		render() {
			if (this.mounted) {
				requestAnimationFrame(this.render);
			}

			// Clear canvas
			this.ctx.clearRect(0, 0, this.width, this.height);

			// Update links
			for (let i = 0; i < this.nodes.length - 1; i++) {
				let a = this.nodes[i];

				for (let j = i + 1; j < this.nodes.length; j++) {
					let b = this.nodes[j];

					// Collision: remove smaller or equal, never both of them
					let squaredDistance = a.squaredDistanceTo(b);
					if (squaredDistance <= (a.m / 2 + b.m / 2) ** 2) {
						if (a.m <= b.m) a.collideTo(b);
						else b.collideTo(a);
						continue;
					}

					// Calculate gravity force
					let force = (2 * (a.m * b.m)) / squaredDistance;
					let distance = a.distanceTo(b);
					let direction = {
						x: distance.x / distance.total,
						y: distance.y / distance.total
					};
					a.addForce(force, direction);
					b.addForce(-force, direction);

					// Draw gravity lines
					let opacity = force * 200;
					if (opacity >= 0.025 && opacity < this.color.a) {
						this.ctx.beginPath();
						this.ctx.strokeStyle = `rgba(
							${this.color.r},
							${this.color.g},
							${this.color.b},
							${opacity}
						)`;
						this.ctx.moveTo(a.x, a.y);
						this.ctx.lineTo(b.x, b.y);
						this.ctx.stroke();
					}
				}
			}

			// Render and update nodes
			for (let i = 0; i < this.nodes.length; i++) {
				this.nodes[i].render();
				this.nodes[i].update();
			}
		},
		resize() {
			// If retina screen, scale canvas
			if (window.devicePixelRatio !== 1) {
				this.$refs.canvas.style.transform = `scale(
					${1 / window.devicePixelRatio}
				)`;
				this.$refs.canvas.style.transformOrigin = '0 0';
			}

			this.width = this.$refs.container.offsetWidth * window.devicePixelRatio;
			this.height = this.$refs.container.offsetHeight * window.devicePixelRatio;
			this.area = this.width * this.height;

			// Calculate nodes needed
			this.nodes.length = (Math.sqrt(this.area) / 10) | 0;
			if (this.nodes.length > 150) this.nodes.length = 150;

			// Set canvas size
			this.$refs.canvas.width = this.width;
			this.$refs.canvas.height = this.height;

			this.ctx.fillStyle = `rgba(
				${this.color.r},
				${this.color.g},
				${this.color.b},
				${this.color.a}
			)`;

			// Create nodes
			for (let i = 0; i < this.nodes.length; i++) {
				if (this.nodes[i]) continue;
				this.nodes[i] = new Node(this);
			}
		},
		onMousemove(event) {
			this.mouseNode.x = event.pageX * window.devicePixelRatio;
			this.mouseNode.y = event.pageY * window.devicePixelRatio;
		},
		onMouseleave() {
			this.mouseNode.x = Number.MAX_SAFE_INTEGER;
			this.mouseNode.y = Number.MAX_SAFE_INTEGER;
		},
		onMousedown() {
			this.mouseNode.m = -50;
		},
		onMouseup() {
			this.mouseNode.m = 5;
		}
	},
	mounted() {
		this.$nextTick(function() {
			this.mounted = true;
			this.ctx = this.$refs.canvas.getContext('2d');
			this.nodes = [];

			// Add mouse node
			this.mouseNode = new Node(this);
			this.mouseNode.update = () => {};
			this.mouseNode.reset = () => {};
			this.mouseNode.render = () => {};
			this.mouseNode.m = 5;
			// Move coordinates to unreachable zone
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
	}
};
</script>

<style scoped lang="scss">
.node-garden {
	overflow: hidden;
}
</style>
