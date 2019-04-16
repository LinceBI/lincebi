<template>
	<b-container class="administration py-5 px-4">
		<b-list-group class="mb-4">
			<b-list-group-item button @click="openAdministration()">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'tools']" />
				<span class="lbl">
					{{ $t('administration.administration') }}
				</span>
			</b-list-group-item>
			<b-list-group-item button @click="openManageDatasources()">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'database']" />
				<span class="lbl">
					{{ $t('administration.manageDatasources') }}
				</span>
			</b-list-group-item>
			<b-list-group-item button @click="openSchedules()">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'hourglass-half']" />
				<span class="lbl">
					{{ $t('administration.schedules') }}
				</span>
			</b-list-group-item>
			<b-list-group-item button @click="openMarketplace()">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'store']" />
				<span class="lbl">
					{{ $t('administration.marketplace') }}
				</span>
			</b-list-group-item>
			<b-list-group-item button @click="openManageCdaCache()">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'table']" />
				<span class="lbl">
					{{ $t('administration.manageCdaCache') }}
				</span>
			</b-list-group-item>
		</b-list-group>
		<b-list-group class="mb-4">
			<b-list-group-item button @click="systemRefresh('systemSettings')">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'sync']" />
				<span class="lbl">
					{{ $t('administration.refreshSystemSettings') }}
				</span>
			</b-list-group-item>
			<b-list-group-item button @click="systemRefresh('metadata')">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'sync']" />
				<span class="lbl">
					{{ $t('administration.refreshReportingMetadata') }}
				</span>
			</b-list-group-item>
			<b-list-group-item button @click="systemRefresh('globalActions')">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'sync']" />
				<span class="lbl">
					{{ $t('administration.refreshGlobalVariables') }}
				</span>
			</b-list-group-item>
			<b-list-group-item button @click="systemRefresh('mondrianSchemaCache')">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'sync']" />
				<span class="lbl">
					{{ $t('administration.refreshMondrianSchemaCache') }}
				</span>
			</b-list-group-item>
			<b-list-group-item button @click="clearCdaCache()">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'sync']" />
				<span class="lbl">
					{{ $t('administration.clearCdaCache') }}
				</span>
			</b-list-group-item>
		</b-list-group>
	</b-container>
</template>

<script>
import clearCdaCache from '@stratebi/biserver-customization-common/src/biserver/clearCdaCache';
import systemRefresh from '@stratebi/biserver-customization-common/src/biserver/systemRefresh';

import router from '@/router';
import eventBus from '@/eventBus';

export default {
	name: 'Administration',
	methods: {
		async openAdministration() {
			router.push({
				name: 'perspective',
				params: { perspective: 'admin.perspective' }
			});
		},
		async openManageDatasources() {
			router.push({
				name: 'perspective',
				params: { perspective: 'admin.perspective' }
			});
			eventBus.$emitWhen('mantle.invoke', mantleWindow => {
				mantleWindow.executeCommand('ManageDatasourcesCommand');
			});
		},
		async openSchedules() {
			router.push({
				name: 'perspective',
				params: { perspective: 'schedules.perspective' }
			});
		},
		async openMarketplace() {
			router.push({
				name: 'perspective',
				params: { perspective: 'marketplace.perspective.osgi' }
			});
		},
		async openManageCdaCache() {
			router.push({
				name: 'perspective',
				params: { perspective: 'opened.perspective' }
			});
			eventBus.$emitWhen('mantle.invoke', mantleWindow => {
				mantleWindow.openURL(
					'CDA cache manager',
					'CDA cache manager',
					'plugin/cda/api/manageCache'
				);
			});
		},
		async systemRefresh(resource) {
			if (await systemRefresh(resource)) {
				this.$notify({
					type: 'success',
					text: this.$t('administration.resourceRefreshSuccess')
				});
			} else {
				this.$notify({
					type: 'error',
					text: this.$t('administration.resourceRefreshError')
				});
			}
		},
		async clearCdaCache() {
			if (await clearCdaCache()) {
				this.$notify({
					type: 'success',
					text: this.$t('administration.cacheClearSuccess')
				});
			} else {
				this.$notify({
					type: 'error',
					text: this.$t('administration.cacheClearError')
				});
			}
		}
	}
};
</script>

<style scoped lang="scss">
.administration {
	min-width: rem(256);
}
</style>
