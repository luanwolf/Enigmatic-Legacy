package com.aizistral.enigmaticlegacy.handlers;

import static com.aizistral.enigmaticlegacy.EnigmaticLegacy.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.WeakHashMap;

import org.apache.commons.lang3.tuple.Triple;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.events.EndPortalActivatedEvent;
import com.aizistral.enigmaticlegacy.api.events.EnterBlockEvent;
import com.aizistral.enigmaticlegacy.api.events.SummonedEntityEvent;
import com.aizistral.enigmaticlegacy.api.items.ICursed;
import com.aizistral.enigmaticlegacy.api.quack.IAbyssalHeartBearer;
import com.aizistral.enigmaticlegacy.api.quack.IProperShieldUser;
import com.aizistral.enigmaticlegacy.client.Quote;
import com.aizistral.enigmaticlegacy.config.OmniconfigHandler;
import com.aizistral.enigmaticlegacy.effects.MoltenHeartEffect;
import com.aizistral.enigmaticlegacy.enchantments.CeaselessEnchantment;
import com.aizistral.enigmaticlegacy.entities.PermanentItemEntity;
import com.aizistral.enigmaticlegacy.gui.EnderChestInventoryButton;
import com.aizistral.enigmaticlegacy.gui.GUIUtils;
import com.aizistral.enigmaticlegacy.gui.PermadeathScreen;
import com.aizistral.enigmaticlegacy.gui.ToggleMagnetEffectsButton;
import com.aizistral.enigmaticlegacy.helpers.BlueSkiesHelper;
import com.aizistral.enigmaticlegacy.helpers.CrossbowHelper;
import com.aizistral.enigmaticlegacy.helpers.EnigmaticEnchantmentHelper;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.aizistral.enigmaticlegacy.helpers.PotionHelper;
import com.aizistral.enigmaticlegacy.items.AngelBlessing;
import com.aizistral.enigmaticlegacy.items.AvariceScroll;
import com.aizistral.enigmaticlegacy.items.BerserkEmblem;
import com.aizistral.enigmaticlegacy.items.BlazingCore;
import com.aizistral.enigmaticlegacy.items.CosmicScroll;
import com.aizistral.enigmaticlegacy.items.CursedRing;
import com.aizistral.enigmaticlegacy.items.CursedScroll;
import com.aizistral.enigmaticlegacy.items.EnderSlayer;
import com.aizistral.enigmaticlegacy.items.EnigmaticAmulet;
import com.aizistral.enigmaticlegacy.items.EyeOfNebula;
import com.aizistral.enigmaticlegacy.items.ForbiddenAxe;
import com.aizistral.enigmaticlegacy.items.ForbiddenFruit;
import com.aizistral.enigmaticlegacy.items.HunterGuidebook;
import com.aizistral.enigmaticlegacy.items.InfernalShield;
import com.aizistral.enigmaticlegacy.items.MiningCharm;
import com.aizistral.enigmaticlegacy.items.MonsterCharm;
import com.aizistral.enigmaticlegacy.items.OceanStone;
import com.aizistral.enigmaticlegacy.items.RevelationTome;
import com.aizistral.enigmaticlegacy.items.TheInfinitum;
import com.aizistral.enigmaticlegacy.items.TheTwist;
import com.aizistral.enigmaticlegacy.items.VoidPearl;
import com.aizistral.enigmaticlegacy.items.EnigmaticAmulet.AmuletColor;
import com.aizistral.enigmaticlegacy.items.generic.ItemSpellstoneCurio;
import com.aizistral.enigmaticlegacy.mixin.AccessorAbstractArrowEntity;
import com.aizistral.enigmaticlegacy.mixin.AccessorAdvancementCommands;
import com.aizistral.enigmaticlegacy.objects.CooldownMap;
import com.aizistral.enigmaticlegacy.objects.DamageSourceNemesisCurse;
import com.aizistral.enigmaticlegacy.objects.DimensionalPosition;
import com.aizistral.enigmaticlegacy.objects.Perhaps;
import com.aizistral.enigmaticlegacy.objects.QuarkHelper;
import com.aizistral.enigmaticlegacy.objects.RegisteredMeleeAttack;
import com.aizistral.enigmaticlegacy.objects.TransientPlayerData;
import com.aizistral.enigmaticlegacy.objects.Vector3;
import com.aizistral.enigmaticlegacy.packets.clients.PacketCosmicRevive;
import com.aizistral.enigmaticlegacy.packets.clients.PacketForceArrowRotations;
import com.aizistral.enigmaticlegacy.packets.clients.PacketPatchouliForce;
import com.aizistral.enigmaticlegacy.packets.clients.PacketPermadeath;
import com.aizistral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.aizistral.enigmaticlegacy.packets.clients.PacketRecallParticles;
import com.aizistral.enigmaticlegacy.packets.clients.PacketSetEntryState;
import com.aizistral.enigmaticlegacy.packets.clients.PacketSlotUnlocked;
import com.aizistral.enigmaticlegacy.packets.clients.PacketSyncPlayTime;
import com.aizistral.enigmaticlegacy.packets.clients.PacketWitherParticles;
import com.aizistral.enigmaticlegacy.packets.server.PacketAnvilField;
import com.aizistral.enigmaticlegacy.packets.server.PacketEnchantingGUI;
import com.aizistral.enigmaticlegacy.packets.server.PacketEnderRingKey;
import com.aizistral.enigmaticlegacy.packets.server.PacketToggleMagnetEffects;
import com.aizistral.enigmaticlegacy.registry.EnigmaticBlocks;
import com.aizistral.enigmaticlegacy.registry.EnigmaticEffects;
import com.aizistral.enigmaticlegacy.registry.EnigmaticEnchantments;
import com.aizistral.enigmaticlegacy.registry.EnigmaticItems;
import com.aizistral.enigmaticlegacy.registry.EnigmaticPotions;
import com.aizistral.enigmaticlegacy.registry.EnigmaticSounds;
import com.aizistral.enigmaticlegacy.triggers.BeheadingTrigger;
import com.aizistral.enigmaticlegacy.triggers.ForbiddenFruitTrigger;
import com.aizistral.enigmaticlegacy.triggers.RevelationTomeBurntTrigger;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractArrow.Pickup;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.ViewportEvent.RenderFog;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent.AdvancementEarnEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import top.theillusivec4.caelus.api.RenderCapeEvent;
import top.theillusivec4.curios.api.event.DropRulesEvent;
import top.theillusivec4.curios.api.type.capability.ICurio.DropRule;
import top.theillusivec4.curios.client.gui.CuriosScreen;

/**
 * Generic event handler of the whole mod.
 * @author Integral
 */

@Mod.EventBusSubscriber(modid = EnigmaticLegacy.MODID)
public class EnigmaticEventHandler {
	private static final String NBT_KEY_PATCHOULIFORCE = "enigmaticlegacy.patchouliforce";
	private static final String NBT_KEY_ENIGMATICGIFT = "enigmaticlegacy.firstjoin";
	private static final String NBT_KEY_CURSEDGIFT = "enigmaticlegacy.cursedgift";
	private static final String NBT_KEY_ENABLESPELLSTONE = "enigmaticlegacy.spellstones_enabled";
	private static final String NBT_KEY_ENABLERING = "enigmaticlegacy.rings_enabled";
	private static final String NBT_KEY_ENABLESCROLL = "enigmaticlegacy.scrolls_enabled";

	public static final ResourceLocation FIREBAR_LOCATION = new ResourceLocation(EnigmaticLegacy.MODID, "textures/gui/firebar.png");
	public static final ResourceLocation ICONS_LOCATION = new ResourceLocation(EnigmaticLegacy.MODID, "textures/gui/generic_icons.png");

	public static final CooldownMap deferredToast = new CooldownMap();
	public static final List<Toast> scheduledToasts = new ArrayList<>();
	public static final Map<LivingEntity, Float> knockbackThatBastard = new WeakHashMap<>();
	public static final Random theySeeMeRollin = new Random();
	public static final Multimap<Player, Item> postmortalPossession = ArrayListMultimap.create();
	public static final Multimap<Player, Guardian> angeredGuardians = ArrayListMultimap.create();
	public static final Map<Player, AABB> desolationBoxes = new WeakHashMap<>();
	public static final Map<Player, Float> lastHealth = new WeakHashMap<>();
	public static final Map<Player, Integer> lastSoulCompassUpdate = new WeakHashMap<>();

