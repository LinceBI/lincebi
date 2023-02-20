<template>
	<div class="p-2">
		<b-form @submit.prevent="createFilePreflight">
			<b-row>
				<b-form-group :label="$t('tools.powerbi.workspaceId.label')" class="col-sm-6">
					<b-form-input
						v-model="workspaceId"
						type="text"
						placeholder="XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX"
						pattern="[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"
						required
					></b-form-input>
				</b-form-group>
				<b-form-group :label="$t('tools.powerbi.reportId.label')" class="col-sm-6">
					<b-form-input
						v-model="reportId"
						type="text"
						placeholder="XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX"
						pattern="[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"
						required
					></b-form-input>
				</b-form-group>
			</b-row>
			<b-row>
				<b-form-group :label="$t('tools.powerbi.reportPageName.label')" class="col-sm-6">
					<b-form-input
						v-model="reportPageName"
						type="text"
						placeholder="ReportSectionXXXXXXXXXXXXXXXXXXXX"
						pattern="ReportSection[0-9a-fA-F]{20}"
					></b-form-input>
				</b-form-group>
				<b-form-group
					v-b-tooltip.hover.bottom
					:label="$t('tools.powerbi.filters.label')"
					:title="$t('tools.powerbi.notAvailableYet')"
					class="col-sm-6"
				>
					<b-form-select disabled></b-form-select>
				</b-form-group>
			</b-row>
			<b-row>
				<b-form-group :label="$t('tools.powerbi.location.label')" class="col-12">
					<b-form-input
						:value="fileLocation?.path"
						type="text"
						:placeholder="$t('tools.powerbi.location.placeholder')"
						required
						@click="pathModalShow = true"
						@keypress.prevent="pathModalShow = true"
					></b-form-input>
				</b-form-group>
			</b-row>
			<b-row>
				<b-form-group :label="$t('tools.powerbi.fileName.label')" class="col-12">
					<b-form-input
						v-model="fileName"
						type="text"
						:placeholder="$t('tools.powerbi.fileName.placeholder')"
						:list="`datalist-file-name-${uniqueId}`"
						pattern="[^/]+"
						autocomplete="off"
						required
					></b-form-input>
					<b-form-datalist :id="`datalist-file-name-${uniqueId}`">
						<option
							v-for="child in fileLocation?.children.filter((c) => !c.isFolder)"
							:key="child.name"
						>
							{{ child.name }}
						</option>
					</b-form-datalist>
				</b-form-group>
			</b-row>
			<b-row>
				<b-col>
					<additional-info
						:title="$t('tools.powerbi.doYouNeedHelp')"
						variant="info"
						:icon="['fas', 'circle-info']"
					>
						<i18n path="tools.powerbi.findWorkspaceAndReport.msg" tag="p" class="mb-2">
							<template #workspaceId>
								<span :style="{ color: '#cd0012' }">{{
									$t('tools.powerbi.findWorkspaceAndReport.workspaceId')
								}}</span>
							</template>
							<template #reportId>
								<span :style="{ color: '#019800' }">{{
									$t('tools.powerbi.findWorkspaceAndReport.reportId')
								}}</span>
							</template>
							<template #reportPageName>
								<span :style="{ color: '#0035a8' }">{{
									$t('tools.powerbi.findWorkspaceAndReport.reportPageName')
								}}</span>
							</template>
						</i18n>
						<b-img class="w-100 h-auto" src="~@/assets/img/powerbi-ids-help.png" />
					</additional-info>
				</b-col>
			</b-row>
			<b-row class="text-center">
				<b-col>
					<b-button type="submit" size="lg" variant="primary" class="m-3 w-50">
						<font-awesome-icon :icon="['fas', 'save']" />
						<span class="lbl">{{ $t('tools.powerbi.save') }}</span>
					</b-button>
				</b-col>
			</b-row>
		</b-form>
		<b-modal
			v-model="pathModalShow"
			:title="$t('tools.powerbi.selectALocation')"
			ok-variant="primary"
			:ok-title="$t('tools.powerbi.accept')"
			cancel-variant="secondary"
			:cancel-title="$t('tools.powerbi.cancel')"
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
			:title="$t('tools.powerbi.overwriteModalTitle')"
			ok-variant="primary"
			:ok-title="$t('tools.powerbi.accept')"
			cancel-variant="secondary"
			:cancel-title="$t('tools.powerbi.cancel')"
			centered
			@ok="createFile"
		>
			<p class="my-4">{{ $t('tools.powerbi.overwriteModalBody') }}</p>
		</b-modal>
		<b-modal
			v-model="submitModalShow"
			:title="$t('tools.powerbi.submitModalTitle')"
			ok-variant="primary"
			:ok-title="$t('tools.powerbi.accept')"
			ok-only
			centered
			@hidden="resetForm"
		>
			<p class="my-4">
				{{ $t('tools.powerbi.submitModalBody', { name: fileName }) }}
			</p>
		</b-modal>
	</div>
</template>

<script>
import store from '@/store';

import AdditionalInfo from '@/components/AdditionalInfo.vue';
import DirectoryTreeSelector from '@/components/DirectoryTreeSelector.vue';

export default {
	name: 'PowerBiForm',
	components: {
		AdditionalInfo,
		DirectoryTreeSelector,
	},
	data() {
		return {
			workspaceId: '',
			reportId: '',
			reportPageName: '',
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
				`URL=plugin/lincebi/api/integration/powerbi/embed/html?${new URLSearchParams({
					workspaceId: this.workspaceId,
					reportId: this.reportId,
					reportPageName: this.reportPageName,
				})}`,
			].join('\n');

			await store.dispatch('createRepositoryFile', { file, content });

			this.submitModalShow = true;
		},
		resetForm() {
			this.reportId = '';
			this.reportPageName = '';
			this.fileName = '';
		},
	},
};
</script>
