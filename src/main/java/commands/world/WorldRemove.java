package main.java.commands.world;

import daybreak.abilitywar.Command;
import daybreak.abilitywar.utils.base.Messager;
import main.java.util.AddonConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorldRemove extends Command {
    private final AddonConfig config;
    private static final String GAMEWORLDS = "gameWorlds";
    public WorldRemove() {
        super(Condition.OP);
        config = AddonConfig.getConfig("worldConfig");
    }
    @Override
    protected boolean onCommand(CommandSender sender, String command, String @NotNull [] args) {
        if (!((boolean) config.get("enabled"))) {
            Messager.sendErrorMessage(sender, "게임 전용 월드를 사용하는 기능이 켜져있지 않습니다. /aw world toggle를 사용해 해당 옵션을 활성화해주세요.");
        }
        else if (args.length < 1) {
            Messager.sendErrorMessage(sender, "월드 이름을 입력해주세요.");
        }
        else if (Bukkit.getWorld(args[0]) == null) {
            Messager.sendErrorMessage(sender, "\"" + args[0] + "\" 월드를 찾을 수 없습니다.");
        }
        else if (!config.getConfig().getStringList(GAMEWORLDS).contains(args[0])) {
            Messager.sendErrorMessage(sender, "\"" + args[0] + "\" 월드는 이미 게임 월드가 아닙니다.");
        }
        else {
            List<String> currentWorlds = config.getConfig().getStringList(GAMEWORLDS);
            currentWorlds.remove(args[0]);
            sender.sendMessage("§a\"" + args[0] + "\" 월드를 게임 전용 월드에서 제거했습니다.");
            config.set(GAMEWORLDS, currentWorlds);
            config.set("gameWorldOptions." + args[0], null);
        }
        return true;
    }
}
