package main.java.commands.world;

import daybreak.abilitywar.Command;
import daybreak.abilitywar.utils.base.Messager;
import main.java.util.AddonConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorldAdd extends Command {
    private final AddonConfig config;
    private static final String GAMEWORLDS = "gameWorlds";
    public WorldAdd() {
        config = AddonConfig.getConfig("worldConfig");
    }
    @Override
    protected boolean onCommand(CommandSender sender, String command, String @NotNull [] args) {
        if (!((boolean) config.get("enabled"))) {
            Messager.sendErrorMessage(sender, "게임 전용 월드를 사용하는 기능이 켜져있지 않습니다. /aw world toggle를 사용해 해당 옵션을 활성화해주세요.");
        }
        else if (args.length < 2) {
            if (args.length == 0) {
                Messager.sendErrorMessage(sender, "월드 이름을 입력해주세요.");
            }
            else {
                Messager.sendErrorMessage(sender, "표시 이름을 입력해주세요.");
            }
        }
        else if (Bukkit.getWorld(args[0]) == null) {
            Messager.sendErrorMessage(sender, "\"" + args[0] + "\" 월드를 찾을 수 없습니다.");
        }
        else if (config.getConfig().getStringList(GAMEWORLDS).contains(args[0])) {
            Messager.sendErrorMessage(sender, "\"" + args[0] + "\" 월드는 이미 게임 월드입니다.");
        }
        else {
            List<String> currentWorlds = config.getConfig().getStringList(GAMEWORLDS);
            currentWorlds.add(args[0]);
            sender.sendMessage("§a\"" + args[0] + "\" 월드를 게임 전용 월드로 설정했습니다. (표시 이름: \"§f" + ChatColor.translateAlternateColorCodes('&', args[1]) + "§a\")");
            config.set(GAMEWORLDS, currentWorlds);
            config.set("gameWorldOptions." + args[0] + ".displayName", args[1]);
            Location l = Bukkit.getWorld(args[0]).getSpawnLocation();
            config.set("gameWorldOptions." + args[0] + ".spawnLocation", l.getX() + "," + l.getY() + "," + l.getZ());
        }
        return true;
    }
}
