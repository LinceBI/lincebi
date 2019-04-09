<template>
	<b-list-group class="Administration p-4">
		<b-list-group-item button @click="openDatasources()">
			Manage datasources
		</b-list-group-item>
		<b-list-group-item button @click="openAdminPerspective()">
			BI Server administration
		</b-list-group-item>
		<b-list-group-item button @click="systemRefresh('systemSettings')">
			Refresh system settings
		</b-list-group-item>
		<b-list-group-item button @click="systemRefresh('metadata')">
			Refresh reporting metadata
		</b-list-group-item>
		<b-list-group-item button @click="systemRefresh('globalActions')">
			Refresh global actions
		</b-list-group-item>
		<b-list-group-item button @click="systemRefresh('mondrianSchemaCache')">
			Refresh Mondrian schema cache
		</b-list-group-item>
		<b-list-group-item button @click="clearCdaCache()">
			Clear CDA cache
		</b-list-group-item>
	</b-list-group>
</template>

<script>
import clearCdaCache from '@stratebi/biserver-customization-common/src/biserver/clearCdaCache';
import systemRefresh from '@stratebi/biserver-customization-common/src/biserver/systemRefresh';

import router from '@/router';
import eventBus from '@/eventBus';

export default {
	name: 'Administration',
	methods: {
		async openDatasources() {
			this.openAdminPerspective();
			eventBus.$emit('mantle.invoke', mantleWindow => {
				mantleWindow.executeCommand('ManageDatasourcesCommand');
			});
		},
		async openAdminPerspective() {
			router.push({
				name: 'perspective',
				params: { perspective: 'admin.perspective' }
			});
		},
		async systemRefresh(resource) {
			const result = await systemRefresh(resource);
			console.log('systemRefresh', result);
		},
		async clearCdaCache() {
			const result = await clearCdaCache();
			console.log('clearCdaCache', result);
		}
	}
};
</script>

<style scoped lang="scss"></style>
