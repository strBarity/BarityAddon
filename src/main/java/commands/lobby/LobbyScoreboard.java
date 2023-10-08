package main.java.commands.lobby;

import daybreak.abilitywar.Command;
import daybreak.abilitywar.utils.base.Messager;
import main.java.commands.ScoreboardHandler;
import main.java.commands.ScoreboardPage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
public class LobbyScoreboard extends Command {
    public LobbyScoreboard() {
        super(Condition.OP);
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
        ScoreboardHandler.open(p, ScoreboardPage.LOBBY);
    }

    public static void startScoreboardTask() {
        ScoreboardHandler.startScoreboardTask(ScoreboardPage.LOBBY);
    }

    protected void reloadMap() {
        ScoreboardHandler.reloadMap(ScoreboardPage.LOBBY);
    }

    public static @NotNull List<String> getAvailableVariablesList() {
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
