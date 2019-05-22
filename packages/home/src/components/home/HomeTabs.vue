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
					draggable: tab.isDraggable
				}"
			>
				<a
					:title="tab.name"
					:class="{ 'nav-link': true, active: index === tabIndex }"
					@click="tabIndex = index"
					@contextmenu="onTabContextmenu"
					href="javascript:void(0)"
				>
					<font-awesome-icon
						v-if="tab.icon"
						:icon="tab.icon"
						class="fa-fw mr-2"
					/>
					<span class="text-truncate">
						{{ tab.name }}
					</span>
					<button
						v-if="tab.isRemovable"
						type="button"
						class="home-tab-close btn btn-link"
						@click="closeTabModalShow = true"
					>
						<font-awesome-icon :icon="['fas', 'times']" />
					</button>
				</a>
				<div class="home-tab-marker" :style="{ borderColor: tab.color }"></div>
			</li>
			<!-- New tab -->
			<li class="home-tab-new nav-item">
				<a
					class="nav-link"
					@click="newTabModalShow = true"
					href="javascript:void(0)"
				>
					<font-awesome-icon :icon="['fas', 'plus']" />
				</a>
			</li>
		</ul>
		<!-- Loading content -->
		<home-tab-empty
			v-if="isRepositoryLoading"
			:icon="['fas', 'sync']"
			:text="$t('home.loadingFiles')"
			spin
		/>
		<!-- No tabs content -->
		<home-tab-empty
			v-else-if="tabs.length === 0"
			:icon="['far', 'window-restore']"
			:text="$t('home.useButtonToCreateTab')"
		/>
		<!-- No files content -->
		<home-tab-empty
			v-else-if="files.length === 0"
			:icon="['far', 'file-alt']"
			:text="$t('home.filesWillAppearHere')"
		/>
		<!-- Normal content -->
		<div v-else class="home-tab-content">
			<div v-if="currentTab.sort" class="home-card-order">
				<div class="input-group">
					<select class="form-control" v-model="currentTab.sort.selected">
						<option
							v-for="option in sort.options"
							:key="option.value"
							:value="option.value"
						>
							{{ option.text }}
						</option>
					</select>
					<div class="input-group-append">
						<button
							class="btn btn-primary"
							type="button"
							@click="currentTab.sort.asc = !currentTab.sort.asc"
						>
							<font-awesome-icon
								:icon="[
									'fas',
									`sort-amount-${currentTab.sort.asc ? 'up' : 'down'}`
								]"
							/>
						</button>
					</div>
				</div>
			</div>
			<div class="home-card-deck card-deck">
				<div
					v-for="file in files"
					:key="file.id"
					:class="{
						'home-card': true,
						card: true,
						draggable: currentTab.isContentDraggable
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
		<!-- New tab modal -->
		<b-modal
			v-model="newTabModalShow"
			:title="$t('home.createTab')"
			ok-variant="primary"
			:ok-title="$t('home.create')"
			cancel-variant="secondary"
			:cancel-title="$t('home.cancel')"
			@ok="handleNewTabModalOk"
			centered
		>
			<form ref="new-tab-form" @submit.stop.prevent="handleNewTabFormSubmit">
				<b-form-group :label="$t('home.tabName.label')">
					<b-form-input
						type="text"
						v-model="newTab.name"
						:placeholder="$t('home.tabName.placeholder')"
						:list="`new-tab-name-${uniqueId}`"
						autofocus
						required
					/>
					<datalist :id="`new-tab-name-${uniqueId}`">
						<option v-for="tab in localTabs" :key="tab.name">
							{{ tab.name }}
						</option>
					</datalist>
				</b-form-group>
				<b-form-group :label="$t('home.tabColor.label')">
					<b-form-color-swatch v-model="newTab.color" />
				</b-form-group>
				<b-form-group :label="$t('home.tabIcon.label')">
					<b-form-icon-swatch v-model="newTab.icon" />
				</b-form-group>
			</form>
		</b-modal>
		<!-- Close tab modal -->
		<b-modal
			v-model="closeTabModalShow"
			:title="$t('home.deleteTab')"
			ok-variant="danger"
			:ok-title="$t('home.delete')"
			cancel-variant="secondary"
			:cancel-title="$t('home.cancel')"
			@ok="handleCloseTabModalOk"
			centered
		>
			{{ $t('home.tabWillBeDeleted', { name: currentTab.name }) }}
		</b-modal>
	</div>
</template>

<script>
import Sortable from 'sortablejs';

import cloneDeep from 'lodash/cloneDeep';
import differenceWith from 'lodash/differenceWith';

