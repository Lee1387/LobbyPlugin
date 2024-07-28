package com.lee1387.lobbyplugin.items.gadgets;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TNTLauncherItem implements Listener {

    private static final long COOLDOWN_TIME = 3000;
    private static final double KNOCKBACK_STRENGTH = 2.0;
    private static final double EXPLOSION_RADIUS = 5.0;
    private static final Map<String, Long> lastUseTime = new HashMap<>();
    private static final Plugin plugin = Bukkit.getPluginManager().getPlugin("LobbyPlugin");

    public TNTLauncherItem(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public static ItemStack createTNTLauncherItem() {
        ItemStack tntLauncherItem = new ItemStack(Material.TNT);
        ItemMeta meta = tntLauncherItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "TNT Launcher");
            tntLauncherItem.setItemMeta(meta);
        }
        return tntLauncherItem;
    }

    public static void onRightClick(Player player) {
        String playerName = player.getName();
        long currentTime = System.currentTimeMillis();

        if (lastUseTime.containsKey(playerName)) {
            long lastUse = lastUseTime.get(playerName);
            long timeLeft = COOLDOWN_TIME - (currentTime - lastUse);

            if (timeLeft > 0) {
                player.sendMessage(ChatColor.RED + "You can use the TNT Launcher again in " + (timeLeft / 1000) + " seconds");
                return;
            }
        }

        lastUseTime.put(playerName, currentTime);

        Vector direction = player.getLocation().getDirection().normalize().multiply(2);
        Location spawnLocation = player.getLocation().add(direction);

        Fireball fireball = (Fireball) player.getWorld().spawnEntity(spawnLocation, EntityType.FIREBALL);
        fireball.setDirection(direction);
        fireball.setYield(0);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Fireball) {
            Fireball fireball = (Fireball) event.getEntity();
            Location explosionLocation = fireball.getLocation();

            Collection<Entity> nearbyEntities = explosionLocation.getWorld().getNearbyEntities(explosionLocation, EXPLOSION_RADIUS, EXPLOSION_RADIUS, EXPLOSION_RADIUS);
            for (Entity entity : nearbyEntities) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    Vector knockback = player.getLocation().toVector().subtract(explosionLocation.toVector()).normalize().multiply(KNOCKBACK_STRENGTH);
                    player.setVelocity(knockback);
                }
            }

            explosionLocation.getWorld().createExplosion(explosionLocation, 8.0F, false, false);
            explosionLocation.getWorld().playSound(explosionLocation, Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
        }
    }
}
