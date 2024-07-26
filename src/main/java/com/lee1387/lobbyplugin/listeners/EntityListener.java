package com.lee1387.lobbyplugin.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EntityListener implements Listener {

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        Material type = event.getBlock().getType();
        if (type == Material.SAND || type == Material.GRAVEL || type.name().contains("CONCRETE_POWDER") ||
            type == Material.WATER || type == Material.LAVA) {
            event.setCancelled(true);
        }
    }
}
