package com.aizistral.etherium.items;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.items.ICreativeTabMember;
import com.aizistral.etherium.core.EtheriumUtil;
import com.aizistral.etherium.core.IEtheriumConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class EtheriumScraps extends Item implements ICreativeTabMember {

	public EtheriumScraps() {
		super(EtheriumUtil.defaultProperties(EtheriumScraps.class).stacksTo(64));
	}

	@Override
	public CreativeModeTab getCreativeTab() {
		return EnigmaticLegacy.mainTab;
	}

}
