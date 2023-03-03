<template>
	<div :id="`home-tab-content-deck-${uniqueId}`" class="home-tab-content-deck">
		<div v-if="tab?.type === 'tag'" class="card-deck-order">
			<div class="input-group input-group-sm">
				<select v-model="sort.selected" class="form-control">
					<option v-for="option in sort.options" :key="option.value" :value="option.value">
						{{ option.text }}
					</option>
				</select>
				<div class="input-group-append">
					<button class="btn btn-primary" type="button" @click="sort.asc = !sort.asc">
						<font-awesome-icon
							:icon="['fas', `arrow-down-${sort.asc ? 'short-wide' : 'wide-short'}`]"
						/>
					</button>
				</div>
			</div>
		</div>
		<div v-if="files.length > 0" ref="deck" class="card-deck">
			<div
				v-for="file in files"
				:key="file.id"
				:class="{ card: true, draggable }"
				tabindex="0"
				@click="onFileOpenClick(file)"
				@keyup.enter="onFileOpenClick(file)"
			>
				<div class="card-container">
					<img class="card-img" :src="getFileThumbnail(file)" @contextmenu.stop.prevent />
					<div
						:id="`card-body-${uniqueId}-${file.id}`"
						class="card-body"
						:title="file.title"
						@click.stop.prevent
						@keyup.enter.stop.prevent
					>
						<h5 class="card-title m-0">
							<font-awesome-icon
								:class="['fa-fw', 'mr-1', getFileColorClass(file)]"
								:icon="['fac', getFileIconName(file)]"
							/>
							{{ file.title }}
						</h5>
					</div>
					<div class="card-toolbar">
						<div class="btn-group btn-group-sm">
							<div
								v-if="!file.isReadonly"
								class="btn btn-dark"
								tabindex="0"
								@click.stop="onFileMetadataEditClick(file)"
								@keyup.enter.stop="onFileMetadataEditClick(file)"
							>
								<font-awesome-icon :icon="['fas', 'list']" />
							</div>
							<div
								v-if="!file.isReadonly && file.editUrl"
								class="btn btn-dark"
								tabindex="0"
								@click.stop="onFileEditClick(file)"
								@keyup.enter.stop="onFileEditClick(file)"
							>
								<font-awesome-icon :icon="['fas', 'pencil']" />
							</div>
							<div
								v-if="draggable"
								class="btn btn-dark drag-handle"
								tabindex="-1"
								@click.stop.prevent
								@contextmenu.stop.prevent
							>
								<font-awesome-icon :icon="['fas', 'up-down-left-right']" />
							</div>
						</div>
					</div>
				</div>
				<b-popover
					v-if="file.description.length > 0"
					:target="`card-body-${uniqueId}-${file.id}`"
					:container="`home-tab-content-deck-${uniqueId}`"
					:title="file.title"
					:content="file.description"
					:triggers="['hover', 'focus']"
					:delay="100"
					placement="top"
				/>
			</div>
		</div>
		<div v-else class="card-deck-empty">
			<div v-if="isRepositoryLoading" class="loading"></div>
			<div v-else>
				<div class="icon">
					<font-awesome-icon class="fa-fw" :icon="['far', 'file-lines']" />
				</div>
				<div class="text">
					{{ $t('home.filesWillAppearHere') }}
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import Sortable from 'sortablejs';

import orderBy from 'lodash/orderBy';

import { library as faLibrary } from '@fortawesome/fontawesome-svg-core';

import fuzzyEquals from '@lincebi/frontend-common/src/fuzzyEquals';
import generateSvg from '@lincebi/frontend-common/src/generateSvg';
import isSmallScreen from '@lincebi/frontend-common/src/isSmallScreen';
import move from '@lincebi/frontend-common/src/move';
import safeJSON from '@lincebi/frontend-common/src/safeJSON';
import waitFor from '@lincebi/frontend-common/src/waitFor';

import eventBus from '@/eventBus';
import router from '@/router';
import store from '@/store';

