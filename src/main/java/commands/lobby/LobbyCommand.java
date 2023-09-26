package main.java.commands.lobby;

import daybreak.abilitywar.AbilityWar;
import daybreak.abilitywar.Command;
import daybreak.abilitywar.utils.base.Formatter;
import daybreak.abilitywar.utils.base.Messager;
import daybreak.abilitywar.utils.base.math.NumberUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class LobbyCommand {
    public LobbyCommand() {
        Command lobbyCommand = new Command() {
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
                    String l = " lobby";
                    s.sendMessage(Formatter.formatTitle(ChatColor.RED, ChatColor.LIGHT_PURPLE, "게임 대기실(로비) 설정"));
                    s.sendMessage(Formatter.formatCommand(c + l, "world <월드 이름>", "로비 월드를 지정합니다. 빈칸 입력시 월드 지정을 해제합니다.", true));
                    s.sendMessage(Formatter.formatCommand(c + l, "blocked", "게임 대기 시간 동안 플레이어가 금지할 행동을 설정합니다.", true));
                    s.sendMessage(Formatter.formatCommand(c + l, "hotbar", "게임 대기 시간 동안 플레이어의 핫바를 설정합니다.", true));
                    s.sendMessage(Formatter.formatCommand(c + l, "scoreboard", "게임 대기 시간 동안 플레이어의 스코어보드를 설정합니다.", true));
                    s.sendMessage(Formatter.formatCommand(c + l, "chat", "게임 대기 시간 동안 플레이어들의 채팅 포맷을 설정하는 창을 엽니다.", true));
                    s.sendMessage(Formatter.formatCommand(c + l, "tablist", "게임 대기 시간 동안 플레이어들의 플레이어 목록 창을 편집하는 창을 엽니다.", true));
                }
            }
        };
        lobbyCommand.addSubCommand("world", new LobbyWorld());
        lobbyCommand.addSubCommand("blocked", new LobbyBlocked());
        lobbyCommand.addSubCommand("hotbar", new LobbyHotbar());
        lobbyCommand.addSubCommand("scoreboard", new LobbyScoreboard());
        lobbyCommand.addSubCommand("chat", new LobbyChat());
        lobbyCommand.addSubCommand("tablist", new LobbyTablist());
        AbilityWar.getPlugin().getCommands().getMainCommand().addSubCommand("lobby", lobbyCommand);
    }

}
