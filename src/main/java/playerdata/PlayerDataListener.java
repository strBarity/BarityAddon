package main.java.playerdata;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerDataListener implements Listener {
    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e) {
        PlayerData.defineData(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(@NotNull PlayerQuitEvent e) {
        PlayerData.clearData(e.getPlayer());
    }
}
