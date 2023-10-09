package main.java.commands.item;

import daybreak.abilitywar.AbilityWar;
import daybreak.abilitywar.Command;
import daybreak.abilitywar.utils.base.Formatter;
import daybreak.abilitywar.utils.base.Messager;
import daybreak.abilitywar.utils.base.math.NumberUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ItemCommand {
    public ItemCommand() {
        Command itemCommand = new Command() {
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
                    s.sendMessage(Formatter.formatTitle(ChatColor.DARK_AQUA, ChatColor.AQUA, "아이템 관련 명령어"));
                    s.sendMessage(Formatter.formatCommand(c + " item", "create <Material> <damage> <name> <lore1//lore2//lore3//...> <enchant1/enhant1lvl//enchant2/enchant2lvl//...> <amount> <isShiny>", "§e아이템을 생성합니다. 자세한 설명:\n §bMaterial: §2아이템 종류 §e(LAPIS_BLOCK 등 영어로)\n §bDamage: §2아이템 Damage 값 §e(색깔, 침대 방향 등 설정)\n §bname: §2아이템 이름 §e(역슬래시(\\)는 띄어쓰기로 인식, 색깔 코드(&) 사용 가능)\n §blore: §2아이템 설명 §e(첫번째 줄 텍스트//두번째 줄 텍스트//... 처럼 작성, null 작성 시 무시, 역슬래시(\\)는 띄어쓰기로 인식, 색깔 코드(&) 사용 가능)\n §benchant: §2아이템 인챈트 §e(DURABILITY/3//DAMAGE_ALL/2//... 처럼 작성, null 작성 시 무시)\n §bamount: §2아이템 갯수\n §bisShiny: §2아이템 반짝이 여부 §e(true/false, 인챈트가 있다면 true 고정)", true));
                    s.sendMessage(Formatter.formatCommand(c + " item", "storage", "파일로 저장되는 관리자용 아이템 창고를 엽니다.", true));
                }
            }
        };
        itemCommand.addSubCommand("create", new ItemCreate());
        itemCommand.addSubCommand("storage", new ItemStorage());
        AbilityWar.getPlugin().getCommands().getMainCommand().addSubCommand("item", itemCommand);
    }

}
