package main.java.commands.gamecommand;

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
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class GameSmallFixes extends Command {
    protected final AddonConfig config;
    protected static final int INV_SIZE = 27;
    protected static final String INV_TITLE = "게임 스몰 픽스 설정";
    protected static final String DEFAULTPATH = "smallfixes.";
    protected static final String AUTO_CHECK_WHEN_SET = DEFAULTPATH + "autoCheckWhenSet";
    public GameSmallFixes() {
        super(Condition.OP);
        config = AddonConfig.getConfig("gameConfig");
        resetConfig();
    }
    @Override
    protected boolean onCommand(CommandSender sender, String command, String[] args) {
        if (sender instanceof Player) {
            open((Player) sender);
        }
        else {
            Messager.sendErrorMessage(sender, "이 명령어는 플레이어만 사용할 수 있습니다.");
        }
        return true;
    }
    protected void open(@NotNull Player p) {
        Inventory gui = InventoryUtil.blankInv(INV_SIZE, INV_TITLE, ItemColor.MAGENTA, false);
        applyConfig(gui);
        p.openInventory(gui);
    }
    private void applyConfig(@NotNull Inventory inv) {
        inv.setItem(13, ItemFactory.createItem(Material.NETHER_STAR, 0, "§e능력 변경 직후 능력 자동 확인", Arrays.asList("§7게임 시작 직후 능력이 배정될 때나, ", "§7/aw no 입력 등 능력이 변경될 때", "§7/aw check도 자동으로 입력해줍니다.", "§7어차피 /aw check로 확인해야할 거,", "§7그냥 자동으로 입력해줍니다. 편하겠죠?", "", "§e현재 설정: " + AddonConfig.boolTranslate((boolean) config.get(AUTO_CHECK_WHEN_SET))), null, 1, true));
    }
    private void resetConfig() {
        if (config.get(AUTO_CHECK_WHEN_SET) == null) {
            config.set(AUTO_CHECK_WHEN_SET, false);
        }
    }
}
