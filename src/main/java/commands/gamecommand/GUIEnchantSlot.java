package main.java.commands.gamecommand;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
    private final String displayName;

    public String getDisplayName() {
        return displayName;
    }

    @Contract(pure = true)
    public static @NotNull List<GUIEnchantSlot> getCommonSlots() {
        return Arrays.asList(ARMOR, MELEE, BOW, TOOL, FISHING_ROD);
    }

    GUIEnchantSlot(String displayName) {
        this.displayName = displayName;
    }
}
