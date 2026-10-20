/*
 * Copyright (C) 2026 Pero
 * This file is part of Lava-core, licensed under the GNU LGPLv3.
 * See the LICENSE file in the project root for more information.
 */
package io.github.lavacore.inventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Random;
import java.util.function.UnaryOperator;
@SuppressWarnings("deprecation")
public final class LavaItemFactory implements ItemFactory {
    public ItemMeta getItemMeta(Material material){ return new ItemStack(material).getItemMeta(); }
    public boolean isApplicable(ItemMeta meta, ItemStack stack){ return true; }
    public boolean isApplicable(ItemMeta meta, Material material){ return true; }
    public boolean equals(ItemMeta a, ItemMeta b){ return a==b || (a!=null && a.equals(b)); }
    public ItemMeta asMetaFor(ItemMeta meta, ItemStack stack){ return meta; }
    public ItemMeta asMetaFor(ItemMeta meta, Material material){ return meta; }
    public Color getDefaultLeatherColor(){ return Color.fromRGB(0xA06540); }
    public ItemStack createItemStack(String input){ Material m=Material.matchMaterial(input); return new ItemStack(m==null?Material.AIR:m); }
    public Material getSpawnEgg(EntityType type){ return Material.AIR; }
    public ItemStack enchantItem(Entity entity, ItemStack item, int level, boolean t){ return item; }
    public ItemStack enchantItem(World world, ItemStack item, int level, boolean t){ return item; }
    public ItemStack enchantItem(ItemStack item, int level, boolean t){ return item; }
    public HoverEvent<HoverEvent.ShowItem> asHoverEvent(ItemStack item, UnaryOperator<HoverEvent.ShowItem> op){ return item.asHoverEvent(op); }
    public Component displayName(ItemStack item){ return Component.text(item.getType().name()); }
    public String getI18NDisplayName(ItemStack item){ return item.getType().name(); }
    public ItemStack ensureServerConversions(ItemStack item){ return item; }
    public net.md_5.bungee.api.chat.hover.content.Content hoverContentOf(ItemStack item){ return null; }
    public net.md_5.bungee.api.chat.hover.content.Content hoverContentOf(Entity entity){ return null; }
    public net.md_5.bungee.api.chat.hover.content.Content hoverContentOf(Entity entity, String s){ return null; }
    public net.md_5.bungee.api.chat.hover.content.Content hoverContentOf(Entity entity, net.md_5.bungee.api.chat.BaseComponent c){ return null; }
    public net.md_5.bungee.api.chat.hover.content.Content hoverContentOf(Entity entity, net.md_5.bungee.api.chat.BaseComponent[] c){ return null; }
    public ItemStack enchantWithLevels(ItemStack item, int levels, boolean treasure, Random random){ return item; }
    public ItemStack enchantWithLevels(ItemStack item, int levels, io.papermc.paper.registry.set.RegistryKeySet<org.bukkit.enchantments.Enchantment> keySet, Random random){ return item; }
}
