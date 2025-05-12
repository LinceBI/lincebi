<template>
	<div class="home-tab-list">
		<!-- Tab nav -->
		<ul ref="home-tab-nav" class="home-tab-nav nav nav-fill">
			<!-- Tabs -->
			<li
				v-for="(t, index) in tabs"
				:key="getTabKey(t)"
				:class="{
					'home-tab': true,
					'nav-item': true,
					global: t.isGlobal,
					draggable: t.isDraggable && (!t.isGlobal || canAdminister),
					active: index === tabIndex,
				}"
				:style="getTabStyle(t, index)"
				:data-v-step="`home-tab-${t.type}`"
			>
				<div
					v-if="getTabVisibility(t)"
					:title="getTabDisplayName(t)"
					class="nav-link"
					tabindex="0"
					@click="tabIndex = index"
					@keyup.enter="tabIndex = index"
				>
					<font-awesome-icon v-if="t.icon" :icon="t.icon" class="fa-fw mr-2" />
					<span class="text-truncate">
						{{ getTabDisplayName(t) }}
					</span>
					<button
						v-if="t.isRemovable && (!t.isGlobal || canAdminister)"
						type="button"
						class="home-tab-close btn"
						@click="closeTabModalShow = true"
						@keyup.enter="closeTabModalShow = true"
					>
						<font-awesome-icon :icon="['fas', 'xmark']" />
					</button>
				</div>
			</li>
			<!-- New tab -->
			<li class="home-tab-new nav-item">
				<div
					class="nav-link"
					data-v-step="home-tab-new"
					tabindex="0"
					@click="newTabModalShow = true"
					@keyup.enter="newTabModalShow = true"
				>
					<font-awesome-icon :icon="['fas', 'plus']" />
				</div>
			</li>
		</ul>
		<!-- New tab modal -->
		<b-modal
			v-model="newTabModalShow"
			:title="$t('home.createTab')"
			ok-variant="primary"
			:ok-title="$t('home.create')"
			cancel-variant="secondary"
			:cancel-title="$t('home.cancel')"
			centered
			@ok="handleNewTabModalOk"
		>
			<form ref="new-tab-form" @submit.stop.prevent="handleNewTabFormSubmit">
				<b-form-group :label="$t('home.tabName.label')" label-class="d-flex">
					<b-form-input
						ref="new-tab-name"
						v-model="newTab.name"
						type="text"
						:placeholder="$t('home.tabName.placeholder')"
						:list="`new-tab-name-datalist-${uniqueId}`"
						required
						autofocus
						@change="fillNewTabForm"
					/>
					<b-form-datalist :id="`new-tab-name-datalist-${uniqueId}`">
						<option v-for="t in tabs.filter((t) => t.type === 'tag' || t.type === 'frame')" :key="getTabKey(t)">
							{{ getTabDisplayName(t) }}
						</option>
						<template v-if="newTab.type === 'tag'">
							<option>----</option>
							<option v-for="t in allTags" :key="t">
								{{ t }}
							</option>
						</template>
					</b-form-datalist>
				</b-form-group>
				<b-form-group :label="$t('home.tabColor.label')" label-class="d-flex">
					<b-form-color-swatch v-model="newTab.color" />
				</b-form-group>
				<b-form-group :label="$t('home.tabIcon.label')" label-class="d-flex">
					<b-form-icon-swatch v-model="newTab.icon" />
				</b-form-group>
				<b-form-group :label="$t('home.tabType.label')" label-class="d-flex">
					<b-form-select v-model="newTab.type">
						<b-form-select-option value="tag">{{ $t('home.tabType.repositoryFiles') }}</b-form-select-option>
						<b-form-select-option value="frame">{{ $t('home.tabType.webPage') }}</b-form-select-option>
					</b-form-select>
				</b-form-group>
				<b-form-group v-if="newTab.type === 'frame'" :label="$t('home.tabUrl.label')" label-class="d-flex">
					<b-form-input v-model="newTab.data.src" type="text" :placeholder="$t('home.tabUrl.placeholder')" required />
				</b-form-group>
				<b-form-group :description="$t('home.tabGlobal.description')">
					<b-form-checkbox v-if="canAdminister" v-model="newTab.isGlobal">
						{{ $t('home.tabGlobal.label') }}
					</b-form-checkbox>
				</b-form-group>
				<b-form-group
					v-if="canAdminister && newTab.isGlobal"
					:label="$t('home.tabShowForRoles.label')"
					label-class="d-flex"
					:description="$t('home.tabShowForUsers.description')"
				>
					<b-fixed-tag-input v-model="newTab.showForRoles" text-field="name" value-field="name" :options="allRoles" />
				</b-form-group>
				<b-form-group
					v-if="canAdminister && newTab.isGlobal"
					:label="$t('home.tabShowForUsers.label')"
					label-class="d-flex"
					:description="$t('home.tabShowForUsers.description')"
				>
					<b-fixed-tag-input v-model="newTab.showForUsers" text-field="name" value-field="name" :options="allUsers" />
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
			centered
			@ok="handleCloseTabModalOk"
		>
			{{ $t('home.tabWillBeDeleted', { name: tab ? getTabDisplayName(tab) : '' }) }}
		</b-modal>
	</div>
