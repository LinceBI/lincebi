<template>
	<b-container class="administration py-5 px-4">
		<b-list-group class="mb-4">
			<b-list-group-item button @click="openAdministration()">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'tools']" />
				<span class="lbl">Administration</span>
			</b-list-group-item>
			<b-list-group-item button @click="openDatasources()">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'database']" />
				<span class="lbl">Manage datasources</span>
			</b-list-group-item>
			<b-list-group-item button @click="openSchedules()">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'hourglass-half']" />
				<span class="lbl">Schedules</span>
			</b-list-group-item>
			<b-list-group-item button @click="openMarketplace()">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'store']" />
				<span class="lbl">Marketplace</span>
			</b-list-group-item>
			<b-list-group-item button @click="openCdaCacheManager()">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'table']" />
				<span class="lbl">CDA cache manager</span>
			</b-list-group-item>
		</b-list-group>
		<b-list-group class="mb-4">
			<b-list-group-item button @click="systemRefresh('systemSettings')">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'sync']" />
				<span class="lbl">Refresh system settings</span>
			</b-list-group-item>
			<b-list-group-item button @click="systemRefresh('metadata')">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'sync']" />
				<span class="lbl">Refresh reporting metadata</span>
			</b-list-group-item>
			<b-list-group-item button @click="systemRefresh('globalActions')">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'sync']" />
				<span class="lbl">Refresh global variables</span>
			</b-list-group-item>
			<b-list-group-item button @click="systemRefresh('mondrianSchemaCache')">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'sync']" />
				<span class="lbl">Refresh Mondrian schema cache</span>
			</b-list-group-item>
			<b-list-group-item button @click="clearCdaCache()">
				<font-awesome-icon class="fa-fw" :icon="['fas', 'sync']" />
				<span class="lbl">Clear CDA cache</span>
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
		async openDatasources() {
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
		async openCdaCacheManager() {
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
			const success = await systemRefresh(resource);
			this.$notify(
				success
					? { type: 'success', text: 'Resource successfully refreshed' }
					: { type: 'error', text: 'Error while refreshing resource' }
			);
		},
		async clearCdaCache() {
			const success = await clearCdaCache();
			this.$notify(
				success
					? { type: 'success', text: 'Cache successfully cleared' }
					: { type: 'error', text: 'Error while clearing cache' }
			);
		}
	}
};
</script>

<style scoped lang="scss">
.administration {
	min-width: rem(256);
}
</style>
