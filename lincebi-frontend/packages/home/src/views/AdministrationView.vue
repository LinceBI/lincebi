<template>
	<b-container class="administration-view p-4">
		<b-list-group class="mb-4">
			<b-list-group-item v-if="canAdminister" button @click="openAdministration()">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'tools']" />
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
				<font-awesome-icon class="fa-fw" :icon="['fas', 'hourglass-half']" />
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
				<font-awesome-icon class="fa-fw" :icon="['fas', 'sync']" />
				<span class="lbl">
					{{ $t('administration.refreshSystemSettings') }}
				</span>
			</b-list-group-item>
			<b-list-group-item v-if="canAdminister" button @click="systemRefresh('metadata')">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'sync']" />
				<span class="lbl">
					{{ $t('administration.refreshReportingMetadata') }}
				</span>
			</b-list-group-item>
			<b-list-group-item v-if="canAdminister" button @click="systemRefresh('globalActions')">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'sync']" />
				<span class="lbl">
					{{ $t('administration.refreshGlobalVariables') }}
				</span>
			</b-list-group-item>
			<b-list-group-item v-if="canAdminister" button @click="systemRefresh('mondrianSchemaCache')">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'sync']" />
				<span class="lbl">
					{{ $t('administration.refreshMondrianSchemaCache') }}
				</span>
			</b-list-group-item>
			<b-list-group-item v-if="canAdminister" button @click="clearCdaCache()">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'sync']" />
				<span class="lbl">
					{{ $t('administration.clearCdaCache') }}
				</span>
			</b-list-group-item>
		</b-list-group>
	</b-container>
</template>

<script>
import clearCdaCache from '@lincebi/frontend-common/src/biserver/clearCdaCache';
import systemRefresh from '@lincebi/frontend-common/src/biserver/systemRefresh';

import eventBus from '@/eventBus';
import router from '@/router';
import store from '@/store';

export default {
	name: 'AdministrationView',
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
			eventBus.$emitWhenAvailable(
				'mantle-home-command',
				this.overlays.get('startup.cda.toolsmenu.cdaCacheManager')
			);
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
				this.overlays.get('startup.saiku-adhoc.toolsmenu.configuration-saiku-adhoc')
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
				this.overlays.get('startup.saiku-adhoc.viewmenu.statistics-saiku-adhoc')
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
	},
};
</script>

<style scoped lang="scss">
.administration-view {
	min-width: rem(256);
}
</style>
