package main.java.commands;

import daybreak.abilitywar.AbilityWar;
import daybreak.abilitywar.game.GameManager;
import main.java.util.ItemColor;
import main.java.util.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreboardHandler {
    protected ScoreboardHandler() {

    }
    protected static final Map<Integer, String> lobbyScoreBoard = new ConcurrentHashMap<>();
    protected static final Map<Integer, String> gameScoreBoard = new ConcurrentHashMap<>();
    protected static int lobbyBlankCount = 1;
    protected static int gameBlankCount = 1;
    protected static final String NAME = "scoreboard.name";
    @Contract(pure = true)
    protected static Map<Integer, String> getScoreboard(@NotNull ScoreboardPage page) {
        switch (page) {
            case LOBBY:
                return lobbyScoreBoard;
            case GAME:
                return gameScoreBoard;
            default:
                throw new IllegalArgumentException("Unexpected value: " + page);
        }
    }
    protected static int getBlankCount(@NotNull ScoreboardPage page) {
        switch (page) {
            case LOBBY:
                return lobbyBlankCount;
            case GAME:
                return gameBlankCount;
            default:
                throw new IllegalArgumentException("Unexpected value: " + page);
        }
    }
    public static void open(Player p, @NotNull ScoreboardPage page) {
        Map<Integer, String> scoreBoard = getScoreboard(page);
        Inventory gui = Bukkit.createInventory(null, 54, page.getInvTitle());
        for (int i = 0; i < 9; i++) {
            gui.setItem(i, ItemFactory.blank(ItemColor.WHITE, true));
        }
        for (int i = 36; i < gui.getSize(); i++) {
            gui.setItem(i, ItemFactory.blank(ItemColor.WHITE, true));
        }
        for (int i : new ArrayList<>(Arrays.asList(9, 17, 18, 26, 27, 35))) {
            gui.setItem(i, ItemFactory.blank(ItemColor.WHITE, true));
        }
        for (int i = 30; i < 35; i++) {
            gui.setItem(i, ItemFactory.createItem(Material.WOOL, 14, " ", null, null, 1, false));
        }
        gui.setItem(4, ItemFactory.createItem(Material.PAPER, 0, "§f스코어보드 이름: " + page.getConfig().get(NAME), Collections.singletonList("§e클릭해서 편집하기 §8(변수 사용 불가능)"), null, 1, false));
        gui.setItem(47, ItemFactory.createItem(Material.BOOK, 0, "§e사용 가능한 변수 목록", page.getAvailableVariables(), null, 1, false));
        gui.setItem(49, ItemFactory.createItem(Material.BARRIER, 0, "§c닫기", null, null, 1, false));
        gui.setItem(51, ItemFactory.createItem(Material.NETHER_STAR, 0, "§2+ §a줄 추가하기", null, null, 1, true));
        for (Map.Entry<Integer, String> entry : scoreBoard.entrySet()) {
            int i = entry.getKey();
            String contents = scoreBoard.get(i);
            if (isBlank(contents)) {
                contents = "§7§o비어 있음";
            }
            List<String> lore = new ArrayList<>();
            lore.add("§f내용: " + contents);
            lore.add(" ");
            lore.add("§aSHIFT + 클릭     》 §e텍스트 편집");
            lore.add("§c좌클릭     》 §e줄 한 칸 올리기");
            lore.add("§c우클릭     》 §e줄 한 칸 내리기");
            lore.add("§cQ     》 §4제거 ");
            int slot;
            if (i < 7) {
                slot = i + 10;
            }
            else if (i < 14) {
                slot = i + 12;
            }
            else {
                slot = i + 14;
            }
            gui.setItem(slot, ItemFactory.createItem(Material.SIGN, 0, "§f" + i + "번째 줄", lore, null, 1, false));
        }
        p.openInventory(gui);
    }
    public static void startScoreboardTask(ScoreboardPage page) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(AbilityWar.getPlugin(), () -> {
            if ((page.equals(ScoreboardPage.LOBBY) && !GameManager.isGameRunning()) || (page.equals(ScoreboardPage.GAME))) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (page.equals(ScoreboardPage.LOBBY) || GameManager.isGameRunning() || p.getOpenInventory() != null && p.getOpenInventory().getTitle().equals(ScoreboardPage.GAME.getInvTitle())) {
                        applyBoard(p, page);
                    }
                }
            }
        }, 0, 20);
    }
    public static void reloadMap(ScoreboardPage page) {
        getScoreboard(page).clear();
        if (page.equals(ScoreboardPage.LOBBY)) {
            lobbyBlankCount = 1;
        }
        else if (page.equals(ScoreboardPage.GAME)) {
            gameBlankCount = 1;
        }
        for (int i = 0; i < 21; i++) {
            Object o = page.getConfig().get("scoreboard." + i);
            if (o != null) {
                if (isBlank(o.toString())) {
                    if (page.equals(ScoreboardPage.LOBBY)) {
                        lobbyBlankCount++;
                    }
                    else if (page.equals(ScoreboardPage.GAME)) {
                        gameBlankCount++;
                    }
                }
                getScoreboard(page).put(i, o.toString());
            }
        }
        if (page.getConfig().get(NAME) == null) {
            page.getConfig().set(NAME, "§2《 §aAbilityWars §2》");
        }
    }
    protected static void applyBoard(Player p, @NotNull ScoreboardPage page) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective("mainBoard", "Dummy");
        objective.setDisplayName(page.getConfig().get(NAME).toString());
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        if (getScoreboard(page).isEmpty()) {
            Score s1 = objective.getScore(" ");
            s1.setScore(2);
            Score s2 = objective.getScore("§c지정되지 않음");
            s2.setScore(1);
            Score s3 = objective.getScore("  ");
            s3.setScore(0);
        } else {
            for (Map.Entry<Integer, String> entry : getScoreboard(page).entrySet()) {
                Score s = objective.getScore(entry.getValue());
                s.setScore(entry.getKey());
            }
        }
        p.setScoreboard(board);
    }
    protected static boolean isBlank(@NotNull String s) {
        return s.replace(" ", "").isEmpty();
    }
}
