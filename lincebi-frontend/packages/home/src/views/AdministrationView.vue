<template>
	<b-container class="administration-view p-4">
		<b-list-group class="mb-4">
			<b-list-group-item v-if="canAdminister" button @click="openAdministration()">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'screwdriver-wrench']" />
				<span class="lbl">
					{{ $t('administration.administration') }}
				</span>
			</b-list-group-item>
			<b-list-group-item v-if="hasDataAccess" button @click="openManageDatasources()">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'database']" />
				<span class="lbl">
					{{ $t('administration.manageDatasources') }}
				</span>
			</b-list-group-item>
			<b-list-group-item v-if="canSchedule" button @click="openSchedules()">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'hourglass']" />
				<span class="lbl">
					{{ $t('administration.schedules') }}
				</span>
			</b-list-group-item>
			<b-list-group-item
				v-if="canAdminister && perspectives.has('marketplace.perspective.osgi')"
				button
				@click="openMarketplace()"
			>
				<font-awesome-icon class="fa-fw" :icon="['fas', 'store']" />
				<span class="lbl">
					{{ $t('administration.marketplace') }}
				</span>
			</b-list-group-item>
			<b-list-group-item
				v-if="canAdminister && overlays.has('startup.cda.toolsmenu.cdaCacheManager')"
				button
				@click="openManageCdaCache()"
			>
				<font-awesome-icon class="fa-fw" :icon="['fas', 'table']" />
				<span class="lbl">
					{{ $t('administration.manageCdaCache') }}
				</span>
			</b-list-group-item>
			<b-list-group-item
				v-if="overlays.has('startup.saiku-adhoc.toolsmenu.configuration-saiku-adhoc')"
				button
				@click="openStreportConf()"
			>
				<font-awesome-icon class="fa-fw" :icon="['fac', 'tool-streport']" />
				<span class="lbl">
					{{ $t('administration.openStreportConf') }}
				</span>
			</b-list-group-item>
			<b-list-group-item
				v-if="overlays.has('startup.saiku-adhoc.viewmenu.statistics-saiku-adhoc')"
				button
				@click="openStreportStats()"
			>
				<font-awesome-icon class="fa-fw" :icon="['fac', 'tool-streport']" />
				<span class="lbl">
					{{ $t('administration.openStreportStats') }}
				</span>
			</b-list-group-item>
		</b-list-group>
		<b-list-group class="mb-4">
			<b-list-group-item v-if="canAdminister" button @click="systemRefresh('systemSettings')">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'arrows-rotate']" />
				<span class="lbl">
					{{ $t('administration.refreshSystemSettings') }}
				</span>
			</b-list-group-item>
			<b-list-group-item v-if="canAdminister" button @click="systemRefresh('metadata')">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'arrows-rotate']" />
				<span class="lbl">
					{{ $t('administration.refreshReportingMetadata') }}
				</span>
			</b-list-group-item>
			<b-list-group-item v-if="canAdminister" button @click="systemRefresh('globalActions')">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'arrows-rotate']" />
				<span class="lbl">
					{{ $t('administration.refreshGlobalVariables') }}
				</span>
			</b-list-group-item>
			<b-list-group-item v-if="canAdminister" button @click="systemRefresh('mondrianSchemaCache')">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'arrows-rotate']" />
				<span class="lbl">
					{{ $t('administration.refreshMondrianSchemaCache') }}
				</span>
			</b-list-group-item>
			<b-list-group-item v-if="canAdminister" button @click="clearCdaCache()">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'arrows-rotate']" />
				<span class="lbl">
					{{ $t('administration.clearCdaCache') }}
				</span>
			</b-list-group-item>
			<b-list-group-item
				v-if="canAdminister && overlays.has('startup.stpanels.refreshmenu.stpanels-clear-cache')"
				button
				@click="clearSTPanelsCache()"
			>
				<font-awesome-icon class="fa-fw" :icon="['fas', 'arrows-rotate']" />
				<span class="lbl">
					{{ $t('administration.clearSTPanelsCache') }}
				</span>
			</b-list-group-item>
		</b-list-group>
		<b-list-group class="mb-4">
			<b-list-group-item v-if="canAdminister" button @click="exportBackup">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'download']" />
				<span class="lbl">
					{{ $t('administration.exportBackup') }}
				</span>
			</b-list-group-item>
			<b-list-group-item v-if="canAdminister" button :disabled="isImportingBackup" @click="importBackup">
				<font-awesome-icon
					class="fa-fw"
					:icon="['fas', isImportingBackup ? 'spinner' : 'upload']"
					:spin="isImportingBackup"
				/>
				<span class="lbl">
					{{ $t('administration.importBackup') }}
				</span>
			</b-list-group-item>
		</b-list-group>
		<!--
		<b-list-group class="mb-4">
			<b-list-group-item v-if="canAdminister" button variant="danger" @click="resetGlobalUserSettings">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'arrows-rotate']" />
				<span class="lbl">
					{{ $t('administration.resetGlobalUserSettings') }}
				</span>
			</b-list-group-item>
			<b-list-group-item button variant="danger" @click="resetUserSettings">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'arrows-rotate']" />
				<span class="lbl">
					{{ $t('administration.resetUserSettings') }}
				</span>
			</b-list-group-item>
		</b-list-group>
		-->
	</b-container>
