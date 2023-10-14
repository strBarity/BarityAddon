package main.java.commands.gamecommand;

import main.java.util.AddonConfig;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum GUIEnchantSlot {
    ARMOR("갑옷"),
    HELMET("헬멧"),
    BOOTS("신발"),
    MELEE("근접 무기"),
    BOW("활"),
    TOOL("도구"),
    FISHING_ROD("낚싯대"),
    BOOK("책"),
    ALL("모든 장비");
    private static final AddonConfig config = AddonConfig.getConfig("gameConfig");
    private final String displayName;
    private static final String DEFAULTPATH = "gui.customEnchantingTable.amountChances.";
    public boolean isAmountChanceNotDefined() {
        return config.get(DEFAULTPATH + this.name()) == null;
    }
    public String getDisplayName() {
        return displayName;
    }
    public double getAmountChance(int amount) {
        return Double.parseDouble(config.get(DEFAULTPATH + this.name() + "." + amount).toString());
    }
    public void amountChance(int amount, double chance) {
        config.set(DEFAULTPATH + this.name() + "." + amount, chance);
    }

    @Contract(pure = true)
    public static @NotNull List<GUIEnchantSlot> getCommonSlots() {
        return Arrays.asList(ARMOR, MELEE, BOW, TOOL, FISHING_ROD);
    }
    public int getEnchantsAmount() {
        int amount = 0;
        if (this.equals(BOOK)) {
            amount = GUIEnchant.values().length;
        }
        else {
            amount += GUIEnchantBundle.getFromSlot(this).size();
            List<GUIEnchant> enchants = new ArrayList<>();
            enchants.addAll(GUIEnchant.getFromSlot(this));
            enchants.addAll(GUIEnchant.getFromSlot(GUIEnchantSlot.ALL));
            for (GUIEnchant e : enchants) {
                if (e.getBundle() == null) {
                    amount++;
                }
            }
        }
        return Math.min(amount, 7);
    }

    GUIEnchantSlot(String displayName) {
        this.displayName = displayName;
    }
}
