<template>
	<div class="side-bar-item" v-if="item.enabled" :title="item.name">
		<div v-if="Array.isArray(item.subitems)">
			<b-nav-item-dropdown
				v-if="item.subitems.some(i => i.enabled)"
				dropright
				no-caret
			>
				<template slot="button-content">
					<font-awesome-icon
						v-if="typeof item.icon !== 'undefined'"
						class="item-icon fa-fw"
						:icon="item.icon"
					/>
					<b-img
						v-else-if="typeof item.img !== 'undefined'"
						class="item-img"
						:src="item.img"
					/>
				</template>
				<side-bar-subitem
					v-for="(subitem, index) in item.subitems"
					:key="index"
					:item="subitem"
				/>
			</b-nav-item-dropdown>
		</div>
		<div v-else>
			<b-nav-item :href="item.href" @click="onClick">
				<font-awesome-icon
					v-if="typeof item.icon !== 'undefined'"
					class="item-icon fa-fw"
					:icon="item.icon"
				/>
				<b-img
					v-else-if="typeof item.img !== 'undefined'"
					class="item-img"
					:src="item.img"
				/>
			</b-nav-item>
		</div>
	</div>
</template>

<script>
import SideBarSubitem from '@/components/SideBarSubitem.vue';

export default {
	name: 'SideBarItem',
	components: {
		SideBarSubitem
	},
	props: { item: Object },
	methods: {
		onClick(event) {
			if (typeof this.item.click !== 'undefined') {
				this.item.click.call(this, event);
			}
		}
	}
};
</script>

<style scoped lang="scss">
.side-bar-item {
	::v-deep .nav-link {
		padding: rem(8) 0;
		font-size: rem(20);
		text-align: center;
		color: map-get($theme-colors, 'light');
	}

	&::v-deep .nav-link:hover,
	&::v-deep .nav-link:focus,
	&.show::v-deep .nav-link {
		background-color: map-get($theme-colors, 'light');
		color: map-get($theme-colors, 'primary');
	}

	&::v-deep .dropdown-menu {
		margin: 0;
		padding: 0;
		border-left: 0;
	}

	.item-img {
		width: 1em;
		height: auto;
	}
}
</style>
