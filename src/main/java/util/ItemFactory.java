package main.java.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemFactory {

    private ItemFactory() {

    }

    public enum ItemColor {
        WHITE(0),
        ORANGE(1),
        MAGENTA(2),
        LIGHT_BLUE(3),
        YELLOW(4),
        LIME(5),
        PINK(6),
        GRAY(7),
        LIGHT_GRAY(8),
        CYAN(9),
        PURPLE(10),
        BLUE(11),
        BROWN(12),
        GREEN(13),
        RED(14),
        BLACK(15);
        private final int color;
        ItemColor(int color) {
            this.color = color;
        }
    }

    public static @NotNull ItemStack createItem(Material type, int damage, String name, @Nullable List<String> lore, @Nullable Map<Enchantment, Integer> enchant, int amount, boolean isShiny) {
        ItemStack i = new ItemStack(type, amount, (short) damage);
        ItemMeta m = i.getItemMeta();
        m.setDisplayName(name);
        if (lore != null) {
            List<String> finalLore = new ArrayList<>();
            for (String s : lore) {
                finalLore.add(ChatColor.WHITE + s);
            }
            m.setLore(finalLore);
        }
        if (enchant != null) {
            for (Map.Entry<Enchantment, Integer> entry : enchant.entrySet()) {
                m.addEnchant(entry.getKey(), entry.getValue(), true);
            }
        }
        if (isShiny) {
            m.addEnchant(Enchantment.DURABILITY, 1, false);
        }
        m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_POTION_EFFECTS);
        m.setUnbreakable(true);
        i.setItemMeta(m);
        return i;
    }

    public static @NotNull ItemStack blank(@NotNull ItemColor color, boolean isShiny) {
        return createItem(Material.STAINED_GLASS_PANE, color.color, " ", null, null, 1, isShiny);
    }
}
