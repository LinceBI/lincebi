<template>
	<div class="p-2">
		<b-form @submit.prevent="createFilePreflight">
			<b-row>
				<b-form-group :label="$t('tools.superset.dashboardId.label')" class="col-12">
					<b-form-input
						v-model="dashboardId"
						type="text"
						placeholder="XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX"
						pattern="[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"
						required
					></b-form-input>
				</b-form-group>
			</b-row>
			<b-row>
				<b-form-group :label="$t('tools.superset.location.label')" class="col-12">
					<b-form-input
						:value="fileLocation?.path"
						type="text"
						:placeholder="$t('tools.superset.location.placeholder')"
						required
						@click="pathModalShow = true"
						@keypress.prevent="pathModalShow = true"
					></b-form-input>
				</b-form-group>
			</b-row>
			<b-row>
				<b-form-group :label="$t('tools.superset.fileName.label')" class="col-12">
					<b-form-input
						v-model="fileName"
						type="text"
						:placeholder="$t('tools.superset.fileName.placeholder')"
						:list="`datalist-file-name-${uniqueId}`"
						pattern="[^/]+"
						autocomplete="off"
						required
					></b-form-input>
					<b-form-datalist :id="`datalist-file-name-${uniqueId}`">
						<option v-for="child in fileLocation?.children.filter((c) => !c.isFolder)" :key="child.name">
							{{ child.name }}
						</option>
					</b-form-datalist>
				</b-form-group>
			</b-row>
			<b-row class="text-center">
				<b-col>
					<b-button type="submit" size="lg" variant="primary" class="m-3 w-50">
						<font-awesome-icon :icon="['fas', 'save']" />
						<span class="lbl">{{ $t('tools.superset.save') }}</span>
					</b-button>
				</b-col>
			</b-row>
		</b-form>
		<b-modal
			v-model="pathModalShow"
			:title="$t('tools.superset.selectALocation')"
			ok-variant="primary"
			:ok-title="$t('tools.superset.accept')"
			cancel-variant="secondary"
			:cancel-title="$t('tools.superset.cancel')"
			centered
			@ok="fileLocation = pathModalSelectedLocation"
		>
			<directory-tree-selector
				v-model="pathModalSelectedLocation"
				:root="repository"
				max-height="250px"
			></directory-tree-selector>
		</b-modal>
		<b-modal
			v-model="overwriteModalShow"
			:title="$t('tools.superset.overwriteModalTitle')"
			ok-variant="primary"
			:ok-title="$t('tools.superset.accept')"
			cancel-variant="secondary"
			:cancel-title="$t('tools.superset.cancel')"
			centered
			@ok="createFile"
		>
			<p class="my-4">{{ $t('tools.superset.overwriteModalBody') }}</p>
		</b-modal>
		<b-modal
			v-model="submitModalShow"
			:title="$t('tools.superset.submitModalTitle')"
			ok-variant="primary"
			:ok-title="$t('tools.superset.accept')"
			ok-only
			centered
			@hidden="resetForm"
		>
			<p class="my-4">
				{{ $t('tools.superset.submitModalBody', { name: fileName }) }}
			</p>
		</b-modal>
	</div>
</template>

<script>
import store from '@/store';

import DirectoryTreeSelector from '@/components/DirectoryTreeSelector.vue';

export default {
	name: 'SupersetForm',
	components: {
		DirectoryTreeSelector,
	},
	data() {
		return {
			dashboardId: '',
			fileLocation: null,
			fileName: '',
			pathModalShow: false,
			pathModalSelectedLocation: null,
			overwriteModalShow: false,
			submitModalShow: false,
		};
	},
	computed: {
		repository() {
			return store.state.repository;
		},
	},
	methods: {
		async createFilePreflight() {
			if (!/\.url$/i.test(this.fileName)) {
				this.fileName += '.url';
			}

			const exists = this.fileLocation.children.some((c) => c.name === this.fileName);
			if (exists) {
				this.overwriteModalShow = true;
			} else {
				await this.createFile();
			}
		},
		async createFile() {
			const file = {
				path: `${this.fileLocation.path}/${this.fileName}`,
			};
			const content = [
				'[InternetShortcut]',
				`URL=plugin/lincebi/api/integration/superset/embed/html?${new URLSearchParams({
					dashboardId: this.dashboardId,
				})}`,
			].join('\n');

			await store.dispatch('createRepositoryFile', { file, content });

			this.submitModalShow = true;
		},
		resetForm() {
			this.dashboardId = '';
			this.fileName = '';
		},
	},
};
</script>
