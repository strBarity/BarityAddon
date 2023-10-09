package main.java.commands.lobby;

import daybreak.abilitywar.Command;
import daybreak.abilitywar.utils.base.Messager;
import main.java.util.AddonConfig;
import main.java.util.InventoryUtil;
import main.java.util.ItemColor;
import main.java.util.ItemFactory;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;

public class LobbyTablist extends Command {
    protected final AddonConfig config;
    protected static final int INV_SIZE = 36;
    protected static final String DEFAULTPATH = "tablist.";
    protected static final String HEADER = DEFAULTPATH + "header";
    protected static final String FOOTER = DEFAULTPATH + "footer";
    protected static final String PLAYER = DEFAULTPATH + "player";
    protected static final String INV_TITLE = "§8로비 플레이어 목록 설정";

    public LobbyTablist() {
        super(Condition.OP);
        config = AddonConfig.getConfig("lobbyConfig");
        resetConfig();
        Bukkit.getOnlinePlayers().forEach(this::updateTablist);
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
        Inventory gui = InventoryUtil.blankInv(INV_SIZE, INV_TITLE, ItemColor.MAGENTA, true);
        String s = "§e클릭해서 설정하기";
        String c = "§f현재 설정: ";
        String header = ChatColor.translateAlternateColorCodes('&', config.get(HEADER).toString());
        String footer = ChatColor.translateAlternateColorCodes('&', config.get(FOOTER).toString());
        if (header.isEmpty()) {
            header = "§7§o비어 있음";
        }
        if (footer.isEmpty()) {
            footer = "§7§o비어 있음";
        }
        gui.setItem(11, ItemFactory.createItem(Material.IRON_HELMET, 0, "§b목록 상단 설정", Arrays.asList("§a플레이어 목록 상단의 텍스트를 설정합니다.", c + header, "", s), null, 1, false));
        gui.setItem(13, ItemFactory.createItem(Material.SKULL_ITEM, 0, "§b플레이어 이름 포맷 설정", Arrays.asList("§a플레이어 목록에 뜨는 플레이어들의 이름 포맷을 설정합니다.", c + ChatColor.translateAlternateColorCodes('&', config.get(PLAYER).toString()), "", s), null, 1, false));
        gui.setItem(15, ItemFactory.createItem(Material.IRON_BOOTS, 0, "§b목록 상단 설정", Arrays.asList("§a플레이어 목록 하단의 텍스트를 설정합니다.", c + footer, "", s), null, 1, false));
        gui.setItem(22, ItemFactory.createItem(Material.BOOK, 0, "§b사용 가능한 변수 목록", Arrays.asList("§e- $name §8: §b플레이어 이름 §c(필수)", "§e - $emblem §8: §b플레이어 엠블럼"), null, 1, false));
        p.openInventory(gui);
    }

    private void resetConfig() {
        if (config.get(HEADER) == null) {
            config.set(HEADER, "");
        }
        if (config.get(FOOTER) == null) {
            config.set(FOOTER, "");
        }
        if (config.get(PLAYER) == null) {
            config.set(PLAYER, "$name");
        }
    }

    protected void updateTablist(@NotNull Player p) {
        p.setPlayerListName(ChatColor.translateAlternateColorCodes('&', config.get(PLAYER).toString()).replace("$name", p.getName()));

        IChatBaseComponent tabHeader = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', config.get(HEADER).toString()) + "\"}");
        IChatBaseComponent tabFooter = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', config.get(FOOTER).toString()) + "\"}");

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try {
            Field headerField = packet.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, tabHeader);
            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, tabFooter);
        } catch (Exception e) {
            Messager.sendConsoleMessage("플레이어 목록의 상단과 하단 텍스트를 설정하는 도중에 오류가 발생했습니다");
        } finally {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