export default {
	name: 'HomeTabContentDeck',
	props: {
		tab: {
			type: Object,
			default: () => null,
		},
	},
	data() {
		return {
			// Sort criteria.
			sort: {
				selected: 'title',
				asc: true,
				options: [
					{
						text: this.$t('home.sort.title'),
						value: 'title',
						iteratee: (a) => a.title,
					},
					{
						text: this.$t('home.sort.extension'),
						value: 'extension',
						iteratee: (a) => a.extension,
					},
					{
						text: this.$t('home.sort.created'),
						value: 'created',
						iteratee: (a) => new Date(a.created),
					},
					{
						text: this.$t('home.sort.modified'),
						value: 'modified',
						iteratee: (a) => new Date(a.modified),
					},
				],
			},
			// Sortable.js object.
			sortable: null,
		};
	},
	computed: {
		canAdminister() {
			return store.state.canAdminister;
		},
		isRepositoryLoading() {
			return store.state.isRepositoryLoading;
		},
		draggable() {
			return (
				(this.tab?.type === 'home' && this.canAdminister) ||
				this.tab?.type === 'favorites' ||
				this.tab?.type === 'recent'
			);
		},
		files: {
			get() {
				let files = [];

				if (!this.tab) {
					return files;
				}

				if (this.tab.type === 'home') {
					const setting = store.state.globalUserSettings[this.tab.type];
					const entries = safeJSON.parse(setting, []);
					for (const entry of entries) {
						if (store.getters.repositoryMap.has(entry.fullPath)) {
							files.push(store.getters.repositoryMap.get(entry.fullPath));
						}
					}
				} else if (this.tab.type === 'favorites' || this.tab.type === 'recent') {
					const setting = store.state.userSettings[this.tab.type];
					const entries = safeJSON.parse(setting, []);
					for (const entry of entries) {
						if (store.getters.repositoryMap.has(entry.fullPath)) {
							files.push(store.getters.repositoryMap.get(entry.fullPath));
						}
					}
				} else if (this.tab.type === 'tag') {
					for (const [, file] of store.getters.repositoryMap) {
						if (
							!file.isFolder &&
							file.properties.tags &&
							file.properties.tags.some((tag) => fuzzyEquals(tag.value, this.tab.name))
						) {
							files.push(file);
						}
					}

					let iteratee;
					const remIteratees = [];
					for (const option of this.sort.options) {
						if (option.value === this.sort.selected) iteratee = option.iteratee;
						else remIteratees.push(option.iteratee);
					}
					files = orderBy(
						files,
						[iteratee, ...remIteratees],
						Array(this.sort.options.length).fill(this.sort.asc ? 'asc' : 'desc')
					);
				}

				return files;
			},
			set(files) {
				if (!this.tab) {
					return;
				}

				if (this.tab.type === 'home') {
					const entries = files.map((file) => ({
						fullPath: file.path,
						title: file.title,
						lastUse: Date.now(),
					}));
					store.dispatch('updateGlobalUserSettings', {
						[this.tab.type]: safeJSON.stringify(entries, '[]'),
					});
				} else if (this.tab.type === 'favorites' || this.tab.type === 'recent') {
					const entries = files.map((file) => ({
						fullPath: file.path,
						title: file.title,
						lastUse: Date.now(),
					}));
					store.dispatch('updateUserSettings', {
						[this.tab.type]: safeJSON.stringify(entries, '[]'),
					});
				} else if (this.tab.type === 'tag') {
					// Unimplemented.
				}
			},
		},
	},
	mounted() {
		this.$nextTick(() => {
			this.updateSortable();
		});
	},
	updated() {
		this.$nextTick(() => {
			this.updateSortable();
		});
	},
	beforeDestroy() {
		if (this.sortable?.el) {
			this.sortable.destroy();
		}
	},
	methods: {
		getFileIconName(file) {
			const faDefs = faLibrary.definitions;
			return faDefs.fac && faDefs.fac[`file-${file.extension}`]
				? `file-${file.extension}`
				: 'file-other';
		},
		getFileColorClass(file) {
			return `text-${this.getFileIconName(file)}`;
		},
		getFileThumbnail(file) {
			return file.properties.thumbnail ? file.properties.thumbnail : generateSvg(file.path, 0);
		},
		onFileOpenClick(file) {
			const embedded =
				file.properties.embedded === 'true' ||
				(file.properties.embedded !== 'false' && !isSmallScreen);
			if (embedded) {
				router
					.push({
						name: 'perspective',
						params: { perspective: 'opened.perspective' },
					})
					.catch(() => {});
				eventBus.$emitWhenAvailable('mantle-invoke', (mantleWindow) => {
					mantleWindow.mantle_openRepositoryFile(file.path, 'RUN');
				});
			} else {
				window.open(file.openUrl, `lincebi_open_${file.id}`, 'noopener');
			}
		},
		onFileEditClick(file) {
			const embedded =
				file.properties.embedded === 'true' ||
				(file.properties.embedded !== 'false' && !isSmallScreen);
			if (embedded) {
				router
					.push({
						name: 'perspective',
						params: { perspective: 'opened.perspective' },
					})
					.catch(() => {});
				eventBus.$emitWhenAvailable('mantle-invoke', (mantleWindow) => {
					mantleWindow.mantle_openRepositoryFile(file.path, 'EDIT');
				});
			} else {
				window.open(file.editUrl, `lincebi_edit_${file.id}`, 'noopener');
			}
		},
		onFileMetadataEditClick(file) {
			router
				.push({
					name: 'perspective',
					params: { perspective: 'search.perspective' },
				})
				.catch(() => {});
			eventBus.$emitWhenAvailable(
				'mantle-perspective-invoke',
				'search.perspective',
				async (perspectiveWindow) => {
					const STSearch = await waitFor(() => perspectiveWindow.STSearch);
					await STSearch.applyConfig({ 'form-file-path': file.path }, true);
				}
			);
		},
		updateSortable() {
			if (this.sortable?.el) {
				this.sortable.destroy();
			}

			if (this.draggable && this.$refs.deck) {
				this.sortable = Sortable.create(this.$refs.deck, {
					forceFallback: false,
					delay: 10,
					animation: 150,
					draggable: '.card.draggable',
					handle: '.drag-handle',
					onMove: (event) => event.related.classList.contains('draggable'),
					onUpdate: (event) => {
						this.files = move(this.files.slice(), event.oldIndex, event.newIndex);
					},
				});
			}
		},
	},
};
</script>

