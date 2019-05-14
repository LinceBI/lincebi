<template>
	<div class="home-tabs" ref="home-tabs">
		<!-- Tab list -->
		<ul class="home-tab-list nav nav-fill">
			<!-- Tabs -->
			<li
				v-for="(tab, index) in tabs"
				:key="tab.name"
				:class="{
					'home-tab': true,
					'nav-item': true,
					draggable: tab.draggable
				}"
			>
				<a
					href="javascript:void(0)"
					:title="tab.name"
					:class="{ 'nav-link': true, active: index === tabIndex }"
					@click="tabIndex = index"
					@contextmenu="onTabContextmenu"
				>
					<font-awesome-icon
						v-if="tab.icon"
						:icon="tab.icon"
						:class="tab.name ? ['fa-fw', 'mr-2'] : []"
					/>
					<span class="text-truncate">
						{{ tab.name }}
					</span>
					<button
						v-if="tab.removable"
						type="button"
						class="home-tab-close btn btn-link"
						@click="closeTab(tab, index)"
					>
						<font-awesome-icon :icon="['fas', 'times']" />
					</button>
				</a>
			</li>
			<!-- New tab -->
			<li class="home-tab-new nav-item">
				<a href="javascript:void(0)" class="nav-link" @click="newTab()">
					<font-awesome-icon :icon="['fas', 'plus']" />
				</a>
			</li>
		</ul>
		<!-- No tabs content -->
		<div v-if="tabs.length === 0" class="home-tab-empty">
			<div class="icon">
				<font-awesome-icon :icon="['far', 'window-restore']" />
			</div>
			<div class="text">
				{{ $t('home.useButtonToCreateTab') }}
			</div>
		</div>
		<!-- No files content -->
		<div v-else-if="files.length === 0" class="home-tab-empty">
			<div class="icon">
				<font-awesome-icon :icon="['far', 'file-alt']" />
			</div>
			<div class="text">
				{{ $t('home.filesWillAppearHere') }}
			</div>
		</div>
		<!-- Normal content -->
		<div v-else class="home-tab-content">
			<div class="home-card-deck card-deck">
				<div
					v-for="file in files"
					:key="file.id"
					:class="{
						'home-card': true,
						card: true,
						sortable: tabs[tabIndex].sortable
					}"
				>
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
		</div>
	</div>
</template>

<script>
import Sortable from 'sortablejs';

import differenceWith from 'lodash/differenceWith';

import fuzzyEquals from '@stratebi/biserver-customization-common/src/fuzzyEquals';
import generateSvg from '@stratebi/biserver-customization-common/src/generateSvg';
import move from '@stratebi/biserver-customization-common/src/move';
import safeJSON from '@stratebi/biserver-customization-common/src/safeJSON';

import store from '@/store';

