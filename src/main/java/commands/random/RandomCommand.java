package main.java.commands.random;

import daybreak.abilitywar.AbilityWar;
import daybreak.abilitywar.Command;
import daybreak.abilitywar.utils.base.Formatter;
import daybreak.abilitywar.utils.base.Messager;
import daybreak.abilitywar.utils.base.math.NumberUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RandomCommand {

    public RandomCommand() {
        Command randomCommand = new Command() {
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
                    s.sendMessage(Formatter.formatTitle(ChatColor.LIGHT_PURPLE, ChatColor.DARK_PURPLE, "랜덤 관련 설정"));
                    s.sendMessage(Formatter.formatCommand(c + " random", "map", "게임 시작 시 맵을 랜덤으로 추첨하는 설정 창을 엽니다.\n §a/aw config gameWorld§e에서 게임용 월드 복사가 켜져있어야 합니다.", true));
                    s.sendMessage(Formatter.formatCommand(c + " random", "gamemode", "게임 시작 시 게임 모드를 랜덤으로 추첨하는 설정 창을 엽니다.", true));
                }
            }
        };
        randomCommand.addSubCommand("gamemode", new RandomGamemode());
        AbilityWar.getPlugin().getCommands().getMainCommand().addSubCommand("random", randomCommand);
    }
}
