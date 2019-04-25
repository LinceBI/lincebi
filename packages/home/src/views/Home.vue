<template>
	<div class="home">
		<b-carousel
			class="home-slider"
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
			@contextmenu.native="onTabContextmenu"
			no-nav-style
			fill
		>
			<b-tab v-for="(name, index) in tabs" :key="`home-tab-${index}`" no-body>
				<template slot="title">
					<div class="home-tab" :title="name">
						<span class="text-truncate">{{ name }}</span>
						<b-button
							class="home-closetab"
							variant="link"
							@click="closeTab(name, index)"
						>
							<font-awesome-icon :icon="['fas', 'times']" />
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
						<font-awesome-icon :icon="['fas', 'plus']" />
					</div>
				</b-nav-item>
			</template>
			<template slot="empty">
				<div class="home-emptytab">
					<div class="icon">
						<font-awesome-icon :icon="['far', 'window-restore']" />
					</div>
					<div class="text">
						{{ $t('home.usePlusToCreateTab') }}
					</div>
				</div>
			</template>
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
				delay: 10,
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
			const $bForminput = this.$createElement('b-form-input', {
				attrs: { placeholder: this.$t('home.categoryName.placeholder') }
			});
			this.$bvModal
				.msgBoxConfirm($bForminput, {
					title: this.$t('home.categoryName.label'),
					okVariant: 'primary',
					okTitle: this.$t('home.create'),
					cancelVariant: 'secondary',
					cancelTitle: this.$t('home.cancel'),
					centered: true
				})
				.then(confirmed => {
					if (confirmed && $bForminput.elm.value.length) {
						this.tabs.push($bForminput.elm.value);
					}
				});
		},
		closeTab(name, index) {
			const message = this.$t('home.categoryWillBeDeleted', { name });
			this.$bvModal
				.msgBoxConfirm(message, {
					title: this.$t('home.deleteCategory'),
					okVariant: 'danger',
					okTitle: this.$t('home.delete'),
					cancelVariant: 'secondary',
					cancelTitle: this.$t('home.cancel'),
					centered: true
				})
				.then(confirmed => {
					if (confirmed) {
						this.tabs.splice(index, 1);
					}
				});
		},
		onTabContextmenu(event) {
			if (event.target.closest('.home-tablist')) {
				event.preventDefault();
			}
		}
	}
};
</script>

<style scoped lang="scss">
.home {
	display: flex;
	flex-grow: 1;
	flex-shrink: 1;
	flex-basis: auto;
	flex-direction: column;

	height: 100%;

	.home-slider {
		display: flex;
		flex-grow: 0;
		flex-shrink: 1;
		flex-basis: auto;
		flex-direction: column;

		text-shadow: 1px 1px 2px #333;

		&::v-deep img {
			min-height: rem(192);
			object-fit: cover;
		}
	}

	.home-tabs {
		display: flex;
		flex-grow: 1;
		flex-shrink: 1;
		flex-basis: auto;
		flex-direction: column;

		border: 0;

		&::v-deep {
			.home-tablist {
				max-height: rem(144);
				background-color: map-get($theme-colors, 'primary');
				overflow-y: auto;

				.nav-item {
					max-width: rem(384);
					@media (max-width: rem(384)) {
						width: 100%;
					}
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
						white-space: nowrap;
						overflow: hidden;
						text-overflow: ellipsis;
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

			.home-tabcontent,
			.home-tabcontent > .tab-pane,
			.home-tabcontent > .tab-pane > .home-emptytab {
				height: 100%;
				width: 100%;
			}

			.home-emptytab {
				display: flex;
				align-items: center;
				justify-content: center;
				flex-direction: column;

				text-align: center;
				font-weight: bold;
				color: map-get($theme-colors, 'secondary');

				.icon {
					padding: rem(5);
					font-size: rem(80);
				}

				.text {
					padding: rem(5);
					font-size: rem(24);
				}
			}
		}
	}
}
</style>
