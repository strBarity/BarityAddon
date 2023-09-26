package main.java.commands.item;

import daybreak.abilitywar.Command;
import daybreak.abilitywar.utils.base.Messager;
import main.java.util.AddonConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemStorage extends Command  {
    protected final AddonConfig config;
    protected static final String DEFAULTPATH = "storage.";
    public ItemStorage() {
        super(Condition.OP);
        config = AddonConfig.getConfig("itemStorage");
        resetConfig();
    }
    @Override
    protected boolean onCommand(CommandSender sender, String command, String[] args) {
        if (sender instanceof Player) {
            open((Player) sender);
            return true;
        }
        Messager.sendErrorMessage(sender, "이 명령어는 플레이어만 사용할 수 있습니다.");
        return false;
    }

    @SuppressWarnings("unchecked")
    private void open(@NotNull Player p) {
        Inventory gui = Bukkit.createInventory(null, config.getConfig().getInt(DEFAULTPATH + "size"), (String) config.get(DEFAULTPATH + "title"));
        ItemStack[] content = ((List<ItemStack>) config.get(DEFAULTPATH + "content")).toArray(new ItemStack[0]);
        gui.setContents(content);
        p.openInventory(gui);
    }

    private void resetConfig() {
        if (config.get(DEFAULTPATH) == null) {
            config.set(DEFAULTPATH + "size", 54);
            config.set(DEFAULTPATH + "title", "§8관리자 전용 아이템 창고");
            config.set(DEFAULTPATH + "content", new ItemStack[]{});
        }
    }
}