export default {
	name: 'HomeTabs',
	data() {
		return {
			tabIndex: 0,
			localTabs: [],
			fixedTabs: [
				{
					name: this.$t('home.global'),
					icon: ['fas', 'globe-europe'],
					type: 'global',
					removable: false,
					draggable: false,
					sortable: true
				},
				{
					name: this.$t('home.home'),
					icon: ['fas', 'home'],
					type: 'home',
					removable: false,
					draggable: false,
					sortable: true
				}
			]
		};
	},
	computed: {
		tabs: {
			get() {
				// Concatenate fixed tabs and local tabs.
				return [...this.fixedTabs, ...this.localTabs];
			},
			set(tabs) {
				// Filter fixed tabs.
				this.localTabs = differenceWith(
					tabs,
					this.fixedTabs,
					(a, b) => a.name === b.name
				);

				// Update remote tabs.
				store.dispatch('updateUserSettings', {
					[`${this.namespace}.tabs`]: safeJSON.stringify(this.localTabs, '[]')
				});
			}
		},
		remoteTabs() {
			// Fetch remote tabs.
			return safeJSON.parse(
				store.state.userSettings[`${this.namespace}.tabs`],
				[]
			);
		},
		files: {
			get() {
				const files = [];

				if (this.tabIndex >= 0 && this.tabIndex < this.tabs.length) {
					const tab = this.tabs[this.tabIndex];

					if (tab.type === 'global') {
						const setting = store.state.globalUserSettings.global;
						const entries = safeJSON.parse(setting, []);
						for (const entry of entries) {
							if (store.getters.repositoryMap.has(entry.fullPath)) {
								files.push(store.getters.repositoryMap.get(entry.fullPath));
							}
						}
					} else if (tab.type === 'home') {
						const setting = store.state.userSettings.home;
						const entries = safeJSON.parse(setting, []);
						for (const entry of entries) {
							if (store.getters.repositoryMap.has(entry.fullPath)) {
								files.push(store.getters.repositoryMap.get(entry.fullPath));
							}
						}
					} else if (tab.type === 'tag') {
						for (const [, file] of store.getters.repositoryMap) {
							if (
								!file.isFolder &&
								file.properties.tags &&
								file.properties.tags.some(tag =>
									fuzzyEquals(tag.value, tab.name)
								)
							) {
								files.push(file);
							}
						}
					}
				}

				return files;
			},
			set(files) {
				if (this.tabIndex >= 0 && this.tabIndex < this.tabs.length) {
					const tab = this.tabs[this.tabIndex];

					if (tab.type === 'global') {
						const entries = files.map(file => ({
							fullPath: file.path,
							title: file.title,
							lastUse: Date.now()
						}));
						store.dispatch('updateGlobalUserSettings', {
							global: safeJSON.stringify(entries, '[]')
						});
					} else if (tab.type === 'home') {
						const entries = files.map(file => ({
							fullPath: file.path,
							title: file.title,
							lastUse: Date.now()
						}));
						store.dispatch('updateUserSettings', {
							home: safeJSON.stringify(entries, '[]')
						});
					} else if (tab.type === 'tag') {
						// Unimplemented.
					}
				}
			}
		}
	},
	watch: {
		remoteTabs(remoteTabs) {
			// Populate local tabs.
			this.localTabs = remoteTabs;
		}
	},
	updated() {
		this.$nextTick(() => {
			const $homeTabs = this.$refs['home-tabs'];

			const $tabList = $homeTabs.querySelector('.home-tab-list');
			if ($tabList !== null) {
				Sortable.create($tabList, {
					delay: 10,
					animation: 150,
					draggable: '.home-tab.draggable',
					onStart: event => (this.tabIndex = event.oldIndex),
					onEnd: event => (this.tabIndex = event.newIndex),
					onMove: event => event.related.classList.contains('draggable'),
					onUpdate: event => {
						this.tabs = move(this.tabs.slice(), event.oldIndex, event.newIndex);
					}
				});
			}

			const $cardDecks = $homeTabs.querySelectorAll('.home-card-deck');
			$cardDecks.forEach($cardDeck => {
				Sortable.create($cardDeck, {
					delay: 10,
					animation: 150,
					draggable: '.home-card.sortable',
					onMove: event => event.related.classList.contains('sortable'),
					onUpdate: event => {
						this.files = move(
							this.files.slice(),
							event.oldIndex,
							event.newIndex
						);
					}
				});
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
						const findIndex = this.tabs.findIndex(tab =>
							fuzzyEquals(tab.name, name)
						);
						if (findIndex === -1) {
							this.tabs = [
								...this.tabs,
								{
									name,
									type: 'tag',
									removable: true,
									draggable: true,
									sortable: false
								}
							];
							this.tabIndex = this.tabs.length - 1;
						} else {
							this.tabIndex = findIndex;
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
						this.tabIndex = this.tabs.length - 1;
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
.home-tabs {
	.home-tab-list {
		display: flex;
		flex-grow: 0;
		flex-shrink: 1;
		flex-basis: auto;
		flex-direction: row;

		max-height: rem(144);
		overflow-y: auto;

		background-color: map-get($theme-colors, 'primary');
		border-bottom: rem(1) solid darken(map-get($theme-colors, 'primary'), 10%);
		box-shadow: $box-shadow;

		.home-tab {
			max-width: rem(384);
			@media (max-width: rem(384)) {
				width: 100%;
			}

			.nav-link {
				position: relative;
				display: flex;
				align-items: center;
				justify-content: center;
				padding: 0 rem(50);
				height: rem(46);
				z-index: 10;

				white-space: nowrap;
				overflow: hidden;
				text-overflow: ellipsis;

				color: map-get($theme-colors, 'light');
				background-color: map-get($theme-colors, 'primary');

				@include border-collapse(
					darken(map-get($theme-colors, 'primary'), 10%)
				);

				.home-tab-close {
					position: absolute;
					display: none;
					right: 0;
				}

				&.active {
					color: map-get($theme-colors, 'primary');
					background-color: map-get($theme-colors, 'light');

					.home-tab-close {
						display: block;
					}
				}
			}
		}

		.home-tab-new {
			flex-grow: 0;

			.nav-link {
				position: relative;
				display: flex;
				align-items: center;
				justify-content: center;
				padding: 0 rem(20);
				height: rem(46);
				z-index: 5;

				color: map-get($theme-colors, 'light');
				background-color: map-get($theme-colors, 'primary');
			}
		}
	}

	.home-tab-content {
		display: flex;
		flex-grow: 1;
		flex-shrink: 1;
		flex-basis: auto;
		flex-direction: column;

		padding: rem(20);

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

				cursor: pointer;
				user-select: none;

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
		flex-grow: 1;
		flex-shrink: 1;
		flex-basis: auto;
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
