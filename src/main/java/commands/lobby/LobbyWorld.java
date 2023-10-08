package main.java.commands.lobby;

import daybreak.abilitywar.Command;
import daybreak.abilitywar.utils.base.Messager;
import main.java.util.AddonConfig;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class LobbyWorld extends Command {
    protected final AddonConfig config;
    private static final String LOBBYPATH = "lobby";
    public LobbyWorld() {
        super(Condition.OP);
        config = AddonConfig.getConfig("lobbyConfig");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String command, String @NotNull [] args) {
        if (args.length < 1) {
            config.set(LOBBYPATH, null);
            sender.sendMessage("§e성공적으로 로비 월드 지정을 해제했습니다.");
        }
        else {
            World newLobby = Bukkit.getWorld(args[0]);
            if (newLobby == null) {
                Messager.sendErrorMessage(sender, "§6\"" + args[0] + "\" §c월드를 찾을 수 없습니다.");
            } else {
                config.set(LOBBYPATH, newLobby.getName());
                sender.sendMessage("§a로비 월드를 성공적으로 §2" + args[0] + "§a으로 설정했습니다.");
            }
        }
        return true;
    }
}
