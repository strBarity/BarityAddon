package main.java.commands.cosmetic;

import daybreak.abilitywar.AbilityWar;
import daybreak.abilitywar.Command;
import daybreak.abilitywar.utils.base.Formatter;
import daybreak.abilitywar.utils.base.Messager;
import daybreak.abilitywar.utils.base.math.NumberUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CosmeticCommand {
    public CosmeticCommand() {
        Command cosmeticCommand = new Command() {
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
                    String w = " cosmetic";
                    s.sendMessage(Formatter.formatTitle(ChatColor.GRAY, ChatColor.WHITE, "치장품 관련 설정"));
                    s.sendMessage(Formatter.formatCommand(c + w, "select", "치장품 메뉴가 열리는 아이템을 설정합니다.", true));
                }
            }
        };
        AbilityWar.getPlugin().getCommands().getMainCommand().addSubCommand("cosmetic", cosmeticCommand);
    }
}
