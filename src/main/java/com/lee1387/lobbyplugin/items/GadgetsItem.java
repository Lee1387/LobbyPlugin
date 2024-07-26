package com.lee1387.lobbyplugin.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GadgetsItem {

    public static ItemStack createGadgetsItem() {
        ItemStack gadgetsItem = new ItemStack(Material.BLAZE_POWDER);

        ItemMeta meta = gadgetsItem.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Gadgets");

            gadgetsItem.setItemMeta(meta);
        }

        return gadgetsItem;
    }
}
