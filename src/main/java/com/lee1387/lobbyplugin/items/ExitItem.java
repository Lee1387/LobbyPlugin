package com.lee1387.lobbyplugin.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ExitItem {

    public static ItemStack createExitItem() {
        ItemStack dyeItem = new ItemStack(Material.RED_DYE);
        ItemMeta meta = dyeItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_RED + "Exit");
            dyeItem.setItemMeta(meta);
        }
        return dyeItem;
    }

    public static void onRightClick(PlayerInteractEvent event) {
        event.getPlayer().getInventory().clear();
        event.getPlayer().getInventory().setItem(2, GadgetsItem.createGadgetsItem());
    }
}
