package main.java.commands.lobby;

import daybreak.abilitywar.Command;
import daybreak.abilitywar.utils.base.Messager;
import main.java.util.AddonConfig;
import main.java.util.InventoryUtil;
import main.java.util.ItemColor;
import main.java.util.ItemFactory;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class LobbyHotbar extends Command {

    protected final AddonConfig config;
    protected static final int INV_SIZE = 36;
    protected static final String INV_TITLE = "§8게임 시작 전 핫바 설정";
    protected static final String DEFAULTPATH = "hotbarItems.";
    protected static final ItemStack hotbarSign = ItemFactory.createItem(Material.SIGN, 0, "§a게임 시작 전 핫바 설정하기", Arrays.asList("§7아래에 아이템을 넣어 핫바를 설정할 수 있습니다.", "§7아이템이 하나라도 있다면, 아래 슬롯을 클릭할 때마다", "§7모든 플레이어의 핫바는 즉시 아래와 같이 업데이트됩니다.", "§2/aw lobby blocked§e에서 인벤토리 클릭을 끄는게 좋습니다."), null, 1, true);

    public LobbyHotbar() {
        super(Condition.OP);
        config = AddonConfig.getConfig("lobbyConfig");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String command, String[] args) {
        if (sender instanceof Player) {
            open((Player) sender);
        } else {
            Messager.sendErrorMessage(sender, "이 명령어는 플레이어만 사용할 수 있습니다.");
        }
        return true;
    }

    protected void open(Player p) {
        Inventory gui = InventoryUtil.blankInv(INV_SIZE, INV_TITLE, ItemColor.LIGHT_BLUE, false);
        gui.setItem(4, hotbarSign);
        applyConfig(gui);
        p.openInventory(gui);
    }

    private void applyConfig(Inventory inv) {
        for (int i = 0; i < 9; i++) {
            ItemStack item = config.getConfig().getItemStack(DEFAULTPATH + i);
            if (item == null) {
                inv.setItem(i + 18, new ItemStack(Material.AIR));
            }
            else {
                inv.setItem(i + 18, item);
            }
        }
    }
}