</template>

<script>
import clearCdaCache from '@lincebi/frontend-common/src/biserver/clearCdaCache';
import clearSTPanelsCache from '@lincebi/frontend-common/src/biserver/clearSTPanelsCache';
import systemRefresh from '@lincebi/frontend-common/src/biserver/systemRefresh';
import exportBackup from '@lincebi/frontend-common/src/biserver/exportBackup';
import importBackup from '@lincebi/frontend-common/src/biserver/importBackup';

import eventBus from '@/eventBus';
import router from '@/router';
import store from '@/store';

export default {
	name: 'AdministrationView',
	data() {
		return {
			isImportingBackup: false,
		};
	},
	computed: {
		canAdminister() {
			return store.state.canAdminister;
		},
		canSchedule() {
			return store.state.canSchedule;
		},
		hasDataAccess() {
			return store.state.hasDataAccess;
		},
		perspectives() {
			return store.state.perspectives;
		},
		overlays() {
			return store.state.overlays;
		},
	},
	methods: {
		async openAdministration() {
			router
				.push({
					name: 'perspective',
					params: { perspective: 'admin.perspective' },
				})
				.catch(() => {});
		},
		async openManageDatasources() {
			router
				.push({
					name: 'perspective',
					params: { perspective: 'browser.perspective' },
				})
				.catch(() => {});
			eventBus.$emitWhenAvailable('mantle-invoke', (mantleWindow) => {
				mantleWindow.executeCommand('ManageDatasourcesCommand');
			});
		},
		async openSchedules() {
			router
				.push({
					name: 'perspective',
					params: { perspective: 'schedules.perspective' },
				})
				.catch(() => {});
		},
		async openMarketplace() {
			router
				.push({
					name: 'perspective',
					params: { perspective: 'marketplace.perspective.osgi' },
				})
				.catch(() => {});
		},
		async openManageCdaCache() {
			router
				.push({
					name: 'perspective',
					params: { perspective: 'opened.perspective' },
				})
				.catch(() => {});
			eventBus.$emitWhenAvailable('mantle-home-command', this.overlays.get('startup.cda.toolsmenu.cdaCacheManager'));
		},
		async openStreportConf() {
			router
				.push({
					name: 'perspective',
					params: { perspective: 'opened.perspective' },
				})
				.catch(() => {});
			eventBus.$emitWhenAvailable(
				'mantle-home-command',
				this.overlays.get('startup.saiku-adhoc.toolsmenu.configuration-saiku-adhoc'),
			);
		},
		async openStreportStats() {
			router
				.push({
					name: 'perspective',
					params: { perspective: 'opened.perspective' },
				})
				.catch(() => {});
			eventBus.$emitWhenAvailable(
				'mantle-home-command',
				this.overlays.get('startup.saiku-adhoc.viewmenu.statistics-saiku-adhoc'),
			);
		},
		async systemRefresh(resource) {
			if (await systemRefresh(resource)) {
				this.$bvToast.toast(this.$t('administration.resourceRefreshSuccess'), {
					title: this.$t('administration.notificationInfo'),
					variant: 'success',
					solid: true,
				});
			} else {
				this.$bvToast.toast(this.$t('administration.resourceRefreshError'), {
					title: this.$t('administration.notificationError'),
					variant: 'danger',
					solid: true,
				});
			}
		},
		async clearCdaCache() {
			if (await clearCdaCache()) {
				this.$bvToast.toast(this.$t('administration.cacheClearSuccess'), {
					title: this.$t('administration.notificationInfo'),
					variant: 'success',
					solid: true,
				});
			} else {
				this.$bvToast.toast(this.$t('administration.cacheClearError'), {
					title: this.$t('administration.notificationError'),
					variant: 'danger',
					solid: true,
				});
			}
		},
		async clearSTPanelsCache() {
			if (await clearSTPanelsCache()) {
				this.$bvToast.toast(this.$t('administration.cacheClearSuccess'), {
					title: this.$t('administration.notificationInfo'),
					variant: 'success',
					solid: true,
				});
			} else {
				this.$bvToast.toast(this.$t('administration.cacheClearError'), {
					title: this.$t('administration.notificationError'),
					variant: 'danger',
					solid: true,
				});
			}
		},
		async exportBackup() {
			exportBackup();
		},
		async importBackup() {
			try {
				this.isImportingBackup = true;
				if (await importBackup()) {
					this.$bvToast.toast(this.$t('administration.importSuccess'), {
						title: this.$t('administration.notificationInfo'),
						variant: 'success',
						solid: true,
					});
				}
			} catch (error) {
				console.error(error);
				this.$bvToast.toast(this.$t('administration.importError'), {
					title: this.$t('administration.notificationError'),
					variant: 'danger',
					solid: true,
				});
			} finally {
				this.isImportingBackup = false;
			}
		},
		async resetGlobalUserSettings() {
			store.dispatch('resetGlobalUserSettings');
		},
		async resetUserSettings() {
			store.dispatch('resetUserSettings');
		},
	},
};
</script>

<style scoped lang="scss">
.administration-view {
	min-width: toRem(256);
}
</style>
