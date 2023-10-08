package main.java.commands.world;

import daybreak.abilitywar.Command;
import daybreak.abilitywar.utils.base.Formatter;
import daybreak.abilitywar.utils.base.Messager;
import main.java.util.AddonConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorldList extends Command {
    private final AddonConfig config;
    public WorldList() {
        config = AddonConfig.getConfig("worldConfig");
    }
    @Override
    protected boolean onCommand(@NotNull CommandSender sender, String command, String[] args) {
        if (!((boolean) config.get("enabled"))) {
            Messager.sendErrorMessage(sender, "게임 전용 월드를 사용하는 기능이 켜져있지 않습니다. /aw world toggle를 사용해 해당 옵션을 활성화해주세요.");
        }
        else {
            sender.sendMessage(Formatter.formatTitle(ChatColor.YELLOW, ChatColor.GREEN, "게임 전용 월드 목록"));
            List<String> gameWorldList = config.getConfig().getStringList("gameWorlds");
            if (gameWorldList == null || gameWorldList.isEmpty()) {
                sender.sendMessage("");
                sender.sendMessage("§8                              비어 있음");
                sender.sendMessage("");
            } else {
                for (String s : gameWorldList) {
                    String[] coords = config.get("gameWorldOptions." + s + ".spawnLocation").toString().split(",");
                    String x = coords[0];
                    String y = coords[1];
                    String z = coords[2];
                    sender.sendMessage("§f- §2이름: §e" + s + "§b | §d표시 이름: §a" + ChatColor.translateAlternateColorCodes('&', config.get("gameWorldOptions." + s + ".displayName").toString()) + "§b | §6스폰 위치: §9(§e" + x + "§a, §e" + y + "§a, §e" + z + "§9)");
                }
            }
        }
        return true;
    }
}
