package main.java.commands.gamecommand;

import main.java.util.IntUtil;
import main.java.util.InventoryUtil;
import main.java.util.ItemColor;
import main.java.util.ItemFactory;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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
    private static final int FIRST_INV_SIZE = 54;
    private static final int ENCHANT_EDIT_INV_SIZE = 54;
    private static final String FIRST_INV_TITLE = "§8인챈트 확률 설정 메뉴 - 첫 페이지";
    private static final String SECOND_INV_TITLE = "§8인챈트 확률 탭 - ";
    private static final int FIRST_ARMOR_SLOT = 11;
    private static final int FIRST_MELEE_SLOT = 13;
    private static final int FIRST_BOW_SLOT = 15;
    private static final int FIRST_TOOL_SLOT = 29;
    private static final int FIRST_FISHING_ROD_SLOT = 31;
    private static final int FIRST_BOOK_SLOT = 33;
    private static final int FIRST_GUIDE_SLOT = 49;
    private static final ItemStack backToFirstPage = ItemFactory.createItem(Material.ARROW, 0, "§e첫 페이지로 돌아가기", null, null, 1, true);
    private static final String AVAILABLE_ITEMS = "§b적용 가능한 아이템: §9";
    private static final String WEIGHT_LORE = "§a§l우선순위§f: ";
    private static final String CHANCE_LORE = "§e§l출현 확률§f: ";
    private static final String LIST_PREFIX = "§f - §d";

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
        if (e.getView().getTitle().contains(SECOND_INV_TITLE)) {
            e.setCancelled(true);
            guiEnchantMenu((Player) e.getWhoClicked(), e.getRawSlot());
        }
    }

    private void guiEnchantMenu(Player p, int slot) {
        // 인챈트 클릭 만들기
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
                openMenu(p, GUIEnchantSlot.ARMOR, ItemColor.ORANGE, ItemColor.RED);
                break;
            case FIRST_MELEE_SLOT:
                openMenu(p, GUIEnchantSlot.MELEE, ItemColor.LIGHT_BLUE, ItemColor.BLUE);
                break;
            case FIRST_BOW_SLOT:
                openMenu(p, GUIEnchantSlot.BOW, ItemColor.WHITE, ItemColor.YELLOW);
                break;
            case FIRST_TOOL_SLOT:
                openMenu(p, GUIEnchantSlot.TOOL, ItemColor.GRAY, ItemColor.BLACK);
                break;
            case FIRST_FISHING_ROD_SLOT:
                openMenu(p, GUIEnchantSlot.FISHING_ROD, ItemColor.LIME, ItemColor.GREEN);
                break;
            case FIRST_BOOK_SLOT:
                openMenu(p, GUIEnchantSlot.BOOK, ItemColor.MAGENTA, ItemColor.PURPLE);
                break;
            default:
                break;
        }
    }

    private void openMenu(@NotNull Player p, @NotNull GUIEnchantSlot slot, ItemColor mainBlankColor, ItemColor borderColor) {
        Inventory gui = InventoryUtil.blankInvFullBorder(ENCHANT_EDIT_INV_SIZE, SECOND_INV_TITLE + slot.getDisplayName(), mainBlankColor, false, borderColor, true);
        gui.setItem(49, backToFirstPage);
        getEnchants(gui, slot);
        p.openInventory(gui);
    }

    private void getEnchants(Inventory gui, GUIEnchantSlot slot) {
        List<GUIEnchant> enchants = new ArrayList<>();
        if (slot != GUIEnchantSlot.BOOK) {
            enchants.addAll(GUIEnchant.getFromSlot(slot));
            enchants.addAll(GUIEnchant.getFromSlot(GUIEnchantSlot.ALL));
            for (GUIEnchant enchant : enchants) {
                if (isBundled(enchant)) {
                    getBundleEnchant(gui, enchant.getBundle(), false);
                } else {
                    getNormalEnchant(gui, slot, enchant, false);
                }
            }
        } else {
            for (GUIEnchant enchant : GUIEnchant.getFromSlot(GUIEnchantSlot.BOOK)) {
                if (isBundled(enchant)) {
                    getBundleEnchant(gui, enchant.getBundle(), true);
                } else {
                    getNormalEnchant(gui, slot, enchant, true);
                }
            }
        }
    }

    private void getNormalEnchant(Inventory gui, GUIEnchantSlot slot, @NotNull GUIEnchant enchant, boolean all) {
        int weight;
        if (enchant.getSlot().equals(GUIEnchantSlot.ALL)) {
            if (all) {
                weight = Integer.parseInt(Integer.toString(enchant.getTotalWeight()));
            } else {
                weight = Integer.parseInt(Integer.toString(enchant.getSlotWeight(slot)));
            }
        } else {
            if (all) {
                weight = Integer.parseInt(Integer.toString(enchant.getTotalWeight()));
            } else {
                weight = Integer.parseInt(Integer.toString(enchant.getWeight()));
            }
        }
        List<String> lore = new ArrayList<>(Arrays.asList(AVAILABLE_ITEMS + enchant.getSlot().getDisplayName(), "§7" + enchant.getDescription(), "", WEIGHT_LORE + weight, CHANCE_LORE + enchant.getChance() + "%"));
        if (enchant.getMaxLevel() != 1) {
            lore.add("");
            lore.add("§a인챈트 레벨 확률§f: ");
            for (int j = 1; j <= enchant.getMaxLevel(); j++) {
                lore.add(LIST_PREFIX + j + "§5(" + IntUtil.integerToRoman(j) + "§5) §f: " + enchant.getLevelChance(j) + "%");
            }
        }
        gui.setItem(InventoryUtil.getFullBorderedInvSlot(weight - 1), ItemFactory.createItem(Material.ENCHANTED_BOOK, 0, "§d" + enchant.getDisplayName(), lore, null, 1, true));
    }

    private void getBundleEnchant(Inventory gui, @NotNull GUIEnchantBundle bundle, boolean all) {
        int weight;
        if (all) {
            weight = Integer.parseInt(Integer.toString(bundle.getTotalWeight()));
        } else {
            weight = Integer.parseInt(Integer.toString(bundle.getWeight()));
        }
        List<String> lore = new ArrayList<>(Arrays.asList(AVAILABLE_ITEMS + GUIEnchant.getFromBukkitEnchantment(bundle.getEnchants().get(0)).getSlot().getDisplayName(), bundle.getDescription(), "", WEIGHT_LORE + weight, CHANCE_LORE + bundle.getChance() + "%", "", "§6결정될 인챈트 확률§f:"));
        for (Enchantment enchantment : bundle.getEnchants()) {
            GUIEnchant e = GUIEnchant.getFromBukkitEnchantment(enchantment);
            lore.add(LIST_PREFIX + e.getDisplayName() + " §f: " + bundle.getEnchantChance(e) + "%");
        }
        gui.setItem(InventoryUtil.getFullBorderedInvSlot(weight - 1), ItemFactory.createItem(Material.ENCHANTED_BOOK, 0, bundle.getDisplayName(), lore, null, 1, true));
    }

    private void openFirstMenu(@NotNull Player p) {
        Inventory gui = InventoryUtil.blankInvVerticalBorder(FIRST_INV_SIZE, FIRST_INV_TITLE, ItemColor.MAGENTA, false, ItemColor.PINK, true);
        gui.setItem(FIRST_ARMOR_SLOT, ItemFactory.createItem(Material.DIAMOND_CHESTPLATE, 0, "§e갑옷 인챈트 확률 조정", Collections.singletonList("§7갑옷에 붙는 인챈트들의 확률을 조정합니다."), null, 1, true));
        gui.setItem(FIRST_MELEE_SLOT, ItemFactory.createItem(Material.DIAMOND_SWORD, 0, "§e근접 무기 인챈트 확률 조정", Collections.singletonList("§7근접 무기에 붙는 인챈트들의 확률을 조정합니다."), null, 1, true));
        gui.setItem(FIRST_BOW_SLOT, ItemFactory.createItem(Material.BOW, 0, "§e활 인챈트 확률 조정", Collections.singletonList("§7활에 붙는 인챈트들의 확률을 조정합니다."), null, 1, true));
        gui.setItem(FIRST_TOOL_SLOT, ItemFactory.createItem(Material.DIAMOND_PICKAXE, 0, "§e도구 인챈트 확률 조정", Collections.singletonList("§7도구에 붙는 인챈트들의 확률을 조정합니다."), null, 1, true));
        gui.setItem(FIRST_FISHING_ROD_SLOT, ItemFactory.createItem(Material.FISHING_ROD, 0, "§e낚싯대 인챈트 확률 조정", Collections.singletonList("§7낚싯대에 붙는 인챈트들의 확률을 조정합니다."), null, 1, true));
        gui.setItem(FIRST_BOOK_SLOT, ItemFactory.createItem(Material.ENCHANTED_BOOK, 0, "§e일반 책 인챈트 확률 조정", Collections.singletonList("§7일반 책에 붙는 인챈트들의 확률을 조정합니다."), null, 1, true));
        gui.setItem(FIRST_GUIDE_SLOT, ItemFactory.createItem(Material.BOOK_AND_QUILL, 0, "§d인챈트 확률 조정 방법 및 인챈트 시스템 로직 설명", Arrays.asList("§d인챈트 확률 조정하는 법", "§7확률을 조정하는 방법은 §b원하는 탭§7에 들어가,", "§7원하는 인챈트의 §e우선 순위§7는 §c좌클릭(⬇)§7/§b우클릭(⬆)", "§7으로 조절할 수 있고, §bQ §7키를 눌러 확률을 §c0%§7로", "§7만들어 §4제외§7시킬 수도 있으며, §2SHIFT 클릭§7으로 §f자세한 확률§7을", "§7조정 가능한 메뉴가 열립니다. 메뉴에서는", "§a출현 확률§7, §a레벨별 확률§7이 있는데, 모두", "§c좌클릭(⬇)§7/§b우클릭(⬆)§7으로 확률을 §c1§7%씩 조절, ", "§2SHIFT §c좌클릭(⬇)§7/§b우클릭(⬆)§7으로 확률을 §c5§7%씩 조절, ", "§bQ§7 키를 누른 후 §e채팅창§7으로 §d직접 확률 입력§7이 가능합니다.", "", "§5인챈트 시스템 로직", "§7음... 이 부분에 대해서는 정말 고민을 많이 했습니다.", "§7그렇게 결정된 시스템은 다음과 같습니다.", "", "§e0. §7인챈트는 기본적으로 게임을 시작할 때 모든", " §7플레이어에 대해 굴려진다.", "§e1. §7가장 먼저 §d아이템의 인챈트 갯수§7가 결정된다.", "§e2. §7이후 §a우선순위§7가 §c높은§8(1부터 시작) §c인챈트§7부터 굴려진다.", "§e3. §7만약 인챈트가 뽑혔다면 인챈트 목록에 추가된다.", "§e4. §7이와 같은 방법으로, 인챈트 목록의 인챈트 갯수가 처음에 굴려진", "§7 인챈트 갯수를 채울 때까지 계속 굴린다.", "§8(만약 끝까지 갔다면 처음부터 다시 굴린다)", "§e5. §7그렇게 결정된 인챈트들은 플레이어 정보에 저장된 후,", "§7플레이어가 인챈팅 테이블을 확인할 때마다 표시된다.", "§e6. §7이 인챈트 중 목록에서 하나를 뽑아 미리보기로 표시한다.", "§e7. §7인챈트를 고르면 플레이어에게 저장된 모든 인챈트가 다시 굴려진다.", "", "§7이렇게 되면 확률을 정할 때 특정 현상이 나타날 수 있습니다.", "§7예를 들어, §e모든 인챈트들의 확률§7이 §e높은 편인 경우§7,", "§7우선순위가 낮은 인챈트가 나올 확률은 §d엄청나게 줄어듭니다.", "§7반대로, §e모든 인챈트의 확률§7이 §b낮은 편인 경우§7,", "§7우선순위가 낮은 인챈트가 나올 확률은 §5약간 상승합니다.", "§7또한, 모든 인챈트의 확률이 §4매우 낮은 편§7이라면,", "§7모든 인챈트가 잘 안 뽑혀 §6인챈트를 굴리는 시간이 길어지고§7, 결국", "§4서버에 렉을 유발§7시키는 원인이 될 수 있습니다.", "§7이와 같은 로직을 고려하여, 인챈트 확률은 §b현명하게 설정해주세요."), null, 1, true));
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
