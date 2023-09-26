package main.java.commands.lobby;

import daybreak.abilitywar.utils.base.Messager;
import main.java.playerdata.PlayerData;
import main.java.util.EditorUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public class LobbyScoreboardListener extends LobbyScoreboard implements Listener {
    private enum lineEditType {
        UP(1),
        DOWN(-1);
        private final int additive;
        lineEditType(int additive) {
            this.additive = additive;
        }}
    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent e) {
        if (e.getView().getTitle().equals(INV_TITLE)) {
            e.setCancelled(true);
            if (e.getRawSlot() == 51) {
                newLine((Player) e.getWhoClicked());
            }
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.SIGN)) {
                compareAction((Player) e.getWhoClicked(), e.getClick(), e.getRawSlot());
            }
        }
    }

    @EventHandler
    public void onChat(@NotNull AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (PlayerData.getData(p).isInScoreboardEdit()) {
            e.setCancelled(true);
            if (e.getMessage().length() > 40) {
                Messager.sendErrorMessage(p, "스코어보드 내용은 40글자를 초과할 수 없습니다.");
            }
            else {
                String s = ChatColor.translateAlternateColorCodes('&', e.getMessage());
                int i = PlayerData.getData(p).getEditingScoreboardId();
                setLine(i, s);
                p.sendMessage("§a성공적으로 §e" + i + "§a번째 스코어보드 내용을 §f\"" + s + "§f\"§a(으)로 변경했습니다.");
                open(p);
            }
            PlayerData.getData(p).setInScoreboardEdit(false);
            PlayerData.getData(p).setInEdit(false);
            p.resetTitle();
        }
    }

    public void compareAction(Player p, @NotNull ClickType c, int slot) {
        int line;
        if (slot < 18) {
            line = slot - 10;
        }
        else if (slot < 27) {
            line = slot - 12;
        }
        else {
            line = slot - 14;
        }
        if (c.equals(ClickType.SHIFT_LEFT) || c.equals(ClickType.SHIFT_RIGHT)) {
            if (EditorUtil.isEditing(p)) {
                return;
            }
            PlayerData.getData(p).setInScoreboardEdit(true);
            PlayerData.getData(p).setEditingScoreboardId(line);
            PlayerData.getData(p).setInEdit(true);
            p.closeInventory();
            p.sendTitle("§a" + line + "§e번째 스코어보드 내용 편집 중", "§b채팅창에 변경할 내용을 입력하세요. §8(색깔 코드 사용 가능)", 0, Integer.MAX_VALUE, 0);
            p.sendMessage("§a" + line + "§e번째 스코어보드 내용 편집 중, 채팅창에 변경할 내용을 입력하세요. §8(색깔 코드 사용 가능)");
        }
        else if (c.equals(ClickType.LEFT)) {
            editLine(lineEditType.UP, p, line);
        }
        else if (c.equals(ClickType.RIGHT)) {
            editLine(lineEditType.DOWN, p, line);
        }
        else if (c.equals(ClickType.DROP)) {
            config.set(DEFAULTPATH + line, null);
            reloadMap();
            open(p);
        }
    }

    private void editLine(lineEditType editType, Player p, int line) {
        if (editType == lineEditType.UP && line == 15) {
            Messager.sendErrorMessage(p, "이미 가장 높은 줄입니다.");
            return;
        }
        else if (editType == lineEditType.DOWN && line == 0) {
            Messager.sendErrorMessage(p, "이미 가장 낮은 줄입니다.");
            return;
        }
        int lineAdditive = editType.additive;
        config.set(DEFAULTPATH + (line + lineAdditive), scoreBoard.get(line));
        if (scoreBoard.get(line + lineAdditive) == null) {
            config.set(DEFAULTPATH + line, getBlank());
        } else {
            config.set(DEFAULTPATH + line, scoreBoard.get(line + lineAdditive));
        }
        reloadMap();
        open(p);
    }

    private void setLine(int i, String s) {
        if (isBlank(s)) {
            s = getBlank();
        }
        config.set(DEFAULTPATH + i, s);
        reloadMap();
    }

    private void newLine(Player p) {
        int line = -1;
        for (int i = 0; i < 22; i++) {
            if (scoreBoard.get(i) == null) {
                line = i;
                break;
            }
            if (i == 15) {
                p.sendMessage("§f[§c!§f] §c줄은 15개를 초과할 수 없습니다.");
                return;
            }
        }
        if (line >= 0) {
            config.set(DEFAULTPATH + line, getBlank());
            reloadMap();
        }
        open(p);
    }

    private @NotNull String getBlank() {
        StringBuilder s = new StringBuilder(" ");
        for (int i = 0; i < blankCount; i++) {
            s.append(" ");
        }
        blankCount++;
        return s.toString();
    }
}
