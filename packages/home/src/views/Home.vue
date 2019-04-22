<template>
	<div class="home">
		<b-carousel
			:interval="4000"
			controls
			indicators
			img-width="256px"
			img-height="256px"
			class="home-carousel"
		>
			<b-carousel-slide
				caption="First slide"
				text="Nulla vitae elit libero, a pharetra augue mollis interdum."
				img-src="https://picsum.photos/1536/256?image=10"
			></b-carousel-slide>
			<b-carousel-slide
				caption="Second slide"
				text="Nulla vitae elit libero, a pharetra augue mollis interdum."
				img-src="https://picsum.photos/1536/256?image=11"
			></b-carousel-slide>
			<b-carousel-slide
				caption="Third slide"
				text="Nulla vitae elit libero, a pharetra augue mollis interdum."
				img-src="https://picsum.photos/1536/256?image=12"
			></b-carousel-slide>
			<b-carousel-slide
				caption="Fourth slide"
				text="Nulla vitae elit libero, a pharetra augue mollis interdum."
				img-src="https://picsum.photos/1536/256?image=13"
			></b-carousel-slide>
		</b-carousel>
		<b-tabs
			class="home-tabs"
			nav-class="home-tablist"
			content-class="home-tabcontent"
			fill
			no-nav-style
		>
			<b-tab v-for="i in tabs" :key="`home-tab-${i}`" no-body>
				<template slot="title">
					<div class="home-tab">
						<span>Tab {{ i }}</span>
						<b-button variant="link" class="home-closetab" @click="closeTab(i)">
							<font-awesome-icon class="fa-fw" :icon="['fas', 'times']" />
						</b-button>
					</div>
				</template>
				<b-container class="py-5 px-4">Tab Contents {{ i }}</b-container>
			</b-tab>
			<template slot="tabs">
				<b-nav-item class="flex-grow-0 ml-auto" @click.prevent="newTab">
					<div class="home-newtab">
						<font-awesome-icon class="fa-fw" :icon="['fas', 'plus']" />
					</div>
				</b-nav-item>
			</template>
			<div slot="empty"></div>
		</b-tabs>
	</div>
</template>

<script>
export default {
	name: 'Home',
	data() {
		return {
			tabs: [],
			tabCounter: 0
		};
	},
	methods: {
		newTab() {
			this.tabs.push(this.tabCounter++);
		},
		closeTab(x) {
			for (let i = 0; i < this.tabs.length; i++) {
				if (this.tabs[i] === x) {
					this.tabs.splice(i, 1);
				}
			}
		}
	}
};
</script>

<style scoped lang="scss">
.home {
	.home-carousel {
		text-shadow: 1px 1px 2px #333;

		&::v-deep img {
			min-height: rem(192);
			object-fit: cover;
		}
	}

	.home-tabs ::v-deep {
		border: 0;

		.home-tablist {
			max-height: rem(144);
			background-color: map-get($theme-colors, 'primary');
			overflow-y: auto;

			.nav-item {
				max-width: rem(384);
			}

			.nav-item > .nav-link {
				padding: 0;

				.home-tab,
				.home-newtab {
					position: relative;
					display: flex;
					align-items: center;
					justify-content: center;
					height: rem(46);
					color: map-get($theme-colors, 'light');
					background-color: map-get($theme-colors, 'primary');
				}

				.home-tab {
					padding: 0 rem(50);
				}

				.home-newtab {
					padding: 0 rem(20);
				}

				.home-closetab {
					display: none;
					position: absolute;
					right: 0;
				}

				&.active {
					.home-tab,
					.home-newtab {
						color: map-get($theme-colors, 'primary');
						background-color: map-get($theme-colors, 'light');
					}

					.home-closetab {
						display: block;
					}
				}
			}
		}
	}
}
</style>
