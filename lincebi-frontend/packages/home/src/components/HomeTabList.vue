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
					/>
					<b-form-datalist :id="`new-tab-name-datalist-${uniqueId}`">
						<option v-for="t in tabs.filter((t) => t.type === 'tag')" :key="getTabKey(t)">
							{{ getTabDisplayName(t) }}
						</option>
					</b-form-datalist>
				</b-form-group>
				<b-form-group :label="$t('home.tabColor.label')" label-class="d-flex">
					<b-form-color-swatch v-model="newTab.color" />
				</b-form-group>
				<b-form-group :label="$t('home.tabIcon.label')" label-class="d-flex">
					<b-form-icon-swatch v-model="newTab.icon" />
				</b-form-group>
				<b-form-checkbox v-if="canAdminister" v-model="newTab.isGlobal">
					{{ $t('home.global') }}
				</b-form-checkbox>
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

import isTouchDevice from '@lincebi/frontend-common/src/isTouchDevice';
import move from '@lincebi/frontend-common/src/move';
import safeJSON from '@lincebi/frontend-common/src/safeJSON';

import BFormColorSwatch from '@lincebi/frontend-common/src/components/BFormColorSwatch.vue';
import BFormIconSwatch from '@lincebi/frontend-common/src/components/BFormIconSwatch.vue';

import store from '@/store';
import i18n from '@/i18n';

export default {
	name: 'HomeTabList',
	components: {
		BFormColorSwatch,
		BFormIconSwatch,
	},
	props: {
		tab: {
			type: Object,
			default: () => null,
		},
	},
	data() {
		return {
			// Selected tab index.
			tabIndex: -1,
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
		canAdminister() {
			return store.state.canAdminister;
		},
	},
	watch: {
		tabs(tabs) {
			this.$emit('update:tab', tabs[this.tabIndex]);
		},
		tabIndex(tabIndex) {
			this.$emit('update:tab', this.tabs[tabIndex]);
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
	},
	mounted() {
		this.$nextTick(() => {
			this.internalGlobalTabs = this.globalTabs;
			this.internalUserTabs = this.userTabs;
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
			const newTabIndex = this.tabs.findIndex((tab) => {
				return tab.isGlobal === newTab.isGlobal && tab.name === newTab.name;
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
			this.tabs = this.tabs.filter((_, index) => {
				return index !== removeTabIndex;
			});
			if (this.tabIndex > 0 && this.tabIndex >= removeTabIndex) {
				this.tabIndex--;
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
						dcl.contains('draggable') &&
						rcl.contains('draggable') &&
						dcl.contains('global') === rcl.contains('global')
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

		max-height: rem(46 * 3);
		overflow-y: auto;
		overflow-x: hidden;

		color: map-get($theme-colors, 'primary');
		background-color: map-get($theme-colors, 'light');

		box-shadow: $box-shadow;

		@include sortablejs-rtl-fix();

		.home-tab {
			position: relative;
			min-width: rem(220);
			max-width: rem(448);
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

			&::before {
				position: absolute;
				top: 0;
				left: 0;
				height: 100%;
				width: rem(10);
				background-color: currentColor;
				content: '';
			}

			.nav-link {
				position: relative;
				display: flex;
				align-items: center;
				justify-content: center;
				padding: 0 rem(50);
				height: rem(46);

				white-space: nowrap;
				overflow: hidden;
				text-overflow: ellipsis;

				.home-tab-close {
					display: none;
					position: absolute;
					right: rem(5);
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
				padding: 0 rem(20);
				height: rem(46);

				color: map-get($theme-colors, 'primary');
				background-color: transparent;

				font-size: rem(24);
			}
		}
	}
}
</style>
