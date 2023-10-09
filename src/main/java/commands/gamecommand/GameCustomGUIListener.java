package main.java.commands.gamecommand;

import main.java.util.IntUtil;
import main.java.util.InventoryUtil;
import main.java.util.ItemColor;
import main.java.util.ItemFactory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GameCustomGUIListener extends GameCustomGUI implements Listener {
    private static final int FIRST_INV_SIZE = 45;
    private static final int ENCHANT_EDIT_INV_SIZE = 54;
    private static final String FIRST_INV_TITLE = "§8인챈트 확률 설정 메뉴 - 첫 페이지";
    private static final String ARMOR_INV_TITLE = "§8인챈트 확률 설정 메뉴 - 갑옷";
    private static final int FIRST_ARMOR_SLOT = 20;
    private static final int FIRST_MELEE_SLOT = 22;
    private static final int FIRST_BOW_SLOT = 24;
    private static final ItemStack backToFirstPage = ItemFactory.createItem(Material.ARROW, 0, "§e첫 페이지로 돌아가기", null, null, 1, true);
    private static final String DEFAULTPATH = "gui.customEnchantingTable.enchants.";
    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent e) {
        if (Objects.equals(e.getCurrentItem(), backToFirstPage)) {
            openFirstMenu((Player) e.getWhoClicked());
            return;
        }
        switch (e.getView().getTitle()) {
            case INV_TITLE:
                e.setCancelled(true);
                guiEditMenu((Player) e.getWhoClicked(), e.getRawSlot());
                break;
            case FIRST_INV_TITLE:
                e.setCancelled(true);
                firstMenu((Player) e.getWhoClicked(), e.getRawSlot());
                break;
            default:
                break;
        }
    }

    private void guiEditMenu(Player p, int slot) {
        switch (slot) {
            case ENABLE_ENCHANTMENT_TABLE_SLOT:
                config.set(IS_ENCHANTING_TABLE_ENABLED, !((boolean) config.get(IS_ENCHANTING_TABLE_ENABLED)));
                open(p);
                break;
            case ENABLE_ANVIL_COMMAND_SLOT:
                config.set(IS_ANVIL_COMMAND_ENABLED, !((boolean) config.get(IS_ANVIL_COMMAND_ENABLED)));
                open(p);
                break;
            case ENABLE_ENCHANTMENT_TABLE_COMMAND_SLOT:
                if (!((boolean) config.get(IS_ENCHANTING_TABLE_ENABLED))) {
                    p.sendMessage("§c커스텀 인첸트 테이블이 비활성화되어 있습니다. 활성화 후 사용해주세요.");
                    break;
                }
                config.set(IS_ENCHANTING_TABLE_COMMAND_ENABLED, !((boolean) config.get(IS_ENCHANTING_TABLE_COMMAND_ENABLED)));
                open(p);
                break;
            case OPEN_ENCHANTMENT_TABLE_EDIT_SLOT:
                openFirstMenu(p);
                break;
            default:
                break;
        }
    }

    private void firstMenu(Player p, int slot) {
        switch (slot) {
            case FIRST_ARMOR_SLOT:
                openArmorMenu(p);
                break;
            case FIRST_MELEE_SLOT:
                openMeleeMenu(p);
                break;
            case FIRST_BOW_SLOT:
                openBowMenu(p);
                break;
            default:
                break;
        }
    }
    private void openBowMenu(@NotNull Player p) {
        Inventory gui = InventoryUtil.blankInvFullBorder(ENCHANT_EDIT_INV_SIZE, ARMOR_INV_TITLE, ItemColor.ORANGE, false, ItemColor.RED, true);
        gui.setItem(49, backToFirstPage);
        p.openInventory(gui);
    }
    private void openMeleeMenu(@NotNull Player p) {
        Inventory gui = InventoryUtil.blankInvFullBorder(ENCHANT_EDIT_INV_SIZE, ARMOR_INV_TITLE, ItemColor.LIGHT_BLUE, false, ItemColor.BLUE, true);
        gui.setItem(49, backToFirstPage);
        p.openInventory(gui);
    }
    private void openArmorMenu(@NotNull Player p) {
        Inventory gui = InventoryUtil.blankInvFullBorder(ENCHANT_EDIT_INV_SIZE, ARMOR_INV_TITLE, ItemColor.WHITE, false, ItemColor.YELLOW, true);
        gui.setItem(49, backToFirstPage);
        int i = 0;
        for (GUIEnchant enchant : GUIEnchant.getArmorEnchants()) {
            List<String> lore = new ArrayList<>(Arrays.asList("§7" + enchant.getDescription(), "§e§l기본 출현 확률§f: " + config.get(DEFAULTPATH + enchant.name() + ".mainChance") + "%", "", "§a레벨별 출현 확률:"));
            for (int j = 1; j <= enchant.getMaxLevel(); j++) {
                lore.add("§f - §d" + j + "§5(" + IntUtil.integerToRoman(j) + "§5) §f: " + config.get(DEFAULTPATH + enchant.name() + ".levelChance." + j) + "%");
            }
            gui.setItem(InventoryUtil.getFullBorderedInvSlot(i), ItemFactory.createItem(Material.ENCHANTED_BOOK, 0, "§d" + enchant.getDisplayName(), lore, null, 1, true));
            i++;
        }
        p.openInventory(gui);
    }

    private void openFirstMenu(@NotNull Player p) {
        Inventory gui = InventoryUtil.blankInvVerticalBorder(FIRST_INV_SIZE, FIRST_INV_TITLE, ItemColor.MAGENTA, false, ItemColor.PINK, true);
        gui.setItem(FIRST_ARMOR_SLOT, ItemFactory.createItem(Material.DIAMOND_CHESTPLATE, 0, "§e갑옷 인챈트 확률 조정", Collections.singletonList("§7갑옷에 붙는 인챈트들의 확률을 조정합니다."), null, 1, true));
        gui.setItem(FIRST_MELEE_SLOT, ItemFactory.createItem(Material.DIAMOND_SWORD, 0, "§e근접 무기 인챈트 확률 조정", Collections.singletonList("§7근접 무기에 붙는 인챈트들의 확률을 조정합니다."), null, 1, true));
        gui.setItem(FIRST_BOW_SLOT, ItemFactory.createItem(Material.BOW, 0, "§e활 인챈트 확률 조정", Collections.singletonList("§7활에 붙는 인챈트들의 확률을 조정합니다."), null, 1, true));
        p.openInventory(gui);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if ((boolean) config.get(IS_ENCHANTING_TABLE_ENABLED) && e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND) && e.getClickedBlock() != null && e.getClickedBlock().getType().equals(Material.ENCHANTMENT_TABLE) && e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            e.setCancelled(true);
            EnchantingTableCommand.open(e.getPlayer(), true);
        }
    }
}
