package main.java.commands.lobby;

import daybreak.abilitywar.utils.base.Messager;
import main.java.playerdata.PlayerData;
import main.java.util.EditorUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public class LobbyChatListener extends LobbyChat implements Listener {
    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent e) {
        if (e.getView().getTitle().equals(INV_TITLE) && e.getRawSlot() == 13) {
            Player p = (Player) e.getWhoClicked();
            if (EditorUtil.isEditing(p)) {
                return;
            }
            p.closeInventory();
            PlayerData.getData(p).setInChatFormatEdit(true);
            PlayerData.getData(p).setInEdit(true);
            p.sendTitle("§e채팅 포맷 편집 중", "§b채팅창에 변경할 내용을 입력하세요. §8(색깔 코드 사용 가능)", 0, Integer.MAX_VALUE, 0);
            p.sendMessage("§e채팅 포맷 편집 중, 채팅창에 변경할 내용을 입력하세요. §8(색깔 코드 사용 가능)");
        }
    }
    @EventHandler
    public void onChat(@NotNull AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (PlayerData.getData(p).isInChatFormatEdit()) {
            e.setCancelled(true);
            if (!e.getMessage().contains("$name")) {
                Messager.sendErrorMessage(p, "채팅 포맷이 플레이어 이름($name)을 포함하고 있지 않습니다.");
            }
            else if (!e.getMessage().contains("$msg")) {
                Messager.sendErrorMessage(p, "채팅 포맷이 메시지($msg)를 포함하고 있지 않습니다.");
            }
            else {
                config.set(CHAT_FORMAT, e.getMessage());
                p.sendMessage("§a성공적으로 채팅 포맷을 §f\"" + ChatColor.translateAlternateColorCodes('&', e.getMessage()) + "§f\"§a(으)로 변경했습니다.");
                open(p);
            }
            p.resetTitle();
            PlayerData.getData(p).setInChatFormatEdit(false);
            PlayerData.getData(p).setInEdit(false);

        }
        else if (!e.isCancelled()) {
            e.setFormat(ChatColor.translateAlternateColorCodes('&', config.get(CHAT_FORMAT).toString()).replace("$name", p.getName()).replace("$msg", e.getMessage()));
        }
    }
}
