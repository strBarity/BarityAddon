package main.java.commands.gamecommand;

import daybreak.abilitywar.AbilityWar;
import daybreak.abilitywar.Command;
import daybreak.abilitywar.utils.base.Messager;
import main.java.util.AddonConfig;
import main.java.util.InventoryUtil;
import main.java.util.ItemColor;
import main.java.util.ItemFactory;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class GameCustomGUI extends Command {
    protected static final AddonConfig config = AddonConfig.getConfig("gameConfig");
    protected static final String IS_ENCHANTING_TABLE_ENABLED = "gui.customEnchantingTable.enabled";
    protected static final String IS_ANVIL_COMMAND_ENABLED = "gui.anvilCommand.enabled";
    protected static final String IS_ENCHANTING_TABLE_COMMAND_ENABLED = "gui.customEnchantingTable.command.enabled";
    protected static final String ENCHANT_CHANCE = "gui.customEnchantingTable.enchants.";
    protected static final int INV_SIZE = 45;
    protected static final String INV_TITLE = "§8커스텀 GUI 설정";
    protected static final int ENABLE_ENCHANTMENT_TABLE_SLOT = 11;
    protected static final int ENABLE_ANVIL_COMMAND_SLOT = 15;
    protected static final int ENABLE_ENCHANTMENT_TABLE_COMMAND_SLOT = 30;
    protected static final int OPEN_ENCHANTMENT_TABLE_EDIT_SLOT = 32;
    private static final String PLAYER_ONLY = "이 명령어는 플레이어만 사용할 수 있습니다.";
    private static final String CURRENT = "§e현재 설정: ";
    private static final String WEIGHT = ".weight";

    public GameCustomGUI() {
        super(Condition.OP);
        resetConfig();
        Command enchantmentTable = new EnchantingTableCommand();
        Command anvil = new AnvilCommand();
        AbilityWar.getPlugin().getCommands().getMainCommand().addSubCommand("enchantmenttable", enchantmentTable);
        AbilityWar.getPlugin().getCommands().getMainCommand().addSubCommand("enchantment", enchantmentTable);
        AbilityWar.getPlugin().getCommands().getMainCommand().addSubCommand("enchantable", enchantmentTable);
        AbilityWar.getPlugin().getCommands().getMainCommand().addSubCommand("enchant", enchantmentTable);
        AbilityWar.getPlugin().getCommands().getMainCommand().addSubCommand("et", enchantmentTable);
        AbilityWar.getPlugin().getCommands().getMainCommand().addSubCommand("anvil", anvil);
        AbilityWar.getPlugin().getCommands().getMainCommand().addSubCommand("av", anvil);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String command, String[] args) {
        if (sender instanceof Player) {
            open((Player) sender);
        } else {
            Messager.sendErrorMessage(sender, PLAYER_ONLY);
        }
        return true;
    }

    protected void open(Player p) {
        Inventory gui = InventoryUtil.blankInv(INV_SIZE, INV_TITLE, ItemColor.WHITE, true);
        for (int i : Arrays.asList(0, 8, 9, 17, 18, 26, 27, 35, 36, 44)) {
            gui.setItem(i, ItemFactory.blank(ItemColor.YELLOW, true));
        }
        gui.setItem(ENABLE_ENCHANTMENT_TABLE_SLOT, ItemFactory.createItem(Material.ENCHANTMENT_TABLE, 0, "§a커스텀 인챈팅 테이블", Arrays.asList("§7커스텀 인챈팅 테이블을 활성화합니다.", "§7설정 시 인챈팅 테이블을 열었을때 기본적으로", "§7커스텀 인챈팅 UI가 열립니다.", "§7기본적으로 책장 레벨을 따르지 않고", "§7청금석을 소모하지 않으며, 인챈트는 30레벨", "§7이상의 경험치가 필요하고 3레벨의 경험치를 소모하는", "§7세 가지의 인챈트 조합이 출현합니다.", "§7인챈트는 미리 볼 수 없습니다.", "§7인챈트들의 세부 확률을 조정 가능하며,", "§7이 기능이 켜져있어야만 명령어를 사용해", "§7인챈팅 테이블을 여는 기능을 사용할 수 있습니다.", "", CURRENT + AddonConfig.boolTranslate((boolean) config.get(IS_ENCHANTING_TABLE_ENABLED))), null, 1, (boolean) config.get(IS_ENCHANTING_TABLE_ENABLED)));
        gui.setItem(ENABLE_ANVIL_COMMAND_SLOT, ItemFactory.createItem(Material.ANVIL, 0, "§a모루 명령어", Arrays.asList("§7모루를 여는 명령어를 활성화합니다.", "§7이 기능을 켜면 플레이어들은 /aw anvil 또는", "§7/aw av를 사용해 모루를 열 수 있습니다.", "", CURRENT + AddonConfig.boolTranslate((boolean) config.get(IS_ANVIL_COMMAND_ENABLED))), null, 1, (boolean) config.get(IS_ANVIL_COMMAND_ENABLED)));
        gui.setItem(ENABLE_ENCHANTMENT_TABLE_COMMAND_SLOT, ItemFactory.createItem(Material.COMMAND, 0, "§a커스텀 인챈팅 테이블 명령어", Arrays.asList("§7커스텀 인챈팅 테이블을 여는 명령어를 활성화합니다.", "§7이 기능을 켜면 플레이어들은 /aw enchantmenttable 또는", "§7/aw enchantment 또는 /aw enchantable 또는 /aw enchant 또는", "§7/aw et를 사용해 커스텀 인챈팅 테이블을 열 수 있습니다.", "§7좀 많지만... /aw et밖에 안 쓸거에요, 아마.", "", CURRENT + AddonConfig.boolTranslate((boolean) config.get(IS_ENCHANTING_TABLE_COMMAND_ENABLED))), null, 1, (boolean) config.get(IS_ENCHANTING_TABLE_COMMAND_ENABLED)));
        gui.setItem(OPEN_ENCHANTMENT_TABLE_EDIT_SLOT, ItemFactory.createItem(Material.BOOK, 0, "§d인챈트 확률 조정 메뉴 열기", Arrays.asList("§7커스텀 인챈팅 테이블에 출현하는", "§7인챈트들의 확률을 조정합니다.", "§7근접 무기, 활, 갑옷, 책에 붙는", "§7모든 종류의 인챈트 확률을", "§7조정할 수 있고, 하나의 아이템에", "§7등장하는 인챈트 수도 조정할 수 있습니다.", "", "§e클릭해서 열기"), null, 1, true));
        p.openInventory(gui);
    }

    private void resetConfig() {
        if (config.get(IS_ENCHANTING_TABLE_ENABLED) == null) {
            config.set(IS_ENCHANTING_TABLE_ENABLED, false);
        }
        if (config.get(IS_ANVIL_COMMAND_ENABLED) == null) {
            config.set(IS_ANVIL_COMMAND_ENABLED, false);
        }
        if (config.get(IS_ENCHANTING_TABLE_COMMAND_ENABLED) == null) {
            config.set(IS_ENCHANTING_TABLE_COMMAND_ENABLED, false);
        }
        resetEnchantChanceConfig();
    }

    private void resetEnchantChanceConfig() {
        int weight = 1;
        for (GUIEnchant enchant : GUIEnchant.getAllEnchants()) {
            String defaultPath = ENCHANT_CHANCE + enchant.name() + ".";
            if (config.get(defaultPath + "mainChance") == null) {
                config.set(defaultPath + "mainChance", 0);
            }
            if (config.get(defaultPath + "totalWeight") == null) {
                config.set(defaultPath + "totalweight", weight);
            }
            weight++;
            for (int i = 1; i <= enchant.getMaxLevel(); i++) {
                if (config.get(defaultPath + "levelChance." + i) == null) {
                    config.set(defaultPath + "levelChance." + i, 0);
                }
            }
        }
        resetArmorEnchantWeights();
        resetMeleeEnchantWeights();
        resetBowEnchantWeights();
        resetToolEnchantWeights();
        resetFishingRodEnchantWeights();
        resetOtherEnchantWeights();
    }
    private void resetOtherEnchantWeights() {
        int otherWeight = 1;
        for (GUIEnchant enchant : GUIEnchant.getOtherEnchants()) {
            String defaultPath = ENCHANT_CHANCE + enchant.name() + WEIGHT;
            if (config.get(defaultPath) == null) {
                config.set(defaultPath, otherWeight);
            }
            otherWeight++;
        }
    }
    private void resetFishingRodEnchantWeights() {
        int fishingRodWeight = 1;
        for (GUIEnchant enchant : GUIEnchant.getFishingRodEnchants()) {
            String defaultPath = ENCHANT_CHANCE + enchant.name() + WEIGHT;
            if (config.get(defaultPath) == null) {
                config.set(defaultPath, fishingRodWeight);
            }
            fishingRodWeight++;
        }
    }
    private void resetToolEnchantWeights() {
        int toolWeight = 1;
        for (GUIEnchant enchant : GUIEnchant.getToolEnchants()) {
            String defaultPath = ENCHANT_CHANCE + enchant.name() + WEIGHT;
            if (config.get(defaultPath) == null) {
                config.set(defaultPath, toolWeight);
            }
            toolWeight++;
        }
    }
    private void resetMeleeEnchantWeights() {
        int meleeWeight = 1;
        for (GUIEnchant enchant : GUIEnchant.getMeleeEnchants()) {
            if (!GUIEnchantBundle.DAMAGE_ENCHANTMENTS.getEnchants().contains(enchant)) {
                meleeWeight++;
            }
            String defaultPath = ENCHANT_CHANCE + enchant.name() + WEIGHT;
            if (config.get(defaultPath) == null) {
                config.set(defaultPath, meleeWeight);
            }
        }
    }
    private void resetBowEnchantWeights() {
        int bowWeight = 1;
        for (GUIEnchant enchant : GUIEnchant.getBowEnchants()) {
            String defaultPath = ENCHANT_CHANCE + enchant.name() + WEIGHT;
            if (config.get(defaultPath) == null) {
                config.set(defaultPath, bowWeight);
            }
            bowWeight++;
        }
    }
    private void resetArmorEnchantWeights() {
        int armorWeight = 1;
        for (GUIEnchant enchant : GUIEnchant.getArmorEnchants()) {
            if (!GUIEnchantBundle.PROTECTION_ENCHANTMENTS.getEnchants().contains(enchant)) {
                armorWeight++;
            }
            String defaultPath = ENCHANT_CHANCE + enchant.name() + WEIGHT;
            if (config.get(defaultPath) == null) {
                config.set(defaultPath, armorWeight);
            }
        }
    }

    protected static class EnchantingTableCommand extends Command {
        private static final int INV_SIZE = 45;
        private static final String INV_TITLE = "§8인챈팅 테이블";

        @Override
        protected boolean onCommand(CommandSender sender, String command, String[] args) {
            if (sender instanceof Player) {
                if ((boolean) config.get(IS_ENCHANTING_TABLE_ENABLED)) {
                    if ((boolean) config.get(IS_ENCHANTING_TABLE_COMMAND_ENABLED)) {
                        open(((Player) sender), true);
                    } else {
                        Messager.sendErrorMessage(sender, "커스텀 인챈팅 테이블 명령어가 비활성화되어 있습니다.");
                    }
                } else {
                    Messager.sendErrorMessage(sender, "커스텀 인챈팅 테이블이 비활성화되어 있습니다.");
                }
            } else {
                Messager.sendErrorMessage(sender, PLAYER_ONLY);
            }
            return true;
        }

        protected static void open(Player p, boolean clearItemSlot) {
            Inventory gui = InventoryUtil.blankInvHorizontalBorder(INV_SIZE, INV_TITLE, ItemColor.BLUE, true, ItemColor.LIGHT_BLUE, true);
            if (clearItemSlot) {
                gui.setItem(19, new ItemStack(Material.AIR));
            }
            gui.setItem(23, ItemFactory.createItem(Material.ENDER_PEARL, 0, "§c아이템 필요", Arrays.asList("§7인챈트를 하기 위해서는", "§7인챈트할 아이템이 필요합니다.", "§7왼쪽 빈칸에 인챈트할 아이템을 넣어주세요."), null, 1, true));
            p.openInventory(gui);
        }
    }

    protected static class AnvilCommand extends Command {
        @Override
        protected boolean onCommand(CommandSender sender, String command, String[] args) {
            if (sender instanceof Player) {
                if ((boolean) config.get(IS_ANVIL_COMMAND_ENABLED)) {
                    open((Player) sender);
                } else {
                    Messager.sendErrorMessage(sender, "모루 명령어가 비활성화되어 있습니다.");
                }
            } else {
                sender.sendMessage(PLAYER_ONLY);
            }
            return true;
        }

        private void open(@NotNull Player p) {
            EntityPlayer player = ((CraftPlayer) p).getHandle();
            Location l = p.getLocation();
            ContainerAnvil anvil = new ContainerAnvil(player.inventory, player.world, new BlockPosition(l.getX(), l.getY(), l.getZ()), player);
            anvil.checkReachable = false;
            int containerId = player.nextContainerCounter();
            player.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerId, "minecraft:anvil", new ChatMessage("모루", new Object()), 0));
            player.activeContainer = anvil;
            player.activeContainer.windowId = containerId;
            player.activeContainer.addSlotListener(player);
        }
    }
}
