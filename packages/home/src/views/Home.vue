<template>
	<div class="home">
		<b-carousel
			class="home-carousel"
			img-width="256px"
			img-height="256px"
			:interval="4000"
			indicators
			controls
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
			ref="tabs"
			class="home-tabs"
			nav-class="home-tablist"
			content-class="home-tabcontent"
			no-nav-style
			fill
		>
			<b-tab v-for="(name, index) in tabs" :key="`home-tab-${index}`" no-body>
				<template slot="title">
					<div class="home-tab">
						<span>{{ name }}</span>
						<b-button
							class="home-closetab"
							variant="link"
							@click="closeTab(name, index)"
						>
							<font-awesome-icon class="fa-fw" :icon="['fas', 'times']" />
						</b-button>
					</div>
				</template>
				<b-container class="py-5 px-4">{{ name }}</b-container>
			</b-tab>
			<template slot="tabs">
				<b-nav-item
					class="unsortable flex-grow-0 ml-auto"
					@click.prevent="newTab()"
				>
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
import Sortable from 'sortablejs';

import swap from '@stratebi/biserver-customization-common/src/swap';

export default {
	name: 'Home',
	data() {
		return {
			tabs: []
		};
	},
	updated() {
		this.$nextTick(() => {
			const $tablist = this.$refs.tabs.$el.querySelector('.nav');
			Sortable.create($tablist, {
				animation: 150,
				draggable: '.nav-item',
				filter: '.unsortable',
				onMove: event => !event.related.classList.contains('unsortable'),
				onUpdate: event => swap(this.tabs, event.oldIndex, event.newIndex)
			});
		});
	},
	methods: {
		newTab() {
			const $bForminput = this.$createElement('b-form-input');
			this.$bvModal
				.msgBoxConfirm($bForminput, {
					title: 'Enter category name',
					okVariant: 'primary',
					okTitle: 'Create',
					cancelTitle: 'Cancel',
					centered: true
				})
				.then(confirmed => {
					if (confirmed && $bForminput.elm.value.length) {
						this.tabs.push($bForminput.elm.value);
					}
				});
		},
		closeTab(name, index) {
			this.$bvModal
				.msgBoxConfirm(`The category "${name}" will be deleted.`, {
					title: 'Delete category',
					okVariant: 'danger',
					okTitle: 'Delete',
					cancelTitle: 'Cancel',
					centered: true
				})
				.then(confirmed => {
					if (confirmed) {
						this.tabs.splice(index, 1);
					}
				});
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
					border: 0 solid darken(map-get($theme-colors, 'primary'), 10%);
					border-right-width: rem(1);
					border-bottom-width: rem(1);
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
