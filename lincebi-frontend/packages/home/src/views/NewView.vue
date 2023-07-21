<template>
	<div class="new-view p-4">
		<div v-for="(group, groupIndex) in groups" :key="group.id">
			<div class="new-card-deck-name">{{ group.name }}</div>
			<div class="new-card-deck card-deck">
				<div
					v-for="tool in groups[groupIndex].tools"
					:key="tool.id"
					:data-v-step="`new-${tool.id}`"
					class="new-card card"
					tabindex="0"
					@click="tool.click()"
					@keyup.enter="tool.click()"
				>
					<div class="card-border" :style="{ backgroundColor: tool.color }"></div>
					<font-awesome-icon
						class="card-icon fa-fw"
						:icon="tool.icon"
						:style="{ color: tool.color }"
					/>
					<div class="card-body">
						<div class="card-title">{{ tool.name }}</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import eventBus from '@/eventBus';
import router from '@/router';
import store from '@/store';

export default {
	name: 'NewView',
	computed: {
		overlays() {
			return store.state.overlays;
		},
		groups() {
			return [
				{
					id: 'lincebi',
					name: this.$t('new.lincebiTools'),
					tools: [
						{
							enabled: this.overlays.has('launch.stpivot'),
							id: 'tool-stpivot',
							name: 'STPivot',
							icon: ['fac', 'tool-stpivot'],
							color: '#B0B916',
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable(
									'mantle-home-command',
									this.overlays.get('launch.stpivot')
								);
							},
						},
						{
							enabled: this.overlays.has('launch.launch_new_stolap_analytics'),
							id: 'tool-stolap',
							name: 'STOlap',
							icon: ['fac', 'tool-stolap'],
							color: '#993711',
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable(
									'mantle-home-command',
									this.overlays.get('launch.launch_new_stolap_analytics')
								);
							},
						},
						{
							enabled: this.overlays.has('launch.launch-new-saiku-adhoc'),
							id: 'tool-streport',
							name: 'STReport',
							icon: ['fac', 'tool-streport'],
							color: '#E67E22',
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable(
									'mantle-home-command',
									this.overlays.get('launch.launch-new-saiku-adhoc')
								);
							},
						},
						{
							enabled: this.overlays.has('launch.launch-new-stpanels'),
							id: 'tool-stpanels',
							name: 'STPanels',
							icon: ['fac', 'tool-stpanels'],
							color: '#000000',
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable(
									'mantle-home-command',
									this.overlays.get('launch.launch-new-stpanels')
								);
							},
						},
						{
							enabled: this.overlays.has('launch.stdashboardButton'),
							id: 'tool-stdashboard',
							name: 'STDashboard',
							icon: ['fac', 'tool-stdashboard'],
							color: '#2980B9',
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable(
									'mantle-home-command',
									this.overlays.get('launch.stdashboardButton')
								);
							},
						},
						{
							enabled: this.overlays.has('launch.launch-new-stagile'),
							id: 'tool-stagile',
							name: 'STAgile',
							icon: ['fac', 'tool-stagile'],
							color: '#B91616',
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable(
									'mantle-home-command',
									this.overlays.get('launch.launch-new-stagile')
								);
							},
						},
						{
							enabled: this.overlays.has('launch.button.stcard'),
							id: 'tool-stcard',
							name: 'STCard',
							icon: ['fac', 'tool-stcard'],
							color: '#00796B',
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable(
									'mantle-home-command',
									this.overlays.get('launch.button.stcard')
								);
							},
						},
					],
				},
				{
					id: 'other',
					name: this.$t('new.otherTools'),
					tools: [
						{
							enabled: this.overlays.has('launch.launch_new_cde'),
							id: 'tool-cde',
							name: 'CDE',
							icon: ['fac', 'tool-cde'],
							color: '#9836E4',
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable('mantle-invoke', (mantleWindow) => {
									mantleWindow.openURL('Webdetails CDE', 'Webdetails CDE', 'api/repos/wcdf/new');
								});
							},
						},
						{
							enabled: true,
							id: 'tool-embed',
							name: 'URL',
							icon: ['fac', 'tool-embed'],
							color: '#2ab7d0',
							click: () => {
								router
									.push({
										name: 'embed',
									})
									.catch(() => {});
							},
						},
						{
							enabled: true,
							id: 'tool-powerbi',
							name: 'Power BI',
							icon: ['fac', 'tool-powerbi'],
							color: '#f2c811',
							click: () => {
								router
									.push({
										name: 'powerbi',
									})
									.catch(() => {});
							},
						},
						{
							enabled: true,
							id: 'tool-superset',
							name: 'Superset',
							icon: ['fac', 'tool-superset'],
							color: '#484848',
							click: () => {
								router
									.push({
										name: 'superset',
									})
									.catch(() => {});
							},
						},
						{
							enabled: this.overlays.has('launch.jpivot'),
							id: 'tool-jpivot',
							name: 'JPivot',
							icon: ['fac', 'tool-jpivot'],
							color: '#00B919',
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable(
									'mantle-home-command',
									this.overlays.get('launch.jpivot')
								);
							},
						},
						{
							enabled: this.overlays.has('launch.SaikuAnalytics'),
							id: 'tool-saiku',
							name: 'Saiku Analytics',
							icon: ['fac', 'tool-saiku'],
							color: '#C52120',
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable(
									'mantle-home-command',
									this.overlays.get('launch.SaikuAnalytics')
								);
							},
						},
						{
							enabled: this.overlays.has(
								'startup.repositorySynchronizer.toolsmenu.repositorySynchronizer'
							),
							id: 'tool-repository-synchronizer',
							name: 'Repository Synchronizer',
							icon: ['fac', 'tool-repository-synchronizer'],
							color: '#4B4B4B',
							click: () => {
								router
									.push({
										name: 'perspective',
										params: { perspective: 'opened.perspective' },
									})
									.catch(() => {});
								eventBus.$emitWhenAvailable('mantle-invoke', (mantleWindow) => {
									mantleWindow.openURL(
										'Repository Synchronizer',
										'Repository Synchronizer',
										'plugin/repositorySynchronizer/api/default'
									);
								});
							},
						},
					],
				},
			]
				.map((group) => ({
					...group,
					tools: group.tools
						.filter((tool) => tool.enabled)
						.sort((a, b) => a.name.localeCompare(b.name)),
				}))
				.filter((group) => group.tools.length > 0);
		},
	},
	methods: {},
};
</script>

