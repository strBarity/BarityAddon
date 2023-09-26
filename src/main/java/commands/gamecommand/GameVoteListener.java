package main.java.commands.gamecommand;

import daybreak.abilitywar.game.event.GameEndEvent;
import main.java.playerdata.PlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public class GameVoteListener extends GameVote implements Listener {
    @EventHandler
    public void onClick(@NotNull PlayerInteractEvent e) {
        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND) && (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && e.getItem() != null && e.getItem().equals(config.get(VOTEITEM))) {
            vote(e.getPlayer());
        }
    }
    @EventHandler
    public void onGameEnd(GameEndEvent e) {
        voteBar.setVisible(true);
    }
    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent e) {
        if (e.getView().getTitle().equals(INV_TITLE)) {
            if (e.getRawSlot() == 31) {
                if (e.getCursor().getType().equals(Material.AIR)) {
                    config.set(VOTEITEM, null);
                }
                else {
                    config.set(VOTEITEM, e.getCursor());
                }
            }
            else if (e.getRawSlot() < INV_SIZE) {
                e.setCancelled(true);
                switch (e.getRawSlot()) {
                    case 13:
                        voteBar.setVisible(!(boolean) config.get(ENABLED));
                        config.set(ENABLED, !(boolean) config.get(ENABLED));
                        break;
                    case 20:
                        config.set(PLAYERS, requiredVotePlayers.ALL.toString());
                        break;
                    case 21:
                        config.set(PLAYERS, requiredVotePlayers.HALF.toString());
                        break;
                    case 23:
                        config.set(PLAYERS, requiredVotePlayers.ALL_MINUS_1.toString());
                        break;
                    default:
                        break;
                }
                updateBar();
                open((Player) e.getWhoClicked());
            }
        }
        else if (e.getCurrentItem() != null && e.getCurrentItem().equals(config.get(VOTEITEM))) {
            vote((Player) e.getWhoClicked());
        }
    }

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e) {
        playerCount++;
        voteBar.addPlayer(e.getPlayer());
        updateBar();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(@NotNull PlayerQuitEvent e) {
        if (PlayerData.getData(e.getPlayer()).isVoted()) {
            voteCount--;
        }
        playerCount--;
        updateBar();
    }
}
