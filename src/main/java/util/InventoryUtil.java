package main.java.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryUtil {
    private InventoryUtil() {

    }
    public static int getFullBorderedInvSlot(int i) {
        int slot;
        if (i < 7) {
            slot = i + 10;
        }
        else if (i < 14) {
            slot = i + 12;
        }
        else if (i < 21) {
            slot = i + 14;
        }
        else if (i < 28) {
            slot = i + 16;
        }
        else {
            throw new IllegalArgumentException("완전한 테두리가 있는 인벤토리는 슬롯을 최대 28칸까지만 지원합니다. 입력됨: " + i);
        }
        return slot;
    }
    public static Inventory blankInv(int size, String title, ItemColor blankColor, boolean isShiny) {
        Inventory gui = Bukkit.createInventory(null, size, title);
        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, ItemFactory.blank(blankColor, isShiny));
        }
        return gui;
    }

    public static @NotNull @Unmodifiable Inventory blankInvVerticalBorder(int size, String title, ItemColor mainBlankColor, boolean isMainShiny, ItemColor borderBlankColor, boolean isBorderShiny) {
        Inventory gui = blankInv(size, title, mainBlankColor, isMainShiny);
        return applyVerticalBorder(gui, borderBlankColor, isBorderShiny);
    }

    public static Inventory blankInvHorizontalBorder(int size, String title, ItemColor mainBlankColor, boolean isMainShiny, ItemColor borderBlankColor, boolean isBorderShiny) {
        Inventory gui = blankInv(size, title, mainBlankColor, isMainShiny);
        return applyHorizontalBorder(gui, borderBlankColor, isBorderShiny);
    }

    public static Inventory blankInvFullBorder(int size, String title, ItemColor mainBlankColor, boolean isMainShiny, ItemColor borderBlankColor, boolean isBorderShiny) {
        Inventory gui = blankInv(size, title, mainBlankColor, isMainShiny);
        return applyHorizontalBorder(applyVerticalBorder(gui, borderBlankColor, isBorderShiny), borderBlankColor, isBorderShiny);
    }

    private static Inventory applyVerticalBorder(Inventory gui, ItemColor borderBlankColor, boolean isBorderShiny) {
        List<Integer> borders = new ArrayList<>();
        for (int i : Arrays.asList(0, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 53)) {
            if (i < gui.getSize()) {
                borders.add(i);
            }
        }
        for (int i : borders) {
            gui.setItem(i, ItemFactory.blank(borderBlankColor, isBorderShiny));
        }
        return gui;
    }

    private static Inventory applyHorizontalBorder(Inventory gui, ItemColor borderBlankColor, boolean isBorderShiny) {
        for (int i = 0; i < 9; i++) {
            gui.setItem(i, ItemFactory.blank(borderBlankColor, isBorderShiny));
        }
        for (int i = gui.getSize() - 9; i < gui.getSize(); i++) {
            gui.setItem(i, ItemFactory.blank(borderBlankColor, isBorderShiny));
        }
        return gui;
    }
}