	public static int scheduledCubeRevive = -1;
	public static boolean isPoisonHurt = false;
	public static boolean isApplyingNightVision = false;
	private long clientWorldTicks = 0;


	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void renderNameplate(RenderNameTagEvent event) {
		if (event.getEntity() == Minecraft.getInstance().player) {
			Player player = Minecraft.getInstance().player;
			ItemStack insignia = SuperpositionHandler.getCurioStack(player, EnigmaticItems.insignia);

			if (insignia != null && ItemNBTHelper.getBoolean(insignia, "tagDisplayEnabled", true)) {
				event.setResult(Result.ALLOW);
			}
		}

		//		try {
		//			if (event.getEntity() == Minecraft.getInstance().player) {
		//				for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
		//					if (element.getClassName().equals("net.minecraft.client.gui.screens.inventory.InventoryScreen")) {
		//						event.setResult(Result.ALLOW);
		//						break;
		//					}
		//				}
		//			}
		//		} catch (Exception ex) {
		//			// NO-OP
		//		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onTooltip(ItemTooltipEvent event) {
		if (event.getEntity() != null && !event.getEntity().isCreative()) {
			if (event.getItemStack().is(EnigmaticItems.cursedRing)) {
				if (CursedRing.concealAbilities.getValue() && !SuperpositionHandler.isTheCursedOne(event.getEntity())) {
					SuperpositionHandler.obscureTooltip(event.getToolTip());
				}
			} else if (event.getItemStack().getItem() instanceof ICursed) {
				if (!SuperpositionHandler.isTheCursedOne(event.getEntity())) {
					event.getToolTip().replaceAll(component -> {
						if (component.getContents() instanceof TranslatableContents loc) {
							if (loc.getKey().startsWith("tooltip.enigmaticlegacy.cursedOnesOnly"))
								return component;
						}

						return Component.literal(SuperpositionHandler.obscureString(component.getString()))
								.withStyle(component.getStyle());
					});
				}
			}
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void renderCape(RenderCapeEvent event) {
		if (SuperpositionHandler.hasEnigmaticElytra(event.getEntity())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onTooltipRendering(RenderTooltipEvent.Color event) {
		ItemStack stack = event.getItemStack();

		if (stack != null && !stack.isEmpty()) { // cause I don't trust you Forge
			if (ForgeRegistries.ITEMS.getKey(stack.getItem()).getNamespace().equals(EnigmaticLegacy.MODID)) {
				int background = GUIUtils.DEFAULT_BACKGROUND_COLOR;
				int borderStart = GUIUtils.DEFAULT_BORDER_COLOR_START;
				int borderEnd = GUIUtils.DEFAULT_BORDER_COLOR_END;

				if (stack.getItem() instanceof ICursed || stack.getItem() instanceof CursedRing) {
					background = 0xF7101010;
					borderStart = 0x50FF0C00;
					borderEnd = borderStart;
					//borderStart = 0x50656565;
					//borderEnd = (borderStart & 0xFEFEFE) >> 1 | borderStart & 0xFF000000;
				} else if (stack.getItem() == EnigmaticItems.cosmicScroll) {
					background = 0xF0100010;
					borderStart = 0xB0A800A8;
					borderEnd = (borderStart & 0x3E3E3E) >> 1 | borderStart & 0xFF000000;
					//borderEnd = borderStart;
				}

				event.setBackground(background);
				event.setBorderStart(borderStart);
				event.setBorderEnd(borderEnd);
			}
		}
	}

	@SubscribeEvent
	public void onCommandRegistry(RegisterCommandsEvent event) {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("haveadv").requires((source) -> {
			return source.hasPermission(2);
		});

		builder.then(Commands.argument("advancement", ResourceLocationArgument.id())
				.suggests(AccessorAdvancementCommands.getAdvancementSuggestions()).executes(source -> {
					ServerPlayer player = source.getSource().getPlayerOrException();
					Advancement adv = ResourceLocationArgument.getAdvancement(source, "advancement");

					if (adv != null) {
						boolean have = SuperpositionHandler.hasAdvancement(player, adv.getId());
						MutableComponent reply = Component.literal("Advancement exists, and you "
								+ (have ? "do" : "don't") + " have it.");
						source.getSource().sendSuccess(reply.withStyle(have ? ChatFormatting.GREEN : ChatFormatting.RED), true);
						return 1;
					} else {
						MutableComponent reply = Component.literal("Advancement does not exist.");
						source.getSource().sendFailure(reply);
						return 0;
					}
				}));

		event.getDispatcher().register(builder);
	}

	@SubscribeEvent
	public void onEnderPearlTeleport(EntityTeleportEvent.EnderPearl event) {
		if (SuperpositionHandler.hasCurio(event.getPlayer(), EnigmaticItems.eyeOfNebula)
				|| SuperpositionHandler.hasCurio(event.getPlayer(), EnigmaticItems.theCube)) {
			event.setAttackDamage(0);
		}
	}

	@SubscribeEvent
	public void onEnderTeleport(EntityTeleportEvent.EnderEntity event) {
		if (event.getEntity().getPersistentData().contains("ELTeleportBlock")) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onApplyPotion(MobEffectEvent.Applicable event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			MobEffectInstance effect = event.getEffectInstance();

			if (isApplyingNightVision)
				return;

			if (ForgeRegistries.MOB_EFFECTS.getKey(effect.getEffect()).equals(new ResourceLocation("mana-and-artifice", "chrono-exhaustion")))
				return;

			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.voidPearl)) {
				event.setResult(Result.DENY);
			} else if ((SuperpositionHandler.hasCurio(player, EnigmaticItems.enigmaticItem)
					|| SuperpositionHandler.hasCurio(player, EnigmaticItems.theCube)) && !effect.getEffect().isBeneficial()) {
				event.setResult(Result.DENY);
			}
		}
	}

	@SubscribeEvent
	public void serverStarted(ServerStartedEvent event) {
		// NO-OP
	}

	@SubscribeEvent
	public void onItemUse(LivingEntityUseItemEvent.Stop event) {
		if (event.getItem().getItem() instanceof CrossbowItem && event.getEntity() instanceof Player) {
			CrossbowItem crossbow = (CrossbowItem) event.getItem().getItem();
			ItemStack crossbowStack = event.getItem();

			/*
			 * Make sure that our crossbow actually has any of this mod's enchantments,
			 * since otherwise overriding it's behavior is unneccessary.
			 */

			if (!EnigmaticEnchantmentHelper.hasCustomCrossbowEnchantments(crossbowStack))
				return;

			// Cancelling the event to prevent vanilla functionality from working
			event.setCanceled(true);

			/*
			 * Same code as in CrossbowItem#onPlayerStoppedUsing, but CrossbowItem#hasAmmo is
			 * replaced with altered method from CrossbowHelper, to enforce custom behavior.
			 */

			int i = crossbow.getUseDuration(crossbowStack) - event.getDuration();
			float f = CrossbowItem.getPowerForTime(i, crossbowStack);
			if (f >= 1.0F && !CrossbowItem.isCharged(crossbowStack) && CrossbowHelper.hasAmmo(event.getEntity(), crossbowStack)) {
				CrossbowItem.setCharged(crossbowStack, true);
				SoundSource soundSource = event.getEntity() instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
				event.getEntity().level.playSound((Player) null, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), SoundEvents.CROSSBOW_LOADING_END, soundSource, 1.0F, 1.0F / (theySeeMeRollin.nextFloat() * 0.5F + 1.0F) + 0.2F);
			}

		}
	}

	@SubscribeEvent
	public void onPlayerClick(PlayerInteractEvent event) {
		if (event instanceof PlayerInteractEvent.RightClickItem || event instanceof PlayerInteractEvent.RightClickBlock || event instanceof PlayerInteractEvent.EntityInteract) {
			if (event.getItemStack().getItem() instanceof CrossbowItem) {
				ItemStack itemstack = event.getItemStack();

				/*
				 * Make sure that our crossbow actually has any of this mod's enchantments,
				 * since otherwise overriding it's behavior is unneccessary.
				 */

				if (!EnigmaticEnchantmentHelper.hasCustomCrossbowEnchantments(itemstack))
					return;

				// Cancelling the event to prevent vanilla functionality from working
				event.setCanceled(true);

				/*
				 * Same code as in CrossbowItem#onItemRightClick, but CrossbowItem#fireProjectiles is
				 * replaced with altered method from CrossbowHelper, to enforce custom behavior.
				 */

				if (CrossbowItem.isCharged(itemstack)) {
					CrossbowHelper.fireProjectiles(event.getLevel(), event.getEntity(), event.getHand(), itemstack, CrossbowItem.getShootingPower(itemstack), 1.0F);
					CrossbowItem.setCharged(itemstack, false);
					event.setCancellationResult(InteractionResult.CONSUME);
				} else if (!event.getEntity().getProjectile(itemstack).isEmpty() || (CeaselessEnchantment.allowNoArrow.getValue() && EnigmaticEnchantmentHelper.hasCeaselessEnchantment(itemstack))) {
					if (!CrossbowItem.isCharged(itemstack)) {
						((CrossbowItem) Items.CROSSBOW).startSoundPlayed = false;
						((CrossbowItem) Items.CROSSBOW).midLoadSoundPlayed = false;
						event.getEntity().startUsingItem(event.getHand());
					}
					event.setCancellationResult(InteractionResult.CONSUME);
				} else {
					event.setCancellationResult(InteractionResult.FAIL);
				}
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent(receiveCanceled = true)
	public void onOverlayRender(RenderGuiOverlayEvent.Pre event) {
		Minecraft mc = Minecraft.getInstance();

		if (event.getOverlay().equals(VanillaGuiOverlay.EXPERIENCE_BAR.type())) {
			TransientPlayerData data = TransientPlayerData.get(mc.player);

			if (data.getFireImmunityTimer() <= 0 || !SuperpositionHandler.hasCurio(mc.player, EnigmaticItems.blazingCore))
				return;

			float partialTick = event.getPartialTick();
			float barFiller = data.getFireImmunityFraction(partialTick);
			PoseStack PoseStack = event.getPoseStack();
			int x = event.getWindow().getGuiScaledWidth() / 2 - 91;

			int xCorrection = 0;
			int yCorrection = 0;
			boolean renderXP = false;

			if (!renderXP) {
				event.setCanceled(true);
			}

			RenderSystem.setShaderTexture(0, FIREBAR_LOCATION);

			if (true) {
				int k = (int)(barFiller * 183.0F);
				int l = event.getWindow().getGuiScaledHeight() - 32 + 3;
				GuiComponent.blit(PoseStack, x+xCorrection, l+yCorrection, 0, 0, 182, 5, 256, 256);
				if (k > 0) {
					GuiComponent.blit(PoseStack, x+xCorrection, l+yCorrection, 0, 5, k, 5, 256, 256);
				}
			}

			if (true) {
				String title = I18n.get("gui.enigmaticlegacy.blazing_core_bar_title");
				int i1 = (event.getWindow().getGuiScaledWidth() - mc.font.width(title)) / 2;
				int j1 = event.getWindow().getGuiScaledHeight() - 31 - 4;

				int stringXCorrection = 0;
				int stringYCorrection = 0;

				try {
					stringXCorrection = Integer.parseInt(I18n.get("gui.enigmaticlegacy.blazing_core_bar_offsetX"));
					stringYCorrection = Integer.parseInt(I18n.get("gui.enigmaticlegacy.blazing_core_bar_offsetY"));
				} catch (Exception ex) {
					// NO-OP
				}

				i1 += xCorrection + stringXCorrection;
				j1 += yCorrection + stringYCorrection;

				int boundaryColor = 5832704;

				mc.font.draw(PoseStack, title, i1 + 1, j1, boundaryColor);
				mc.font.draw(PoseStack, title, i1 - 1, j1, boundaryColor);
				mc.font.draw(PoseStack, title, i1, j1 + 1, boundaryColor);
				mc.font.draw(PoseStack, title, i1, j1 - 1, boundaryColor);
				mc.font.draw(PoseStack, title, i1, j1, 16770638);
			}

		} else if (event.getOverlay().equals(VanillaGuiOverlay.AIR_LEVEL.type())) {
			if (SuperpositionHandler.hasCurio(mc.player, EnigmaticItems.oceanStone) || SuperpositionHandler.hasCurio(mc.player, EnigmaticItems.voidPearl)
					|| SuperpositionHandler.hasCurio(mc.player, EnigmaticItems.theCube)) {
				if (OceanStone.preventOxygenBarRender.getValue()) {
					event.setCanceled(true);
				}
			}
		} else if (event.getOverlay().equals(VanillaGuiOverlay.FOOD_LEVEL.type())) {
			if (EnigmaticItems.forbiddenFruit.haveConsumedFruit(mc.player)) {
				if (!ForbiddenFruit.renderHungerBar.getValue()) {
					event.setCanceled(true);
					return;
				} else if (mc.player.isCreative() || mc.player.isSpectator() || !ForbiddenFruit.replaceHungerBar.getValue())
					return;

				event.setCanceled(true);
				RenderSystem.setShaderTexture(0, ICONS_LOCATION);

				int width = event.getWindow().getGuiScaledWidth();
				int height = event.getWindow().getGuiScaledHeight();

				Player player = mc.player;
				RenderSystem.enableBlend();
				int left = width / 2 + 91;
				int top = height - ((ForgeGui)Minecraft.getInstance().gui).rightHeight;

				((ForgeGui)Minecraft.getInstance().gui).rightHeight += 10;
				boolean unused = false;// Unused flag in vanilla, seems to be part of a 'fade out' mechanic

				FoodData stats = mc.player.getFoodData();
				int level = stats.getFoodLevel();

				int barPx = 8;
				int barNum = 10;

				for (int i = 0; i < barNum; ++i)
				{
					int idx = i * 2 + 1;
					int x = left - i * barPx - 9;
					int y = top;
					int icon = 16;
					byte background = 0;

					/*
					if (mc.player.isPotionActive(MobEffects.HUNGER))
					{
						icon += 36;
						background = 13;
					}
					if (unused)
					{
						background = 1; //Probably should be a += 1 but vanilla never uses this
					}
					 */

					if (player.getFoodData().getSaturationLevel() <= 0.0F && mc.gui.getGuiTicks() % (level * 3 + 1) == 0)
					{
						y = top + (theySeeMeRollin.nextInt(3) - 1);
					}

					mc.gui.blit(event.getPoseStack(), x, y, 0, 0, 9, 9);

					/*
					if (idx < level) {
						mc.ingameGUI.blit(event.getPoseStack(), x, y, icon + 36, 27, 9, 9);
					} else if (idx == level) {
						mc.ingameGUI.blit(event.getPoseStack(), x, y, icon + 45, 27, 9, 9);
					}
					 */
				}
				RenderSystem.disableBlend();

				RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
			}

		}
	}

	@SubscribeEvent
	public void onEnchantmentLevelSet(EnchantmentLevelSetEvent event) {
		BlockPos where = event.getPos();
		boolean shouldBoost = false;

		int radius = 16;
		List<Player> players = event.getLevel().getEntitiesOfClass(Player.class, new AABB(where.offset(-radius, -radius, -radius), where.offset(radius, radius, radius)));

		for (Player player : players)
			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.cursedRing)) {
				shouldBoost = true;
			}

		if (shouldBoost) {
			event.setEnchantLevel(event.getEnchantLevel()+CursedRing.enchantingBonus.getValue());
		}
	}

	@SubscribeEvent(receiveCanceled = true)
	public void onItemBurnt(FurnaceFuelBurnTimeEvent event) {
		if (event.getItemStack() != null && event.getItemStack().getItem() instanceof RevelationTome) {
			if (ServerLifecycleHooks.getCurrentServer() != null && ItemNBTHelper.verifyExistance(event.getItemStack(), RevelationTome.lastHolderTag)) {
				ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(ItemNBTHelper.getUUID(event.getItemStack(), RevelationTome.lastHolderTag, Mth.createInsecureUUID()));

				if (player != null) {
					RevelationTomeBurntTrigger.INSTANCE.trigger(player);
				}
			}
		}
	}


	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent(priority = EventPriority.LOW)
	public void onInventoryGuiInit(ScreenEvent.Init.Post evt) {
		Screen screen = evt.getScreen();

		if (screen instanceof EnchantmentScreen || (QuarkHelper.getMatrixEnchanterClass() != null
				&& QuarkHelper.getMatrixEnchanterClass().isInstance(screen))) {
			EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketEnchantingGUI());
		}

		if (screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen || screen instanceof CuriosScreen) {
			AbstractContainerScreen<?> gui = (AbstractContainerScreen<?>) screen;
			boolean isCreative = screen instanceof CreativeModeInventoryScreen;

			EnderChestInventoryButton enderButton = new EnderChestInventoryButton(gui, 0, 0, 20, 18, 0, 0, 19,
					new ResourceLocation(
							"enigmaticlegacy:textures/gui/ender_chest_button.png"),(input) -> {
								EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketEnderRingKey(true));
							});

			Tuple<Integer, Integer> enderOffsets = enderButton.getOffsets(isCreative);
			int x = enderOffsets.getA();
			int y = enderOffsets.getB();

			enderButton.x = gui.getGuiLeft() + x;
			enderButton.y = gui.getGuiTop() + y;

			evt.addListener(enderButton);

			ToggleMagnetEffectsButton magnetButton = new ToggleMagnetEffectsButton(gui, 0, 0, 20, 18, 21, 0, 19,
					new ResourceLocation(
							"enigmaticlegacy:textures/gui/ender_chest_button.png"),(input) -> {
								EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketToggleMagnetEffects());
							});

			Tuple<Integer, Integer> magnetOffsets = magnetButton.getOffsets(isCreative);
			x = magnetOffsets.getA();
			y = magnetOffsets.getB();

			magnetButton.x = gui.getGuiLeft() + x;
			magnetButton.y = gui.getGuiTop() + y;

			evt.addListener(magnetButton);

			if (QuarkHelper.getMiniButtonClass() != null) {
				evt.getListenersList().forEach(listener -> {
					if (QuarkHelper.getMiniButtonClass().isInstance(listener)) {
						Button button = (Button) listener;
						button.y -= 22;
					}
				});
			}
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onFogRender(RenderFog event) {
		if (event.getCamera().getFluidInCamera() == FogType.LAVA) {
			if (Minecraft.getInstance().player.hasEffect(EnigmaticEffects.moltenHeartEffect)) {
				RenderSystem.setShaderFogStart(0.0F);
				RenderSystem.setShaderFogEnd((float) MoltenHeartEffect.lavafogDensity.getValue());
			} else if (SuperpositionHandler.hasCurio(Minecraft.getInstance().player, EnigmaticItems.blazingCore)) {
				RenderSystem.setShaderFogStart(0.0F);
				RenderSystem.setShaderFogEnd((float) BlazingCore.lavafogDensity.getValue());
			}
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onEntityTick(TickEvent.ClientTickEvent event) {
		if (event.phase == Phase.END) {
			try {
				Player player = Minecraft.getInstance().player;

				if (player != null) {
					if (this.clientWorldTicks != player.level.getGameTime()) {
						this.clientWorldTicks = player.level.getGameTime();
						EnigmaticLegacy.PROXY.updateInfinitumCounters();
					}
				}

				/*
				 * This event seems to be the only way to detect player logging out of server
				 * on client side, since PlayerEvent.PlayerLoggedOutEvent seems to only be fired
				 * on the server side.
				 */

				if (player == null) {
					if (OmniconfigWrapper.onRemoteServer) {

						/*
						 * After we log out of remote server, dismiss config values it
						 * sent us and load our own ones from local file.
						 */

						for (OmniconfigWrapper wrapper : OmniconfigWrapper.wrapperRegistry.values()) {

							EnigmaticLegacy.LOGGER.info("Dismissing values of " + wrapper.config.getConfigFile().getName() + " in favor of local config...");

							for (Omniconfig.GenericParameter param : wrapper.retrieveInvocationList()) {
								if (param.isSynchronized()) {
									String oldValue = param.valueToString();
									param.invoke(wrapper.config);

									EnigmaticLegacy.LOGGER.info("Value of '" + param.getId() + "' was restored to '" + param.valueToString() + "'; former server-forced value: " + oldValue);
								}
							}
						}

						OmniconfigWrapper.onRemoteServer = false;

					}
				}


				/*
				 * Handler for displaying queued Toasts on client.
				 */

				EnigmaticEventHandler.deferredToast.tick(player);

				if (EnigmaticEventHandler.deferredToast.getCooldown(player) == 1) {
					Minecraft.getInstance().getToasts().addToast(EnigmaticEventHandler.scheduledToasts.get(0));
					EnigmaticEventHandler.scheduledToasts.remove(0);

					if (EnigmaticEventHandler.scheduledToasts.size() > 0) {
						EnigmaticEventHandler.deferredToast.put(player, 5);
					}
				}

			} catch (Exception ex) {
				// DO NOTHING
			}

		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLooting(LootingLevelEvent event) {
		if (event.getDamageSource() != null)
			if (event.getDamageSource().getEntity() instanceof Player) {
				Player player = (Player) event.getDamageSource().getEntity();

				// NO-OP
			}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void miningStuff(PlayerEvent.BreakSpeed event) {

		/*
		 * Handler for calculating mining speed boost from wearing Charm of Treasure Hunter.
		 */

		float originalSpeed = event.getOriginalSpeed();
		float correctedSpeed = originalSpeed;

		float miningBoost = 1.0F;
		if (SuperpositionHandler.hasCurio(event.getEntity(), EnigmaticItems.miningCharm)) {
			miningBoost += MiningCharm.breakSpeedBonus.getValue().asModifier();
		}

		if (SuperpositionHandler.hasCurio(event.getEntity(), EnigmaticItems.cursedScroll)) {
			miningBoost += CursedScroll.miningBoost.getValue().asModifier()*SuperpositionHandler.getCurseAmount(event.getEntity());
		}

		if (EnigmaticItems.enigmaticAmulet.hasColor(event.getEntity(), AmuletColor.GREEN)) {
			miningBoost += 0.25F;
		}

		if (SuperpositionHandler.hasCurio(event.getEntity(), EnigmaticItems.theCube)) {
			miningBoost += 0.6F;
		}

		if (!event.getEntity().isOnGround())
			if (SuperpositionHandler.hasCurio(event.getEntity(), EnigmaticItems.heavenScroll) || SuperpositionHandler.hasCurio(event.getEntity(), EnigmaticItems.fabulousScroll) || SuperpositionHandler.hasCurio(event.getEntity(), EnigmaticItems.enigmaticItem)) {
				correctedSpeed *= 5F;
			}

		if (event.getEntity().isEyeInFluidType(ForgeMod.WATER_TYPE.get()) && !EnchantmentHelper.hasAquaAffinity(event.getEntity())) {
			if (SuperpositionHandler.hasCurio(event.getEntity(), EnigmaticItems.oceanStone)) {
				correctedSpeed *= 5F;
			}
		}

		/*
		if (SuperpositionHandler.hasCurio(event.getEntity(), EnigmaticLegacy.cursedRing) && event.getEntity().inventory.getDestroySpeed(event.getState()) == 1.0F) {
			correctedSpeed += 3.0F;
		}
		 */

		correctedSpeed = correctedSpeed * miningBoost;
		correctedSpeed -= event.getOriginalSpeed();

		event.setNewSpeed(event.getNewSpeed() + correctedSpeed);
	}

	@SubscribeEvent
	public void onHarvestCheck(PlayerEvent.HarvestCheck event) {
		if (event.getEntity() instanceof Player)
			if (!event.canHarvest() && SuperpositionHandler.hasCurio(event.getEntity(), EnigmaticItems.cursedRing)) {
				//event.setCanHarvest(event.getTargetBlock().getHarvestLevel() <= 2);
			}
	}

	@SubscribeEvent
	public void onLivingKnockback(LivingKnockBackEvent event) {
		if (knockbackThatBastard.containsKey(event.getEntity())) {
			float knockbackPower = knockbackThatBastard.get(event.getEntity());
			event.setStrength(event.getStrength() * knockbackPower);
			knockbackThatBastard.remove(event.getEntity());
		}

		if (event.getEntity() instanceof Player && SuperpositionHandler.hasCurio(event.getEntity(), EnigmaticItems.cursedRing)) {
			event.setStrength(event.getStrength()*CursedRing.knockbackDebuff.getValue().asModifier());
		}
	}

	@SubscribeEvent
	public void onEntitySpawn(LivingSpawnEvent.CheckSpawn event) {
		if (event.getSpawnReason() == MobSpawnType.NATURAL) {
			LivingEntity entity = event.getEntity();

			if (entity instanceof Piglin || entity instanceof ZombifiedPiglin || entity instanceof IronGolem
					|| entity instanceof EnderMan) {
				if (desolationBoxes.values().stream().anyMatch(entity.getBoundingBox()::intersects)) {
					event.setResult(Result.DENY);
				}
			}
		}
	}

	private void syncPlayTime(Player player) {
		if (!player.level.isClientSide) {
			int withCurses = EnigmaticLegacy.PROXY.getStats(player, timeWithCursesStat);
			int withoutCurses = EnigmaticLegacy.PROXY.getStats(player, timeWithoutCursesStat);

			EnigmaticLegacy.PROXY.cacheStats(player.getUUID(), withoutCurses, withCurses);
			EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getX(), player.getY(), player.getZ(), 64, player.level.dimension())),
					new PacketSyncPlayTime(player.getUUID(), withCurses, withoutCurses));
		}
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerChangedDimensionEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			lastSoulCompassUpdate.remove(player);

			if (event.getEntity().level.dimension() == Level.NETHER) {
				Quote.SULFUR_AIR.playOnceIfUnlocked(player, 240);
			} else if (event.getEntity().level.dimension() == Level.END) {
				Quote.TORTURED_ROCKS.playOnceIfUnlocked(player, 240);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerTick(LivingTickEvent event) {
		if (!event.getEntity().isAlive())
			return;

		if (!event.getEntity().level.isClientSide) {
			if (event.getEntity() instanceof EnderMan || event.getEntity() instanceof Shulker) {
				int cooldown = event.getEntity().getPersistentData().getInt("ELTeleportBlock");

				if (cooldown > 0) {
					if (--cooldown > 0) {
						event.getEntity().getPersistentData().putInt("ELTeleportBlock", cooldown);
					} else {
						event.getEntity().getPersistentData().remove("ELTeleportBlock");
					}
				}
			}
		}

		if (event.getEntity() instanceof Player player) {
			if (!player.level.isClientSide) {
				if (SuperpositionHandler.isTheCursedOne(player)) {
					player.awardStat(Stats.CUSTOM.get(timeWithCursesStat), 1);
				} else {
					player.awardStat(Stats.CUSTOM.get(timeWithoutCursesStat), 1);
				}

				if (SuperpositionHandler.hasCurio(player, EnigmaticItems.desolationRing) && SuperpositionHandler.isTheWorthyOne(player)) {
					desolationBoxes.put(player, SuperpositionHandler.getBoundingBoxAroundEntity(player, 128));
				} else {
					desolationBoxes.remove(player);
				}

				if (SuperpositionHandler.hasItem(player, EnigmaticItems.soulCompass)) {
					Integer lastUpdate = lastSoulCompassUpdate.get(player);

					if (lastUpdate == null || player.tickCount - lastUpdate > 10) {
						var optional = SuperpositionHandler.updateSoulCompass((ServerPlayer) player);


						optional.ifPresent(tuple -> {
							BlockPos pos = tuple.getB();

							if (player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 256) {
								player.level.getChunkAt(pos);
								UUID id = tuple.getA();
								int radius = 3;

								AABB box = new AABB(pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius,
										pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius);

								List<PermanentItemEntity> list = player.level.getEntitiesOfClass(PermanentItemEntity.class, box, entity -> entity.getUUID().equals(id));

								if (list.size() <= 0) {
									SoulArchive.getInstance().removeItem(id);
								}
							}
						});
					}
				}

				if (player.tickCount % 100 == 0) {
					this.syncPlayTime(player);
				}
			} else {
				if (scheduledCubeRevive > 0 && !(Minecraft.getInstance().screen instanceof ReceivingLevelScreen)) {
					scheduledCubeRevive--;

					if (scheduledCubeRevive == 0) {
						PROXY.displayReviveAnimation(player.getId(), 1);
						scheduledCubeRevive = -1;
					}
				}
			}

			/*
			 * Temporary handler for fixing player state corruption.
			 * I still do not know why it happens.
			 */

			if (Float.isNaN(player.getHealth())) {
				player.setHealth(0F);
			}

			if (Float.isNaN(player.getAbsorptionAmount())) {
				player.setAbsorptionAmount(0F);
			}

			BlueSkiesHelper.maybeFixCapability(player);

			if (EnigmaticItems.forbiddenFruit.haveConsumedFruit(player)) {
				FoodData foodStats = player.getFoodData();
				foodStats.setFoodLevel(20);
				foodStats.saturationLevel = 0F;

				if (player.hasEffect(MobEffects.HUNGER)) {
					player.removeEffect(MobEffects.HUNGER);
				}
			}

			if (!CursedRing.disableInsomnia.getValue())
				if (player.isSleeping() && SuperpositionHandler.hasCurio(player, EnigmaticItems.cursedRing)) {
					if (player.getSleepTimer() == 5) {
						if (player instanceof ServerPlayer) {
							player.sendSystemMessage(Component.translatable("message.enigmaticlegacy.cursed_sleep")
									.withStyle(ChatFormatting.RED));
						}
					} else if (player.getSleepTimer() > 90) {
						player.sleepCounter = 90;
					}
				}

			if (player.isOnFire() && SuperpositionHandler.hasCurio(player, EnigmaticItems.cursedRing)) {
				player.setRemainingFireTicks(player.getRemainingFireTicks()+2);
			}

			/*
			 * Handler for faster effect ticking, if player is bearing Blazing Core.
			 */

			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.blazingCore))
				if (!player.getActiveEffects().isEmpty()) {
					Collection<MobEffectInstance> effects = new ArrayList<>();
					effects.addAll(player.getActiveEffects());

					for (MobEffectInstance effect : effects) {
						if (effect.getEffect().equals(EnigmaticEffects.moltenHeartEffect)) {
							if (player.tickCount % 2 == 0 && effect.duration > 0) {
								effect.duration += 1;
							}
						} else {
							effect.tick(player, () -> {});
						}
					}

				}

			/*
			 * Handler for removing debuffs from players protected by Etherium Armor Shield.
			 * Removed as of Release 2.5.0.
			 */

			/*
			if (EnigmaticLegacy.etheriumChestplate.hasShield(player))
				if (!player.getActivePotionEffects().isEmpty()) {
					List<MobEffectInstance> effects = new ArrayList<MobEffectInstance>(player.getActivePotionEffects());

					for (MobEffectInstance effect : effects) {
						if (!effect.getPotion().isBeneficial()) {
							player.removePotionEffect(effect.getPotion());
						}
					}
				}
			 */

			if (player instanceof ServerPlayer) {

				/*
				 * Handler for players' spellstone cooldowns.
				 */

				if (SuperpositionHandler.hasSpellstoneCooldown(player)) {
					SuperpositionHandler.tickSpellstoneCooldown(player, 1);
				}

				TransientPlayerData data = TransientPlayerData.get(player);
				data.setFireImmunityTimer(data.getFireImmunityTimer() - (player.isOnFire() ? 100 : 200));

				if (data.needsSync) {
					data.syncToPlayer();
					data.needsSync = false;
				}

				EnigmaticItems.enigmaticItem.handleEnigmaticFlight(player);

				/*
				 * Detect Unwitnessed Amulet and turn in into random color;
				 */

				for (NonNullList<ItemStack> list : player.getInventory().compartments) {
					for (int i = 0; i < list.size(); ++i) {
						ItemStack stack = list.get(i);

						// Null check 'cause I don't trust you
						if (stack != null) {
							if (stack.is(EnigmaticItems.unwitnessedAmulet)) {
								stack = new ItemStack(EnigmaticItems.enigmaticAmulet);
								EnigmaticItems.enigmaticAmulet.setInscription(stack, player.getGameProfile().getName());

								if (EnigmaticAmulet.seededColorGen.getValue()) {
									EnigmaticItems.enigmaticAmulet.setSeededColor(stack);
								} else {
									EnigmaticItems.enigmaticAmulet.setRandomColor(stack);
								}

								list.set(i, stack);
							} else if (CursedRing.autoEquip.getValue() && !player.isCreative() && !player.isSpectator()
									&& stack.is(EnigmaticItems.cursedRing)) {
								if (!SuperpositionHandler.hasCurio(player, EnigmaticItems.cursedRing)) {
									if (SuperpositionHandler.tryForceEquip(player, stack)) {
										list.set(i, ItemStack.EMPTY);
									}
								}
							}
						}
					}
				}
			}

		}

	}

	/**
	 * This was done solely to fix interaction with Corpse Complex.
	 * Still needs a more elegant workaround, but should do the trick for now.
	 * @param event
	 */

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onProbableDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof ServerPlayer) {
			ServerPlayer player = (ServerPlayer) event.getEntity();

			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.cursedRing)) {
				postmortalPossession.put(player, EnigmaticItems.cursedRing);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
	public void onConfirmedDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof ServerPlayer) {
			ServerPlayer player = (ServerPlayer) event.getEntity();

			/*
			 * Updates Enigmatic Amulet/Scroll of Postmortal Recall possession status for LivingDropsEvent.
			 */

			if (event.isCanceled()) {
				postmortalPossession.removeAll(player);
				return;
			}

			SuperpositionHandler.setPersistentBoolean(player, "DeathFromEntity", event.getSource().getEntity() instanceof LivingEntity);

			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.enigmaticAmulet) || SuperpositionHandler.hasItem(player, EnigmaticItems.enigmaticAmulet)) {
				postmortalPossession.put(player, EnigmaticItems.enigmaticAmulet);
			} else if (SuperpositionHandler.hasCurio(player, EnigmaticItems.ascensionAmulet) || SuperpositionHandler.hasItem(player, EnigmaticItems.ascensionAmulet)) {
				postmortalPossession.put(player, EnigmaticItems.ascensionAmulet);
			}

