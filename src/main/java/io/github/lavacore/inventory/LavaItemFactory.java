package io.github.lavacore.inventory;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class LavaItemFactory {
    public static ItemStack createItem(Material material) {
        return new ItemStack(material);
    }
}
