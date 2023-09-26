package main.java.commands.lobby;

import daybreak.abilitywar.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LobbyWorldListener extends LobbyWorld implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!GameManager.isGameRunning()) {
            e.getPlayer().teleport(Bukkit.getWorld(config.get("lobby").toString()).getSpawnLocation());
        }
    }
}
