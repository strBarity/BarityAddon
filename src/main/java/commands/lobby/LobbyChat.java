package main.java.commands.lobby;

import daybreak.abilitywar.Command;
import daybreak.abilitywar.utils.base.Messager;
import main.java.util.AddonConfig;
import main.java.util.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class LobbyChat extends Command {
    protected AddonConfig config;
    protected static final int INV_SIZE = 45;
    protected static final String CHAT_FORMAT = "chatFormat";
    protected static final String INV_TITLE = "§8로비 채팅 포맷 설정";
    public LobbyChat() {
        super(Condition.OP);
        config = AddonConfig.getConfig("lobbyConfig");
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
    protected void open(Player p) {
        Inventory gui = Bukkit.createInventory(null, INV_SIZE, INV_TITLE);
        for (int i = 0; i < INV_SIZE; i++) {
            gui.setItem(i, ItemFactory.blank(ItemFactory.ItemColor.YELLOW, true));
        }
        gui.setItem(13, ItemFactory.createItem(Material.SIGN, 0, "§a채팅 포맷 설정", Arrays.asList("§f현재 포맷: " + ChatColor.translateAlternateColorCodes('&', config.get(CHAT_FORMAT).toString()), "", "§e클릭해서 변경하기"), null, 1, true));
        gui.setItem(31, ItemFactory.createItem(Material.BOOK, 0, "§a사용 가능한 변수 목록", Arrays.asList("§e- $name §8: §b플레이어 이름 §c(필수)", "§e- $msg §8: §b메시지 §c(필수)"), null, 1, false));
        p.openInventory(gui);
    }

    private void resetConfig() {
        if (config.get(CHAT_FORMAT) == null) {
            config.set(CHAT_FORMAT, "<$name> $msg");
        }
    }
}
