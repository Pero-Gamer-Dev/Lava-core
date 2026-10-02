package io.github.lavacore.inventory;

import org.bukkit.inventory.ItemStack;

public class LavaItemFactory {
    public static ItemStack createItem(int typeId, int amount) {
        return new ItemStack(typeId, amount);
    }
}
