package main.java.commands.gamecommand;

import daybreak.abilitywar.game.event.participant.ParticipantAbilitySetEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class GameSmallFixesListener extends GameSmallFixes implements Listener {
    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent e) {
        if (e.getView().getTitle().equals(INV_TITLE)) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            if (e.getRawSlot() == 13) {
                config.set(AUTO_CHECK_WHEN_SET, !((boolean) config.get(AUTO_CHECK_WHEN_SET)));
                open(p);
            }
        }
    }
    @EventHandler
    public void onAbilityChange(ParticipantAbilitySetEvent e) {
        if ((boolean) config.get(AUTO_CHECK_WHEN_SET)) {
            runCheck(e.getPlayer());
        }
    }
    private void runCheck(@NotNull Player p) {
        p.performCommand("aw check");
    }
}
