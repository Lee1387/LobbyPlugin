package com.lee1387.lobbyplugin;

import com.lee1387.lobbyplugin.commands.LobbyCommand;
import com.lee1387.lobbyplugin.commands.SetLobbyCommand;
import com.lee1387.lobbyplugin.items.gadgets.SpidermanItem;
import com.lee1387.lobbyplugin.items.gadgets.TNTLauncherItem;
import com.lee1387.lobbyplugin.listeners.BlockListener;
import com.lee1387.lobbyplugin.listeners.EntityListener;
import com.lee1387.lobbyplugin.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class LobbyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        getServer().getPluginManager().registerEvents(new EntityListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(), this);

        getCommand("setlobby").setExecutor(new SetLobbyCommand(this));
        getCommand("lobby").setExecutor(new LobbyCommand(this));

        new TNTLauncherItem(this);
        new SpidermanItem(this);

        teleportPlayersToLobby();
        setWorldToDay();
        disableWeather();

        getServer().getScheduler().runTaskTimer(this, () -> {
            for (World world : getServer().getWorlds()) {
                world.setTime(6000);
            }
        }, 0L, 6000);
    }

    @Override
    public void onDisable() {

    }

    public Location getLobbyLocation() {
        if (getConfig().contains("lobby")) {
            double x = getConfig().getDouble("lobby.x");
            double y = getConfig().getDouble("lobby.y");
            double z = getConfig().getDouble("lobby.z");
            float yaw = (float) getConfig().getDouble("lobby.yaw");
            float pitch = (float) getConfig().getDouble("lobby.pitch");
            String worldName = getConfig().getString("lobby.world");
            return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
        }
        return null;
    }

    private void teleportPlayersToLobby() {
        Location lobbyLocation = getLobbyLocation();
        if (lobbyLocation != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.teleport(lobbyLocation);
            }
        }
    }

    private void setWorldToDay() {
        for (World world : getServer().getWorlds()) {
            world.setTime(6000);
        }
    }

    private void disableWeather() {
        for (World world : getServer().getWorlds()) {
            world.setStorm(false);
            world.setThundering(false);
        }
    }
}
