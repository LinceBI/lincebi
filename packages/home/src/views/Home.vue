<template>
	<div class="home">
		<b-tabs class="home-tabs" no-nav-style>
			<b-tab v-for="i in tabs" :key="`home-tab-${i}`" no-body>
				<template slot="title">
					<div class="home-tab">
						<span>Tab {{ i }}</span>
						<b-button variant="link" class="home-closetab" @click="closeTab(i)">
							<font-awesome-icon class="fa-fw" :icon="['fas', 'times']" />
						</b-button>
					</div>
				</template>
				<b-container class="py-5 px-4">Tab Contents {{ i }}</b-container>
			</b-tab>
			<template slot="tabs">
				<b-nav-item @click.prevent="newTab">
					<span class="home-newtab">
						<font-awesome-icon class="fa-fw" :icon="['fas', 'plus']" />
					</span>
				</b-nav-item>
			</template>
			<div slot="empty"></div>
		</b-tabs>
	</div>
</template>

<script>
export default {
	name: 'Home',
	data() {
		return {
			tabs: [],
			tabCounter: 0
		};
	},
	methods: {
		newTab() {
			this.tabs.push(this.tabCounter++);
		},
		closeTab(x) {
			for (let i = 0; i < this.tabs.length; i++) {
				if (this.tabs[i] === x) {
					this.tabs.splice(i, 1);
				}
			}
		}
	}
};
</script>

<style scoped lang="scss">
.home {
	.home-tabs ::v-deep {
		border: 0;

		> div > .nav {
			background-color: map-get($theme-colors, 'primary');
		}

		> div > .nav > .nav-item > .nav-link {
			padding: 0;

			color: map-get($theme-colors, 'light');
			background-color: map-get($theme-colors, 'primary');

			&.active,
			&:hover,
			&:focus {
				color: map-get($theme-colors, 'primary');
				background-color: map-get($theme-colors, 'light');
			}
		}

		.home-tab,
		.home-newtab {
			display: flex;
			align-items: center;
			justify-content: center;
			height: rem(46);
			padding: 0 0 0 rem(30);
		}

		.home-newtab {
			padding: 0 rem(20);
		}
	}
}
</style>
