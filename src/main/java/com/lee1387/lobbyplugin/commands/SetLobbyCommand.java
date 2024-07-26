package com.lee1387.lobbyplugin.commands;

import com.lee1387.lobbyplugin.LobbyPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobbyCommand implements CommandExecutor {

    private final LobbyPlugin plugin;

    public SetLobbyCommand(LobbyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setlobby")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    Location loc = player.getLocation();
                    plugin.getConfig().set("lobby.x", loc.getX());
                    plugin.getConfig().set("lobby.y", loc.getY());
                    plugin.getConfig().set("lobby.z", loc.getZ());
                    plugin.getConfig().set("lobby.yaw", loc.getYaw());
                    plugin.getConfig().set("lobby.pitch", loc.getPitch());
                    plugin.getConfig().set("lobby.world", loc.getWorld().getName());
                    plugin.saveConfig();
                    player.sendMessage(ChatColor.GREEN + "Lobby position set!");
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
            }
            return true;
        }
        return false;
    }
}
