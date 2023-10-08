package main.java.commands;

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

public class ScoreboardListener extends ScoreboardHandler implements Listener {
    private static final String DEFAULTPATH = "scoreboard.";
    private enum lineEditType {
        UP(1),
        DOWN(-1);
        private final int additive;
        lineEditType(int additive) {
            this.additive = additive;
        }}
    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent e) {
        String title = e.getView().getTitle();
        if (title.equals(ScoreboardPage.LOBBY.getInvTitle())) {
            e.setCancelled(true);
            if (e.getRawSlot() == 51) {
                newLine((Player) e.getWhoClicked(), ScoreboardPage.LOBBY);
            }
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.SIGN)) {
                compareAction((Player) e.getWhoClicked(), ScoreboardPage.LOBBY, e.getClick(), e.getRawSlot());
            }
        }
        else if (title.equals(ScoreboardPage.GAME.getInvTitle())) {
            e.setCancelled(true);
            if (e.getRawSlot() == 51) {
                newLine((Player) e.getWhoClicked(), ScoreboardPage.GAME);
            }
            if (e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.SIGN)) {
                compareAction((Player) e.getWhoClicked(), ScoreboardPage.GAME, e.getClick(), e.getRawSlot());
            }
        }
    }

    @EventHandler
    public void onChat(@NotNull AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (PlayerData.getData(p).isInLobbyScoreboardEdit() || PlayerData.getData(p).isInGameScoreboardEdit()) {
            e.setCancelled(true);
            if (e.getMessage().length() > 40) {
                Messager.sendErrorMessage(p, "스코어보드 내용은 40글자를 초과할 수 없습니다.");
            }
            else {
                String s = ChatColor.translateAlternateColorCodes('&', e.getMessage());
                int i = PlayerData.getData(p).getEditingScoreboardId();
                if (PlayerData.getData(p).isInLobbyScoreboardEdit()) {
                    setLine(ScoreboardPage.LOBBY, i, s);
                    open(p, ScoreboardPage.LOBBY);
                }
                else if (PlayerData.getData(p).isInGameScoreboardEdit()) {
                    setLine(ScoreboardPage.GAME, i, s);
                    open(p, ScoreboardPage.GAME);
                }
                p.sendMessage("§a성공적으로 §e" + i + "§a번째 스코어보드 내용을 §f\"" + s + "§f\"§a(으)로 변경했습니다.");
            }
            PlayerData.getData(p).setInLobbyScoreboardEdit(false);
            PlayerData.getData(p).setInGameScoreboardEdit(false);
            PlayerData.getData(p).setInEdit(false);
            p.resetTitle();
        }
    }

    public void compareAction(Player p, ScoreboardPage page, @NotNull ClickType c, int slot) {
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
            if (page.equals(ScoreboardPage.LOBBY)) {
                PlayerData.getData(p).setInLobbyScoreboardEdit(true);
            }
            else if (page.equals(ScoreboardPage.GAME)) {
                PlayerData.getData(p).setInGameScoreboardEdit(true);
            }
            PlayerData.getData(p).setEditingScoreboardId(line);
            PlayerData.getData(p).setInEdit(true);
            p.closeInventory();
            p.sendTitle("§a" + line + "§e번째 스코어보드 내용 편집 중", "§b채팅창에 변경할 내용을 입력하세요. §8(색깔 코드 사용 가능)", 0, Integer.MAX_VALUE, 0);
            p.sendMessage("§a" + line + "§e번째 스코어보드 내용 편집 중, 채팅창에 변경할 내용을 입력하세요. §8(색깔 코드 사용 가능)");
        }
        else if (c.equals(ClickType.LEFT)) {
            editLine(page, lineEditType.UP, p, line);
        }
        else if (c.equals(ClickType.RIGHT)) {
            editLine(page, lineEditType.DOWN, p, line);
        }
        else if (c.equals(ClickType.DROP)) {
            page.getConfig().set(DEFAULTPATH + line, null);
            reloadMap(page);
            open(p, page);
        }
    }

    private void editLine(ScoreboardPage page, lineEditType editType, Player p, int line) {
        if (editType == lineEditType.UP && line == 15) {
            Messager.sendErrorMessage(p, "이미 가장 높은 줄입니다.");
            return;
        }
        else if (editType == lineEditType.DOWN && line == 0) {
            Messager.sendErrorMessage(p, "이미 가장 낮은 줄입니다.");
            return;
        }
        int lineAdditive = editType.additive;
        page.getConfig().set(DEFAULTPATH + (line + lineAdditive), getScoreboard(page).get(line));
        if (getScoreboard(page).get(line + lineAdditive) == null) {
            page.getConfig().set(DEFAULTPATH + line, getBlank(page));
        } else {
            page.getConfig().set(DEFAULTPATH + line, getScoreboard(page).get(line + lineAdditive));
        }
        reloadMap(page);
        open(p, page);
    }

    private void setLine(ScoreboardPage page, int i, String s) {
        if (isBlank(s)) {
            s = getBlank(page);
        }
        page.getConfig().set(DEFAULTPATH + i, s);
        reloadMap(page);
    }

    private void newLine(Player p, ScoreboardPage page) {
        int line = -1;
        for (int i = 0; i < 22; i++) {
            if (getScoreboard(page).get(i) == null) {
                line = i;
                break;
            }
            if (i == 15) {
                p.sendMessage("§f[§c!§f] §c줄은 15개를 초과할 수 없습니다.");
                return;
            }
        }
        if (line >= 0) {
            page.getConfig().set(DEFAULTPATH + line, getBlank(page));
            reloadMap(page);
        }
        open(p, page);
    }

    private static @NotNull String getBlank(ScoreboardPage page) {
        StringBuilder s = new StringBuilder(" ");
        for (int i = 0; i < getBlankCount(page); i++) {
            s.append(" ");
        }
        if (page.equals(ScoreboardPage.LOBBY)) {
            lobbyBlankCount++;
        }
        else if (page.equals(ScoreboardPage.GAME)) {
            gameBlankCount++;
        }
        return s.toString();
    }
}
