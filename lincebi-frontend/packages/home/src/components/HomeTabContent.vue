<template>
	<div class="home-tab-content">
		<div v-if="tab && !tab.isContentDraggable" class="home-card-order">
			<div class="input-group input-group-sm">
				<select v-model="sort.selected" class="form-control">
					<option v-for="option in sort.options" :key="option.value" :value="option.value">
						{{ option.text }}
					</option>
				</select>
				<div class="input-group-append">
					<button class="btn btn-primary" type="button" @click="sort.asc = !sort.asc">
						<font-awesome-icon :icon="['fas', `sort-amount-${sort.asc ? 'up' : 'down'}`]" />
					</button>
				</div>
			</div>
		</div>
		<div v-if="files.length === 0" class="home-tab-empty">
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
		<home-card-deck v-else :files.sync="files" :draggable="tab?.isContentDraggable" />
	</div>
</template>

<script>
import fuzzyEquals from '@lincebi/frontend-common/src/fuzzyEquals';
import safeJSON from '@lincebi/frontend-common/src/safeJSON';
import stringCompare from '@lincebi/frontend-common/src/stringCompare';

import store from '@/store';

import HomeCardDeck from '@/components/HomeCardDeck';

export default {
	name: 'HomeTabContent',
	components: {
		HomeCardDeck,
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
				asc: false,
				options: [
					{
						text: this.$t('home.sort.title'),
						value: 'title',
						type: String,
					},
					{
						text: this.$t('home.sort.extension'),
						value: 'extension',
						type: String,
					},
					{
						text: this.$t('home.sort.created'),
						value: 'created',
						type: Date,
					},
					{
						text: this.$t('home.sort.modified'),
						value: 'modified',
						type: Date,
					},
				],
			},
		};
	},
	computed: {
		isRepositoryLoading() {
			return store.state.isRepositoryLoading;
		},
		files: {
			get() {
				const files = [];

				if (!this.tab) {
					return files;
				}

				if (this.tab.type === 'global') {
					const setting = store.state.globalUserSettings[this.tab.type];
					const entries = safeJSON.parse(setting, []);
					for (const entry of entries) {
						if (store.getters.repositoryMap.has(entry.fullPath)) {
							files.push(store.getters.repositoryMap.get(entry.fullPath));
						}
					}
				} else if (
					this.tab.type === 'home' ||
					this.tab.type === 'favorites' ||
					this.tab.type === 'recent'
				) {
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

					if (this.sort) {
						const asc = this.sort.asc;
						const option = this.sort.options.find((options) => {
							return options.value === this.sort.selected;
						});
						const fallback = this.sort.options[0];
						files.sort((a, b) => {
							const comparison = stringCompare(a[option.value], b[option.value], option.type, asc);
							if (comparison === 0 && option !== fallback) {
								return stringCompare(a[fallback.value], b[fallback.value], fallback.type, asc);
							}
							return comparison;
						});
					}
				}

				return files;
			},
			set(files) {
				if (!this.tab) {
					return;
				}

				if (this.tab.type === 'global') {
					const entries = files.map((file) => ({
						fullPath: file.path,
						title: file.title,
						lastUse: Date.now(),
					}));
					store.dispatch('updateGlobalUserSettings', {
						[this.tab.type]: safeJSON.stringify(entries, '[]'),
					});
				} else if (
					this.tab.type === 'home' ||
					this.tab.type === 'favorites' ||
					this.tab.type === 'recent'
				) {
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
.home-tab-content {
	display: flex;
	flex-grow: 1;
	flex-shrink: 1;
	flex-basis: auto;
	flex-direction: column;
	position: relative;

	padding: rem(20);

	.home-card-order {
		margin-bottom: rem(20);
		align-self: flex-end;
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
	}

	.loading {
		@include loading();
	}
}
</style>
