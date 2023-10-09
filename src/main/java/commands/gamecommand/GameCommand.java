package main.java.commands.gamecommand;

import daybreak.abilitywar.AbilityWar;
import daybreak.abilitywar.Command;
import daybreak.abilitywar.utils.base.Formatter;
import daybreak.abilitywar.utils.base.Messager;
import daybreak.abilitywar.utils.base.math.NumberUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class GameCommand {
    public GameCommand() {
        Command gameCommand = new Command() {
            @Override
            protected boolean onCommand(CommandSender sender, String command, String @NotNull [] args) {
                if (args.length > 0) {
                    if (NumberUtil.isInt(args[0])) {
                        send(sender, command, Integer.parseInt(args[0]));
                    } else {
                        Messager.sendErrorMessage(sender, "§c존재하지 않는 페이지입니다.");
                    }
                } else {
                    send(sender, command, 1);
                }
                return true;
            }
            private void send(CommandSender s, String c, int i) {
                if (i == 1) {
                    String w = " game";
                    s.sendMessage(Formatter.formatTitle(ChatColor.GOLD, ChatColor.YELLOW, "게임 관련 설정"));
                    s.sendMessage(Formatter.formatCommand(c + w, "vote", "게임 시작 투표 관련 기능 창을 엽니다.", true));
                    s.sendMessage(Formatter.formatCommand(c + w, "smallfixes", "뭔가 새벽(AbilityWars 제작자)님에게는 말하기에는 너무 사소한... 개인적으로 추가했으면 하는 인게임에서의 작은 픽스들을 모아봤습니다... 기본값은 모두 꺼짐이며 키고 안키고는 자유입니다.", true));
                    s.sendMessage(Formatter.formatCommand(c + w, "scoreboard", "게임 도중 플레이어의 스코어보드를 설정합니다.", true));
                    s.sendMessage(Formatter.formatCommand(c + w, "customgui", "인챈팅 테이블/모루 커맨드 및 커스텀 인챈팅 테이블 관련 설정 창을 엽니다.", true));
                }
            }
        };
        gameCommand.addSubCommand("vote", new GameVote());
        gameCommand.addSubCommand("smallfixes", new GameSmallFixes());
        gameCommand.addSubCommand("scoreboard", new GameScoreboard());
        gameCommand.addSubCommand("customgui", new GameCustomGUI());
        AbilityWar.getPlugin().getCommands().getMainCommand().addSubCommand("game", gameCommand);
    }
}
