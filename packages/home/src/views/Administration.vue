<template>
	<b-container class="administration py-5 px-4">
		<b-list-group class="mb-4">
			<b-list-group-item
				v-if="canAdminister"
				button
				@click="openAdministration()"
			>
				<font-awesome-icon class="fa-fw" :icon="['fas', 'tools']" />
				<span class="lbl">
					{{ $t('administration.administration') }}
				</span>
			</b-list-group-item>
			<b-list-group-item
				v-if="hasDataAccess"
				button
				@click="openManageDatasources()"
			>
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
				v-if="canAdminister && installedPlugins.includes('marketplace')"
				button
				@click="openMarketplace()"
			>
				<font-awesome-icon class="fa-fw" :icon="['fas', 'store']" />
				<span class="lbl">
					{{ $t('administration.marketplace') }}
				</span>
			</b-list-group-item>
			<b-list-group-item
				v-if="canAdminister"
				button
				@click="openManageCdaCache()"
			>
				<font-awesome-icon class="fa-fw" :icon="['fas', 'table']" />
				<span class="lbl">
					{{ $t('administration.manageCdaCache') }}
				</span>
			</b-list-group-item>
			<b-list-group-item
				v-if="installedPlugins.includes('streport')"
				button
				@click="openStreportStats()"
			>
				<font-awesome-icon class="fa-fw" :icon="['fas', 'chart-bar']" />
				<span class="lbl">
					{{ $t('administration.openStreportStats') }}
				</span>
			</b-list-group-item>
		</b-list-group>
		<b-list-group class="mb-4">
			<b-list-group-item
				v-if="canAdminister"
				button
				@click="systemRefresh('systemSettings')"
			>
				<font-awesome-icon class="fa-fw" :icon="['fas', 'sync']" />
				<span class="lbl">
					{{ $t('administration.refreshSystemSettings') }}
				</span>
			</b-list-group-item>
			<b-list-group-item
				v-if="canAdminister"
				button
				@click="systemRefresh('metadata')"
			>
				<font-awesome-icon class="fa-fw" :icon="['fas', 'sync']" />
				<span class="lbl">
					{{ $t('administration.refreshReportingMetadata') }}
				</span>
			</b-list-group-item>
			<b-list-group-item
				v-if="canAdminister"
				button
				@click="systemRefresh('globalActions')"
			>
				<font-awesome-icon class="fa-fw" :icon="['fas', 'sync']" />
				<span class="lbl">
					{{ $t('administration.refreshGlobalVariables') }}
				</span>
			</b-list-group-item>
			<b-list-group-item
				v-if="canAdminister"
				button
				@click="systemRefresh('mondrianSchemaCache')"
			>
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
import clearCdaCache from '@lincebi/biserver-frontend-common/src/biserver/clearCdaCache';
import systemRefresh from '@lincebi/biserver-frontend-common/src/biserver/systemRefresh';

import eventBus from '@/eventBus';
import router from '@/router';
import store from '@/store';

export default {
	name: 'Administration',
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
		installedPlugins() {
			return store.state.installedPlugins;
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
			eventBus.$emitWhen('mantle.invoke', (mantleWindow) => {
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
			eventBus.$emitWhen('mantle.invoke', (mantleWindow) => {
				mantleWindow.openURL(
					'CDA cache manager',
					'CDA cache manager',
					'plugin/cda/api/manageCache'
				);
			});
		},
		async openStreportStats() {
			router
				.push({
					name: 'perspective',
					params: { perspective: 'opened.perspective' },
				})
				.catch(() => {});
			eventBus.$emitWhen('mantle.invoke', (mantleWindow) => {
				mantleWindow.openURL(
					'STReport statistics',
					'STReport statistics',
					'api/repos/saiku-adhoc/statistics'
				);
			});
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
.administration {
	min-width: rem(256);
}
</style>