import fuzzyEquals from '@stratebi/biserver-customization-common/src/fuzzyEquals';
import generateSvg from '@stratebi/biserver-customization-common/src/generateSvg';
import move from '@stratebi/biserver-customization-common/src/move';
import safeJSON from '@stratebi/biserver-customization-common/src/safeJSON';
import stringCompare from '@stratebi/biserver-customization-common/src/stringCompare';

import BFormColorSwatch from '@stratebi/biserver-customization-common/src/components/BFormColorSwatch.vue';
import BFormIconSwatch from '@stratebi/biserver-customization-common/src/components/BFormIconSwatch.vue';

import HomeTabEmpty from '@/components/home/HomeTabEmpty';

import store from '@/store';

export default {
	name: 'HomeTabs',
	components: {
		BFormColorSwatch,
		BFormIconSwatch,
		HomeTabEmpty
	},
	data() {
		return {
			// Selected tab index.
			tabIndex: 0,
			// Local tabs are populated with remote tabs.
			localTabs: [],
			// Fixed tabs will never be stored server side.
			fixedTabs: [
				{
					type: 'global',
					name: this.$t('home.global'),
					color: 'transparent',
					icon: { prefix: 'fas', iconName: 'globe' },
					isRemovable: false,
					isDraggable: false,
					isContentDraggable: true
				},
				{
					type: 'home',
					name: this.$t('home.home'),
					color: 'transparent',
					icon: { prefix: 'fas', iconName: 'home' },
					isRemovable: false,
					isDraggable: false,
					isContentDraggable: true
				}
			],
			// New tab template.
			newTab: {
				type: 'tag',
				name: '',
				color: 'transparent',
				icon: null,
				isRemovable: true,
				isDraggable: true,
				isContentDraggable: false,
				sort: { asc: false, selected: 'title' }
			},
			// Sort criteria.
			sort: {
				options: [
					{
						text: this.$t('home.sort.title'),
						value: 'title',
						type: String
					},
					{
						text: this.$t('home.sort.extension'),
						value: 'extension',
						type: String
					},
					{
						text: this.$t('home.sort.created'),
						value: 'created',
						type: Date
					},
					{
						text: this.$t('home.sort.modified'),
						value: 'modified',
						type: Date
					}
				]
			},
			// Variables to control the display of modals.
			newTabModalShow: false,
			closeTabModalShow: false,
			// Map of elements associated to Sortable.js objects.
			sortables: new Map()
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
		currentTab() {
			return this.tabIndex >= 0 && this.tabIndex < this.tabs.length
				? this.tabs[this.tabIndex]
				: null;
		},
		files: {
			get() {
				const files = [];

				if (!this.currentTab) {
					return files;
				}

				if (this.currentTab.type === 'global') {
					const setting = store.state.globalUserSettings.global;
					const entries = safeJSON.parse(setting, []);
					for (const entry of entries) {
						if (store.getters.repositoryMap.has(entry.fullPath)) {
							files.push(store.getters.repositoryMap.get(entry.fullPath));
						}
					}
				} else if (this.currentTab.type === 'home') {
					const setting = store.state.userSettings.home;
					const entries = safeJSON.parse(setting, []);
					for (const entry of entries) {
						if (store.getters.repositoryMap.has(entry.fullPath)) {
							files.push(store.getters.repositoryMap.get(entry.fullPath));
						}
					}
				} else if (this.currentTab.type === 'tag') {
					for (const [, file] of store.getters.repositoryMap) {
						if (
							!file.isFolder &&
							file.properties.tags &&
							file.properties.tags.some(tag =>
								fuzzyEquals(tag.value, this.currentTab.name)
							)
						) {
							files.push(file);
						}
					}
				}

				if (this.currentTab.sort) {
					const asc = this.currentTab.sort.asc;
					const option = this.sort.options.find(options => {
						return options.value === this.currentTab.sort.selected;
					});
					const fallback = this.sort.options[0];
					files.sort((a, b) => {
						const comparison = stringCompare(
							a[option.value],
							b[option.value],
							option.type,
							asc
						);

						if (comparison === 0 && option !== fallback) {
							return stringCompare(
								a[fallback.value],
								b[fallback.value],
								fallback.type,
								asc
							);
						}

						return comparison;
					});
				}

				return files;
			},
			set(files) {
				if (!this.currentTab) {
					return;
				}

				if (this.currentTab.type === 'global') {
					const entries = files.map(file => ({
						fullPath: file.path,
						title: file.title,
						lastUse: Date.now()
					}));
					store.dispatch('updateGlobalUserSettings', {
						global: safeJSON.stringify(entries, '[]')
					});
				} else if (this.currentTab.type === 'home') {
					const entries = files.map(file => ({
						fullPath: file.path,
						title: file.title,
						lastUse: Date.now()
					}));
					store.dispatch('updateUserSettings', {
						home: safeJSON.stringify(entries, '[]')
					});
				} else if (this.currentTab.type === 'tag') {
					// Unimplemented.
				}
			}
		},
		isRepositoryLoading() {
			return store.state.isRepositoryLoading;
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

			// Create Sortable.js object for "home-tab-list" element if it does not exist on the map.
			const $homeTabList = $homeTabs.querySelector('.home-tab-list');
			if (!this.sortables.has($homeTabList)) {
				this.sortables.set(
					$homeTabList,
					Sortable.create($homeTabList, {
						delay: 10,
						animation: 150,
						draggable: '.home-tab.draggable',
						onStart: event => (this.tabIndex = event.oldIndex),
						onEnd: event => (this.tabIndex = event.newIndex),
						onMove: event => event.related.classList.contains('draggable'),
						onUpdate: event => {
							this.tabs = move(
								this.tabs.slice(),
								event.oldIndex,
								event.newIndex
							);
						}
					})
				);
			}

			// Create Sortable.js objects for all "home-card-deck" elements that do not exist on the map.
			const $homeCardDecks = $homeTabs.querySelectorAll('.home-card-deck');
			$homeCardDecks.forEach($homeCardDeck => {
				if (!this.sortables.has($homeCardDeck)) {
					this.sortables.set(
						$homeCardDeck,
						Sortable.create($homeCardDeck, {
							delay: 10,
							animation: 150,
							draggable: '.home-card.draggable',
							onMove: event => event.related.classList.contains('draggable'),
							onUpdate: event => {
								this.files = move(
									this.files.slice(),
									event.oldIndex,
									event.newIndex
								);
							}
						})
					);
				}
			});

			// Destroy Sortable.js objects that are no longer needed.
			this.sortables.forEach((sortable, $element) => {
				if (!$homeTabs.contains($element)) {
					this.sortables.delete($element);
					sortable.destroy();
				}
			});
		});
	},
	beforeDestroy() {
		// Destroy all Sortable.js objects.
		this.sortables.forEach(sortable => sortable.destroy());
	},
	methods: {
		handleNewTabModalOk(event) {
			event.preventDefault();
			this.handleNewTabFormSubmit();
		},
		handleNewTabFormSubmit() {
			if (this.$refs['new-tab-form'].reportValidity()) {
				this.createTab(cloneDeep(this.newTab));
				this.newTabModalShow = false;
				this.newTab.name = '';
			}
		},
		handleCloseTabModalOk() {
			this.removeTab(this.tabIndex);
		},
		createTab(newTab) {
			const newTabIndex = this.tabs.findIndex(tab => {
				return fuzzyEquals(tab.name, newTab.name);
			});

			if (newTabIndex === -1) {
				this.tabs = [...this.tabs, newTab];
				this.tabIndex = this.tabs.length - 1;
			} else {
				const foundTab = this.tabs[newTabIndex];
				// Replace tab if type equals tag.
				if (foundTab.type === 'tag') {
					const updatedTabs = this.tabs.slice();
					updatedTabs[newTabIndex] = newTab;
					this.tabs = updatedTabs;
				}
				this.tabIndex = newTabIndex;
			}
		},
		removeTab(removeTabIndex) {
			this.tabs = this.tabs.filter((tab, index) => {
				return index !== removeTabIndex || !tab.isRemovable;
			});
			if (this.tabIndex >= removeTabIndex) {
				this.tabIndex--;
			}
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

		// Hide side border of tabs.
		margin: 0 rem(-1);
		width: calc(100% + #{rem(1)});

		background-color: map-get($theme-colors, 'primary');
		border-bottom: rem(1) solid darken(map-get($theme-colors, 'primary'), 10%);
		box-shadow: $box-shadow;

		.home-tab {
			position: relative;
			max-width: rem(400);

			@media (max-width: rem(400)) {
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

			.home-tab-marker {
				position: absolute;
				left: rem(1);
				bottom: 0;
				height: 0;
				width: calc(100% - #{rem(1)});
				border-width: rem(3);
				border-style: solid;
				border-color: transparent;
				z-index: 15;
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

		.home-card-order {
			margin-bottom: rem(20);
			align-self: flex-end;
		}

		.card-deck {
			.card {
				margin-bottom: $grid-gutter-width;

				flex-grow: 0;
				flex-shrink: 1;
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
					text-shadow: rem(1) rem(1) rem(4) #000;
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
}
</style>
