package com.lee1387.lobbyplugin.items.gadgets;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class SpidermanItem implements Listener {

    private static final long COOLDOWN_TIME = 3000;
    private static final Map<String, Long> lastUseTime = new HashMap<>();
    private static final Map<String, Boolean> climbingEnabled = new HashMap<>();
    private static final Plugin plugin = Bukkit.getPluginManager().getPlugin("LobbyPlugin");

    public SpidermanItem(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public static ItemStack createSpidermanItem() {
        ItemStack spidermanItem = new ItemStack(Material.COBWEB);
        ItemMeta meta = spidermanItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_GRAY + "Spiderman");
            spidermanItem.setItemMeta(meta);
        }
        return spidermanItem;
    }

    public static void onRightClick(Player player) {
        String playerName = player.getName();
        long currentTime = System.currentTimeMillis();

        if (lastUseTime.containsKey(playerName)) {
            long lastUse = lastUseTime.get(playerName);
            long timeLeft = COOLDOWN_TIME - (currentTime - lastUse);

            if (timeLeft > 0) {
                player.sendMessage(ChatColor.RED + "You must wait before toggling again!");
                return;
            }
        }

        lastUseTime.put(playerName, currentTime);

        boolean isClimbingEnabled = climbingEnabled.getOrDefault(playerName, false);
        climbingEnabled.put(playerName, !isClimbingEnabled);

        if (!isClimbingEnabled) {
            player.sendMessage(ChatColor.GREEN + "You feel like Spiderman!");
        } else {
            player.sendMessage(ChatColor.RED + "You no longer feel like Spiderman!");
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        boolean isClimbingEnabled = climbingEnabled.getOrDefault(playerName, false);

        if (isClimbingEnabled && isNearVerticalWall(player)) {
            Vector velocity = player.getVelocity();
            velocity.setY(0.2);
            player.setVelocity(velocity);
        }
    }

    private boolean isNearVerticalWall(Player player) {
        Block block = player.getLocation().getBlock();
        BlockFace[] faces = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};

        for (BlockFace face : faces) {
            Block relativeBlock = block.getRelative(face);
            if (isFullBlock(relativeBlock)) {
                return true;
            }
        }
        return false;
    }

    private boolean isFullBlock(Block block) {
        Material type = block.getType();
        return type.isSolid() && type.isOccluding();
    }
}


