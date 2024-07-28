package com.lee1387.lobbyplugin.listeners;

import com.lee1387.lobbyplugin.LobbyPlugin;
import com.lee1387.lobbyplugin.items.ExitItem;
import com.lee1387.lobbyplugin.items.GadgetsItem;
import com.lee1387.lobbyplugin.items.gadgets.ColorfulArmorItem;
import com.lee1387.lobbyplugin.items.gadgets.SpidermanItem;
import com.lee1387.lobbyplugin.items.gadgets.TNTLauncherItem;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    private final LobbyPlugin plugin;

    public PlayerListener(LobbyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        event.setDroppedExp(0);
        event.getDrops().clear();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        Location lobbyLocation = plugin.getLobbyLocation();
        if (lobbyLocation != null) {
            player.teleport(lobbyLocation);
            player.sendMessage(ChatColor.AQUA + "Welcome to the server! Teleported to the lobby.");
        }

        player.setGameMode(GameMode.ADVENTURE);

        player.getInventory().clear();

        player.getInventory().setItem(2, GadgetsItem.createGadgetsItem());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location lobbyLocation = plugin.getLobbyLocation();
        if (lobbyLocation != null) {
            double y = player.getLocation().getY();
            if (y < -23) {
                player.teleport(lobbyLocation);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().name().contains("RIGHT_CLICK") && event.hasItem() && event.getItem() != null) {
            ItemStack item = event.getItem();
            if (item.isSimilar(GadgetsItem.createGadgetsItem())) {
                player.getInventory().clear();

                player.getInventory().setItem(0, TNTLauncherItem.createTNTLauncherItem());
                player.getInventory().setItem(1, ColorfulArmorItem.createColorfulArmorItem());
                player.getInventory().setItem(2, SpidermanItem.createSpidermanItem());
                player.getInventory().setItem(8, ExitItem.createExitItem());

            } else if (item.isSimilar(ColorfulArmorItem.createColorfulArmorItem())) {
                event.setCancelled(true);
                ColorfulArmorItem.onRightClick(player);
            } else if (item.isSimilar(TNTLauncherItem.createTNTLauncherItem())) {
                TNTLauncherItem.onRightClick(player);
            } else if (item.isSimilar(SpidermanItem.createSpidermanItem())) {
                SpidermanItem.onRightClick(player);
            } else if (item.isSimilar(ExitItem.createExitItem())) {
                ExitItem.onRightClick(event);
            }
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        event.setCancelled(true);
    }
}