<style scoped lang="scss">
.home-tab-content-deck {
	display: flex;
	flex-grow: 1;
	flex-shrink: 1;
	flex-basis: auto;
	flex-direction: column;
	position: relative;
	padding: rem(20);

	@include sortablejs-rtl-fix();

	.card-deck-order {
		margin-bottom: rem(20);
		align-self: flex-end;
	}

	.card-deck-empty {
		display: flex;
		flex-grow: 1;
		flex-shrink: 1;
		flex-basis: auto;
		align-items: center;
		justify-content: center;
		flex-direction: column;

		text-align: center;
		font-weight: 700;
		color: map-get($theme-colors, 'secondary');

		.icon {
			padding: rem(5);
			font-size: rem(80);
		}

		.text {
			padding: rem(5);
			font-size: rem(24);
		}

		.loading {
			@include loading();
		}
	}

	.card {
		margin-bottom: $grid-gutter-width;

		flex-grow: 0;
		flex-shrink: 1;
		flex-basis: calc(100% / 1 - #{$grid-gutter-width});

		@for $i from 0 through 10 {
			@media (min-width: (672px + (320px * $i))) {
				flex-basis: calc(100% / #{$i + 2} - #{$grid-gutter-width});
			}
		}

		cursor: pointer;
		user-select: none;

		&:not(.sortable-drag) {
			transform: scale(1);
			transition: transform 200ms ease-in;

			&:focus,
			&:focus-within,
			&:hover {
				transform: scale(1.05);

				.card-toolbar .btn {
					opacity: 1;
				}
			}
		}

		.card-container {
			position: relative;
			padding-top: calc((9 / 16) * 100%);
			height: 0;
			width: 100%;

			.card-body {
				position: absolute;
				left: 0;
				right: 0;
				bottom: 0;
				padding: rem(10);
				max-height: 50%;
				overflow: auto;

				color: map-get($theme-colors, 'dark');
				background-color: rgba(map-get($theme-colors, 'light'), 0.9);

				.card-title {
					display: -moz-box;
					display: -webkit-box;
					font-size: rem(16);
					line-height: rem(22);
					line-clamp: 2;
					-moz-line-clamp: 2;
					-webkit-line-clamp: 2;
					line-clamp: 2;
					-moz-box-orient: vertical;
					-webkit-box-orient: vertical;
					box-orient: horizontal;
					overflow: hidden;
				}
			}

			.card-img {
				position: absolute;
				top: 0;
				height: 100%;
				width: 100%;
				object-fit: cover;
			}

			.card-toolbar {
				position: absolute;
				top: rem(5);
				right: rem(5);

				.btn {
					opacity: 0;
					transition: opacity 200ms ease-in;

					@include button-variant(
						rgba(map-get($theme-colors, 'dark'), 0.6),
						rgba(map-get($theme-colors, 'dark'), 0.6)
					);
				}

				.btn:focus,
				.btn:hover {
					opacity: 1;
				}
			}
		}
	}

	:deep(.popover) {
		.arrow::after {
			border-bottom-color: map-get($theme-colors, 'primary');
		}

		.popover-header {
			background-color: map-get($theme-colors, 'primary');
			border-bottom-color: darken(map-get($theme-colors, 'primary'), 10%);
			color: $yiq-text-light;

			&::before {
				border: none;
			}
		}
	}
}
</style>
