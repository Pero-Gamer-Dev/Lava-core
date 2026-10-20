/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.unsafe;
import com.google.common.collect.ImmutableMultimap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import io.github.lavacore.loader.LavaClassRemapper;
import java.util.List;
import java.util.function.BooleanSupplier;
@SuppressWarnings({"deprecation","removal","override"})
public final class LavaUnsafeValues implements UnsafeValues {
    public ComponentFlattener componentFlattener(){return ComponentFlattener.basic();}
    public PlainComponentSerializer plainComponentSerializer(){return PlainComponentSerializer.plain();}
    public PlainTextComponentSerializer plainTextSerializer(){return PlainTextComponentSerializer.plainText();}
    public GsonComponentSerializer gsonComponentSerializer(){return GsonComponentSerializer.gson();}
    public GsonComponentSerializer colorDownsamplingGsonComponentSerializer(){return GsonComponentSerializer.colorDownsamplingGson();}
    public LegacyComponentSerializer legacyComponentSerializer(){return LegacyComponentSerializer.legacySection();}
    public Component resolveWithContext(Component c, CommandSender s, Entity e, boolean b){return c;}
    public Material toLegacy(Material m){return m;}
    public Material fromLegacy(Material m){return m;}
    public Material fromLegacy(MaterialData d){return d.getItemType();}
    public Material fromLegacy(MaterialData d, boolean item){return d.getItemType();}
    public BlockData fromLegacy(Material m, byte data){return m.createBlockData();}
    public Material getMaterial(String name, int version){return Material.matchMaterial(name);}
    public int getDataVersion(){return 4189;}
    public ItemStack modifyItemStack(ItemStack stack, String arguments){return stack;}
    public void checkSupported(PluginDescriptionFile pdf) throws InvalidPluginException {}
    public byte[] processClass(PluginDescriptionFile pdf, String path, byte[] clazz){return LavaClassRemapper.remap(clazz);}
    public Advancement loadAdvancement(NamespacedKey key, String advancement){return null;}
    public boolean removeAdvancement(NamespacedKey key){return false;}
    public com.google.common.collect.Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(Material m, EquipmentSlot slot){return ImmutableMultimap.of();}
    public CreativeCategory getCreativeCategory(Material m){return CreativeCategory.MISC;}
    public String getBlockTranslationKey(Material m){return m.getTranslationKey();}
    public String getItemTranslationKey(Material m){return m.getTranslationKey();}
    public String getTranslationKey(EntityType type){return type.getTranslationKey();}
    public String getTranslationKey(ItemStack item){return item.getType().getTranslationKey();}
    public String getTranslationKey(Attribute attribute){return attribute.getTranslationKey();}
    public org.bukkit.potion.PotionType.InternalPotionData getInternalPotionData(NamespacedKey key){return null;}
    public org.bukkit.damage.DamageSource.Builder createDamageSourceBuilder(org.bukkit.damage.DamageType type){return org.bukkit.damage.DamageSource.builder(type);}
    public String get(Class<?> aClass, String value){return value;}
    public <B extends Keyed> B get(io.papermc.paper.registry.RegistryKey<B> registryKey, NamespacedKey namespacedKey){return null;}
    public Biome getCustomBiome(){return Biome.PLAINS;}
    public boolean isSupportedApiVersion(String version){return true;}
    public byte[] serializeItem(ItemStack item){return new byte[0];}
    public ItemStack deserializeItem(byte[] data){return new ItemStack(Material.AIR);}
    public com.google.gson.JsonObject serializeItemAsJson(ItemStack item){return new com.google.gson.JsonObject();}
    public ItemStack deserializeItemFromJson(com.google.gson.JsonObject json){return new ItemStack(Material.AIR);}
    public byte[] serializeEntity(Entity entity){return new byte[0];}
    public byte[] serializeEntity(Entity entity, io.papermc.paper.entity.EntitySerializationFlag... flags){return new byte[0];}
    public Entity deserializeEntity(byte[] data, World world){return null;}
    public Entity deserializeEntity(byte[] data, World world, boolean preserveUUID){return null;}
    public Entity deserializeEntity(byte[] data, World world, boolean preserveUUID, boolean preservePassengers){return null;}
    public ItemStack createEmptyStack(){return new ItemStack(Material.AIR);}
    public List<Component> computeTooltipLines(ItemStack item, io.papermc.paper.inventory.tooltip.TooltipContext ctx, Player player){return List.of();}
    public io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager<org.bukkit.plugin.Plugin> createPluginLifecycleEventManager(JavaPlugin plugin, BooleanSupplier supplier){ return config -> {}; }
    public NamespacedKey getBiomeKey(RegionAccessor a, int x, int y, int z){return NamespacedKey.minecraft("plains");}
    public void setBiomeKey(RegionAccessor a, int x, int y, int z, NamespacedKey key){}
    public org.bukkit.attribute.Attributable getDefaultEntityAttributes(NamespacedKey key){return null;}
    public boolean hasDefaultEntityAttributes(NamespacedKey key){return false;}
    public String getMainLevelName(){return "world";}
    public int getProtocolVersion(){return 769;}
    public Color getSpawnEggLayerColor(EntityType type, int layer){return Color.WHITE;}
    public String getStatisticCriteriaKey(Statistic statistic){return statistic.getKey().toString();}
    public boolean isValidRepairItemStack(ItemStack a, ItemStack b){return false;}
    public int nextEntityId(){return 0;}
}
