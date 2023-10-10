package main.java.commands.gamecommand;

import main.java.util.AddonConfig;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum GUIEnchantBundle {
    DAMAGE_ENCHANTMENTS("§c§l공격력 증가 인챈트", "§7근접 무기의 공격력을 증가시키는 인챈트들입니다.", Enchantment.DAMAGE_ALL, Enchantment.DAMAGE_UNDEAD, Enchantment.DAMAGE_ARTHROPODS),
    PROTECTION_ENCHANTMENTS("§b§l보호 인챈트", "§7갑옷의 방어력을 증가시키는 인챈트들입니다.", Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_FIRE, Enchantment.PROTECTION_EXPLOSIONS, Enchantment.PROTECTION_PROJECTILE),
    BOOTS_WATER_ENCHANTMENTS("§9§l신발 물 인챈트", "§7신발의 물과 관련된 인챈트들입니다.", Enchantment.DEPTH_STRIDER, Enchantment.FROST_WALKER);
    private final String displayName;
    private final String description;
    private final List<Enchantment> enchants;
    private static final AddonConfig config = AddonConfig.getConfig("gameConfig");
    private static final String ENCHANT_BUNDLE_PATH = "gui.customEnchantingTable.enchantBundles.";
    private static final String CHANCE = ".chance";
    private static final String WEIGHT = ".weight";
    private static final String TOTAL_WEIGHT = ".totalWeight";
    private static final String CHANCES = ".chances.";
    public boolean isChanceNotDefined() {
        return config.get(ENCHANT_BUNDLE_PATH + this.name() + CHANCE) == null;
    }
    public boolean isWeightNotDefined() {
        return config.get(ENCHANT_BUNDLE_PATH + this.name() + WEIGHT) == null;
    }
    public boolean isTotalWeightNotDefined() {
        return config.get(ENCHANT_BUNDLE_PATH + this.name() + TOTAL_WEIGHT) == null;
    }
    public boolean isEnchantChanceNotDefined(@NotNull GUIEnchant enchant) {
        return config.get(ENCHANT_BUNDLE_PATH + this.name() + CHANCES + enchant.name()) == null;
    }
    public double getChance() {
        return Double.parseDouble(config.get(ENCHANT_BUNDLE_PATH + this.name() + CHANCE).toString());
    }
    public int getWeight() {
        return Integer.parseInt(config.get(ENCHANT_BUNDLE_PATH + this.name() + WEIGHT).toString());
    }
    public int getTotalWeight() {
        return Integer.parseInt(config.get(ENCHANT_BUNDLE_PATH + this.name() + TOTAL_WEIGHT).toString());
    }
    public double getEnchantChance(@NotNull GUIEnchant enchant) {
        return Double.parseDouble(config.get(ENCHANT_BUNDLE_PATH + this.name() + CHANCES + enchant.name()).toString());
    }
    public void chance(double chance) {
        config.set(ENCHANT_BUNDLE_PATH + this.name() + CHANCE, chance);
    }
    public void weight(int weight) {
        config.set(ENCHANT_BUNDLE_PATH + this.name() + WEIGHT, weight);
    }
    public void totalWeight(int totalWeight) {
        config.set(ENCHANT_BUNDLE_PATH + this.name() + TOTAL_WEIGHT, totalWeight);
    }
    public void enchantChance(@NotNull GUIEnchant enchant, double chance) {
        config.set(ENCHANT_BUNDLE_PATH + this.name() + CHANCES + enchant.name(), chance);
    }
    public List<Enchantment> getEnchants() {
        return enchants;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    GUIEnchantBundle(String displayName, String description, Enchantment... enchants) {
        this.displayName = displayName;
        this.description = description;
        this.enchants = Arrays.stream(enchants).collect(Collectors.toList());
    }
}
