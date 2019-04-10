<template>
	<div class="App">
		<NavBar class="page-navbar" />
		<b-container class="page-container" fluid>
			<b-row class="page-row">
				<b-col class="page-col sidebar">
					<SideBar />
				</b-col>
				<b-col class="page-col content">
					<router-multi-view class="page-router" />
					<BNotifications class="page-notifications" />
				</b-col>
			</b-row>
		</b-container>
	</div>
</template>

<script>
import BNotifications from '@stratebi/biserver-customization-common/src/components/BNotifications.vue';
import generateAvatar from '@stratebi/biserver-customization-common/src/generateAvatar';

import store from '@/store';

import NavBar from '@/components/NavBar.vue';
import SideBar from '@/components/SideBar.vue';

export default {
	name: 'App',
	components: {
		BNotifications,
		NavBar,
		SideBar
	},
	async created() {
		await store.dispatch(
			'fetchUserSettings',
			Object.keys(store.state.userSettings)
		);

		if (store.state.userSettings.avatar.length === 0) {
			store.commit('setUserSetting', {
				key: 'avatar',
				value: generateAvatar(store.state.userSettings.name)
			});
		}
	}
};
</script>

<style lang="scss">
body {
	margin: 0;
	height: 100vh;
	font-family: 'Titillium Web', $system-sans-serif;
	font-size: $em-base + 0px;
	background-image: url('~@/assets/img/background.jpg');
	background-size: cover;
	background-repeat: no-repeat;
}

.App {
	display: flex;
	flex-direction: column;
	height: 100%;

	.page-navbar {
		display: flex;
		flex-direction: row;
		z-index: 1000;
	}

	.page-container {
		display: flex;
		flex-direction: column;
		flex-grow: 1;

		.page-row {
			flex-wrap: nowrap;
			flex-grow: 1;

			.page-col.sidebar {
				flex: 0 0 rem(50);
				padding: 0;
				z-index: 500;
			}

			.page-col.content {
				padding: 0;
				z-index: 0;
			}
		}

		.page-router {
			height: 100%;
		}

		.page-notifications {
			position: absolute;
			padding-top: rem(10);
		}
	}
}
</style>
