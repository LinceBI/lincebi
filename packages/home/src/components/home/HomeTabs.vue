<template>
	<b-tabs
		ref="home-tabs"
		class="home-tabs"
		nav-class="home-tab-list"
		content-class="home-tab-content"
		@contextmenu.native="onTabContextmenu"
		v-model="tabIndex"
		no-nav-style
		no-fade
		fill
	>
		<b-tab v-for="(tab, index) in tabs" :key="tab.name" no-body>
			<template slot="title">
				<div class="home-tab" :title="tab.name">
					<font-awesome-icon
						v-if="tab.icon"
						:icon="tab.icon"
						:class="tab.name ? ['fa-fw', 'mr-2'] : []"
					/>
					<span class="text-truncate">
						<span v-if="tab.translate">{{ $t(tab.name) }}</span>
						<span v-else>{{ tab.name }}</span>
					</span>
					<b-button
						v-if="tab.removable"
						class="home-tab-close"
						variant="link"
						@click="closeTab(tab, index)"
					>
						<font-awesome-icon :icon="['fas', 'times']" />
					</b-button>
				</div>
			</template>
			<b-container v-if="filteredFiles.length > 0" class="py-5 px-4" fluid>
				<div class="card-deck">
					<div class="card" v-for="file in filteredFiles" :key="file.id">
						<img
							class="card-img"
							:src="getThumbnailOrDefault(file)"
							:alt="file.title"
						/>
						<div class="card-body card-img-overlay">
							<h5 class="card-title">
								<font-awesome-icon
									:class="['fa-fw', 'mr-1']"
									:icon="['fac', `file-${file.extension}`]"
								/>
								{{ file.title }}
							</h5>
							<p class="card-text">{{ file.description }}</p>
							<div class="card-footer mr-n2">
								<div class="btn-group btn-group-sm">
									<a
										class="btn btn-link"
										rel="noopener"
										:href="file.openUrl"
										:target="file.id"
									>
										<font-awesome-icon
											:class="['fa-fw']"
											:icon="['fas', 'link']"
										/>
									</a>
								</div>
							</div>
						</div>
					</div>
				</div>
			</b-container>
			<div v-else class="home-tab-empty">
				<div class="icon">
					<font-awesome-icon :icon="['far', 'file-alt']" />
				</div>
				<div class="text">
					{{ $t('home.filesWillAppearHere') }}
				</div>
			</div>
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
					{{ $t('home.useButtonToCreateTab') }}
				</div>
			</div>
		</template>
	</b-tabs>
</template>

<script>
import Sortable from 'sortablejs';

import fuzzyEquals from '@stratebi/biserver-customization-common/src/fuzzyEquals';
import generateSvg from '@stratebi/biserver-customization-common/src/generateSvg';
import safeJSON from '@stratebi/biserver-customization-common/src/safeJSON';
import swap from '@stratebi/biserver-customization-common/src/swap';

import store from '@/store';

export default {
	name: 'HomeTabs',
	data() {
		return {
			tabIndex: 0
		};
	},
	computed: {
		userSettings() {
			return store.state.userSettings;
		},
		tabs: {
			get() {
				return safeJSON.parse(this.userSettings[`${this.namespace}.tabs`], []);
			},
			set(tabs) {
				store.dispatch('updateUserSettings', {
					[`${this.namespace}.tabs`]: safeJSON.stringify(tabs)
				});
			}
		},
		filteredFiles() {
			if (this.tabIndex >= 0 && this.tabIndex < this.tabs.length) {
				const tab = this.tabs[this.tabIndex];

				if (tab.type === 'global') {
					return store.getters.globalFiles;
				} else if (tab.type === 'home') {
					return store.getters.homeFiles;
				} else if (tab.type === 'tag') {
					return store.getters.repositoryFiles.filter(file => {
						return (
							Array.isArray(file.properties.tags) &&
							file.properties.tags.some(tag => fuzzyEquals(tag.value, tab.name))
						);
					});
				}
			}

			return [];
		}
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
				onUpdate: event => {
					const tabs = this.tabs.slice(0);
					this.tabs = swap(tabs, event.oldIndex, event.newIndex);
				}
			});
		});
	},
	methods: {
		newTab() {
			const $bForminput = this.$createElement('b-form-input', {
				attrs: { placeholder: this.$t('home.tabName.placeholder') }
			});
			this.$bvModal
				.msgBoxConfirm($bForminput, {
					title: this.$t('home.tabName.label'),
					okVariant: 'primary',
					okTitle: this.$t('home.create'),
					cancelVariant: 'secondary',
					cancelTitle: this.$t('home.cancel'),
					centered: true
				})
				.then(confirmed => {
					const name = $bForminput.elm.value;
					if (confirmed && name.length > 0) {
						const tabIndex = this.tabs.findIndex(tab =>
							fuzzyEquals(tab.name, name)
						);
						if (tabIndex === -1) {
							this.tabs = [
								...this.tabs,
								{ name, type: 'tag', removable: true }
							];
						} else {
							this.tabIndex = tabIndex;
						}
					}
				});
		},
		closeTab(tab, index) {
			const message = this.$t('home.tabWillBeDeleted', { name: tab.name });
			this.$bvModal
				.msgBoxConfirm(message, {
					title: this.$t('home.deleteTab'),
					okVariant: 'danger',
					okTitle: this.$t('home.delete'),
					cancelVariant: 'secondary',
					cancelTitle: this.$t('home.cancel'),
					centered: true
				})
				.then(confirmed => {
					if (confirmed) {
						this.tabs = this.tabs.filter((t, i) => i !== index);
					}
				});
		},
		onTabContextmenu(event) {
			if (event.target.closest('.home-tab-list')) {
				event.preventDefault();
			}
		},
		getThumbnailOrDefault(file) {
			return file.properties.thumbnail
				? file.properties.thumbnail
				: generateSvg(file.path, 0);
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
		.card-deck {
			.card {
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
					text-shadow: 1px 1px 4px #000;
					background-color: rgba(0, 0, 0, 0.6);
					transition: background-color 200ms ease-in;

					.card-title {
						overflow: hidden;
						white-space: nowrap;
						text-overflow: ellipsis;
					}

					.card-text {
						height: calc(100% - #{rem(75)});
						overflow: hidden;
						transition: opacity 200ms ease-in;
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
					filter: blur(rem(2));
					transition: filter 200ms ease-in;
				}

				&:hover {
					.card-body {
						background-color: rgba(0, 0, 0, 0.3);

						.card-text {
							opacity: 0;
						}
					}

					.card-img {
						filter: none;
					}
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
