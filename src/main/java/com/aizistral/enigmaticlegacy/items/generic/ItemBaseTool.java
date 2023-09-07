package com.aizistral.enigmaticlegacy.items.generic;

import java.util.Set;

import com.aizistral.enigmaticlegacy.api.items.ICreativeTabMember;
import com.aizistral.enigmaticlegacy.api.materials.EnigmaticMaterials;
import com.aizistral.enigmaticlegacy.registries.EnigmaticTabs;
import com.google.common.collect.Sets;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;

public abstract class ItemBaseTool extends DiggerItem implements ICreativeTabMember {
	public Set<ToolAction> toolActions;

	public ItemBaseTool(float attackDamageIn, float attackSpeedIn, Tier tier, TagKey<Block> effectiveBlocksIn, Properties builder) {
		super(attackDamageIn, attackSpeedIn, tier, effectiveBlocksIn, builder);
		this.toolActions = Sets.newHashSet();
	}

	public ItemBaseTool(Tier tier) {
		this(4F, -2.8F, tier, BlockTags.MINEABLE_WITH_PICKAXE, ItemBaseTool.getDefaultProperties());
	}

	public ItemBaseTool() {
		this(4F, -2.8F, EnigmaticMaterials.ETHERIUM, BlockTags.MINEABLE_WITH_PICKAXE, ItemBaseTool.getDefaultProperties());
	}

	@Override
	public boolean isCorrectToolForDrops(ItemStack stack, BlockState blockIn) { // TODO Something about this
		return super.isCorrectToolForDrops(stack, blockIn) /*|| this.effectiveMaterials.contains(blockIn.getMaterial())*/;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return !this.isCorrectToolForDrops(stack, state) ? super.getDestroySpeed(stack, state) : this.speed;
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
		return super.canPerformAction(stack, toolAction) || this.toolActions.contains(toolAction);
	}

	@Override
	public CreativeModeTab getCreativeTab() {
		return EnigmaticTabs.MAIN;
	}

	public static Properties getDefaultProperties() {
		Properties props = new Item.Properties();

		props.stacksTo(1);
		props.rarity(Rarity.COMMON);

		return props;
	}

}
