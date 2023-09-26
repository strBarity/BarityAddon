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
                }
            }
        };
        gameCommand.addSubCommand("vote", new GameVote());
        AbilityWar.getPlugin().getCommands().getMainCommand().addSubCommand("game", gameCommand);
    }
}
