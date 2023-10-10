package main.java.commands.gamecommand;

import main.java.util.AddonConfig;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public enum GUIEnchant {
    PROTECTION(Enchantment.PROTECTION_ENVIRONMENTAL, "보호", GUIEnchantSlot.ARMOR, "방어력이 증가해 받는 피해가 감소합니다.", 4, GUIEnchantBundle.PROTECTION_ENCHANTMENTS, Enchantment.PROTECTION_FIRE, Enchantment.PROTECTION_EXPLOSIONS, Enchantment.PROTECTION_PROJECTILE),
    FIRE_PROTECTION(Enchantment.PROTECTION_FIRE, "화염으로부터 보호", GUIEnchantSlot.ARMOR, "불/용암에 대한 방어력이 증가해 받는 불/용암 피해가 감소합니다.", 4, GUIEnchantBundle.PROTECTION_ENCHANTMENTS, Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_EXPLOSIONS, Enchantment.PROTECTION_PROJECTILE),
    PROJECTILE_PROTECTION(Enchantment.PROTECTION_PROJECTILE, "발사체로부터 보호", GUIEnchantSlot.ARMOR, "발사체(화살)에 대한 방어력이 증가해 받는 발사체 피해가 감소합니다.", 4, GUIEnchantBundle.PROTECTION_ENCHANTMENTS, Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_FIRE, Enchantment.PROTECTION_EXPLOSIONS),
    BLAST_PROTECTION(Enchantment.PROTECTION_EXPLOSIONS, "폭발로부터 보호", GUIEnchantSlot.ARMOR, "폭발에 대한 방어력이 증가해 받는 폭발 피해가 감소합니다.", 4, GUIEnchantBundle.PROTECTION_ENCHANTMENTS, Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_FIRE, Enchantment.PROTECTION_PROJECTILE),
    THORNS(Enchantment.THORNS, "가시", GUIEnchantSlot.ARMOR, "공격을 받을 때마다 공격한 적에게 일정 확률로 일정량의 피해를 입힙니다.", 3),
    DEPTH_STRIDER(Enchantment.DEPTH_STRIDER, "물갈퀴", GUIEnchantSlot.BOOTS, "물 속에서의 이동속도 감소를 상쇄시킵니다.", 3, GUIEnchantBundle.BOOTS_WATER_ENCHANTMENTS, Enchantment.FROST_WALKER),
    FROST_WALKER(Enchantment.FROST_WALKER, "차가운 걸음", GUIEnchantSlot.BOOTS, "근처 물 블록을 살얼음으로 만들고, 마그마 블록 위에서 피해를 입지 않습니다.", 2, GUIEnchantBundle.PROTECTION_ENCHANTMENTS, Enchantment.DEPTH_STRIDER),
    FEATHER_FALLING(Enchantment.PROTECTION_FALL, "가벼운 착지", GUIEnchantSlot.BOOTS, "낙하 피해를 감소시킵니다.", 4),
    RESPIRATION(Enchantment.OXYGEN, "호흡", GUIEnchantSlot.HELMET, "수중 호흡 시간이 증가하며 일정 확률로 익사 피해를 무효화합니다.", 3),
    AQUA_AFFINITY(Enchantment.WATER_WORKER, "친수성", GUIEnchantSlot.HELMET, "물 속에서의 이동 속도 감소를 상쇄시킵니다.", 1),
    SHARPNESS(Enchantment.DAMAGE_ALL, "날카로움", GUIEnchantSlot.MELEE, "공격력이 증가해 주는 피해를 증가시킵니다.", 5, GUIEnchantBundle.DAMAGE_ENCHANTMENTS, Enchantment.DAMAGE_UNDEAD, Enchantment.DAMAGE_ARTHROPODS),
    SMITE(Enchantment.DAMAGE_UNDEAD, "강타", GUIEnchantSlot.MELEE, "언데드(좀비, 스켈레톤 등) 몹에 대한 공격력이 크게 증가합니다.", 5, GUIEnchantBundle.DAMAGE_ENCHANTMENTS, Enchantment.DAMAGE_ALL, Enchantment.DAMAGE_ARTHROPODS),
    BANE_OF_ARTHROPODS(Enchantment.DAMAGE_ARTHROPODS, "살충", GUIEnchantSlot.MELEE, "벌레(거미, 좀벌레 등) 몹에 대한 공격력이 크게 증가하고, 공격 시 잠깐의 속도 감소를 부여합니다.", 5, GUIEnchantBundle.DAMAGE_ENCHANTMENTS, Enchantment.DAMAGE_ALL, Enchantment.DAMAGE_UNDEAD),
    KNOCKBACK(Enchantment.KNOCKBACK, "밀치기", GUIEnchantSlot.MELEE, "공격한 대상이 밀려나는 거리가 크게 증가합니다.", 2),
    FIRE_ASPECT(Enchantment.FIRE_ASPECT, "발화", GUIEnchantSlot.MELEE, "공격한 대상에게 일정 시간 동안 불이 붙습니다.", 2),
    LOOTING(Enchantment.LOOT_BONUS_MOBS, "약탈", GUIEnchantSlot.MELEE, "몹을 죽였을 때 드롭되는 아이템의 확률과 갯수가 증가합니다.", 3),
    SWEEPING_EDGE(Enchantment.SWEEPING_EDGE, "휩쓸기", GUIEnchantSlot.MELEE, "휩쓸기 공격의 공격력이 크게 증가합니다.", 3),
    EFFICIENCY(Enchantment.DIG_SPEED, "효율", GUIEnchantSlot.TOOL, "블럭을 캐는 속도가 증가합니다.", 5),
    SILK_TOUCH(Enchantment.SILK_TOUCH, "섬세한 손길", GUIEnchantSlot.TOOL, "일반적인 방법으로는 얻을 수 없는 블록을 얻을 수 있습니다.", 1),
    FORTUNE(Enchantment.LOOT_BONUS_BLOCKS, "행운", GUIEnchantSlot.TOOL, "블럭을 파괴했을 때 드롭되는 아이템의 확률과 갯수가 증가합니다.", 3),
    POWER(Enchantment.ARROW_DAMAGE, "힘", GUIEnchantSlot.BOW, "화살의 공격력이 증가합니다.", 5),
    PUNCH(Enchantment.ARROW_KNOCKBACK, "밀어내기", GUIEnchantSlot.BOW, "화살을 맞은 대상이 밀려나는 거리가 증가합니다.", 2),
    FLAME(Enchantment.ARROW_FIRE, "화염", GUIEnchantSlot.BOW, "화살에 불이 붙어 맞은 대상에게 불을 붙게 합니다.", 1),
    INFINITY(Enchantment.ARROW_INFINITE, "무한", GUIEnchantSlot.BOW, "화살을 발사해도 화살의 수가 줄어들지 않습니다.", 1),
    LUCK_OF_THE_SEA(Enchantment.LUCK, "바다의 행운", GUIEnchantSlot.FISHING_ROD, "낚시로 얻는 아이템의 희귀도가 상승합니다.", 3),
    LURE(Enchantment.LURE, "미끼", GUIEnchantSlot.FISHING_ROD, "미끼를 물 때 까지의 시간이 감소합니다.", 3),
    MENDING(Enchantment.MENDING, "수선", GUIEnchantSlot.ALL, "경험치를 소모해 아이템의 내구도를 회복시킵니다.", 1),
    UNBREAKING(Enchantment.DURABILITY, "내구성", GUIEnchantSlot.ALL, "아이템을 사용할 때 내구도가 감소하지 않을 확률이 증가합니다.", 3),
    VANISHING_CURSE(Enchantment.VANISHING_CURSE, "§4소실 저주", GUIEnchantSlot.ALL, "§c사망 시 아이템이 사라집니다.", 1),
    BINDING_CURSE(Enchantment.BINDING_CURSE, "§4귀속 저주", GUIEnchantSlot.ARMOR, "§c아이템을 장착하면 벗을 수 없습니다.", 1);
    private final Enchantment bukkitEnchant;
    private final String displayName;
    private final GUIEnchantSlot slot;
    private final String description;
    private GUIEnchantBundle bundle;
    private Enchantment[] conflicts = new Enchantment[]{};
    private final int maxLevel;
    private static final AddonConfig config = AddonConfig.getConfig("gameConfig");
    private static final String DEFAULTPATH = "gui.customEnchantingTable.enchants.";
    private static final String CHANCE = ".chance";
    private static final String WEIGHT = ".weight";
    private static final String TOTAL_WEIGHT = ".totalWeight";
    private static final String LEVEL_CHANCE = ".levelChance.";
    private static final String SLOT_WEIGHTS = ".slotWeights.";
    public boolean isChanceNotDefined() {
        return config.get(DEFAULTPATH + this.name() + CHANCE) == null;
    }
    public boolean isWeightNotDefined() {
        return config.get(DEFAULTPATH + this.name() + WEIGHT) == null;
    }
    public boolean isTotalWeightNotDefined() {
        return config.get(DEFAULTPATH + this.name() + TOTAL_WEIGHT) == null;
    }
    public boolean isLevelChanceNotDefined(int level) {
        compareCorrectLevel(level);
        return config.get(DEFAULTPATH + this.name() + LEVEL_CHANCE + level) == null;
    }
    public boolean isSlotWeightNotDefined(@NotNull GUIEnchantSlot slot) {
        compareIsAllEnchant();
        return config.get(DEFAULTPATH + this.name() + SLOT_WEIGHTS + slot.name()) == null;
    }
    public double getChance() {
        return Double.parseDouble(config.get(DEFAULTPATH + this.name() + CHANCE).toString());
    }
    public int getWeight() {
        return Integer.parseInt(config.get(DEFAULTPATH + this.name() + WEIGHT).toString());
    }
    public int getTotalWeight() {
        return Integer.parseInt(config.get(DEFAULTPATH + this.name() + TOTAL_WEIGHT).toString());
    }
    public double getLevelChance(int level) {
        compareCorrectLevel(level);
        return Double.parseDouble(config.get(DEFAULTPATH + this.name() + LEVEL_CHANCE + level).toString());
    }
    public int getSlotWeight(@NotNull GUIEnchantSlot slot) {
        compareIsAllEnchant();
        return Integer.parseInt(config.get(DEFAULTPATH + this.name() + SLOT_WEIGHTS + slot.name()).toString());
    }
    public void chance(double chance) {
        config.set(DEFAULTPATH + this.name() + CHANCE, chance);
    }
    public void weight(int weight) {
        config.set(DEFAULTPATH + this.name() + WEIGHT, weight);
    }
    public void totalWeight(int totalWeight) {
        config.set(DEFAULTPATH + this.name() + TOTAL_WEIGHT, totalWeight);
    }
    public void levelChance(int level, double chance) {
        compareCorrectLevel(level);
        config.set(DEFAULTPATH + this.name() + LEVEL_CHANCE + level, chance);
    }
    public void slotWeight(@NotNull GUIEnchantSlot slot, int weight) {
        compareIsAllEnchant();
        config.set(DEFAULTPATH + this.name() + SLOT_WEIGHTS + slot.name(), weight);
    }
    private void compareIsAllEnchant() {
        if (this.slot != GUIEnchantSlot.ALL) {
            throw new IllegalArgumentException("이 인챈트는 모든 장비에 적용되지 않습니다.");
        }
    }
    private void compareCorrectLevel(int level) {
        if (this.maxLevel == 1) {
            throw new IllegalArgumentException("이 인챈트는 하나의 레벨만을 가지고 있습니다.");
        }
        else if (level > this.maxLevel) {
            throw new IllegalArgumentException("이 인챈트의 최대 레벨은 " + this.maxLevel + " 레벨입니다.");
        }
        else if (level < 1) {
            throw new IllegalArgumentException("레벨은 0보다 커야 합니다.");
        }
    }
    public static @NotNull List<GUIEnchant> getArmorEnchants() {
        return Arrays.asList(PROTECTION, FIRE_PROTECTION, PROJECTILE_PROTECTION, BLAST_PROTECTION, THORNS, DEPTH_STRIDER, FROST_WALKER, FEATHER_FALLING, RESPIRATION, AQUA_AFFINITY, BINDING_CURSE);
    }

    public static @NotNull List<GUIEnchant> getMeleeEnchants() {
        return Arrays.asList(SHARPNESS, SMITE, BANE_OF_ARTHROPODS, KNOCKBACK, FIRE_ASPECT, LOOTING, SWEEPING_EDGE);
    }

    public static @NotNull List<GUIEnchant> getToolEnchants() {
        return Arrays.asList(EFFICIENCY, SILK_TOUCH, FORTUNE);
    }

    public static @NotNull List<GUIEnchant> getBowEnchants() {
        return Arrays.asList(POWER, PUNCH, FLAME, INFINITY);
    }

    public static @NotNull List<GUIEnchant> getFishingRodEnchants() {
        return Arrays.asList(LUCK_OF_THE_SEA, LURE);
    }

    public static @NotNull List<GUIEnchant> getOtherEnchants() {
        return Arrays.asList(MENDING, UNBREAKING, VANISHING_CURSE);
    }

    public static @NotNull List<GUIEnchant> getAllEnchants() {
        return Arrays.asList(PROTECTION, FIRE_PROTECTION, PROJECTILE_PROTECTION, BLAST_PROTECTION, THORNS, DEPTH_STRIDER, FROST_WALKER, FEATHER_FALLING, RESPIRATION, AQUA_AFFINITY, BINDING_CURSE, SHARPNESS, SMITE, BANE_OF_ARTHROPODS, KNOCKBACK, FIRE_ASPECT, LOOTING, SWEEPING_EDGE, EFFICIENCY, SILK_TOUCH, FORTUNE, POWER, PUNCH, FLAME, INFINITY, LUCK_OF_THE_SEA, LURE, MENDING, UNBREAKING, VANISHING_CURSE);
    }

    public static @NotNull List<GUIEnchant> getFromSlot(@NotNull GUIEnchantSlot slot) {
        switch (slot) {
            case ARMOR:
                return getArmorEnchants();
            case MELEE:
                return getMeleeEnchants();
            case TOOL:
                return getToolEnchants();
            case BOW:
                return getBowEnchants();
            case FISHING_ROD:
                return getFishingRodEnchants();
            case ALL:
                return getOtherEnchants();
            case BOOK:
                return getAllEnchants();
            default:
                throw new IllegalArgumentException("Unexpected value: " + slot);
        }
    }
    public static @NotNull GUIEnchant getFromBukkitEnchantment(Enchantment bukkitEnchant) {
        for (GUIEnchant enchant : getAllEnchants()) {
            if (enchant.getBukkitEnchant().equals(bukkitEnchant)) {
                return enchant;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + bukkitEnchant);
    }

    public Enchantment getBukkitEnchant() {
        return bukkitEnchant;
    }

    public String getDisplayName() {
        return displayName;
    }

    public GUIEnchantSlot getSlot() {
        return slot;
    }

    public String getDescription() {
        return description;
    }

    public Enchantment[] getConflicts() {
        return conflicts;
    }

    public int getMaxLevel() {
        return maxLevel;
    }
    public GUIEnchantBundle getBundle() {
        return bundle;
    }

    GUIEnchant(Enchantment bukkitEnchant, String displayName, GUIEnchantSlot slot, String description, int maxLevel) {
        this.bukkitEnchant = bukkitEnchant;
        this.displayName = displayName;
        this.slot = slot;
        this.description = description;
        this.maxLevel = maxLevel;
    }
    GUIEnchant(Enchantment bukkitEnchant, String displayName, GUIEnchantSlot slot, String description, int maxLevel, GUIEnchantBundle bundle, Enchantment... conflicts) {
        this.bukkitEnchant = bukkitEnchant;
        this.displayName = displayName;
        this.slot = slot;
        this.description = description;
        this.maxLevel = maxLevel;
        this.bundle = bundle;
        this.conflicts = conflicts;
    }

}
