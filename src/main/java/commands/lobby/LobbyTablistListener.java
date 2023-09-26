package main.java.commands.lobby;

import daybreak.abilitywar.utils.base.Messager;
import main.java.playerdata.PlayerData;
import main.java.util.EditorUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class LobbyTablistListener extends LobbyTablist implements Listener {
    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent e) {
        if (e.getView().getTitle().equals(INV_TITLE)) {
            Player p = (Player) e.getWhoClicked();
            PlayerData data = PlayerData.getData(p);
            boolean inEdit = true;
            String editing = null;
            if (Arrays.asList(11, 13, 15).contains(e.getRawSlot()) && EditorUtil.isEditing(p)) {
                return;
            }
            switch (e.getRawSlot()) {
                case 11:
                    editing = "플레이어 목록 상단 텍스트";
                    data.setInTablistHeaderEdit(true);
                    break;
                case 13:
                    editing = "플레이어 목록 이름 포맷";
                    data.setInTablistPlayerFormatEdit(true);
                    break;
                case 15:
                    editing = "플레이어 목록 하단 텍스트";
                    data.setInTablistFooterEdit(true);
                    break;
                default:
                    inEdit = false;
                    break;
            }
            if (inEdit) {
                data.setInEdit(true);
                p.closeInventory();
                p.sendTitle("§a" + editing + "§e 편집 중", "§b채팅창에 변경할 내용을 입력하세요. §8(색깔 코드 사용 가능)", 0, Integer.MAX_VALUE, 0);
                p.sendMessage("§a" + editing + "§e 편집 중, 채팅창에 변경할 내용을 입력하세요. §8(색깔 코드 사용 가능)");
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        PlayerData data = PlayerData.getData(p);
        String s = "§f\"§a(으)로 변경했습니다.";
        boolean edited = false;
        if (data.isInTablistHeaderEdit()) {
            edited = true;
            config.set(HEADER, e.getMessage());
            p.sendMessage("§a성공적으로 플레이어 목록 상단 텍스트를 §f\"" + ChatColor.translateAlternateColorCodes('&', e.getMessage()) + s);
            data.setInTablistHeaderEdit(false);
        }
        if (data.isInTablistPlayerFormatEdit()) {
            edited = true;
            if (!e.getMessage().contains("$name")) {
                Messager.sendErrorMessage(p, "포맷이 플레이어 이름($name)을 포함하고 있지 않습니다.");
            } else {
                config.set(PLAYER, e.getMessage());
                p.sendMessage("§a성공적으로 플레이어 목록 이름 포맷을 §f\"" + ChatColor.translateAlternateColorCodes('&', e.getMessage()) + s);
            }
            data.setInTablistPlayerFormatEdit(false);
        }
        if (data.isInTablistFooterEdit()) {
            edited = true;
            config.set(FOOTER, e.getMessage());
            p.sendMessage("§a성공적으로 플레이어 목록 하단 텍스트를 §f\"" + ChatColor.translateAlternateColorCodes('&', e.getMessage()) + s);
            data.setInTablistFooterEdit(false);
        }
        if (edited) {
            e.setCancelled(true);
            open(p);
            p.resetTitle();
            data.setInEdit(false);
            Bukkit.getOnlinePlayers().forEach(this::updateTablist);
        }
    }

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e) {
        Player p = e.getPlayer();
        updateTablist(p);
    }
}
