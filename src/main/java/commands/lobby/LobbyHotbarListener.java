package main.java.commands.lobby;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LobbyHotbarListener extends LobbyHotbar implements Listener {
    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent e) {
        if (e.getView().getTitle().equals(INV_TITLE)) {
            if (e.getRawSlot() >= 18 && e.getRawSlot() <= 26) {
                update(e);
                Bukkit.getOnlinePlayers().forEach(this::updateHotbar);
            } else if (e.getRawSlot() < e.getInventory().getSize()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(@NotNull InventoryCloseEvent e) {
        if (e.getView().getTitle().equals(INV_TITLE)) {
            for (int i = 0; i < 9; i++) {
                config.set(DEFAULTPATH + i, e.getInventory().getItem(i +  18));
            }
            Bukkit.getOnlinePlayers().forEach(this::updateHotbar);
        }
    }

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e) {
        e.getPlayer().getInventory().clear();
        updateHotbar(e.getPlayer());
    }

    public void updateHotbar(Player p) {
        if (isHotbarExists()) {
            for (int i = 0; i < 9; i++) {
                ItemStack item = config.getConfig().getItemStack(DEFAULTPATH + i);
                if (item != null) {
                    p.getInventory().setItem(i, item);
                }
                else {
                    p.getInventory().setItem(i, new ItemStack(Material.AIR));
                }
            }
        }
    }

    private void update(@NotNull InventoryClickEvent e) {
        if (e.getCursor() != null) {
            for (int i = 0; i < 9; i++) {
                if (i == e.getSlot() - 18) {
                    if (e.getCursor() == null || e.getCursor().getType().equals(Material.AIR)) {
                        config.set(DEFAULTPATH + (e.getSlot() - 18), null);
                    }
                    else {
                        config.set(DEFAULTPATH + (e.getSlot() - 18), e.getCursor());
                    }
                } else {
                    config.set(DEFAULTPATH + i, e.getInventory().getItem(i + 18));
                }
            }
        } else if (e.getCurrentItem() != null) {
            config.set(DEFAULTPATH + (e.getSlot() - 18), null);
        } else {
            e.setCancelled(true);
        }
    }

    private boolean isHotbarExists() {
        for (int i = 0; i < 9; i++) {
            if (config.get(DEFAULTPATH + i) != null) {
                return true;
            }
        }
        return false;
    }
}
