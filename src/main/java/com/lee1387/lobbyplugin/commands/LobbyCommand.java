package com.lee1387.lobbyplugin.commands;

import com.lee1387.lobbyplugin.LobbyPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand implements CommandExecutor {

    private final LobbyPlugin plugin;

    public LobbyCommand(LobbyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("lobby")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Location lobbyLocation = plugin.getLobbyLocation();
                if (lobbyLocation != null) {
                    player.teleport(lobbyLocation);
                    player.sendMessage(ChatColor.GREEN + "Teleported to the lobby!");
                } else {
                    player.sendMessage(ChatColor.RED + "Lobby location is not set!");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
            }
            return true;
        }
        return false;
    }
}