</template>

<script>
import Sortable from 'sortablejs';

import cloneDeep from 'lodash/cloneDeep';
import isEqual from 'lodash/isEqual';
import partition from 'lodash/partition';

import fuzzyEquals from '@lincebi/frontend-common/src/fuzzyEquals';
import invokeWhen from '@lincebi/frontend-common/src/invokeWhen';
import isTouchDevice from '@lincebi/frontend-common/src/isTouchDevice';
import move from '@lincebi/frontend-common/src/move';
import safeJSON from '@lincebi/frontend-common/src/safeJSON';

import BFormColorSwatch from '@lincebi/frontend-common/src/components/BFormColorSwatch.vue';
import BFormIconSwatch from '@lincebi/frontend-common/src/components/BFormIconSwatch.vue';
import BFixedTagInput from '@lincebi/frontend-common/src/components/BFixedTagInput.vue';

import store from '@/store';
import i18n from '@/i18n';

export default {
	name: 'HomeTabList',
	components: {
		BFormColorSwatch,
		BFormIconSwatch,
		BFixedTagInput,
	},
	model: {
		prop: 'tab',
		event: 'change',
	},
	props: {
		tab: {
			type: Object,
			default: () => null,
		},
		initialTab: {
			type: String,
			default: '',
		},
	},
	expose: ['changeTab'],
	data() {
		return {
			// Selected tab index.
			tabIndex: 0,
			// Internal tabs are populated with remote tabs.
			internalGlobalTabs: [],
			internalUserTabs: [],
			// New tab template.
			newTab: {
				type: 'tag',
				name: '',
				color: null,
				icon: null,
				isGlobal: false,
				isRemovable: true,
				isDraggable: true,
				showForRoles: [],
				showForUsers: [],
				data: { src: '' },
			},
			// Variables to control the display of modals.
			newTabModalShow: false,
			closeTabModalShow: false,
			// Sortable.js object.
			sortable: null,
		};
	},
	computed: {
		tabs: {
			get() {
				return [...this.internalGlobalTabs, ...this.internalUserTabs];
			},
			set(tabs) {
				[this.internalGlobalTabs, this.internalUserTabs] = partition(tabs, 'isGlobal');
			},
		},
		globalTabs: {
			get() {
				const k = `${this.namespace}.globalTabs`;
				return safeJSON.parse(store.state.globalUserSettings[k], []);
			},
			set(tabs) {
				const k = `${this.namespace}.globalTabs`;
				const v = safeJSON.stringify(tabs, '[]');
				store.dispatch('updateGlobalUserSettings', { [k]: v });
			},
		},
		userTabs: {
			get() {
				const k = `${this.namespace}.tabs`;
				return safeJSON.parse(store.state.userSettings[k], []);
			},
			set(tabs) {
				const k = `${this.namespace}.tabs`;
				const v = safeJSON.stringify(tabs, '[]');
				store.dispatch('updateUserSettings', { [k]: v });
			},
		},
		userId() {
			return store.state.userId;
		},
		userSettings() {
			return store.state.userSettings;
		},
		canAdminister() {
			return store.state.canAdminister;
		},
		allUsers() {
			return store.state.allUsers;
		},
		allRoles() {
			return store.state.allRoles;
		},
		ownRoles() {
			return store.state.ownRoles;
		},
		allTags() {
			return store.getters.repositoryTags;
		},
		showHiddenTabs() {
			const key = `${this.namespace}.show_hidden_tabs`;
			const value = this.userSettings[key] === 'true';
			return value;
		},
	},
	watch: {
		tabs(tabs) {
			this.$emit('change', tabs[this.tabIndex]);
		},
		tabIndex(tabIndex) {
			this.$emit('change', this.tabs[tabIndex]);
		},
		globalTabs(globalTabs) {
			if (!isEqual(this.internalGlobalTabs, globalTabs)) {
				this.internalGlobalTabs = globalTabs;
				this.tabIndex = 0;
			}
		},
		internalGlobalTabs(internalGlobalTabs) {
			if (!isEqual(this.globalTabs, internalGlobalTabs)) {
				this.globalTabs = internalGlobalTabs;
			}
		},
		userTabs(userTabs) {
			if (!isEqual(this.internalUserTabs, userTabs)) {
				this.internalUserTabs = userTabs;
				this.tabIndex = 0;
			}
		},
		internalUserTabs(internalUserTabs) {
			if (!isEqual(this.userTabs, internalUserTabs)) {
				this.userTabs = internalUserTabs;
			}
		},
		showHiddenTabs(showHiddenTabs) {
			if (!showHiddenTabs && !this.getTabVisibility(this.tabs[this.tabIndex])) {
				this.tabIndex = 0;
			}
		},
		'newTab.isGlobal'(isGlobal) {
			if (isGlobal) {
				store.dispatch('fetchAllUsers');
				store.dispatch('fetchAllRoles');
			}
		},
	},
	mounted() {
		this.$nextTick(() => {
			this.internalGlobalTabs = this.globalTabs;
			this.internalUserTabs = this.userTabs;
			this.updateSortable();

			// Do not set the initial tab until the settings have loaded to ensure the tab is found.
			invokeWhen(
				() => store.state.settingsLoaded,
				() => this.changeTab(this.initialTab),
			);
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
			let newTabIndex = this.tabs.findIndex((tab) => {
				return tab.isGlobal === newTab.isGlobal && fuzzyEquals(tab.name, newTab.name);
			});

			if (newTabIndex === -1) {
				this.tabs = [...this.tabs, newTab];
				newTabIndex = this.tabs.length - 1;
			} else {
				const foundTab = this.tabs[newTabIndex];
				// Replace tab if type equals tag or frame.
				if (foundTab.type === 'tag' || foundTab.type === 'frame') {
					const updatedTabs = this.tabs.slice();
					updatedTabs[newTabIndex] = newTab;
					this.tabs = updatedTabs;
				}
			}

			if (this.getTabVisibility(newTab)) {
				this.tabIndex = newTabIndex;
			}
		},
		removeTab(removeTabIndex) {
			this.tabs = this.tabs.filter((_, index) => {
				return index !== removeTabIndex;
			});
			if (this.tabIndex > 0 && this.tabIndex >= removeTabIndex) {
				this.tabIndex--;
			}
		},
		changeTab(tabNameOrIndex) {
			const tabIndex =
				typeof tabNameOrIndex === 'string'
					? this.tabs.findIndex((tab) => fuzzyEquals(tabNameOrIndex, tab.name))
					: tabNameOrIndex;
			if (tabIndex > -1 && tabIndex < this.tabs.length) {
				this.tabIndex = tabIndex;
			}
		},
		getTabKey(tab) {
			return `${tab.isGlobal ? 'g' : 'u'}:${tab.name}`;
		},
		getTabDisplayName(tab) {
			// Replace tab name with translated string.
			if (tab.name.startsWith('t:')) {
				return i18n.t(tab.name.replace(/^t:/, ''));
			}
			return tab.name;
		},
		getTabStyle(tab, index) {
			return index === this.tabIndex ? { backgroundColor: tab.color } : { color: tab.color };
		},
		getTabVisibility(tab) {
			return (
				this.showHiddenTabs ||
				(!tab.showForRoles?.length && !tab.showForUsers?.length) ||
				tab.showForRoles?.some((r) => this.ownRoles.some(({ name }) => r === name)) ||
				tab.showForUsers?.some((u) => u === this.userId)
			);
		},
		fillNewTabForm(tabName) {
			const tab = this.tabs.find((tab) => tab.name === tabName);
			if (tab) {
				this.newTab = cloneDeep(tab);
			}
		},
		updateSortable() {
			if (this.sortable?.el) {
				this.sortable.destroy();
			}

			this.sortable = Sortable.create(this.$refs['home-tab-nav'], {
				forceFallback: isTouchDevice,
				delay: isTouchDevice ? 100 : 10,
				animation: 150,
				filter: '.nav-item:not(.draggable)',
				preventOnFilter: false,
				onMove: (event) => {
					const dcl = event.dragged.classList;
					const rcl = event.related.classList;
					return (
						dcl.contains('draggable') && rcl.contains('draggable') && dcl.contains('global') === rcl.contains('global')
					);
				},
				onUpdate: (event) => {
					this.tabs = move(this.tabs.slice(), event.oldIndex, event.newIndex);
					this.tabIndex = event.newIndex;
				},
			});
		},
	},
};
</script>

<style scoped lang="scss">
.home-tab-list {
	.home-tab-nav {
		display: flex;
		flex-grow: 0;
		flex-shrink: 1;
		flex-basis: auto;
		flex-direction: row;

		max-height: toRem(46 * 3);
		overflow-y: auto;
		overflow-x: hidden;

		color: map-get($theme-colors, 'primary');
		background-color: map-get($theme-colors, 'light');

		box-shadow: $box-shadow;

		@include sortablejs-rtl-fix();

		.home-tab {
			position: relative;
			min-width: toRem(220);
			max-width: toRem(448);
			z-index: 10;

			cursor: pointer;
			user-select: none;

			@include media-breakpoint-down(xs) {
				width: 100%;
				max-width: none;
			}

			color: map-get($theme-colors, 'primary');
			background-color: transparent;

			&.active {
				color: map-get($theme-colors, 'light');
				background-color: map-get($theme-colors, 'primary');

				&::before {
					background-color: rgba(0, 0, 0, 0.2);
				}

				.nav-link .home-tab-close {
					display: block;
				}
			}

			&:empty {
				display: none;
			}

			&::before {
				position: absolute;
				top: 0;
				left: 0;
				height: 100%;
				width: toRem(10);
				background-color: currentColor;
				content: '';
			}

			.nav-link {
				position: relative;
				display: flex;
				align-items: center;
				justify-content: center;
				padding: 0 toRem(50);
				height: toRem(46);

				white-space: nowrap;
				overflow: hidden;
				text-overflow: ellipsis;

				.home-tab-close {
					display: none;
					position: absolute;
					right: toRem(5);
					color: currentColor;
					z-index: 15;
				}
			}
		}

		.home-tab-new {
			flex-grow: 0;

			cursor: pointer;
			user-select: none;

			.nav-link {
				position: relative;
				display: flex;
				align-items: center;
				justify-content: center;
				padding: 0 toRem(20);
				height: toRem(46);

				color: map-get($theme-colors, 'primary');
				background-color: transparent;

				font-size: toRem(24);
			}
		}
	}
}
</style>
