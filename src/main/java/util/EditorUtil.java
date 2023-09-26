package main.java.util;

import daybreak.abilitywar.utils.base.Messager;
import main.java.playerdata.PlayerData;
import org.bukkit.entity.Player;

public class EditorUtil {
    private EditorUtil() {

    }
    public static boolean isEditing(Player p) {
        if (PlayerData.getData(p).isInEdit()) {
            Messager.sendErrorMessage(p, "이미 편집 중인 텍스트가 있습니다.");
            return true;
        }
        return false;
    }
}
