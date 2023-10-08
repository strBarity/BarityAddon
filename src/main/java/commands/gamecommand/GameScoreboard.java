package main.java.commands.gamecommand;

import daybreak.abilitywar.Command;
import daybreak.abilitywar.utils.base.Messager;
import main.java.commands.ScoreboardHandler;
import main.java.commands.ScoreboardPage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GameScoreboard extends Command {
    public GameScoreboard() {
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
        ScoreboardHandler.open(p, ScoreboardPage.GAME);
    }

    public static void startScoreboardTask() {
        ScoreboardHandler.startScoreboardTask(ScoreboardPage.GAME);
    }

    protected void reloadMap() {
        ScoreboardHandler.reloadMap(ScoreboardPage.GAME);
    }

    public static @NotNull List<String> getAvailableVariablesList() {
        List<String> listLore = new ArrayList<>();
        listLore.add("§e- §a$GameMode §8: §b현재 게임모드");
        listLore.add("§e- §a$MapName §8: §b현재 맵 표시 이름");
        listLore.add("§e- §a$PlayerLefts §8: §b생존 중인 플레이어 수");
        listLore.add("§e- §a$PlayerAbility §8: §b플레이어의 능력 이름");
        listLore.add("§e- §a$BorderPhase §8: §b현재 보더 페이즈");
        listLore.add("§e- §a$BorderBlock §8: §b현재 보더 넓이");
        listLore.add("§e- §a$BorderNext §8: §b다음 페이즈 보더 넓이");
        listLore.add("§e- §a$BorderTimer §8: §b다음 보더 진행 또는 보더 정지까지 남은 시간");
        listLore.add("§e- §a$BorderVar_§e[§6숫자§e] §8: §d<커스터마이즈 가능>");
        listLore.add(" §b보더 정지 또는 진행 중에 따른 변수");
        listLore.add(" §7(/aw border variable <숫자> <정지 때 값> <진행 때 값>)");
        return listLore;
    }
}
