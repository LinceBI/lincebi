<template>
	<div class="home-tab-content-files">
		<div v-if="tab?.type === 'tag'" class="files-order">
			<div class="input-group input-group-sm">
				<select v-model="sort.selected" class="form-control">
					<option v-for="option in sort.options" :key="option.value" :value="option.value">
						{{ option.text }}
					</option>
				</select>
				<div class="input-group-append">
					<button class="btn btn-primary" type="button" @click="sort.asc = !sort.asc">
						<font-awesome-icon :icon="['fas', `arrow-down-${sort.asc ? 'short-wide' : 'wide-short'}`]" />
					</button>
				</div>
			</div>
		</div>
		<div v-if="isRepositoryLoading" class="files-loading"></div>
		<div v-else-if="files.length === 0" class="files-empty">
			<div class="icon">
				<font-awesome-icon class="fa-fw" :icon="['far', 'file-lines']" />
			</div>
			<div class="text">
				{{ $t('home.filesWillAppearHere') }}
			</div>
		</div>
		<home-tab-content-files-deck v-else :files.sync="files" :draggable="draggable" />
	</div>
</template>

<script>
import orderBy from 'lodash/orderBy';

import fuzzyEquals from '@lincebi/frontend-common/src/fuzzyEquals';
import safeJSON from '@lincebi/frontend-common/src/safeJSON';

import store from '@/store';

import HomeTabContentFilesDeck from '@/components/HomeTabContentFilesDeck.vue';

export default {
	name: 'HomeTabContentFiles',
	components: {
		HomeTabContentFilesDeck,
	},
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
						Array(this.sort.options.length).fill(this.sort.asc ? 'asc' : 'desc'),
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
};
</script>

<style scoped lang="scss">
.home-tab-content-files {
	display: flex;
	flex-grow: 1;
	flex-shrink: 1;
	flex-basis: auto;
	flex-direction: column;
	position: relative;
	padding: toRem(20);

	.files-order {
		margin-bottom: toRem(20);
		align-self: flex-end;
	}

	.files-loading {
		@include loading();
	}

	.files-empty {
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
			padding: toRem(5);
			font-size: toRem(80);
		}

		.text {
			padding: toRem(5);
			font-size: toRem(24);
		}
	}
}
</style>
