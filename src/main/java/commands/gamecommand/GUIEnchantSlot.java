package main.java.commands.gamecommand;

public enum GUIEnchantSlot {
    ARMOR("갑옷"),
    HELMET("헬멧"),
    BOOTS("신발"),
    WEAPON("근접 무기"),
    BOW("활"),
    TOOL("도구"),
    FISHING_ROD("낚싯대"),
    ALL("모든 장비");
    private final String displayName;
    GUIEnchantSlot(String displayName) {
        this.displayName = displayName;
    }
}
