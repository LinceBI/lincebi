<template>
	<b-tabs
		ref="home-tabs"
		class="home-tabs"
		nav-class="home-tab-list"
		content-class="home-tab-content"
		@contextmenu.native="onTabContextmenu"
		no-nav-style
		fill
	>
		<b-tab v-for="(name, index) in tabs" :key="index" no-body>
			<template slot="title">
				<div class="home-tab" :title="name">
					<span class="text-truncate">{{ name }}</span>
					<b-button
						class="home-tab-close"
						variant="link"
						@click="closeTab(name, index)"
					>
						<font-awesome-icon :icon="['fas', 'times']" />
					</b-button>
				</div>
			</template>
			<b-container class="py-5 px-4" fluid>
				<b-card-group class="home-tab-card-group" deck>
					<b-card
						class="home-tab-card shadow"
						v-for="(file, index) in getFilesForTag(name)"
						:key="index"
						:img-alt="file.title"
						:img-src="getThumbnailOrDefault(file)"
						overlay
					>
						<b-tooltip :target="`file-${file.id}-title`">
							{{ file.title }}
						</b-tooltip>
						<b-card-title :id="`file-${file.id}-title`">
							<font-awesome-icon
								:class="['fa-fw', 'mr-1']"
								:icon="['fac', `file-${file.extension}`]"
							/>
							{{ file.title }}
						</b-card-title>
						<b-card-text>
							{{ file.description }}
						</b-card-text>
						<!--
						<b-card-footer>
							<b-button-group size="sm">
								<b-button variant="light">
									<font-awesome-icon :icon="['fas', 'edit']" />
								</b-button>
								<b-button variant="light">
									<font-awesome-icon :icon="['fas', 'external-link-alt']" />
								</b-button>
							</b-button-group>
						</b-card-footer>
						-->
					</b-card>
				</b-card-group>
			</b-container>
		</b-tab>
		<template slot="tabs">
			<b-nav-item
				class="unsortable flex-grow-0 ml-auto"
				@click.prevent="newTab()"
			>
				<div class="home-tab-new">
					<font-awesome-icon :icon="['fas', 'plus']" />
				</div>
			</b-nav-item>
		</template>
		<template slot="empty">
			<div class="home-tab-empty">
				<div class="icon">
					<font-awesome-icon :icon="['far', 'window-restore']" />
				</div>
				<div class="text">
					{{ $t('home.usePlusToCreateTab') }}
				</div>
			</div>
		</template>
	</b-tabs>
</template>

<script>
import Sortable from 'sortablejs';

import fuzzyEquals from '@stratebi/biserver-customization-common/src/fuzzyEquals';
import generateImage from '@stratebi/biserver-customization-common/src/generateImage';
import swap from '@stratebi/biserver-customization-common/src/swap';

import store from '@/store';

export default {
	name: 'HomeTabs',
	data() {
		return {
			tabs: ['Tag1', 'Tag2', 'Tag3']
		};
	},
	updated() {
		this.$nextTick(() => {
			const $tablist = this.$refs['home-tabs'].$el.querySelector('.nav');
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
			if (event.target.closest('.home-tab-list')) {
				event.preventDefault();
			}
		},
		getFilesForTag(tag) {
			return store.getters.flattenedRepository.filter(
				file =>
					Array.isArray(file.properties.tags) &&
					file.properties.tags.some(t => fuzzyEquals(t.value, tag))
			);
		},
		getThumbnailOrDefault(file) {
			return file.properties.thumbnail
				? file.properties.thumbnail
				: generateImage(file.path, 0);
		}
	}
};
</script>

<style scoped lang="scss">
.home-tabs::v-deep {
	.home-tab-list {
		max-height: rem(144);
		background-color: map-get($theme-colors, 'primary');
		border-bottom: rem(1) solid darken(map-get($theme-colors, 'primary'), 10%);
		box-shadow: $box-shadow;
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
			.home-tab-new {
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
				white-space: nowrap;
				overflow: hidden;
				text-overflow: ellipsis;
				z-index: 10;
				@include border-collapse(
					darken(map-get($theme-colors, 'primary'), 10%)
				);
			}

			.home-tab-new {
				padding: 0 rem(20);
				z-index: 5;
			}

			.home-tab-close {
				display: none;
				position: absolute;
				right: 0;
			}

			&.active {
				.home-tab,
				.home-tab-new {
					color: map-get($theme-colors, 'primary');
					background-color: map-get($theme-colors, 'light');
				}

				.home-tab-close {
					display: block;
				}
			}
		}
	}

	.home-tab-content,
	.home-tab-content > .tab-pane,
	.home-tab-content > .tab-pane > .home-tab-empty {
		height: 100%;
		width: 100%;
	}

	.home-tab-content {
		.home-tab-card-group {
			.home-tab-card {
				margin-bottom: $grid-gutter-width;

				flex-grow: 0;
				flex-shrink: 0;
				flex-basis: calc(#{100% / 1} - #{$grid-gutter-width});

				@include media-breakpoint-up(md) {
					flex-basis: calc(#{100% / 2} - #{$grid-gutter-width});
				}

				@include media-breakpoint-up(lg) {
					flex-basis: calc(#{100% / 3} - #{$grid-gutter-width});
				}

				@include media-breakpoint-up(xl) {
					flex-basis: calc(#{100% / 4} - #{$grid-gutter-width});
				}

				.card-body {
					color: #ffffff;
					text-shadow: 1px 1px 2px #333;
					background-color: rgba(0, 0, 0, 0.4);
					backdrop-filter: blur(rem(4));

					.card-title {
						overflow: hidden;
						white-space: nowrap;
						text-overflow: ellipsis;
					}

					.card-text {
						height: calc(100% - #{rem(75)});
						overflow: hidden;
					}

					.card-footer {
						padding: 0;
						border: none;
						background-color: transparent;
						text-align: right;

						.btn.btn-link {
							color: #ffffff;
						}
					}
				}

				.card-img {
					height: rem(256);
					object-fit: cover;
				}
			}
		}
	}

	.home-tab-empty {
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
</style>
