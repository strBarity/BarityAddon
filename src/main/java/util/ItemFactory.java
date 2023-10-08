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
