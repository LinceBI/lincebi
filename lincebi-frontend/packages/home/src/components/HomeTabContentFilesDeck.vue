<template>
	<div :id="`home-tab-content-files-deck-${uniqueId}`" ref="deck" class="home-tab-content-files-deck card-deck">
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
							v-if="canAdminister"
							class="btn btn-dark"
							tabindex="0"
							@click.stop="onFileHomeClick(file)"
							@keyup.enter.stop="onFileHomeClick(file)"
						>
							<font-awesome-icon :icon="['fac', file.isHome ? 'home-solid' : 'home-outline']" />
						</div>
						<div
							class="btn btn-dark"
							tabindex="0"
							@click.stop="onFileFavoriteClick(file)"
							@keyup.enter.stop="onFileFavoriteClick(file)"
						>
							<font-awesome-icon :icon="[file.isFavorite ? 'fas' : 'far', 'heart']" />
						</div>
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
							v-if="downloadableExtensions.has(file.extension)"
							class="btn btn-dark"
							tabindex="0"
							@click.stop="onFileDownloadClick(file)"
							@keyup.enter.stop="onFileDownloadClick(file)"
						>
							<font-awesome-icon :icon="['fas', 'file-arrow-down']" />
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
				:container="`home-tab-content-files-deck-${uniqueId}`"
				:title="file.title"
				:content="file.description"
				:triggers="['hover', 'focus']"
				:delay="100"
				placement="top"
			/>
		</div>
	</div>
</template>

<script>
import Sortable from 'sortablejs';

import { library as faLibrary } from '@fortawesome/fontawesome-svg-core';

import generateSvg from '@lincebi/frontend-common/src/generateSvg';
import isSmallScreen from '@lincebi/frontend-common/src/isSmallScreen';
import move from '@lincebi/frontend-common/src/move';
import waitFor from '@lincebi/frontend-common/src/waitFor';

import getFileResourceUrl from '@lincebi/frontend-common/src/biserver/getFileResourceUrl';
import getReportUrl from '@lincebi/frontend-common/src/biserver/getReportUrl';

import eventBus from '@/eventBus';
import router from '@/router';
import store from '@/store';

export default {
	name: 'HomeTabContentFilesDeck',
	props: {
		files: {
			type: Array,
			default: () => [],
		},
		draggable: {
			type: Boolean,
			default: false,
		},
	},
	data() {
		return {
			// Sortable.js object.
			sortable: null,
		};
	},
	computed: {
		canAdminister() {
			return store.state.canAdminister;
		},
		downloadableExtensions() {
			return store.state.downloadableExtensions;
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
			return faDefs.fac && faDefs.fac[`file-${file.extension}`] ? `file-${file.extension}` : 'file-other';
		},
		getFileColorClass(file) {
			return `text-${this.getFileIconName(file)}`;
		},
		getFileThumbnail(file) {
			return file.properties.thumbnail ? file.properties.thumbnail : generateSvg(file.path, 0);
		},
		onFileOpenClick(file) {
			const embedded = file.properties.embedded === 'true' || (file.properties.embedded !== 'false' && !isSmallScreen);
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
			const embedded = file.properties.embedded === 'true' || (file.properties.embedded !== 'false' && !isSmallScreen);
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
			const href = window.location.href;
			router
				.push({
					name: 'perspective',
					params: { perspective: 'search.perspective' },
				})
				.catch(() => {});
			eventBus.$emitWhenAvailable('mantle-perspective-invoke', 'search.perspective', async (perspectiveWindow) => {
				const STSearch = await waitFor(() => perspectiveWindow.STSearch);
				await STSearch.applyConfig({ 'form-file-path': file.path, 'form-return-href': href }, true);
			});
		},
		onFileHomeClick(file) {
			store.dispatch('updateRepositoryFile', {
				path: file.path,
				isHome: !file.isHome,
			});
		},
		onFileFavoriteClick(file) {
			store.dispatch('updateRepositoryFile', {
				path: file.path,
				isFavorite: !file.isFavorite,
			});
		},
		async onFileDownloadClick(file) {
			let url;
			let download;
			if (file.extension === 'prpt') {
				this.$bvToast.toast(this.$t('home.reportDownloadInfo'), {
					title: this.$t('home.notificationInfo'),
					variant: 'info',
					solid: true,
				});
				url = await getReportUrl(file.path, 'pageable/pdf');
				download = `${file.name.slice(0, -(file.extension.length + 1))}.pdf`;
			} else {
				url = `${await getFileResourceUrl(file.path)}/inline`;
				download = file.name;
			}
			if (url) {
				const anchor = document.createElement('a');
				anchor.setAttribute('href', url);
				anchor.setAttribute('target', '_blank');
				anchor.setAttribute('rel', 'noopener noreferrer');
				anchor.setAttribute('download', download);
				anchor.click();
			} else {
				this.$bvToast.toast(this.$t('home.fileDownloadError'), {
					title: this.$t('home.notificationError'),
					variant: 'danger',
					solid: true,
				});
			}
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
						const files = move(this.files.slice(), event.oldIndex, event.newIndex);
						this.$emit('update:files', files);
					},
				});
			}
		},
	},
};
</script>

<style scoped lang="scss">
.home-tab-content-files-deck {
	@include sortablejs-rtl-fix();

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
				padding: toRem(10);
				max-height: 50%;
				overflow: auto;

				color: map-get($theme-colors, 'dark');
				background-color: rgba(map-get($theme-colors, 'light'), 0.9);

				.card-title {
					display: -moz-box;
					display: -webkit-box;
					font-size: toRem(16);
					line-height: toRem(22);
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
				top: toRem(5);
				right: toRem(5);

				.btn {
					opacity: 0;
					transition: opacity 200ms ease-in;

					@include button-variant(rgba(map-get($theme-colors, 'dark'), 0.6), rgba(map-get($theme-colors, 'dark'), 0.6));
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