			if (SuperpositionHandler.hasItem(player, EnigmaticItems.cursedStone)) {
				postmortalPossession.put(player, EnigmaticItems.cursedStone);

				for (List<ItemStack> list : player.getInventory().compartments) {
					for (ItemStack itemstack : list) {
						if (!itemstack.isEmpty() && itemstack.getItem() == EnigmaticItems.cursedStone) {
							itemstack.setCount(0);
						}
					}
				}
			}

			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.escapeScroll)) {
				postmortalPossession.put(player, EnigmaticItems.escapeScroll);

				if (!player.level.isClientSide) {
					ItemStack tomeStack = SuperpositionHandler.getCurioStack(player, EnigmaticItems.escapeScroll);
					PermanentItemEntity droppedTomeStack = new PermanentItemEntity(player.level, player.getX(), player.getY() + (player.getBbHeight() / 2), player.getZ(), tomeStack.copy());
					droppedTomeStack.setPickupDelay(10);
					player.level.addFreshEntity(droppedTomeStack);

					tomeStack.shrink(1);
				}
			}

			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.eldritchAmulet) && SuperpositionHandler.isTheWorthyOne(player)) {
				EnigmaticItems.eldritchAmulet.storeInventory(player);
				postmortalPossession.put(player, EnigmaticItems.eldritchAmulet);
			} else {
				for (int i = 0; i < player.getInventory().armor.size(); i++) {
					ItemStack armor = player.getInventory().armor.get(i);

					if (armor.getEnchantmentLevel(EnigmaticEnchantments.eternalBindingCurse) > 0) {
						player.getInventory().armor.set(i, ItemStack.EMPTY);
						SuperpositionHandler.setPersistentTag(player, "EternallyBoundArmor" + i, armor.serializeNBT());
					}
				}
			}
		}

		if (event.getSource().getEntity() instanceof ServerPlayer player) {
			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.theCube)) {
				EnigmaticItems.theCube.applyRandomEffect(player, true);
			}

			if (event.getEntity() instanceof WitherBoss) {
				int killedWither = SuperpositionHandler.getPersistentInteger(player, "TimesKilledWither", 0);

				if (killedWither <= 0) {
					Quote.BREATHES_RELIEVED.play(player, 140);
					killedWither++;
				} else if (killedWither == 1) {
					Quote.APPALING_PRESENCE.play(player, 140);
					killedWither++;
				} else if (killedWither == 2) {
					Quote.TERRIFYING_FORM.play(player, 140);
					killedWither++;
				} else if (killedWither > 2 && killedWither < 5) {
					killedWither++;
				} else if (killedWither == 4) {
					Quote.WHETHER_IT_IS.play(player, 140);
					killedWither++;
				}

				SuperpositionHandler.setPersistentInteger(player, "TimesKilledWither", killedWither);
			}
		}
	}

	@SubscribeEvent
	public void onCurioDrops(DropRulesEvent event) {
		event.addOverride(stack -> stack.getEnchantmentLevel(EnigmaticEnchantments.eternalBindingCurse) > 0,
				DropRule.ALWAYS_KEEP);

		if (event.getEntity() instanceof ServerPlayer player) {
			if (postmortalPossession.containsEntry(player, EnigmaticItems.eldritchAmulet)) {
				event.addOverride(stack -> stack.
						getEnchantmentLevel(Enchantments.VANISHING_CURSE) <= 0, DropRule.ALWAYS_KEEP);
			}

			if (this.hadUnholyStone(player) && player.level.dimension() == PROXY.getNetherKey()) {
				BlockPos deathPos = player.blockPosition();

				if (this.isThereLava(player.level, deathPos)) {
					BlockPos surfacePos = deathPos;

					while (true) {
						BlockPos nextAbove = surfacePos.offset(0, 1, 0);
						if (this.isThereLava(player.level, nextAbove)) {
							surfacePos = nextAbove;
							continue;
						} else {
							break;
						}
					}

					boolean confirmLavaPool = true;

					for(int i = -3; i <= 2; ++i) {
						final int fi = i;

						boolean checkArea = BlockPos.betweenClosedStream(surfacePos.offset(-3, i, -3), surfacePos.offset(3, i, 3))
								.map(blockPos -> {
									if (fi <= 0)
										return this.isThereLava(player.level, blockPos);
									else
										return player.level.isEmptyBlock(blockPos) || this.isThereLava(player.level, blockPos);
								})
								.reduce((prevResult, nextElement) -> {
									return prevResult && nextElement;
								}).orElse(false);

						confirmLavaPool = confirmLavaPool && checkArea;
					}

					if (confirmLavaPool) {
						event.addOverride(stack -> stack != null && (stack.is(EnigmaticItems.cursedRing) || stack.is(EnigmaticItems.desolationRing)), DropRule.DESTROY);
						EnigmaticItems.soulCrystal.setLostCrystals(player, EnigmaticItems.soulCrystal.getLostCrystals(player)+1);
						SuperpositionHandler.destroyCurio(player, EnigmaticItems.cursedRing);
						SuperpositionHandler.destroyCurio(player, EnigmaticItems.desolationRing);

						player.level.playSound(null, player.blockPosition(), SoundEvents.WITHER_DEATH, SoundSource.PLAYERS, 1.0F, 0.5F);
						SuperpositionHandler.setPersistentBoolean(player, "DestroyedCursedRing", true);
					}
				}
			}
		}
	}

	private boolean isThereLava(Level world, BlockPos pos) {
		FluidState fluidState = world.getBlockState(pos).getFluidState();
		if (fluidState != null && fluidState.is(FluidTags.LAVA) && fluidState.isSource())
			return true;
		else
			return false;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onLivingDeath(LivingDeathEvent event) {

		if (event.getEntity() instanceof Player && !event.getEntity().level.isClientSide) {
			Player player = (Player) event.getEntity();

			/*
			 * Immortality handler for Heart of Creation and Pearl of the Void.
			 */

			if (isPoisonHurt && event.getSource() == DamageSource.MAGIC) {
				event.setCanceled(true);
				player.setHealth(1);
			} else if (lastHealth.containsKey(player) && lastHealth.get(player) >= 1.5F && SuperpositionHandler.hasCurio(player, EnigmaticItems.theCube)) {
				event.setCanceled(true);
				player.setHealth(1);
			} else if (SuperpositionHandler.hasCurio(player, EnigmaticItems.enigmaticItem) || player.getInventory().contains(new ItemStack(EnigmaticItems.enigmaticItem)) || event.getSource() instanceof DamageSourceNemesisCurse) {
				event.setCanceled(true);
				player.setHealth(1);
			} else if (SuperpositionHandler.hasCurio(player, EnigmaticItems.voidPearl) && Math.random() <= VoidPearl.undeadProbability.getValue().asMultiplier(false)) {
				event.setCanceled(true);
				player.setHealth(1);
			} else {
				if (SuperpositionHandler.isTheWorthyOne(player)) {
					boolean infinitum = false;

					if (player.getMainHandItem() != null && player.getMainHandItem().getItem() == EnigmaticItems.theInfinitum) {
						infinitum = true;
					} else if (player.getMainHandItem() != null && player.getMainHandItem().getItem() == EnigmaticItems.theInfinitum) {
						infinitum = true;
					}

					if (infinitum) {
						if (Math.random() <= TheInfinitum.undeadProbability.getValue().asMultiplier(false)) {
							event.setCanceled(true);
							player.setHealth(1);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onDeathLow(LivingDeathEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.theCube)) {
				if (!SuperpositionHandler.hasSpellstoneCooldown(player)) {
					event.setCanceled(true);
					player.setHealth(player.getMaxHealth()*0.3F);

					EnigmaticItems.theCube.triggerActiveAbility(player.level, player, SuperpositionHandler.getCurioStack(player, EnigmaticItems.theCube));

					player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 1200, 2));
					player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200, 1));
					player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1200, 0));
					player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 1));

					EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getX(), player.getY(), player.getZ(), 128, player.level.dimension())),
							new PacketCosmicRevive(player.getId(), 1));
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingHeal(LivingHealEvent event) {

		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();

			/*
			 * Regeneration slowdown handler for Pearl of the Void.
			 * Removed as of Release 2.5.0.

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.voidPearl)) {
				if (event.getAmount() <= 1.0F) {
					event.setAmount((float) (event.getAmount() / (1.5F * ConfigHandler.VOID_PEARL_REGENERATION_MODIFIER.getValue())));
				}
			}
			 */

			if (event.getAmount() <= 1.0F)
				if (EnigmaticItems.forbiddenFruit.haveConsumedFruit(player)) {
					event.setAmount(event.getAmount()*ForbiddenFruit.regenerationSubtraction.getValue().asModifierInverted());
				}

			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.cursedScroll)) {
				event.setAmount(event.getAmount() + (event.getAmount()*(CursedScroll.regenBoost.getValue().asModifier()*SuperpositionHandler.getCurseAmount(player))));
			}
		}

	}

	@SubscribeEvent
	public void onProjectileImpact(ProjectileImpactEvent event) {
		if (event.getRayTraceResult() instanceof EntityHitResult) {
			EntityHitResult result = (EntityHitResult) event.getRayTraceResult();

			if (result.getEntity() instanceof Player) {
				Player player = (Player) result.getEntity();
				Entity arrow = event.getEntity();

				if (!player.level.isClientSide) {
					if (arrow instanceof Projectile) {
						Projectile projectile = (Projectile) arrow;

						if (projectile.getOwner() == player) {
							for (String tag : arrow.getTags()) {
								if (tag.startsWith("AB_DEFLECTED")) {
									try {
										int time = Integer.parseInt(tag.split(":")[1]);
										if (arrow.tickCount - time < 10)
											// If we cancel the event here it gets stuck in the infinite loop
											return;
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								}
							}
						}
					}

					boolean trigger = false;
					double chance = 0.0D;

					if (SuperpositionHandler.hasCurio(player, EnigmaticItems.angelBlessing)) {
						trigger = true;
						chance += AngelBlessing.deflectChance.getValue().asModifier(false);
					}

					if (SuperpositionHandler.hasCurio(player, EnigmaticItems.theCube)) {
						trigger = true;
						chance += 0.35;
					}

					if (EnigmaticItems.enigmaticAmulet.hasColor(player, AmuletColor.VIOLET)) {
						trigger = true;
						chance += 0.15;
					}

					if (trigger && Math.random() <= chance) {
						event.setCanceled(true);

						arrow.setDeltaMovement(arrow.getDeltaMovement().scale(-1.0D));
						arrow.yRotO = arrow.getYRot() + 180.0F;
						arrow.setYRot(arrow.getYRot() + 180.0F);;

						if (arrow instanceof AbstractArrow absar) {
							if (!(absar instanceof ThrownTrident)) {
								absar.setOwner(player);
							}

							((AccessorAbstractArrowEntity)absar).clearHitEntities();
							absar.pickup = Pickup.CREATIVE_ONLY;
							absar.setPierceLevel((byte) 0);
						} else if (arrow instanceof AbstractHurtingProjectile) {
							((AbstractHurtingProjectile) arrow).setOwner(player);

							((AbstractHurtingProjectile) arrow).xPower = -((AbstractHurtingProjectile) arrow).xPower;
							((AbstractHurtingProjectile) arrow).yPower = -((AbstractHurtingProjectile) arrow).yPower;
							((AbstractHurtingProjectile) arrow).zPower = -((AbstractHurtingProjectile) arrow).zPower;
						}

						arrow.getTags().removeIf(tag -> tag.startsWith("AB_DEFLECTED"));
						arrow.addTag("AB_DEFLECTED:" + arrow.tickCount);

						EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getX(), player.getY(), player.getZ(), 64.0D, player.level.dimension())), new PacketForceArrowRotations(arrow.getId(), arrow.getYRot(), arrow.getXRot(), arrow.getDeltaMovement().x, arrow.getDeltaMovement().y, arrow.getDeltaMovement().z, arrow.getX(), arrow.getY(), arrow.getZ()));
						player.level.playSound(null, player.blockPosition(), EnigmaticSounds.soundDeflect, SoundSource.PLAYERS, 1.0F, 0.95F + (float) (Math.random() * 0.1D));
					}
				} /*else {
					if (event.getEntity().getTags().contains("enigmaticlegacy.redirected")) {
						event.setCanceled(true);
						event.getEntity().removeTag("enigmaticlegacy.redirected");
					}
				}*/
			}
		}
	}

	@SubscribeEvent
	public void onEntityAttacked(LivingAttackEvent event) {
		if (event.getEntity().level.isClientSide)
			return;

		/*
		 * Handler for immunities and projectile deflection.
		 */

		if (event.getEntity() instanceof Player player) {
			if (player.hasEffect(EnigmaticEffects.moltenHeartEffect) && EnigmaticEffects.moltenHeartEffect.providesImmunity(event.getSource())) {
				event.setCanceled(true);
				return;
			}

			if (event.getSource().getDirectEntity() instanceof AbstractHurtingProjectile || event.getSource().getDirectEntity() instanceof AbstractArrow) {
				/*if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.angelBlessing) && Math.random() <= ConfigHandler.ANGEL_BLESSING_DEFLECT_CHANCE.getValue().asModifier(false)) {
					event.setCanceled(true);
					Entity arrow = event.getSource().getImmediateSource();

					if (!arrow.world.isRemote) {
						CompoundTag data = arrow.serializeNBT();
						Entity entity = arrow.getType().create(arrow.world);
						entity.deserializeNBT(data);
						entity.setUniqueId(Mth.getRandomUUID());
						entity.setPositionAndUpdate(arrow.getPosX(), arrow.getPosY(), arrow.getPosZ());
						entity.setMotion(arrow.getMotion().scale(-1.0D));

						entity.rotationYaw = arrow.rotationYaw + 180.0F;
						entity.prevRotationYaw = arrow.rotationYaw + 180.0F;

						if (entity instanceof AbstractArrow && !(entity instanceof ThrownTrident)) {
							((AbstractArrow) entity).setShooter(player);
						} else if (entity instanceof AbstractHurtingProjectile) {
							((AbstractHurtingProjectile) entity).setShooter(player);

							((AbstractHurtingProjectile) entity).accelerationX = -((AbstractHurtingProjectile) arrow).accelerationX;
							((AbstractHurtingProjectile) entity).accelerationY = -((AbstractHurtingProjectile) arrow).accelerationY;
							((AbstractHurtingProjectile) entity).accelerationZ = -((AbstractHurtingProjectile) arrow).accelerationZ;
						}

						arrow.remove();
						arrow.forceSetPosition(0, 0, 0);

						EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getPosX(), player.getPosY(), player.getPosZ(), 64.0D, player.world.getDimensionKey())), new PacketForceArrowRotations(arrow.getEntityId(), arrow.rotationYaw, arrow.rotationPitch, arrow.getMotion().x, arrow.getMotion().y, arrow.getMotion().z));

						//player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundSource.PLAYERS, 1.0F, 0.95F + (float) (Math.random() * 0.1D));
						player.world.playSound(null, player.getPosition(), EnigmaticLegacy.DEFLECT, SoundSource.PLAYERS, 1.0F, 0.95F + (float) (Math.random() * 0.1D));

						player.world.addEntity(entity);
					}

				}*/
			}

			if (event.getSource().msgId == DamageSource.FALL.msgId) {
				if (EnigmaticItems.enigmaticAmulet.hasColor(player, AmuletColor.MAGENTA) && event.getAmount() <= 2.0f) {
					event.setCanceled(true);
				}
			}

			List<ItemStack> advancedCurios = SuperpositionHandler.getAdvancedCurios(player);
			if (advancedCurios.size() > 0) {
				for (ItemStack advancedCurioStack : advancedCurios) {
					ItemSpellstoneCurio advancedCurio = (ItemSpellstoneCurio) advancedCurioStack.getItem();

					if (advancedCurio.immunityList.contains(event.getSource().msgId)) {
						event.setCanceled(true);
					}

					/*
					 * This used to heal player from debuff damage if they have Pearl of the Void.
					 * Removed in 2.5.0 Release.
					 */

					/*
					if (advancedCurio == EnigmaticLegacy.voidPearl && EnigmaticLegacy.voidPearl.healList.contains(event.getSource().damageType)) {
						player.heal(event.getAmount());
						event.setCanceled(true);
					}
					 */
				}
			}

			/*
			 * Handler for Eye of the Nebula dodge effect.
			 */

			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.eyeOfNebula) && !event.isCanceled()) {
				if (EyeOfNebula.dodgeProbability.getValue().roll() && player.invulnerableTime <= 10 && event.getSource().getEntity() instanceof LivingEntity) {

					for (int counter = 0; counter <= 32; counter++) {
						if (SuperpositionHandler.validTeleportRandomly(player, player.level, (int) EyeOfNebula.dodgeRange.getValue())) {
							break;
						}
					}

					player.invulnerableTime = 20;
					event.setCanceled(true);
				}
			}

			if (!player.getAbilities().invulnerable && player.getEffect(MobEffects.FIRE_RESISTANCE) == null)
				if (SuperpositionHandler.hasCurio(player, EnigmaticItems.blazingCore) && !event.isCanceled() && event.getSource().msgId.equals(DamageSource.LAVA.msgId)) {
					TransientPlayerData data = TransientPlayerData.get(player);

					if (data.getFireImmunityTimer() < data.getFireImmunityTimerCap()) {
						if (data.getFireImmunityTimer() < (data.getFireImmunityTimerCap() - (data.getFireDiff()))) {
							event.setCanceled(true);
						}

						if (data.getFireImmunityTimer() == 0 && !player.level.isClientSide) {
							player.level.playSound(null, player.blockPosition(), SoundEvents.LAVA_EXTINGUISH, SoundSource.PLAYERS, 1.0F, 0.5F + (theySeeMeRollin.nextFloat() * 0.5F));
							EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getX(), player.getY(), player.getZ(), 32, player.level.dimension())), new PacketWitherParticles(player.getX(), player.getY(0.25D), player.getZ(), 8, false, 1));
						}

						data.setFireImmunityTimer(data.getFireImmunityTimer()+200);
					}
				}

		} else if (event.getSource().getDirectEntity() instanceof Player && "player".equals(event.getSource().msgId)) {
			Player player = (Player) event.getSource().getDirectEntity();

			/*
			 * Handler for triggering Extradimensional Eye's effects when player left-clicks
			 * (or, more technically correct, directly attacks) the entity with the Eye in
			 * main hand.
			 */

			if (player.getMainHandItem() != null && player.getMainHandItem().getItem() == EnigmaticItems.extradimensionalEye)
				if (ItemNBTHelper.verifyExistance(player.getMainHandItem(), "BoundDimension"))
					if (ItemNBTHelper.getString(player.getMainHandItem(), "BoundDimension", "minecraft:overworld").equals(event.getEntity().level.dimension().location().toString())) {
						event.setCanceled(true);
						ItemStack stack = player.getMainHandItem();

						EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), 128, event.getEntity().level.dimension())), new PacketPortalParticles(event.getEntity().getX(), event.getEntity().getY() + (event.getEntity().getBbHeight() / 2), event.getEntity().getZ(), 96, 1.5D, false));

						event.getEntity().level.playSound(null, event.getEntity().blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
						event.getEntity().teleportTo(ItemNBTHelper.getDouble(stack, "BoundX", 0D), ItemNBTHelper.getDouble(stack, "BoundY", 0D), ItemNBTHelper.getDouble(stack, "BoundZ", 0D));
						event.getEntity().level.playSound(null, event.getEntity().blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));

						EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), 128, event.getEntity().level.dimension())), new PacketRecallParticles(event.getEntity().getX(), event.getEntity().getY() + (event.getEntity().getBbHeight() / 2), event.getEntity().getZ(), 48, false));

						if (!player.getAbilities().instabuild) {
							stack.shrink(1);
						}
					}

			float knockbackPower = 1F;

			if (player.getMainHandItem() != null && player.getMainHandItem().getItem() == EnigmaticItems.theTwist && SuperpositionHandler.isTheCursedOne(player)) {
				knockbackPower += TheTwist.knockbackBonus.getValue().asModifier(false);
			} else if (player.getMainHandItem() != null && player.getMainHandItem().getItem() == EnigmaticItems.theInfinitum && SuperpositionHandler.isTheWorthyOne(player)) {
				knockbackPower += TheInfinitum.knockbackBonus.getValue().asModifier(false);
			} else if (player.getMainHandItem() != null && player.getMainHandItem().is(EnigmaticItems.enderSlayer) && SuperpositionHandler.isTheCursedOne(player)) {
				if (EnigmaticItems.enderSlayer.isEndDweller(event.getEntity())) {
					knockbackPower += EnderSlayer.endKnockbackBonus.getValue().asModifier(false);
				}
			}

			if (event.getEntity() instanceof Player victim) {
				if (SuperpositionHandler.hasArchitectsFavor(player)) {
					if (!SuperpositionHandler.isTheBlessedOne(victim)) {
						knockbackPower += CosmicScroll.unchosenKnockbackBonus.getValue().asModifier(false);
					}
				}
			}

			if (knockbackPower > 1) {
				knockbackThatBastard.put(event.getEntity(), (event.getEntity() instanceof Phantom ? (knockbackPower*1.5F) : knockbackPower));
			}
		}

		if (event.getEntity() instanceof Animal animal && event.getSource().getEntity() instanceof Player player) {
			if (SuperpositionHandler.hasItem(player, EnigmaticItems.animalGuidebook)) {
				if (EnigmaticItems.animalGuidebook.isProtectedAnimal(animal)) {
					event.setCanceled(true);

					if (animal.getTarget() == player) {
						event.setCanceled(false);
					} else {
						for (WrappedGoal goal : animal.targetSelector.getAvailableGoals())
							if (goal.getGoal() instanceof TargetGoal targetGoal) {
								if (targetGoal.targetMob == player) {
									event.setCanceled(false);
								}
							}
					}

					Brain<?> brain = animal.getBrain();

					if (brain != null) {
						try {
							var memory = brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET)
									? brain.getMemory(MemoryModuleType.ATTACK_TARGET) : Optional.empty();
							if (memory.isPresent() && memory.get() == player) {
								event.setCanceled(false);
							}
						} catch (NullPointerException ex) {
							// I don't get why it happens here but it does lol
						}
					}
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onEntityDamaged(LivingDamageEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			lastHealth.put(player, player.getHealth());
		}

		if (event.getSource().getDirectEntity() instanceof Player && !event.getSource().getDirectEntity().level.isClientSide) {
			Player player = (Player) event.getSource().getDirectEntity();

			float lifesteal = 0;

			if (EnigmaticItems.enigmaticAmulet.hasColor(player, AmuletColor.BLACK)) {
				lifesteal += event.getAmount() * 0.1F;
			}

			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.eldritchAmulet)) {
				if (SuperpositionHandler.isTheWorthyOne(player)) {
					lifesteal += event.getAmount() * 0.15F;
				}
			}

			if (player.getMainHandItem() != null && player.getMainHandItem().getItem() == EnigmaticItems.theInfinitum) {
				if (SuperpositionHandler.isTheWorthyOne(player)) {
					lifesteal += event.getAmount() * 0.1F;
				}
			}

			if (lifesteal > 0) {
				player.heal(lifesteal);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onEntityAttacked(AttackEntityEvent event) {
		if (!event.getEntity().level.isClientSide) {
			RegisteredMeleeAttack.registerAttack(event.getEntity());
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerHurt(LivingHurtEvent event) {
		if (event.getEntity() instanceof Player player && event.getAmount() > 0) {
			/*
			 * Handler for stripping away Blazing Strength.
			 */

			if (player.hasEffect(EnigmaticEffects.blazingStrengthEffect)) {
				player.removeEffect(EnigmaticEffects.blazingStrengthEffect);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void endEntityHurt(LivingHurtEvent event) {
		if (event.getEntity() instanceof ServerPlayer player && event.getSource().getEntity() != null) {
			if (event.getAmount() > EnigmaticItems.theCube.getDamageLimit(player) && SuperpositionHandler.hasCurio(player, EnigmaticItems.theCube)) {
				event.setCanceled(true);
				player.level.playSound(null, player.blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1F, 1F);

				if (event.getSource().getDirectEntity() instanceof LivingEntity living) {
					Vector3 look = new Vector3(living.position()).subtract(new Vector3(player.position())).normalize();
					Vector3 dir = look.multiply(1D);

					etheriumConfig.knockBack(living, 1.0F, dir.x, dir.z);
				}
			}
		}
	}

	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event) {
		if (event.getAmount() >= Float.MAX_VALUE)
			return;

		// TODO The priorities are messed up as fuck. We gotta do something about it.

		/*
		 * Ideally fixed numerical increases should come first, then percentage alterations,
		 * and the last in order - handlers that use the event for notification purpose, doing
		 * something other with it than altering damage dealt.
		 */

		if (event.getSource().getEntity() instanceof LivingEntity) {
			LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
			Entity immediateSource = event.getSource().getDirectEntity();

			if (immediateSource instanceof ThrownTrident || immediateSource instanceof LivingEntity) {
				ItemStack mainhandStack = ItemStack.EMPTY;
				if (immediateSource instanceof ThrownTrident) {
					if (((ThrownTrident)immediateSource).tridentItem != null) {
						mainhandStack = ((ThrownTrident)immediateSource).tridentItem;
					}
				} else {
					if (((LivingEntity)immediateSource).getMainHandItem() != null) {
						mainhandStack = ((LivingEntity)immediateSource).getMainHandItem();
					}
				}

				int torrentLevel = 0;
				int wrathLevel = 0;

				if ((torrentLevel = mainhandStack.getEnchantmentLevel(EnigmaticEnchantments.torrentEnchantment)) > 0) {
					event.setAmount(event.getAmount() + EnigmaticEnchantments.torrentEnchantment.bonusDamageByCreature(attacker, event.getEntity(), torrentLevel));
				}

				if ((wrathLevel = mainhandStack.getEnchantmentLevel(EnigmaticEnchantments.wrathEnchantment)) > 0) {
					event.setAmount(event.getAmount() + EnigmaticEnchantments.wrathEnchantment.bonusDamageByCreature(attacker, event.getEntity(), wrathLevel));
				}
			}
		}

		if (event.getSource().getDirectEntity() instanceof AbstractArrow) {
			AbstractArrow arrow = (AbstractArrow) event.getSource().getDirectEntity();

			for (String tag : arrow.getTags()) {
				if (tag.startsWith(CrossbowHelper.sharpshooterTagPrefix)) {

					/*
					 * Since our custom tag is just a prefix + enchantment level, we can remove
					 * that prefix from received String and freely parse remaining Integer.
					 */

					int dmg = Integer.parseInt(tag.replace(CrossbowHelper.sharpshooterTagPrefix, ""));
					event.setAmount(dmg);
					break;
				}
			}
		} else if (event.getSource().getDirectEntity() instanceof Player && "player".equals(event.getSource().msgId)) {
			Player player = (Player) event.getSource().getDirectEntity();

			/*
			 * Handler for applying Withering to victims of bearer of the Void Pearl.
			 */

			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.voidPearl)) {
				event.getEntity().addEffect(new MobEffectInstance(MobEffects.WITHER, VoidPearl.witheringTime.getValue(), VoidPearl.witheringLevel.getValue(), false, true));
			}

			if (player instanceof ServerPlayer && SuperpositionHandler.isTheCursedOne(player)) {
				if (event.getEntity() instanceof ServerPlayer targetPlayer) {
					targetPlayer.getCooldowns().addCooldown(Items.ENDER_PEARL, 400);
					targetPlayer.getCooldowns().addCooldown(EnigmaticItems.recallPotion, 400);
					targetPlayer.getCooldowns().addCooldown(EnigmaticItems.twistedMirror, 400);

					if (SuperpositionHandler.hasCurio(targetPlayer, EnigmaticItems.eyeOfNebula)
							|| SuperpositionHandler.hasCurio(targetPlayer, EnigmaticItems.theCube)) {
						SuperpositionHandler.setSpellstoneCooldown(targetPlayer, 400);
					}
				}

				if (event.getEntity() instanceof EnderMan || event.getEntity() instanceof Shulker) {
					event.getEntity().getPersistentData().putInt("ELTeleportBlock", 400);
				}
			}

			float bonusDamage = 0F;

			if (player.getMainHandItem() != null) {
				ItemStack mainhandStack = player.getMainHandItem();

				if (mainhandStack.is(EnigmaticItems.theTwist)) {
					if (SuperpositionHandler.isTheCursedOne(player)) {
						if (OmniconfigHandler.isBossOrPlayer(event.getEntity())) {
							bonusDamage += event.getAmount() * TheTwist.bossDamageBonus.getValue().asModifier(false);
						}
					} else {
						event.setCanceled(true);
					}
				} else if (mainhandStack.is(EnigmaticItems.theInfinitum)) {
					if (SuperpositionHandler.isTheWorthyOne(player)) {
						if (OmniconfigHandler.isBossOrPlayer(event.getEntity())) {
							//event.setAmount(10000000);
							bonusDamage += event.getAmount() * TheInfinitum.bossDamageBonus.getValue().asModifier(false);
						}
					} else {
						event.setCanceled(true);

						player.addEffect(new MobEffectInstance(MobEffects.WITHER,            160, 3, false, true));
						player.addEffect(new MobEffectInstance(MobEffects.CONFUSION,         500, 3, false, true));
						player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,          300, 3, false, true));
						player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 3, false, true));
						player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN,      300, 3, false, true));
						player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS,         100, 3, false, true));
					}
				} else if (mainhandStack.is(EnigmaticItems.enderSlayer)) {
					if (SuperpositionHandler.isTheCursedOne(player)) {
						if (EnigmaticItems.enderSlayer.isEndDweller(event.getEntity())) {
							if (player.level.dimension().equals(PROXY.getEndKey())) {
								if (event.getEntity() instanceof EnderMan
										&& RegisteredMeleeAttack.getRegisteredAttackStregth(player) >= 1F) {
									event.setAmount((event.getAmount() + 100F) * 10F);
								}
								event.getEntity().getPersistentData().putBoolean("EnderSlayerVictim", true);
							}

							bonusDamage += event.getAmount() * EnderSlayer.endDamageBonus.getValue().asModifier(false);
						}
					} else {
						event.setCanceled(true);
					}
				}

				event.setAmount(event.getAmount() + bonusDamage);

				int slayerLevel = 0;
				if ((slayerLevel = mainhandStack.getEnchantmentLevel(EnigmaticEnchantments.slayerEnchantment)) > 0) {
					event.setAmount(event.getAmount() + EnigmaticEnchantments.slayerEnchantment.bonusDamageByCreature(player, event.getEntity(), slayerLevel));

					if (event.getEntity() instanceof Monster) {
						int i = 20 + player.getRandom().nextInt(10 * slayerLevel);
						event.getEntity().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, i, 3));
					}
				}

				if (EnigmaticEnchantmentHelper.hasNemesisCurseEnchantment(mainhandStack)) {
					float supposedDamage = event.getAmount()*0.35F;

					player.hurt(new DamageSourceNemesisCurse(event.getEntity()), supposedDamage);
				}
			} else {
				event.setAmount(event.getAmount() + bonusDamage);
			}
		}

		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();

			/*
			 * Handler for resistance lists.
			 */

			List<ItemStack> advancedCurios = SuperpositionHandler.getAdvancedCurios(player);
			if (advancedCurios.size() > 0) {
				for (ItemStack advancedCurioStack : advancedCurios) {
					ItemSpellstoneCurio advancedCurio = (ItemSpellstoneCurio) advancedCurioStack.getItem();
					Mob trueSource = event.getSource().getEntity() instanceof Mob ? (Mob)event.getSource().getEntity() : null;

					if (event.getSource().msgId.startsWith("explosion") && advancedCurio == EnigmaticItems.golemHeart && SuperpositionHandler.hasAnyArmor(player)) {
						continue;
					} else if (advancedCurio == EnigmaticItems.blazingCore && trueSource != null && (trueSource.getMobType() == MobType.WATER || trueSource instanceof Drowned)) {
						event.setAmount(event.getAmount() * 2F);
					} else if (advancedCurio == EnigmaticItems.eyeOfNebula && player.isInWater()) {
						event.setAmount(event.getAmount() * 2F);
					} else if (advancedCurio == EnigmaticItems.oceanStone && trueSource != null && (trueSource.getMobType() == MobType.WATER || trueSource instanceof Drowned)) {
						event.setAmount(event.getAmount() * OceanStone.underwaterCreaturesResistance.getValue().asModifierInverted());
					}

					if (advancedCurio.resistanceList.containsKey(event.getSource().msgId)) {
						event.setAmount(event.getAmount() * advancedCurio.resistanceList.get(event.getSource().msgId).get());
					}
				}
			}

			if (event.getSource().msgId == DamageSource.FALL.msgId) {
				if (EnigmaticItems.enigmaticAmulet.hasColor(player, AmuletColor.MAGENTA)) {
					event.setAmount(event.getAmount() - 2.0f);
				}
			}

			/*
			 * Handler for damaging feedback of Blazing Core.
			 */

			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.blazingCore)) {
				if (event.getSource().getEntity() instanceof LivingEntity && EnigmaticItems.blazingCore.nemesisList.contains(event.getSource().msgId)) {
					LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
					if (!attacker.fireImmune()) {
						attacker.hurt(new EntityDamageSource(DamageSource.ON_FIRE.msgId, player), (float) BlazingCore.damageFeedback.getValue());
						attacker.setSecondsOnFire(BlazingCore.ignitionFeedback.getValue());
					}
				}

			}

			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.berserkCharm)) {
				event.setAmount(event.getAmount() * (1.0F - (SuperpositionHandler.getMissingHealthPool(player)*(float)BerserkEmblem.damageResistance.getValue())));
			}

			/*
			 * Handler for doubling damage on bearers of Ring of the Seven Curses.
			 */

			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.cursedRing)) {
				event.setAmount(event.getAmount()*CursedRing.painMultiplier.getValue().asModifier());
			}

			/*
			 * Handler for increasing damage on users of Bulwark of Blazing Pride.
			 */
			if (event.getSource().getEntity() != null) {
				if (player.getUseItem().getItem() instanceof InfernalShield && ((IProperShieldUser) player).isActuallyReallyBlocking()) {
					Vec3 sourcePos = event.getSource().getSourcePosition();
					if (sourcePos != null) {
						Vec3 lookVec = player.getViewVector(1.0F);
						Vec3 sourceToSelf = sourcePos.vectorTo(player.position()).normalize();
						sourceToSelf = new Vec3(sourceToSelf.x, 0.0D, sourceToSelf.z);
						if (!(sourceToSelf.dot(lookVec) < 0.0D)) {
							event.setAmount(event.getAmount() * 1.5F);
						}
					}
				}
			}
		} else if (event.getEntity() instanceof Monster || event.getEntity() instanceof EnderDragon) {
			Mob mob = (Mob) event.getEntity();

			if (event.getSource().getEntity() instanceof Player) {
				Player player = (Player) event.getSource().getEntity();

				if (mob instanceof EnderDragon && player instanceof ServerPlayer splayer) {
					Quote.POOR_CREATURE.playOnceIfUnlocked(splayer, 60);
				}

				/*
				 * Handler for damage bonuses of Charm of Monster Slayer.
				 */

				if (SuperpositionHandler.hasCurio(player, EnigmaticItems.monsterCharm)) {
					if (mob.isInvertedHealAndHarm()) {
						event.setAmount(event.getAmount() * MonsterCharm.undeadDamageBonus.getValue().asModifier(true));
					} else if (mob.isAggressive() || mob instanceof Creeper) {

						if (mob instanceof EnderMan || mob instanceof ZombifiedPiglin || mob instanceof Blaze || mob instanceof Guardian || mob instanceof ElderGuardian || !mob.canChangeDimensions()) {
							// NO-OP
						} else {
							event.setAmount(event.getAmount() * MonsterCharm.hostileDamageBonus.getValue().asModifier(true));
						}

					}
				}

				/*
				 * Handler for damage debuff of Ring of the Seven Curses.
				 */

				if (SuperpositionHandler.isTheCursedOne(player)) {
					boolean bypass = false;

					if (event.getSource().getDirectEntity() == player)
						if (player.getMainHandItem().getItem() == EnigmaticItems.theTwist || player.getMainHandItem().getItem() == EnigmaticItems.theInfinitum) {
							// Don't do worthiness check since event is gonna be canceled for non-worthy already
							bypass = true;
						}

					if (!bypass) {
						event.setAmount(event.getAmount() * CursedRing.monsterDamageDebuff.getValue().asModifierInverted());
					}
				}
			}
		}

		if (event.getSource().getEntity() instanceof Player player) {
			Entity immediateSource = event.getSource().getDirectEntity();

			float damageBoost = 0F;

			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.berserkCharm)) {
				damageBoost += event.getAmount()*(SuperpositionHandler.getMissingHealthPool(player)*(float)BerserkEmblem.attackDamage.getValue());
			}

			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.cursedScroll)) {
				damageBoost += event.getAmount()*(CursedScroll.damageBoost.getValue().asModifier()*SuperpositionHandler.getCurseAmount(player));
			}

			event.setAmount(event.getAmount()+damageBoost);

			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.eyeOfNebula)) {
				if (event.getSource().isMagic() || event.getSource().msgId == DamageSource.WITHER.msgId || event.getSource().msgId == DamageSource.DRAGON_BREATH.msgId) {
					event.setAmount(event.getAmount() * EyeOfNebula.magicBoost.getValue().asModifier(true));
				}
			}

			if (TransientPlayerData.get(player).hasEyeOfNebulaPower()) {
				event.setAmount(event.getAmount() * EyeOfNebula.attackEmpower.getValue().asModifier(true));
				TransientPlayerData.get(player).setEyeOfNebulaPower(false);
			}
		}

		if (event.getEntity() instanceof TamableAnimal) {
			TamableAnimal pet = (TamableAnimal) event.getEntity();

			if (pet.isTame()) {
				LivingEntity owner = pet.getOwner();

				if (owner instanceof Player && SuperpositionHandler.hasItem((Player)owner, EnigmaticItems.hunterGuidebook)) {
					if (owner.level == pet.level && owner.distanceTo(pet) <= HunterGuidebook.effectiveDistance.getValue()) {
						event.setCanceled(true);
						owner.hurt(event.getSource(), SuperpositionHandler.hasItem((Player)owner, EnigmaticItems.animalGuidebook) ? (event.getAmount()*HunterGuidebook.synergyDamageReduction.getValue().asModifierInverted()) : event.getAmount());
					}
				}
			}
		}

		if (event.getEntity() instanceof ServerPlayer player && event.getSource().getDirectEntity() instanceof LivingEntity living) {
			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.theCube)) {
				if (event.getAmount() <= EnigmaticItems.theCube.getDamageLimit(player) && theySeeMeRollin.nextDouble() <= 0.35) {
					event.setCanceled(true);
					living.hurt(event.getSource(), event.getAmount());
					player.level.playSound(null, player.blockPosition(), EnigmaticSounds.soundSwordHitReject, SoundSource.PLAYERS, 1F, 1F);
				} else {
					EnigmaticItems.theCube.applyRandomEffect(living, false);
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLivingHurtFinal(LivingHurtEvent event) {
		if (event.getEntity() instanceof ServerPlayer player && event.getSource().getEntity() != null) {
			int sorrowLevel = 0;

			for (ItemStack armor : player.getInventory().armor) {
				sorrowLevel += armor.getEnchantmentLevel(EnigmaticEnchantments.sorrowCurse);
			}

			for (int i = 0; i < sorrowLevel; i++) {
				EnigmaticEnchantments.sorrowCurse.maybeApplyDebuff(player, event.getAmount());
			}
		}
	}

	@SubscribeEvent
	public void playerClone(PlayerEvent.Clone event) {
		Player newPlayer = event.getEntity();
		Player oldPlayer = event.getOriginal();

		if (event.isWasDeath() && newPlayer instanceof ServerPlayer && oldPlayer instanceof ServerPlayer) {
			if (!EnigmaticItems.eldritchAmulet.reclaimInventory((ServerPlayer) oldPlayer, (ServerPlayer) newPlayer)) {
				for (int i = 0; i < 4; i++) {
					Tag tag = SuperpositionHandler.getPersistentTag(oldPlayer, "EternallyBoundArmor" + i, null);

					if (tag instanceof CompoundTag armor) {
						ItemStack stack = ItemStack.of(armor);
						newPlayer.getInventory().armor.set(i, stack);
					}

					SuperpositionHandler.removePersistentTag(newPlayer, "EternallyBoundArmor" + i);
				}
			}
		}

		/*
		 * Handler for destroying three random items in player's inventory on death,
		 * granted they bear Ring of the Seven Curses.
		 */

		/*
		if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.cursedRing) & !player.world.isRemote) {
			List<ItemStack> bigInventoryList = new ArrayList<>();

			for (int i = 0; i < 3; i++) {
				NonNullList<ItemStack> inventoryList = null;

				if (i == 0) {
					inventoryList = player.getInventory().mainInventory;
				}
				if (i == 1) {
					//inventoryList = player.getInventory().armorInventory;
				}
				if (i == 2) {
					//inventoryList = player.getInventory().offHandInventory;
				}

				if (inventoryList != null) {
					inventoryList.forEach(stack -> {
						if (!stack.isEmpty() && !EnigmaticLegacy.cursedRing.isItemDeathPersistent(stack) && stack != player.getHeldItemMainhand()) {
							bigInventoryList.add(stack);
						}
					});
				}
			}

		 *
			CuriosApi.getCuriosHelper().getEquippedCurios(player).ifPresent(handler -> {
				for (int index = 0; index < handler.getSlots(); index++) {
					if (handler.getStackInSlot(index) != null && !handler.getStackInSlot(index).isEmpty() && !EnigmaticLegacy.cursedRing.isItemDeathPersistent(handler.getStackInSlot(index))) {
						bigInventoryList.add(handler.getStackInSlot(index));
					}
				}
			});
		 *

			bigInventoryList.forEach(stack -> {
				System.out.println(stack);
			});

			final int itemsToDestroy = bigInventoryList.size() >= 3 ? 3 : bigInventoryList.size();
			List<Integer> rolledIndexes = new ArrayList<>();

			for (int i = 0; i < itemsToDestroy; i++) {
				int randomIndex = EnigmaticEventHandler.theySeeMeRollin.nextInt(itemsToDestroy);

				while (rolledIndexes.contains(randomIndex)) {
					EnigmaticEventHandler.theySeeMeRollin.nextInt(itemsToDestroy);
				}

				rolledIndexes.add(randomIndex);
				bigInventoryList.get(randomIndex).setCount(0);
			}
		}

		 */

		EnigmaticItems.soulCrystal.updatePlayerSoulMap(newPlayer);
	}
	@SubscribeEvent
	public void entityJoinWorld(EntityJoinLevelEvent event) {
		Entity entity = event.getEntity();

		if (entity instanceof ServerPlayer joinedPlayer) {
			EnigmaticItems.soulCrystal.updatePlayerSoulMap(joinedPlayer);
			ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers().forEach(serverPlayer -> {
				TransientPlayerData.get(joinedPlayer).syncToPlayer(serverPlayer);
				TransientPlayerData.get(serverPlayer).syncToPlayer(joinedPlayer);
			});
		}

		if (entity instanceof PathfinderMob mob && ((PathfinderMob)entity).getMobType() == MobType.ARTHROPOD) {
			mob.goalSelector.addGoal(3, new AvoidEntityGoal<>((PathfinderMob)entity, Player.class,
					(targetEntity) -> targetEntity instanceof Player &&
					SuperpositionHandler.hasAntiInsectAcknowledgement((Player)targetEntity),
					6, 1, 1.3, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test));
		}

		if (entity instanceof Piglin) {
			// NO-OP
		}

	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onExperienceDrop(LivingExperienceDropEvent event) {
		Player player = event.getAttackingPlayer();

		int bonusExp = 0;

		if (event.getEntity() instanceof ServerPlayer) {
			if (this.hadEnigmaticAmulet((Player) event.getEntity()) && !event.getEntity().level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
				event.setCanceled(true);
			}
		} else if (event.getEntity() instanceof Monster) {
			if (player != null && SuperpositionHandler.hasCurio(player, EnigmaticItems.monsterCharm)) {
				bonusExp += event.getOriginalExperience() * (EnigmaticItems.monsterCharm.bonusXPModifier-1.0F);
			}
		}

		if (player != null && SuperpositionHandler.hasCurio(player, EnigmaticItems.cursedRing)) {
			bonusExp += event.getOriginalExperience() * CursedRing.experienceBonus.getValue().asMultiplier();
		}

		event.setDroppedExperience(event.getDroppedExperience() + bonusExp);

		if (event.getEntity() instanceof EnderDragon) {

		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onLivingDrops(LivingDropsEvent event) {
		/*
		 * Beheading handler for Axe of Executioner.
		 */

		if (event.getEntity().getClass() == Skeleton.class && event.isRecentlyHit() && event.getSource().getEntity() != null && event.getSource().getEntity() instanceof Player) {
			ItemStack weap = ((Player) event.getSource().getEntity()).getMainHandItem();
			if (weap != null && weap.getItem() == EnigmaticItems.forbiddenAxe && !SuperpositionHandler.ifDroplistContainsItem(event.getDrops(), Items.SKELETON_SKULL) && this.theySeeMeRollin(event.getLootingLevel())) {
				this.addDrop(event, new ItemStack(Items.SKELETON_SKULL, 1));

				if (event.getSource().getEntity() instanceof ServerPlayer) {
					BeheadingTrigger.INSTANCE.trigger((ServerPlayer) event.getSource().getEntity());
				}
			}
		} else if (event.getEntity().getClass() == WitherSkeleton.class && event.isRecentlyHit() && event.getSource().getEntity() != null && event.getSource().getEntity() instanceof Player) {
			ItemStack weap = ((Player) event.getSource().getEntity()).getMainHandItem();
			if (weap != null && weap.getItem() == EnigmaticItems.forbiddenAxe) {

				if (!SuperpositionHandler.ifDroplistContainsItem(event.getDrops(), Items.WITHER_SKELETON_SKULL) && this.theySeeMeRollin(event.getLootingLevel())) {
					this.addDrop(event, new ItemStack(Items.WITHER_SKELETON_SKULL, 1));
				}

				if (event.getSource().getEntity() instanceof ServerPlayer && SuperpositionHandler.ifDroplistContainsItem(event.getDrops(), Items.WITHER_SKELETON_SKULL)) {
					BeheadingTrigger.INSTANCE.trigger((ServerPlayer) event.getSource().getEntity());
				}
			}
		} else if (event.getEntity().getClass() == Zombie.class && event.isRecentlyHit() && event.getSource().getEntity() != null && event.getSource().getEntity() instanceof Player) {
			ItemStack weap = ((Player) event.getSource().getEntity()).getMainHandItem();
			if (weap != null && weap.getItem() == EnigmaticItems.forbiddenAxe && !SuperpositionHandler.ifDroplistContainsItem(event.getDrops(), Items.ZOMBIE_HEAD) && this.theySeeMeRollin(event.getLootingLevel())) {
				this.addDrop(event, new ItemStack(Items.ZOMBIE_HEAD, 1));

				if (event.getSource().getEntity() instanceof ServerPlayer) {
					BeheadingTrigger.INSTANCE.trigger((ServerPlayer) event.getSource().getEntity());
				}
			}
		} else if (event.getEntity().getClass() == Creeper.class && event.isRecentlyHit() && event.getSource().getEntity() != null && event.getSource().getEntity() instanceof Player) {
			ItemStack weap = ((Player) event.getSource().getEntity()).getMainHandItem();
			if (weap != null && weap.getItem() == EnigmaticItems.forbiddenAxe && !SuperpositionHandler.ifDroplistContainsItem(event.getDrops(), Items.CREEPER_HEAD) && this.theySeeMeRollin(event.getLootingLevel())) {
				this.addDrop(event, new ItemStack(Items.CREEPER_HEAD, 1));

				if (event.getSource().getEntity() instanceof ServerPlayer) {
					BeheadingTrigger.INSTANCE.trigger((ServerPlayer) event.getSource().getEntity());
				}
			}
		} else if (event.getEntity().getClass() == EnderDragon.class && event.isRecentlyHit() && event.getSource().getEntity() != null && event.getSource().getEntity() instanceof Player) {
			ItemStack weap = ((Player) event.getSource().getEntity()).getMainHandItem();
			if (weap != null && weap.getItem() == EnigmaticItems.forbiddenAxe && !SuperpositionHandler.ifDroplistContainsItem(event.getDrops(), Items.DRAGON_HEAD) && this.theySeeMeRollin(event.getLootingLevel())) {
				this.addDrop(event, new ItemStack(Items.DRAGON_HEAD, 1));

				if (event.getSource().getEntity() instanceof ServerPlayer) {
					BeheadingTrigger.INSTANCE.trigger((ServerPlayer) event.getSource().getEntity());
				}
			}
		}

		if (event.isRecentlyHit() && event.getSource() != null && event.getSource().getEntity() instanceof Player && SuperpositionHandler.isTheCursedOne((Player) event.getSource().getEntity())) {
			Player player = (Player) event.getSource().getEntity();
			LivingEntity killed = event.getEntity();

			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.avariceScroll)) {
				this.addDropWithChance(event, new ItemStack(Items.EMERALD, 1), AvariceScroll.emeraldChance.getValue());
			}

			if (killed instanceof EnderDragon) {
				if (SuperpositionHandler.isTheWorthyOne(player)) {
					int heartsGained = SuperpositionHandler.getPersistentInteger(player, "AbyssalHeartsGained", 0);

					if (heartsGained < 4) { // Only as many as there are unique items from them, +1
						((IAbyssalHeartBearer) killed).dropAbyssalHeart(player);
					}
				}
			}

			if (!CursedRing.enableSpecialDrops.getValue())
				return;

			if (killed.getClass() == Shulker.class) {
				this.addDropWithChance(event, new ItemStack(EnigmaticItems.astralDust, 1), 20);
			} else if (killed.getClass() == Skeleton.class || killed.getClass() == Stray.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.ARROW, 3, 15));
			} else if (killed.getClass() == Zombie.class || killed.getClass() == Husk.class) {
				this.addDropWithChance(event, this.getRandomSizeStack(Items.SLIME_BALL, 1, 3), 25);
			} else if (killed.getClass() == Spider.class || killed.getClass() == CaveSpider.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.STRING, 2, 12));
			} else if (killed.getClass() == Guardian.class) {
				this.addDropWithChance(event, new ItemStack(Items.NAUTILUS_SHELL, 1), 15);
				this.addDrop(event, this.getRandomSizeStack(Items.PRISMARINE_CRYSTALS, 2, 5));
			} else if (killed instanceof ElderGuardian) {
				this.addDrop(event, this.getRandomSizeStack(Items.PRISMARINE_CRYSTALS, 4, 16));
				this.addDrop(event, this.getRandomSizeStack(Items.PRISMARINE_SHARD, 7, 28));
				this.addOneOf(event,
						new ItemStack(EnigmaticItems.guardianHeart, 1),
						new ItemStack(Items.HEART_OF_THE_SEA, 1),
						new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 1),
						new ItemStack(Items.ENDER_EYE, 1),
						EnchantmentHelper.enchantItem(killed.getRandom(), new ItemStack(Items.TRIDENT, 1), 25+theySeeMeRollin.nextInt(15), true));
			} else if (killed.getClass() == EnderMan.class) {
				this.addDropWithChance(event, this.getRandomSizeStack(Items.ENDER_EYE, 1, 2), 40);
			} else if (killed.getClass() == Blaze.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.BLAZE_POWDER, 0, 5));
				//this.addDropWithChance(event, new ItemStack(EnigmaticLegacy.livingFlame, 1), 15);
			} else if (killed.getClass() == ZombifiedPiglin.class) {
				this.addDropWithChance(event, this.getRandomSizeStack(Items.GOLD_INGOT, 1, 3), 40);
				this.addDropWithChance(event, this.getRandomSizeStack(Items.GLOWSTONE_DUST, 1, 7), 30);
			} else if (killed.getClass() == Witch.class) {
				this.addDropWithChance(event, new ItemStack(Items.GHAST_TEAR, 1), 30);
				this.addDropWithChance(event, this.getRandomSizeStack(Items.PHANTOM_MEMBRANE, 1, 3), 50);
			} else if (killed.getClass() == Pillager.class || killed.getClass() == Vindicator.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.EMERALD, 0, 4));
			} else if (killed.getClass() == Villager.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.EMERALD, 2, 6));
			} else if (killed.getClass() == Creeper.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.GUNPOWDER, 4, 12));
			} else if (killed.getClass() == PiglinBrute.class) {
				this.addDropWithChance(event, new ItemStack(Items.NETHERITE_SCRAP, 1), 20);
			} else if (killed.getClass() == Evoker.class) {
				this.addDrop(event, new ItemStack(Items.TOTEM_OF_UNDYING, 1));
				this.addDrop(event, this.getRandomSizeStack(Items.EMERALD, 5, 20));
				this.addDropWithChance(event, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 1), 10);
				this.addDropWithChance(event, this.getRandomSizeStack(Items.ENDER_PEARL, 1, 3), 30);
				this.addDropWithChance(event, this.getRandomSizeStack(Items.BLAZE_ROD, 2, 4), 30);
				this.addDropWithChance(event, this.getRandomSizeStack(Items.EXPERIENCE_BOTTLE, 4, 10), 50);
			} else if (killed.getClass() == WitherSkeleton.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.BLAZE_POWDER, 0, 3));
				this.addDropWithChance(event, new ItemStack(Items.GHAST_TEAR, 1), 20);
				this.addDropWithChance(event, new ItemStack(Items.NETHERITE_SCRAP, 1), 7);
			} else if (killed.getClass() == Ghast.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.PHANTOM_MEMBRANE, 1, 4));
			} else if (killed.getClass() == Drowned.class) {
				this.addDropWithChance(event, this.getRandomSizeStack(Items.LAPIS_LAZULI, 1, 3), 30);
			} else if (killed.getClass() == Vex.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.GLOWSTONE_DUST, 0, 2));
				this.addDropWithChance(event, new ItemStack(Items.PHANTOM_MEMBRANE, 1), 30);
			} else if (killed.getClass() == Phantom.class) {
				// NO-OP
			} else if (killed.getClass() == Piglin.class) {
				this.addDropWithChance(event, this.getRandomSizeStack(Items.GOLD_INGOT, 2, 4), 50);
			} else if (killed.getClass() == Ravager.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.EMERALD, 3, 10));
				this.addDrop(event, this.getRandomSizeStack(Items.LEATHER, 2, 7));
				this.addDropWithChance(event, this.getRandomSizeStack(Items.DIAMOND, 0, 4), 50);
			} else if (killed.getClass() == Silverfish.class) {
				// NO-OP
			} else if (killed.getClass() == MagmaCube.class) {
				this.addDropWithChance(event, new ItemStack(Items.BLAZE_POWDER, 1), 50);
			} else if (killed.getClass() == Chicken.class) {
				this.addDropWithChance(event, new ItemStack(Items.EGG, 1), 50);
			} else if (killed instanceof WitherBoss) {
				this.addDrop(event, this.getRandomSizeStack(EnigmaticItems.evilEssence, 1, 4));
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLivingDropsLowest(LivingDropsEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			CompoundTag deathLocation = new CompoundTag();
			deathLocation.putDouble("x", player.getX());
			deathLocation.putDouble("y", player.getY());
			deathLocation.putDouble("z", player.getZ());
			deathLocation.putString("dimension", player.level.dimension().location().toString());

			SuperpositionHandler.setPersistentTag(player, "LastDeathLocation", deathLocation);

			boolean droppedCrystal = false;
			boolean hadEscapeScroll = this.hadEscapeScroll(player);

			DimensionalPosition dimPoint = hadEscapeScroll ? SuperpositionHandler.getRespawnPoint(player) : new DimensionalPosition(player.getX(), player.getY(), player.getZ(), player.level);

			if (hadEscapeScroll) {
				BlockPos respawnPos = player.getRespawnPosition();
				ServerLevel respawnLevel = ServerLifecycleHooks.getCurrentServer().getLevel(player.getRespawnDimension());
				boolean isEndAnchor = false;

				if (respawnLevel.getBlockState(respawnPos).is(EnigmaticBlocks.endAnchor)) {
					dimPoint = new DimensionalPosition(respawnPos.getX() + 0.5, respawnPos.getY() + 1.5,
							respawnPos.getZ() + 0.5, respawnLevel);
					isEndAnchor = true;
				}

				player.level.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
				EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getX(), player.getY(), player.getZ(), 128, player.level.dimension())), new PacketPortalParticles(player.getX(), player.getY() + (player.getBbHeight() / 2), player.getZ(), 100, 1.25F, false));

				for (ItemEntity dropIt : event.getDrops()) {
					ItemEntity alternativeDrop = new ItemEntity(dimPoint.world, dimPoint.posX, dimPoint.posY, dimPoint.posZ, dropIt.getItem());
					alternativeDrop.teleportTo(dimPoint.posX, dimPoint.posY, dimPoint.posZ);

					if (!isEndAnchor) {
						alternativeDrop.setDeltaMovement(theySeeMeRollin.nextDouble()-0.5, theySeeMeRollin.nextDouble()-0.5, theySeeMeRollin.nextDouble()-0.5);
					} else {
						alternativeDrop.setDeltaMovement(0, 0, 0);
					}

					dimPoint.world.addFreshEntity(alternativeDrop);
					dropIt.setItem(ItemStack.EMPTY);
				}

				event.getDrops().clear();

				final DimensionalPosition dimPointFinal = dimPoint;

				dimPoint.world.playSound(null, new BlockPos(dimPoint.posX, dimPoint.posY, dimPoint.posZ), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
				EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(dimPointFinal.posX, dimPointFinal.posY, dimPointFinal.posZ, 128, dimPointFinal.world.dimension())), new PacketRecallParticles(dimPoint.posX, dimPoint.posY, dimPoint.posZ, 48, false));
			}

			if (this.hadEnigmaticAmulet(player) && !event.getDrops().isEmpty() && EnigmaticItems.enigmaticAmulet.isVesselEnabled()) {
				ItemStack soulCrystal = SuperpositionHandler.canDropSoulCrystal(player, this.hadCursedRing(player)) ? EnigmaticItems.soulCrystal.createCrystalFrom(player) : null;
				ItemStack storageCrystal = EnigmaticItems.storageCrystal.storeDropsOnCrystal(event.getDrops(), player, soulCrystal);
				PermanentItemEntity droppedStorageCrystal = new PermanentItemEntity(dimPoint.world, dimPoint.getPosX(), dimPoint.getPosY() + 1.5, dimPoint.getPosZ(), storageCrystal);
				droppedStorageCrystal.setOwnerId(player.getUUID());
				dimPoint.world.addFreshEntity(droppedStorageCrystal);
				EnigmaticLegacy.LOGGER.info("Summoned Extradimensional Storage Crystal for " + player.getGameProfile().getName() + " at X: " + dimPoint.getPosX() + ", Y: " + dimPoint.getPosY() + ", Z: " + dimPoint.getPosZ());
				event.getDrops().clear();

				if (soulCrystal != null) {
					droppedCrystal = true;
				}

				SoulArchive.getInstance().addItem(droppedStorageCrystal);
			} else if (SuperpositionHandler.canDropSoulCrystal(player, this.hadCursedRing(player))) {
				ItemStack soulCrystal = EnigmaticItems.soulCrystal.createCrystalFrom(player);
				PermanentItemEntity droppedSoulCrystal = new PermanentItemEntity(dimPoint.world, dimPoint.getPosX(), dimPoint.getPosY() + 1.5, dimPoint.getPosZ(), soulCrystal);
				droppedSoulCrystal.setOwnerId(player.getUUID());
				dimPoint.world.addFreshEntity(droppedSoulCrystal);
				EnigmaticLegacy.LOGGER.info("Teared Soul Crystal from " + player.getGameProfile().getName() + " at X: " + dimPoint.getPosX() + ", Y: " + dimPoint.getPosY() + ", Z: " + dimPoint.getPosZ());

				droppedCrystal = true;
				SoulArchive.getInstance().addItem(droppedSoulCrystal);
			}

			if (SuperpositionHandler.isPermanentlyDead(player)) {
				EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> player),
						new PacketPermadeath());
				SuperpositionHandler.setCurrentWorldFractured(true);
			}

			ResourceLocation soulLossAdvancement = new ResourceLocation(EnigmaticLegacy.MODID, "book/soul_loss");

			if (SuperpositionHandler.isAffectedBySoulLoss(player, this.hadCursedRing(player))) {
				SuperpositionHandler.grantAdvancement(player, soulLossAdvancement);
			} else if (SuperpositionHandler.hasAdvancement(player, soulLossAdvancement)) {
				SuperpositionHandler.revokeAdvancement(player, soulLossAdvancement);
			}

			postmortalPossession.removeAll(player);
			return;
		} else if (event.getEntity() instanceof Player) {
			postmortalPossession.removeAll(event.getEntity());
		}

		/*
		 * Unique drops for Ring of the Seven Curses.
		 */

		if (event.isRecentlyHit() && event.getSource() != null && event.getSource().getEntity() instanceof Player && SuperpositionHandler.isTheCursedOne((Player) event.getSource().getEntity())) {
			Player player = (Player) event.getSource().getEntity();
			LivingEntity killed = event.getEntity();

			if (killed instanceof EnderMan && killed.level.dimension().equals(PROXY.getEndKey())
					&& killed.getPersistentData().getBoolean("EnderSlayerVictim")) {
				int extraXP = 0;

				for (ItemEntity entity : event.getDrops()) {
					if (entity.getItem() != null) {
						if (entity.getItem().is(Items.ENDER_PEARL)) {
							this.dropXPOrb(killed.level, killed.getX(), killed.getY(), killed.getZ(), 10);
							//extraXP += 10;
						} else if (entity.getItem().is(Items.ENDER_EYE)) {
							this.dropXPOrb(killed.level, killed.getX(), killed.getY(), killed.getZ(), 20);
							//extraXP += 20;
						}
					}
				}

				//killed.getPersistentData().remove("EnderSlayerVictim");
				//killed.getPersistentData().putInt("EnderSlayerExtraXP", extraXP);
				event.getDrops().clear();
				event.setCanceled(true);
			}
		}
	}

	private void dropXPOrb(Level level, double x, double y, double z, int xp) {
		ExperienceOrb orb = new ExperienceOrb(level, x, y, z, xp);
		level.addFreshEntity(orb);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLootTablesLoaded(LootTableLoadEvent event) {
		if (!OmniconfigHandler.customDungeonLootEnabled.getValue())
			return;

		List<ResourceLocation> underwaterRuins = new ArrayList<ResourceLocation>();
		underwaterRuins.add(BuiltInLootTables.UNDERWATER_RUIN_BIG);
		underwaterRuins.add(BuiltInLootTables.UNDERWATER_RUIN_SMALL);

		LootPool overworldLiterature = !OmniconfigHandler.isItemEnabled(EnigmaticItems.overworldRevelationTome) ? null :
			SuperpositionHandler.constructLootPool("overworldLiterature", -7F, 2F,
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.overworldRevelationTome, 100).apply(LootFunctionRevelation.of(UniformGenerator.between(1, 3), UniformGenerator.between(50, 500))));

		LootPool netherLiterature = !OmniconfigHandler.isItemEnabled(EnigmaticItems.netherRevelationTome) ? null :
			SuperpositionHandler.constructLootPool("netherLiterature", -7F, 2F,
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.netherRevelationTome, 100).apply(LootFunctionRevelation.of(UniformGenerator.between(1, 3), UniformGenerator.between(100, 700))));

		LootPool endLiterature = !OmniconfigHandler.isItemEnabled(EnigmaticItems.endRevelationTome) ? null :
			SuperpositionHandler.constructLootPool("endLiterature", -7F, 2F,
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.endRevelationTome, 100).apply(LootFunctionRevelation.of(UniformGenerator.between(1, 4), UniformGenerator.between(200, 1000))));

		/*
		 * Handlers for adding spellstones to dungeon loot.
		 */

		if (SuperpositionHandler.getMergedAir$EarthenDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -12F, 1F,
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.golemHeart, 35),
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.angelBlessing, 65),
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.ichorBottle, 65));

			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);
		} else if (SuperpositionHandler.getMergedEnder$EarthenDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -10F, 1F,
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.eyeOfNebula, 35),
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.golemHeart, 65));

			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);

		} else if (SuperpositionHandler.getAirDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -10F, 1F,
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.angelBlessing, 100),
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.ichorBottle, 100));

			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);

		} else if (SuperpositionHandler.getEarthenDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -20F, 1F,
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.golemHeart, 100));

			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);

		} else if (SuperpositionHandler.getNetherDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -24F, 1F,
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.blazingCore, 100));

			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);

		} else if (SuperpositionHandler.getWaterDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -20F, 1F,
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.oceanStone, 100));

			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);

		} else if (SuperpositionHandler.getEnderDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -12F, 1F,
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.eyeOfNebula, 90),
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.voidPearl, 10));

			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);

		}

		/*
		 * Handlers for adding epic dungeon loot to most dungeons.
		 */

		if (SuperpositionHandler.getOverworldDungeons().contains(event.getName())) {
			LootPool epic = SuperpositionHandler.constructLootPool("epic", 1F, 2F,
					SuperpositionHandler.itemEntryBuilderED(Items.IRON_PICKAXE, 10, 20F, 30F, 1.0F, 0.8F),
					SuperpositionHandler.itemEntryBuilderED(Items.IRON_AXE, 10, 20F, 30F, 1.0F, 0.8F),
					SuperpositionHandler.itemEntryBuilderED(Items.IRON_SWORD, 10, 20F, 30F, 1.0F, 0.8F),
					SuperpositionHandler.itemEntryBuilderED(Items.IRON_SHOVEL, 10, 20F, 30F, 1.0F, 0.8F),
					SuperpositionHandler.itemEntryBuilderED(Items.BOW, 10, 20F, 30F, 1.0F, 0.8F),
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.ironRing, 20),
					LootItem.lootTableItem(EnigmaticItems.commonPotionBase).setWeight(20).apply(SetNbtFunction.setTag(PotionHelper.createAdvancedPotion(EnigmaticItems.commonPotionBase, EnigmaticPotions.haste).getTag())),
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.magnetRing, 8),
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.unholyGrail, 1),
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.loreInscriber, 5),
					// TODO Maybe reconsider
					// SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.voidStone, 4),
					LootItem.lootTableItem(Items.CLOCK).setWeight(10),
					LootItem.lootTableItem(Items.COMPASS).setWeight(10),
					LootItem.lootTableItem(Items.EMERALD).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4F))),
					LootItem.lootTableItem(Items.SLIME_BALL).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 10F))),
					LootItem.lootTableItem(Items.LEATHER).setWeight(35).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 8F))),
					LootItem.lootTableItem(Items.PUMPKIN_PIE).setWeight(25).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 16F))),
					SuperpositionHandler.getWaterDungeons().contains(event.getName()) || event.getName().equals(BuiltInLootTables.PILLAGER_OUTPOST) ? null : SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.earthHeart, 7)
					);

			LootTable modified = event.getTable();
			modified.addPool(epic);

			if (event.getName() != BuiltInLootTables.SHIPWRECK_SUPPLY) {
				if (overworldLiterature != null) {
					modified.addPool(overworldLiterature);
				}
			}

			event.setTable(modified);

		} else if (SuperpositionHandler.getNetherDungeons().contains(event.getName())) {
			ItemStack fireResistancePotion = new ItemStack(Items.POTION);
			fireResistancePotion = PotionUtils.setPotion(fireResistancePotion, Potions.LONG_FIRE_RESISTANCE);

			LootPool epic = SuperpositionHandler.constructLootPool("epic", 1F, 2F,
					SuperpositionHandler.itemEntryBuilderED(Items.GOLDEN_PICKAXE, 10, 25F, 30F, 1.0F, 1.0F),
					SuperpositionHandler.itemEntryBuilderED(Items.GOLDEN_AXE, 10, 25F, 30F, 1.0F, 1.0F),
					SuperpositionHandler.itemEntryBuilderED(Items.GOLDEN_SWORD, 10, 25F, 30F, 1.0F, 1.0F),
					SuperpositionHandler.itemEntryBuilderED(Items.GOLDEN_SHOVEL, 10, 25F, 30F, 1.0F, 1.0F),
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.voidStone, 8),
					LootItem.lootTableItem(Items.EMERALD).setWeight(30).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 7F))),
					LootItem.lootTableItem(Items.WITHER_ROSE).setWeight(25).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4F))),
					LootItem.lootTableItem(Items.GHAST_TEAR).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2F))),
					LootItem.lootTableItem(Items.LAVA_BUCKET).setWeight(30),
					LootItem.lootTableItem(Items.POTION).setWeight(15).apply(SetNbtFunction.setTag(fireResistancePotion.getTag())),
					SuperpositionHandler.getBastionChests().contains(event.getName()) ? LootItem.lootTableItem(EnigmaticItems.forbiddenFruit).setWeight(4) : null
					);

			LootTable modified = event.getTable();

			if (!event.getName().equals(BuiltInLootTables.BASTION_TREASURE)) {
				modified.addPool(epic);
			} else {
				LootPool scroll = SuperpositionHandler.constructLootPool("darkest_scroll", 0F, 1F,
						LootItem.lootTableItem(EnigmaticItems.darkestScroll).setWeight(100).apply(SetItemCountFunction.setCount(UniformGenerator.between(1F, 1F)))
						);

				modified.addPool(scroll);
			}

			if (netherLiterature != null) {
				modified.addPool(netherLiterature);
			}
			event.setTable(modified);

		} else if (event.getName().equals(BuiltInLootTables.END_CITY_TREASURE)) {
			LootPool epic = SuperpositionHandler.constructLootPool("epic", 1F, 2F,
					LootItem.lootTableItem(Items.ENDER_PEARL).setWeight(40).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5F))),
					LootItem.lootTableItem(Items.ENDER_EYE).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2F))),
					LootItem.lootTableItem(Items.GLISTERING_MELON_SLICE).setWeight(30).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4F))),
					LootItem.lootTableItem(Items.GOLDEN_CARROT).setWeight(30).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4F))),
					LootItem.lootTableItem(Items.PHANTOM_MEMBRANE).setWeight(25).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7F))),
					LootItem.lootTableItem(Items.ENCHANTING_TABLE).setWeight(10),
					LootItem.lootTableItem(Items.CAKE).setWeight(15),
					LootItem.lootTableItem(Items.END_CRYSTAL).setWeight(7),
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.loreInscriber, 10),
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.recallPotion, 15),
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.mendingMixture, 40),
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.astralDust, 85, 1F, 4F),
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.etheriumOre, 60, 1F, 2F),
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.extradimensionalEye, 20)
					);


			LootTable modified = event.getTable();
			modified.addPool(epic);
			if (endLiterature != null) {
				modified.addPool(endLiterature);
			}
			event.setTable(modified);
		} else if (event.getName().equals(BuiltInLootTables.DESERT_PYRAMID)) {
			LootPool epic = SuperpositionHandler.constructLootPool("specialpyramidloot", -7, 1F,
					SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.insignia, 100)
					);

			LootTable modified = event.getTable();
			modified.addPool(epic);
			if (endLiterature != null) {
				modified.addPool(endLiterature);
			}
			event.setTable(modified);
		}

		/*
		 * Handlers for adding special loot to some dungeons.
		 */

		if (SuperpositionHandler.getLibraries().contains(event.getName())) {
			LootPool special = SuperpositionHandler.constructLootPool("el_special", 2F, 3F,
					LootItem.lootTableItem(EnigmaticItems.thiccScroll).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(2F, 6F))),
					LootItem.lootTableItem(EnigmaticItems.loreFragment).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1F, 2F))));


			LootTable modified = event.getTable();
			modified.addPool(special);

			if (OmniconfigHandler.isItemEnabled(EnigmaticItems.overworldRevelationTome)) {
				LootPool literature = SuperpositionHandler.constructLootPool("literature", -4F, 3F,
						SuperpositionHandler.createOptionalLootEntry(EnigmaticItems.overworldRevelationTome, 100).apply(LootFunctionRevelation.of(UniformGenerator.between(1, 3), UniformGenerator.between(50, 500))));

				modified.addPool(literature);
			}

			event.setTable(modified);
		}


		if (event.getName().equals(BuiltInLootTables.UNDERWATER_RUIN_BIG) || event.getName().equals(BuiltInLootTables.UNDERWATER_RUIN_SMALL)) {
			LootPool special = SuperpositionHandler.constructLootPool("el_special", -5F, 1F,
					LootItem.lootTableItem(Items.TRIDENT).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.5F, 1.0F))).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(15F, 40F)).allowTreasure())
					);

			LootTable modified = event.getTable();
			modified.addPool(special);
			event.setTable(modified);
		}

	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer))
			return;

		ServerPlayer player = (ServerPlayer) event.getEntity();

		if (SuperpositionHandler.isPermanentlyDead(player)) {
			EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> player),
					new PacketPermadeath());
			SuperpositionHandler.setCurrentWorldFractured(true);
		} else {
			SuperpositionHandler.setCurrentWorldFractured(false);
		}

		if (!OmniconfigWrapper.syncAllToPlayer((ServerPlayer) event.getEntity())) {
			OmniconfigWrapper.onRemoteServer = false;
			EnigmaticLegacy.LOGGER.info("Logging in to local integrated server; no synchronization is required.");
		}

		try {
			this.syncPlayTime(player);

			if (!enigmaticLegacy.isCSGPresent()) {
				grantStarterGear(player);
			}

			if (EnigmaticItems.forbiddenFruit.haveConsumedFruit(player)) {
				ForbiddenFruitTrigger.INSTANCE.trigger(player);
			}

			/*
			if (SuperpositionHandler.isTheBlessedOne(player) && !SuperpositionHandler.hasAdvancement(player, new ResourceLocation(MODID, "recipes/generic/cosmic_scroll"))) {
				SuperpositionHandler.grantAdvancement(player, new ResourceLocation(MODID, "recipes/generic/cosmic_scroll"));
			}
			 */

			/*
			 * Handlers for fixing missing Curios slots upong joining the world.
			 */

			if (SuperpositionHandler.hasAdvancement(player, new ResourceLocation(EnigmaticLegacy.MODID, "main/discover_spellstone"))) {
				SuperpositionHandler.unlockSpecialSlot("spellstone", event.getEntity());
				SuperpositionHandler.setPersistentBoolean(player, EnigmaticEventHandler.NBT_KEY_ENABLESPELLSTONE, true);
			}

			if (SuperpositionHandler.hasAdvancement(player, new ResourceLocation(EnigmaticLegacy.MODID, "main/discover_scroll"))) {
				SuperpositionHandler.unlockSpecialSlot("scroll", event.getEntity());
				SuperpositionHandler.setPersistentBoolean(player, EnigmaticEventHandler.NBT_KEY_ENABLESCROLL, true);
			}

			ServerRecipeBook book = player.getRecipeBook();
			if (OmniconfigHandler.retriggerRecipeUnlocks.getValue()) {
				for (Recipe<?> theRecipe : player.level.getRecipeManager().getRecipes()) {
					if (book.contains(theRecipe)) {
						CriteriaTriggers.RECIPE_UNLOCKED.trigger(player, theRecipe);
					}
				}
			}
		} catch (Exception ex) {
			EnigmaticLegacy.LOGGER.error("Failed to check player's advancements upon joining the world!");
			ex.printStackTrace();
		}
	}

	@SubscribeEvent
	@SuppressWarnings("deprecation")
	public void onAdvancement(AdvancementEarnEvent event) {
		String id = event.getAdvancement().getId().toString();
		Player player = event.getEntity();

		if (player instanceof ServerPlayer && id.startsWith(EnigmaticLegacy.MODID+":book/")) {
			EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player) , new PacketSetEntryState(false, id.replace("book/", "")));
		}

		/*
		 * Handler for permanently unlocking Curio slots to player once they obtain
		 * respective advancement.
		 */

		if (id.equals(EnigmaticLegacy.MODID + ":main/discover_spellstone")) {
			//if (SuperpositionHandler.isSlotLocked("spellstone", player)) {
			EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new PacketSlotUnlocked("spellstone"));
			//}

			SuperpositionHandler.unlockSpecialSlot("spellstone", player);
			SuperpositionHandler.setPersistentBoolean(player, EnigmaticEventHandler.NBT_KEY_ENABLESPELLSTONE, true);
		} else if (id.equals(EnigmaticLegacy.MODID + ":main/discover_scroll")) {
			//if (SuperpositionHandler.isSlotLocked("scroll", player)) {
			EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new PacketSlotUnlocked("scroll"));
			//}

			SuperpositionHandler.unlockSpecialSlot("scroll", player);
			SuperpositionHandler.setPersistentBoolean(player, EnigmaticEventHandler.NBT_KEY_ENABLESCROLL, true);
		}
	}

	@SubscribeEvent
	public void onEndPortal(EndPortalActivatedEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			Quote.END_DOORSTEP.playOnceIfUnlocked(player, 40);
		}
	}

	@SubscribeEvent
	public void onEntitySummon(SummonedEntityEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			if (event.getSummonedEntity() instanceof WitherBoss) {
				Quote.COUNTLESS_DEAD.playOnceIfUnlocked(player, 20);
			} else if (event.getSummonedEntity() instanceof EnderDragon) {
				Quote.HORRIBLE_EXISTENCE.playOnceIfUnlocked(player, 100);
			}
		}
	}

	@SubscribeEvent
	public void onEnteredBlock(EnterBlockEvent event) {
		if (event.getEntity() instanceof ServerPlayer player && event.getBlockState().getBlock() == Blocks.END_GATEWAY) {
			Quote.I_WANDERED.playOnceIfUnlocked(player, 160);
		}
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getEntity();

		/*
		 * Handler for re-enabling disabled Curio slots that are supposed to be
		 * permanently unlocked, when the player respawns.
		 */

		if (!player.level.isClientSide) {
			lastSoulCompassUpdate.remove(player);

			if (!event.isEndConquered()) {
				if (SuperpositionHandler.getPersistentBoolean(player, "DestroyedCursedRing", false)) {
					SuperpositionHandler.removePersistentTag(player, "DestroyedCursedRing");
					Quote.getRandom(Quote.RING_DESTRUCTION).playIfUnlocked((ServerPlayer) player, 10);
				} else if (theySeeMeRollin.nextDouble() > 0.5)
					if (SuperpositionHandler.getPersistentBoolean(player, "DeathFromEntity", false)) {
						Quote.getRandom(Quote.DEATH_QUOTES_ENTITY).playIfUnlocked((ServerPlayer) player, 10);
					} else {
						Quote.getRandom(Quote.DEATH_QUOTES).playIfUnlocked((ServerPlayer) player, 10);
					}

				if (!player.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
					if (SuperpositionHandler.hasPersistentTag(player, EnigmaticEventHandler.NBT_KEY_ENABLESCROLL)) {
						SuperpositionHandler.unlockSpecialSlot("scroll", event.getEntity());
					}
					if (SuperpositionHandler.hasPersistentTag(player, EnigmaticEventHandler.NBT_KEY_ENABLESPELLSTONE)) {
						SuperpositionHandler.unlockSpecialSlot("spellstone", event.getEntity());
					}
				}
			}

			SuperpositionHandler.setCurrentWorldCursed(SuperpositionHandler.isTheCursedOne(player));
		}

	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onAnvilOpen(ScreenEvent.Init event) {
		if (PermadeathScreen.active != null && event.getScreen() != PermadeathScreen.active) {
			Minecraft.getInstance().setScreen(PermadeathScreen.active);
		}

		if (event.getScreen() instanceof AnvilScreen screen) {
			EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketAnvilField(""));

			try {
				for (Field f : screen.getClass().getDeclaredFields()) {

					f.setAccessible(true);

					if (f.get(screen) instanceof EditBox) {
						EditBox widget = (EditBox) f.get(screen);
						widget.setMaxLength(64);
					}

				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onLogin(PlayerLoggedInEvent event) {
		//enigmaticLegacy.performCleanup();
	}

	@SubscribeEvent
	public void onAnvilUpdate(AnvilUpdateEvent event) {
		if (event.getLeft().getCount() == 1)
			if (event.getRight().getItem().equals(EnigmaticItems.loreFragment) && event.getRight().getTagElement("display") != null) {
				event.setCost(4);
				event.setMaterialCost(1);
				event.setOutput(ItemLoreHelper.mergeDisplayData(event.getRight(), event.getLeft().copy()));
			}
	}

	@SubscribeEvent
	public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
		if (event.getEntity() != null && !event.getEntity().level.isClientSide) {
			if ((event.getInventory().countItem(EnigmaticItems.enchantmentTransposer) == 1 || event.getInventory().countItem(EnigmaticItems.curseTransposer) == 1) && event.getCrafting().getItem() == Items.ENCHANTED_BOOK) {
				event.getEntity().level.playSound(null, event.getEntity().blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, (float) (0.9F + (Math.random() * 0.1F)));
			} else if (event.getCrafting().getItem() == EnigmaticItems.cursedStone) {
				event.getEntity().level.playSound(null, event.getEntity().blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, (float) (0.9F + (Math.random() * 0.1F)));
			}
		}
	}


	@SubscribeEvent
	public void onAttackTargetSet(LivingChangeTargetEvent event) {
		if (event.getNewTarget() instanceof Player) {
			Player player = (Player) event.getNewTarget();

			if (event.getEntity() instanceof Mob) {
				Mob insect = (Mob) event.getEntity();

				if (insect.getMobType() == MobType.ARTHROPOD)
					if (SuperpositionHandler.hasAntiInsectAcknowledgement(player)) {
						event.setCanceled(true);
					}

				if (insect instanceof Guardian && insect.getClass() != ElderGuardian.class) {
					if (SuperpositionHandler.hasItem(player, EnigmaticItems.guardianHeart) && SuperpositionHandler.isTheCursedOne(player)) {
						boolean isBlacklisted = angeredGuardians.containsEntry(player, insect);

						if (insect.getLastHurtByMob() != player && !isBlacklisted) {
							event.setCanceled(true);
						} else {
							if (!isBlacklisted) {
								angeredGuardians.put(player, (Guardian)insect);
							}
						}
					}
				}
			}
		}
	}


	/**
	 * Adds passed ItemStack to LivingDropsEvent.
	 *
	 * @author Integral
	 */

	public void addDrop(LivingDropsEvent event, ItemStack drop) {
		ItemEntity entityitem = new ItemEntity(event.getEntity().level, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), drop);
		entityitem.setPickUpDelay(10);
		event.getDrops().add(entityitem);
	}

	public void addDropWithChance(LivingDropsEvent event, ItemStack drop, int chance) {
		if (theySeeMeRollin.nextInt(100) < chance) {
			this.addDrop(event, drop);
		}
	}

	public ItemStack getRandomSizeStack(Item item, int minAmount, int maxAmount) {
		return new ItemStack(item, minAmount + theySeeMeRollin.nextInt(maxAmount-minAmount+1));
	}

	public void addOneOf(LivingDropsEvent event, ItemStack... itemStacks) {
		int chosenStack = theySeeMeRollin.nextInt(itemStacks.length);
		this.addDrop(event, itemStacks[chosenStack]);
	}

	public static void grantStarterGear(ServerPlayer player) {
		EnigmaticLegacy.LOGGER.info("Granting starter gear to " + player.getGameProfile().getName());

		/*
		 * Eh, annoying defaults.
		 */

		if (!SuperpositionHandler.hasPersistentTag(player, NBT_KEY_PATCHOULIFORCE)) {
			EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> player), new PacketPatchouliForce());
			SuperpositionHandler.setPersistentBoolean(player, EnigmaticEventHandler.NBT_KEY_PATCHOULIFORCE, true);
		}

		/*
		 * Handler for bestowing Enigmatic Amulet to the player, when they first join
		 * the world.
		 */

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.enigmaticAmulet))
			if (!SuperpositionHandler.hasPersistentTag(player, EnigmaticEventHandler.NBT_KEY_ENIGMATICGIFT)) {

				ItemStack amuletStack = new ItemStack(EnigmaticItems.enigmaticAmulet);
				EnigmaticItems.enigmaticAmulet.setInscription(amuletStack, player.getGameProfile().getName());
				EnigmaticItems.enigmaticAmulet.setProperlyGranted(amuletStack);

				if (!EnigmaticAmulet.seededColorGen.getValue()) {
					EnigmaticItems.enigmaticAmulet.setPseudoRandomColor(amuletStack);
				} else {
					EnigmaticItems.enigmaticAmulet.setSeededColor(amuletStack);
				}

				if (player.getInventory().getItem(8).isEmpty()) {
					player.getInventory().setItem(8, amuletStack);
				} else {
					if (!player.getInventory().add(amuletStack)) {
						ItemEntity dropAmulet = new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), amuletStack);
						player.level.addFreshEntity(dropAmulet);
					}
				}

				SuperpositionHandler.setPersistentBoolean(player, EnigmaticEventHandler.NBT_KEY_ENIGMATICGIFT, true);
			}

		/*
		 * Another one for Ring of the Seven Curses.
		 */

		if (OmniconfigHandler.isItemEnabled(EnigmaticItems.cursedRing))
			if (!SuperpositionHandler.hasPersistentTag(player, EnigmaticEventHandler.NBT_KEY_CURSEDGIFT)) {
				ItemStack cursedRingStack = new ItemStack(EnigmaticItems.cursedRing);

				if (!CursedRing.ultraHardcore.getValue()) {
					if (player.getInventory().getItem(7).isEmpty()) {
						player.getInventory().setItem(7, cursedRingStack);
					} else {
						if (!player.getInventory().add(cursedRingStack)) {
							ItemEntity dropRing = new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), cursedRingStack);
							player.level.addFreshEntity(dropRing);
						}
					}
				} else {
					SuperpositionHandler.tryForceEquip(player, cursedRingStack);
				}

				SuperpositionHandler.setPersistentBoolean(player, EnigmaticEventHandler.NBT_KEY_CURSEDGIFT, true);
			}
	}

	/**
	 * Calculates the chance for Axe of Executioner to behead an enemy.
	 *
	 * @param lootingLevel Amount of looting levels applied to axe or effective otherwise.
	 * @return True if chance works and head should drop, false otherwise.
	 * @author Integral
	 */

	private boolean theySeeMeRollin(int lootingLevel) {
		int chance = Math.min(ForbiddenAxe.beheadingBase.getValue().asPercentage() + (ForbiddenAxe.beheadingBonus.getValue().asPercentage() * lootingLevel), 100);
		return new Perhaps(chance).roll();
	}

	private boolean hadEnigmaticAmulet(Player player) {
		return postmortalPossession.containsEntry(player, EnigmaticItems.enigmaticAmulet) || postmortalPossession.containsEntry(player, EnigmaticItems.ascensionAmulet);
	}

	private boolean hadEscapeScroll(Player player) {
		return postmortalPossession.containsKey(player) ? postmortalPossession.containsEntry(player, EnigmaticItems.escapeScroll) : false;
	}

	private boolean hadUnholyStone(Player player) {
		return postmortalPossession.containsKey(player) ? postmortalPossession.containsEntry(player, EnigmaticItems.cursedStone) : false;
	}

	private boolean hadCursedRing(Player player) {
		return postmortalPossession.containsKey(player) ? postmortalPossession.containsEntry(player, EnigmaticItems.cursedRing) : false;
	}


}