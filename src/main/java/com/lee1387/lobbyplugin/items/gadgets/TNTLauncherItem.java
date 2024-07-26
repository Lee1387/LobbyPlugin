package com.lee1387.lobbyplugin.items.gadgets;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class TNTLauncherItem {

    private static final long COOLDOWN_TIME = 3000;
    private static final long DELAY_AFTER_MESSAGE = 20;
    private static final Map<String, Long> lastUseTime = new HashMap<>();
    private static final Plugin plugin = Bukkit.getPluginManager().getPlugin("LobbyPlugin");


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

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        lastUseTime.remove(playerName);
                    }
                }.runTaskLater(plugin, DELAY_AFTER_MESSAGE);

                return;
            }
        }

        lastUseTime.put(playerName, currentTime);

        Vector direction = player.getLocation().getDirection().normalize().multiply(2);
        Location spawnLocation = player.getLocation().add(direction);

        Fireball fireball = (Fireball) player.getWorld().spawnEntity(spawnLocation, EntityType.FIREBALL);
        fireball.setDirection(direction);
        fireball.setYield(0);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!fireball.isDead() && fireball.isValid()) {
                    fireball.getWorld().createExplosion(fireball.getLocation(), 8.0F, false, false);
                    fireball.getWorld().playSound(fireball.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                    fireball.remove();
                }
            }
        }.runTaskLater(plugin, 40);
    }
}
