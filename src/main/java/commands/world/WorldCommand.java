package main.java.commands.world;

import daybreak.abilitywar.AbilityWar;
import daybreak.abilitywar.Command;
import daybreak.abilitywar.utils.base.Formatter;
import daybreak.abilitywar.utils.base.Messager;
import daybreak.abilitywar.utils.base.math.NumberUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class WorldCommand {
    public WorldCommand() {
        Command worldCommand = new Command() {
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
                    String w = " world";
                    s.sendMessage(Formatter.formatTitle(ChatColor.DARK_GREEN, ChatColor.GREEN, "월드 설정"));
                    s.sendMessage(" §c대부분의 기능은 §a/aw lobby world§c에서 로비를 지정해야 제대로 작동합니다!");
                    s.sendMessage(Formatter.formatCommand(c + w, "toggle", "게임 전용 월드를 사용하는 기능을 키고 켭니다.", true));
                    s.sendMessage(Formatter.formatCommand(c + w, "add <월드 이름>", "해당 월드를 게임 전용 월드로 추가합니다.", true));
                    s.sendMessage(Formatter.formatCommand(c + w, "remove <월드 이름>", "해당 월드를 게임 전용 월드에서 제거합니다.", true));
                }
            }
        };
        AbilityWar.getPlugin().getCommands().getMainCommand().addSubCommand("world", worldCommand);
    }
}
