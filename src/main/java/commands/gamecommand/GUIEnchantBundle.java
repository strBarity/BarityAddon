package main.java.commands.gamecommand;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum GUIEnchantBundle {
    DAMAGE_ENCHANTMENTS(GUIEnchant.SHARPNESS, GUIEnchant.SMITE, GUIEnchant.BANE_OF_ARTHROPODS),
    PROTECTION_ENCHANTMENTS(GUIEnchant.PROTECTION, GUIEnchant.FIRE_PROTECTION, GUIEnchant.PROJECTILE_PROTECTION, GUIEnchant.BLAST_PROTECTION);
    private final List<GUIEnchant> enchants;

    public List<GUIEnchant> getEnchants() {
        return enchants;
    }

    GUIEnchantBundle(GUIEnchant... enchants) {
        this.enchants = Arrays.stream(enchants).collect(Collectors.toList());
    }
}
