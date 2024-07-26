package com.lee1387.lobbyplugin.items.gadgets;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColorfulArmorItem {

    private static final List<Color> COLORS = Arrays.asList(
            Color.RED,
            Color.GREEN,
            Color.BLUE,
            Color.YELLOW,
            Color.AQUA,
            Color.FUCHSIA,
            Color.GRAY,
            Color.LIME,
            Color.OLIVE,
            Color.ORANGE,
            Color.PURPLE,
            Color.SILVER,
            Color.WHITE,
            Color.YELLOW
    );

    private static final Plugin PLUGIN = Bukkit.getPluginManager().getPlugin("LobbyPlugin");
    private static final long COLOR_CHANGE_INTERVAL = 20L;
    private static final long TOGGLE_COOLDOWN = 60L;
    private static final Map<String, BukkitRunnable> colorTasks = new HashMap<>();
    private static final Map<String, Boolean> colorModeEnabled = new HashMap<>();
    private static final Map<String, Long> lastToggleTimes = new HashMap<>();

    public static ItemStack createColorfulArmorItem() {
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemMeta meta = chestplate.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Colorful Armor");
            chestplate.setItemMeta(meta);
        }
        return chestplate;
    }

    public static void onRightClick(Player player) {
        String playerName = player.getName();
        long currentTime = System.currentTimeMillis();
        long lastToggleTime = lastToggleTimes.getOrDefault(playerName, 0L);

        if (currentTime - lastToggleTime < TOGGLE_COOLDOWN * 50) {
            player.sendMessage(ChatColor.RED + "You must wait before toggling again!");
            return;
        }

        lastToggleTimes.put(playerName, currentTime);

        if (colorModeEnabled.getOrDefault(playerName, false)) {
            disableColorCycling(player);
        } else {
            cancelExistingTask(player);

            player.getInventory().setHelmet(null);
            player.getInventory().setChestplate(null);
            player.getInventory().setLeggings(null);
            player.getInventory().setBoots(null);

            startColorCycling(player);
            colorModeEnabled.put(playerName, true);
        }
    }

    private static void startColorCycling(Player player) {
        BukkitRunnable colorTask = new BukkitRunnable() {
            private int currentIndex = 0;

            @Override
            public void run() {
                if (player.isOnline() && colorModeEnabled.getOrDefault(player.getName(), false)) {
                    Color color = COLORS.get(currentIndex);
                    applyArmorColor(player, color);
                    currentIndex = (currentIndex + 1) % COLORS.size();
                } else {
                    cancel();
                }
            }
        };

        colorTask.runTaskTimer(PLUGIN, 0, COLOR_CHANGE_INTERVAL);
        colorTasks.put(player.getName(), colorTask);
    }

    private static void cancelExistingTask(Player player) {
        BukkitRunnable existingTask = colorTasks.remove(player.getName());
        if (existingTask != null) {
            existingTask.cancel();
        }
    }

    private static void disableColorCycling(Player player) {
        cancelExistingTask(player);

        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);

        colorModeEnabled.put(player.getName(), false);
    }

    private static void applyArmorColor(Player player, Color color) {
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
        LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();
        LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();

        if (helmetMeta != null) {
            helmetMeta.setColor(color);
            helmet.setItemMeta(helmetMeta);
        }

        if (chestplateMeta != null) {
            chestplateMeta.setColor(color);
            chestplate.setItemMeta(chestplateMeta);
        }

        if (leggingsMeta != null) {
            leggingsMeta.setColor(color);
            leggings.setItemMeta(leggingsMeta);
        }

        if (bootsMeta != null) {
            bootsMeta.setColor(color);
            boots.setItemMeta(bootsMeta);
        }

        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);
    }
}
