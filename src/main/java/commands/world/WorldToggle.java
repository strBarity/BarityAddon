package main.java.commands.world;

import daybreak.abilitywar.Command;
import main.java.util.AddonConfig;
import org.bukkit.command.CommandSender;

public class WorldToggle extends Command {
    private final AddonConfig config;
    private static final String ENABLED = "enabled";
    public WorldToggle() {
        super(Condition.OP);
        config = AddonConfig.getConfig("worldConfig");
    }
    @Override
    protected boolean onCommand(CommandSender sender, String command, String[] args) {
        Object value = config.get(ENABLED);
        if (value == null) {
            sender.sendMessage("§e이 기능을 처음 사용하시는 것 같습니다. 이 설정을 활성화하면 다음과 같은 변경 사항이 적용됩니다:\n§f- §b게임 시작 직후 게임 전용 월드가 생성되며, 이 월드는 게임이 끝난 후 §c제거§b됩니다.\n§f- §b게임 시작 시 게임 전용 월드로 이동, 게임이 종료될 시 로비 월드로 이동합니다. 이동 위치는 모두 기본 월드 스폰 위치§7(/setworldspawn)§b입니다.\n§f- §b게임 전용 월드는 설정된 게임 전용 월드 목록에서 하나가 §e추첨§b되어 월드를 통째로 복제합니다.\n§e위의 사항을 모두 확인하였다면 명령어를 다시 입력해주세요.");
            config.set(ENABLED, false);
        }
        else if ((boolean) value) {
            sender.sendMessage("§c게임 전용 월드 기능을 비활성화했습니다.");
            config.set(ENABLED, false);
        }
        else {
            if (AddonConfig.getConfig("lobbyConfig").get("lobby") == null) {
                sender.sendMessage("§c로비 월드가 지정되지 않았습니다. §e/aw lobby world <월드 이름>§c으로 로비 월드를 설정한 뒤 다시 시도해주세요.");
            }
            else {
                sender.sendMessage("§a게임 전용 월드 기능을 활성화했습니다.");
                config.set(ENABLED, true);
            }
        }
        return true;
    }
}
