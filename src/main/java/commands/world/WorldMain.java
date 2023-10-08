package main.java.commands.world;

import daybreak.abilitywar.Command;
import daybreak.abilitywar.utils.base.Messager;
import main.java.util.AddonConfig;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class WorldMain extends Command {
    private final AddonConfig config;
    private static final String GAMEWORLDNAME = "gameWorldName";
    public WorldMain() {
        config = AddonConfig.getConfig("worldConfig");
        if (config.get(GAMEWORLDNAME) == null) {
            config.set(GAMEWORLDNAME, "main");
        }
    }
    @Override
    protected boolean onCommand(CommandSender sender, String command, String @NotNull [] args) {
        if (args.length < 1) {
            Messager.sendErrorMessage(sender, "생성될 게임 전용 월드의 이름을 입력해주세요.");
        }
        else if (!args[0].matches("^[a-z_]{1,16}$")) {
            Messager.sendErrorMessage(sender, "월드의 이름은 영어 알파벳 소문자와 언더바만 포함할 수 있습니다.");
        }
        else {
            config.set(GAMEWORLDNAME, args[0]);
            sender.sendMessage("§a성공적으로 게임 전용 월드의 생성 이름을 §e" + args[0] + "§a으로 설정했습니다.");
        }
        return true;
    }
}
