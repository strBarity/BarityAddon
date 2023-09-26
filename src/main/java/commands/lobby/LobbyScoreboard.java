package main.java.commands.lobby;

import daybreak.abilitywar.AbilityWar;
import daybreak.abilitywar.Command;
import daybreak.abilitywar.game.GameManager;
import daybreak.abilitywar.utils.base.Messager;
import main.java.util.AddonConfig;
import main.java.util.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LobbyScoreboard extends Command {
    protected AddonConfig config;
    protected static final int INV_SIZE = 54;
    protected static final String DEFAULTPATH = "scoreboard.";
    protected static final String INV_TITLE = "§8스코어보드 설정";
    protected int blankCount = 0;
    protected static final Map<Integer, String> scoreBoard = new ConcurrentHashMap<>();
    public LobbyScoreboard() {
        super(Condition.OP);
        config = AddonConfig.getConfig("lobbyConfig");
        reloadMap();
    }
    @Override
    protected boolean onCommand(CommandSender sender, String command, String[] args) {
        if (sender instanceof Player) {
            open((Player) sender);
        }
        else {
            Messager.sendErrorMessage(sender, "이 명령어는 플레이어만 사용할 수 있습니다.");
        }
        return true;
    }
    protected void open(Player p) {
        Inventory gui = Bukkit.createInventory(null, INV_SIZE, INV_TITLE);
        for (int i = 0; i < 9; i++) {
            gui.setItem(i, ItemFactory.blank(ItemFactory.ItemColor.WHITE, true));
        }
        for (int i = 36; i < gui.getSize(); i++) {
            gui.setItem(i, ItemFactory.blank(ItemFactory.ItemColor.WHITE, true));
        }
        for (int i : new ArrayList<>(Arrays.asList(9, 17, 18, 26, 27, 35))) {
            gui.setItem(i, ItemFactory.blank(ItemFactory.ItemColor.WHITE, true));
        }
        for (int i = 30; i < 35; i++) {
            gui.setItem(i, ItemFactory.createItem(Material.WOOL, 14, " ", null, null, 1, false));
        }
        gui.setItem(4, ItemFactory.createItem(Material.PAPER, 0, "§f스코어보드 이름: " + config.get(DEFAULTPATH + "name"), Collections.singletonList("§e클릭해서 편집하기 §8(변수 사용 불가능)"), null, 1, false));
        gui.setItem(47, ItemFactory.createItem(Material.BOOK, 0, "§e사용 가능한 변수 목록", getAvailableVariablesList(), null, 1, false));
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

    public static void startScoreboardTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(AbilityWar.getPlugin(), () -> {
            if (!GameManager.isGameRunning()) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
                    Objective objective = board.registerNewObjective("mainBoard", "Dummy");
                    objective.setDisplayName(AddonConfig.getConfig("lobbyConfig").get(DEFAULTPATH + "name").toString());
                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                    if (scoreBoard.isEmpty()) {
                        Score s1 = objective.getScore(" ");
                        s1.setScore(2);
                        Score s2 = objective.getScore("§c지정되지 않음");
                        s2.setScore(1);
                        Score s3 = objective.getScore("  ");
                        s3.setScore(0);
                    } else {
                        for (Map.Entry<Integer, String> entry : scoreBoard.entrySet()) {
                            Score s = objective.getScore(entry.getValue());
                            s.setScore(entry.getKey());
                        }
                    }
                    p.setScoreboard(board);
                }
            }
        }, 0, 20);
    }

    protected void reloadMap() {
        scoreBoard.clear();
        blankCount = 1;
        for (int i = 0; i < 21; i++) {
            Object o = config.get(DEFAULTPATH + i);
            if (o != null) {
                if (isBlank(o.toString())) {
                    blankCount++;
                }
                scoreBoard.put(i, o.toString());
            }
        }
        if (config.get(DEFAULTPATH + "name") == null) {
            config.set(DEFAULTPATH + "name", "§2《 §aAbilityWars §2》");
        }
    }

    protected boolean isBlank(@NotNull String s) {
        return s.replace(" ", "").isEmpty();
    }

    private static @NotNull List<String> getAvailableVariablesList() {
        List<String> listLore = new ArrayList<>();
        listLore.add("§e- §a$Level §8: §b플레이어 레벨");
        listLore.add("§e- §a$ExpBar §8: §b플레이어 경험치 바");
        listLore.add("§e- §a$ExpPer §8: §b플레이어 경험치 퍼센트");
        listLore.add("§e- §a$LevelTag §8: §b플레이어 레벨 칭호");
        listLore.add("§e- §a$Totalills §8: §b플레이어 누적 킬");
        listLore.add("§e- §a$TotalWins §8: §b플레이어 누적 우승");
        listLore.add("§e- §a$Coins §8: §b플레이어 코인");
        listLore.add("§e- §a$VoteTopModeName §8: §b최고 득표 게임모드 이름");
        listLore.add("§e- §a$VoteTopModeCount §8: §b최고 득표 게임모드 득표 수");
        listLore.add("§e- §a$VoteTopModePer §8: §b최고 득표 게임모드 득표율");
        listLore.add("§e- §a$VoteTopMapName §8: §b최고 득표 맵 이름");
        listLore.add("§e- §a$VoteTopMapCount §8: §b최고 득표 맵 득표 수");
        listLore.add("§e- §a$VoteTopMapPer §8: §b최고 득표 맵 득표율");
        return listLore;
    }
}
