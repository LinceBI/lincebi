<template>
	<div class="p-2">
		<b-form @submit.prevent="createFilePreflight">
			<b-row>
				<b-form-group :label="$t('tools.embed.url.label')" class="col-12">
					<b-form-input v-model="url" type="text" placeholder="https://example.com" required></b-form-input>
				</b-form-group>
			</b-row>
			<b-row>
				<b-form-group :label="$t('tools.embed.location.label')" class="col-12">
					<b-form-input
						:value="fileLocation?.path"
						type="text"
						:placeholder="$t('tools.embed.location.placeholder')"
						required
						@click="pathModalShow = true"
						@keypress.prevent="pathModalShow = true"
					></b-form-input>
				</b-form-group>
			</b-row>
			<b-row>
				<b-form-group :label="$t('tools.embed.fileName.label')" class="col-12">
					<b-form-input
						v-model="fileName"
						type="text"
						:placeholder="$t('tools.embed.fileName.placeholder')"
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
						<span class="lbl">{{ $t('tools.embed.save') }}</span>
					</b-button>
				</b-col>
			</b-row>
		</b-form>
		<b-modal
			v-model="pathModalShow"
			:title="$t('tools.embed.selectALocation')"
			ok-variant="primary"
			:ok-title="$t('tools.embed.accept')"
			cancel-variant="secondary"
			:cancel-title="$t('tools.embed.cancel')"
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
			:title="$t('tools.embed.overwriteModalTitle')"
			ok-variant="primary"
			:ok-title="$t('tools.embed.accept')"
			cancel-variant="secondary"
			:cancel-title="$t('tools.embed.cancel')"
			centered
			@ok="createFile"
		>
			<p class="my-4">{{ $t('tools.embed.overwriteModalBody') }}</p>
		</b-modal>
		<b-modal
			v-model="submitModalShow"
			:title="$t('tools.embed.submitModalTitle')"
			ok-variant="primary"
			:ok-title="$t('tools.embed.accept')"
			ok-only
			centered
			@hidden="resetForm"
		>
			<p class="my-4">
				{{ $t('tools.embed.submitModalBody', { name: fileName }) }}
			</p>
		</b-modal>
	</div>
</template>

<script>
import store from '@/store';

import DirectoryTreeSelector from '@/components/DirectoryTreeSelector.vue';

export default {
	name: 'EmbedForm',
	components: {
		DirectoryTreeSelector,
	},
	data() {
		return {
			url: '',
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
			if (!/^https?:\/\//i.test(this.url)) {
				this.url = `https://${this.url}`;
			}

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
			const content = ['[InternetShortcut]', `URL=${this.url}`].join('\n');

			await store.dispatch('createRepositoryFile', { file, content });

			this.submitModalShow = true;
		},
		resetForm() {
			this.url = '';
			this.fileName = '';
		},
	},
};
</script>
