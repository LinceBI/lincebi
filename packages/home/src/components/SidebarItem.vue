<template>
	<div class="sidebar-item" v-if="item.enabled" :title="item.name">
		<div v-if="Array.isArray(item.subitems)">
			<b-nav-item-dropdown
				ref="dropdown"
				v-if="item.subitems.some(i => i.enabled)"
				@mouseenter.native="onDropdownMouseenter"
				@mouseleave.native="onDropdownMouseleave"
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
				<sidebar-subitem
					v-for="(subitem, index) in item.subitems"
					:key="index"
					:item="subitem"
				/>
			</b-nav-item-dropdown>
		</div>
		<div v-else>
			<b-nav-item :href="item.href" @click="onItemClick">
				<font-awesome-icon
					ref="item"
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
import SidebarSubitem from '@/components/SidebarSubitem.vue';

export default {
	name: 'SidebarItem',
	components: {
		SidebarSubitem
	},
	props: { item: Object },
	methods: {
		onDropdownMouseenter(event) {
			this.$refs.dropdown.show();
		},
		onDropdownMouseleave(event) {
			this.$refs.dropdown.hide();
		},
		onItemClick(event) {
			if (typeof this.item.click !== 'undefined') {
				this.item.click.call(this, event);
			}
		}
	}
};
</script>

<style scoped lang="scss">
.sidebar-item::v-deep {
	.nav-link {
		padding: rem(8) 0;
		font-size: rem(20);
		text-align: center;
		color: map-get($theme-colors, 'light');
	}

	.nav-link:hover,
	.nav-link:focus,
	&.show .nav-link {
		background-color: map-get($theme-colors, 'light');
		color: map-get($theme-colors, 'primary');
	}

	.dropdown-menu {
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
