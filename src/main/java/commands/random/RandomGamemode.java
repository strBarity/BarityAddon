package main.java.commands.random;

import daybreak.abilitywar.Command;
import daybreak.abilitywar.utils.base.Messager;
import main.java.util.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class RandomGamemode extends Command {
    @Override
    protected boolean onCommand(CommandSender sender, String command, String[] args) {
        if (sender instanceof Player) {
            open((Player) sender);
            return true;
        }
        Messager.sendErrorMessage(sender, "이 명령어는 플레이어만 사용할 수 있습니다.");
        return false;
    }

    private void open(Player p) {
        Inventory gui = Bukkit.createInventory(null, 54, "§8랜덤 게임모드 설정");
        for (int i = 0; i < 9; i++) {
            gui.setItem(i, ItemFactory.blank(ItemFactory.ItemColor.GRAY, false));
        }
        for (int i = 27; i < 36; i++) {
            gui.setItem(i, ItemFactory.createItem(Material.STAINED_GLASS_PANE, 4, " ", Arrays.asList("§6⬆ §a랜덤 게임모드 추첨에서 포함될 게임모드 §6⬆", "§e--------------------------------------------", "§6⬇ §c랜덤 게임모드 추첨에서 제외될 게임모드 §6⬇"), null, 1, true));
        }
        p.openInventory(gui);
    }
}
