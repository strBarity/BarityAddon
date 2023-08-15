package main.java.commands.item;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemStorageListener extends ItemStorage implements Listener {
    private final String title = (String) config.get(DEFAULTPATH + "title");

    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent e) {
        if (e.getView().getTitle().equals(title) && !e.getSlotType().equals(InventoryType.SlotType.OUTSIDE) && e.getRawSlot() == e.getSlot()) {
            syncInventory(e.getInventory(), e.getWhoClicked());
        }
    }

    @EventHandler
    public void onInventoryClose(@NotNull InventoryCloseEvent e) {
        if (e.getView().getTitle().equals(title)) {
            saveContent(e.getInventory());
            e.getPlayer().sendMessage("§a변경 사항이 저장되었습니다.");
        }
    }

    private void syncInventory(Inventory inventory, @NotNull HumanEntity whoClicked) {
        saveContent(inventory);
        updateContent((Player) whoClicked);
        whoClicked.sendMessage("§aSAVE");
    }

    private void saveContent(@NotNull Inventory gui) {
        config.set(DEFAULTPATH + "content", gui.getContents());
    }

    @SuppressWarnings("unchecked")
    private void updateContent(Player originalPlayer) {
        config.reload();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.equals(originalPlayer) && p.getOpenInventory().getTopInventory() != null && p.getOpenInventory().getTopInventory().getTitle().equals(title)) {
                p.getOpenInventory().getTopInventory().setContents(((List<ItemStack>) config.get(DEFAULTPATH + "content")).toArray(new ItemStack[0]));
            }
        }
    }

}
