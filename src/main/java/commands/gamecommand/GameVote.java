package main.java.commands.gamecommand;

import daybreak.abilitywar.Command;
import daybreak.abilitywar.game.GameManager;
import daybreak.abilitywar.utils.base.Messager;
import main.java.playerdata.PlayerData;
import main.java.util.AddonConfig;
import main.java.util.ItemFactory;
import main.java.util.SoundUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class GameVote extends Command {
    public static final BossBar voteBar = Bukkit.createBossBar("§b게임 시작 투표 §90§1/§91", BarColor.BLUE, BarStyle.SOLID);

    protected int voteCount = 0;
    protected int playerCount;
    protected int requiredVote = 0;
    protected enum requiredVotePlayers {
        ALL("모든 플레이어"),
        HALF("절반 플레이어(반올림)"),
        ALL_MINUS_1("모든 플레이어 - 1"),
        CUSTOM("");
        private final String desc;
        requiredVotePlayers(String desc) {
            this.desc = desc;
        }
    }
    protected AddonConfig config;
    protected static final int INV_SIZE = 54;
    protected static final String DEFAULTPATH = "vote.";
    protected static final String ENABLED = DEFAULTPATH + "enabled";
    protected static final String PLAYERS = DEFAULTPATH + "requiredVotePlayers";
    protected static final String VOTEITEM = DEFAULTPATH + "voteItem";
    protected static final String INV_TITLE = "§8게임 시작 투표 설정";
    private static final String VOTE_SLASH = "§1/§9";
    public GameVote() {
        super(Condition.OP);
        config = AddonConfig.getConfig("gameConfig");
        playerCount = Bukkit.getOnlinePlayers().size();
        Bukkit.getOnlinePlayers().forEach(voteBar::addPlayer);
        resetConfig();
        updateBar();
    }
    @Override
    protected boolean onCommand(CommandSender sender, String command, String[] args) {
        if (sender instanceof Player) {
            open((Player) sender);
            return true;
        }
        Messager.sendErrorMessage(sender, "이 명령어는 플레이어만 사용할 수 있습니다.");
        return true;
    }

    private void resetConfig() {
        if (config.get(ENABLED) == null) {
            config.set(ENABLED, false);
        }
        if (config.get(PLAYERS) == null) {
            config.set(PLAYERS, requiredVotePlayers.HALF.toString());
        }
    }

    protected void open(Player p) {
        Inventory gui = Bukkit.createInventory(null, INV_SIZE, INV_TITLE);
        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, ItemFactory.blank(ItemFactory.ItemColor.LIGHT_BLUE, false));
        }
        String s = "§a선택됨!";
        String c = "§e클릭해서 선택하기";
        String allS = c;
        String halfS = c;
        String allminusoneS = c;
        boolean all = false;
        boolean half = false;
        boolean allminusone = false;
        requiredVotePlayers r = requiredVotePlayers.valueOf(config.get(PLAYERS).toString());
        switch (r) {
            case ALL:
                all = true;
                allS = s;
                break;
            case HALF:
                half = true;
                halfS = s;
                break;
            case ALL_MINUS_1:
                allminusone = true;
                allminusoneS = s;
                break;
            case CUSTOM:
                break;
        }
        gui.setItem(13, ItemFactory.createItem(Material.PAPER, 0, "§b투표해서 게임 시작하기", Arrays.asList("§e특정 인원의 플레이어가 투표해야" ,"§e게임을 시작할 수 있습니다.", "§e명령어는 §a/aw vote§e입니다.", "", "§f현재 설정: " + AddonConfig.boolTranslate((boolean) config.get(ENABLED)), "§f현재 필요 투표 수: §a" + requiredVotePlayers.valueOf(config.get(PLAYERS).toString()).desc), null, 1, true));
        gui.setItem(20, ItemFactory.createItem(Material.CAKE, 0, "§f필요: §a모든 플레이어", Arrays.asList("§e모든 플레이어가 투표해야 게임을 시작할 수 있습니다.", "", allS), null, 1, all));
        gui.setItem(21, ItemFactory.createItem(Material.MELON, 0, "§f필요: §a절반 플레이어", Arrays.asList("§e절반의 플레이어가 투표해야 게임을 시작할 수 있습니다.", "", halfS), null, 1, half));
        gui.setItem(23, ItemFactory.createItem(Material.PUMPKIN_PIE, 0, "§f필요: §a모든 플레이어 - 1", Arrays.asList("§e(모든 플레이어 - 1)명이 투표해야 게임을 시작할 수 있습니다.", "", allminusoneS), null, 1, allminusone));
        gui.setItem(24, ItemFactory.createItem(Material.SIGN, 0, "§f필요: §a직접 입력", Arrays.asList("§e필요한 플레이어를 직접 입력할 수 있습니다.", "§e변수 §b$AllPlayers§e와 사칙연산을 사용할 수 있습니다.", "", "§cCOMING SOON"), null, 1, false));
        if (config.get(DEFAULTPATH + "voteItem") == null) {
            gui.setItem(31, new ItemStack(Material.AIR));
        }
        else {
            gui.setItem(31, config.getConfig().getItemStack(VOTEITEM));
        }
        gui.setItem(40, ItemFactory.createItem(Material.STAINED_GLASS, 5, "§a투표 아이템 추가하기", Arrays.asList("§e상단의 빈칸에 아이템을 넣어 투표 아이템으로 설정합니다.", "§e플레이어들은 해당 아이템을 우클릭해서도 투표할 수 있습니다.", "§c주의: §6완벽히 일치하는 아이템만 감지합니다."), null, 1, false));
        p.openInventory(gui);
    }

    protected void updateBar() {
        requiredVotePlayers r = requiredVotePlayers.valueOf(config.get(PLAYERS).toString());
        switch (r) {
            case ALL:
                requiredVote = playerCount;
                break;
            case HALF:
                requiredVote = Math.round((float) playerCount / 2);
                break;
            case ALL_MINUS_1:
                requiredVote = playerCount - 1;
                break;
            case CUSTOM:
                break;
        }
        voteBar.setTitle("§b게임 시작 투표 §9" + voteCount + VOTE_SLASH + requiredVote);
        if (requiredVote != 0) {
            voteBar.setProgress((double) voteCount / requiredVote);
        }
        else {
            voteBar.setProgress(0D);
        }
    }

    protected void vote(Player p) {
        if (Bukkit.getOnlinePlayers().size() == 1) {
            Messager.sendErrorMessage(p, "플레이어가 한 명일 땐 게임 시작에 투표할 수 없습니다.");
            Bukkit.getOnlinePlayers().forEach(s -> SoundUtil.playChord(p, Sound.BLOCK_ANVIL_LAND, 1));
            return;
        }
        PlayerData data = PlayerData.getData(p);
        if (data.isVoted()) {
            voteCount--;
            Bukkit.broadcastMessage("§e" + p.getName() + "§f님이 게임 시작 투표를 §c취소했습니다§f. §b(§9" + voteCount + VOTE_SLASH + requiredVote + "§b)");
            Bukkit.getOnlinePlayers().forEach(s -> SoundUtil.playChord(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F));
        }
        else {
            voteCount++;
            Bukkit.broadcastMessage("§e" + p.getName() + "§f님이 게임 시작에 §b투표했습니다§f. §b(§9" + voteCount + VOTE_SLASH + requiredVote + "§b)");
            Bukkit.getOnlinePlayers().forEach(s -> SoundUtil.playChord(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1));
            if (voteCount == requiredVote) {
                voteCount = 0;
                Bukkit.getOnlinePlayers().forEach(s -> PlayerData.getData(s).setVoted(false));
                voteBar.setVisible(false);
                GameManager.startGame(new String[]{});
                return;
            }
        }
        updateBar();
        data.setVoted(!data.isVoted());
    }
}