<style scoped lang="scss">
.new-view {
	min-width: rem(256);

	.new-card-deck-name {
		margin-bottom: rem(20);
		font-size: rem(24);
		font-weight: 400;
		line-height: rem(22);
	}

	.new-card-deck {
		display: flex;
		flex-flow: row wrap;
		margin-left: rem(-15);
		margin-right: rem(-15);

		.new-card {
			margin-left: rem(15);
			margin-right: rem(15);
			margin-bottom: $grid-gutter-width;

			flex-grow: 0;
			flex-shrink: 1;
			flex-basis: rem(140);

			@for $i from 0 through 10 {
				@media (min-width: 672px + (320px * $i)) {
					flex-basis: calc(#{rem(140 + ($i * 4))});
				}
			}

			color: map-get($theme-colors, 'dark');
			background-color: rgba(map-get($theme-colors, 'light'), 0.9);

			cursor: pointer;
			user-select: none;

			transform: scale(1);
			transition: transform 200ms ease-in;

			&:focus,
			&:focus-within,
			&:hover {
				transform: scale(1.05);
			}

			.card-border {
				height: rem(10);
				width: 100%;
			}

			.card-icon {
				margin: 0 auto;
				padding: rem(15) 0 0;
				width: 50%;
				height: auto;
			}

			.card-body {
				padding: rem(15) rem(10);

				.card-title {
					font-size: rem(18);
					font-weight: 700;
					line-height: rem(22);
					text-align: center;
					margin: 0 auto;
				}
			}
		}
	}
}
</style>
