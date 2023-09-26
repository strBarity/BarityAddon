package main.java.playerdata;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerData {

    private boolean isVoted;
    private boolean inEdit;
    private boolean inScoreboardEdit;
    private int editingScoreboardId;
    private boolean inChatFormatEdit;
    private boolean inTablistHeaderEdit;
    private boolean inTablistFooterEdit;
    private boolean inTablistPlayerFormatEdit;

    private static final Map<Player, PlayerData> DATA = new ConcurrentHashMap<>();

    public PlayerData(boolean isVoted,
                      boolean inEdit,
                      boolean inScoreboardEdit,
                      int editingScoreboardId,
                      boolean inChatFormatEdit,
                      boolean inTablistHeaderEdit,
                      boolean inTablistFooterEdit,
                      boolean inTablistPlayerFormatEdit) {
        this.isVoted = isVoted;
        this.inEdit = inEdit;
        this.inScoreboardEdit = inScoreboardEdit;
        this.editingScoreboardId = editingScoreboardId;
        this.inChatFormatEdit = inChatFormatEdit;
        this.inTablistHeaderEdit = inTablistHeaderEdit;
        this.inTablistFooterEdit = inTablistFooterEdit;
        this.inTablistPlayerFormatEdit = inTablistPlayerFormatEdit;
    }

    public boolean isVoted() {
        return isVoted;
    }

    public void setVoted(boolean voted) {
        isVoted = voted;
    }

    public boolean isInScoreboardEdit() {
        return inScoreboardEdit;
    }

    public void setInScoreboardEdit(boolean inScoreboardEdit) {
        this.inScoreboardEdit = inScoreboardEdit;
    }

    public int getEditingScoreboardId() {
        return editingScoreboardId;
    }

    public void setEditingScoreboardId(int editingScoreboardId) {
        this.editingScoreboardId = editingScoreboardId;
    }

    public boolean isInChatFormatEdit() {
        return inChatFormatEdit;
    }

    public void setInChatFormatEdit(boolean inChatFormatEdit) {
        this.inChatFormatEdit = inChatFormatEdit;
    }

    public boolean isInTablistHeaderEdit() {
        return inTablistHeaderEdit;
    }

    public void setInTablistHeaderEdit(boolean inTablistHeaderEdit) {
        this.inTablistHeaderEdit = inTablistHeaderEdit;
    }

    public boolean isInTablistFooterEdit() {
        return inTablistFooterEdit;
    }

    public void setInTablistFooterEdit(boolean inTablistFooterEdit) {
        this.inTablistFooterEdit = inTablistFooterEdit;
    }

    public boolean isInTablistPlayerFormatEdit() {
        return inTablistPlayerFormatEdit;
    }

    public void setInTablistPlayerFormatEdit(boolean inTablistPlayerFormatEdit) {
        this.inTablistPlayerFormatEdit = inTablistPlayerFormatEdit;
    }

    public boolean isInEdit() {
        return inEdit;
    }

    public void setInEdit(boolean inEdit) {
        this.inEdit = inEdit;
    }

    public static void defineData(Player p) {
        if (DATA.containsKey(p)) {
            throw new PlayerDataAlreadyExistsException("플레이어 " + p.getName() + "의 데이터는 이미 존재합니다");
        }

        DATA.put(p, new PlayerData(false, false, false, 0, false, false, false, false));
    }


    public static void clearData(Player p) {
        DATA.remove(p);
    }

    public static PlayerData getData(Player p) {
        PlayerData data = DATA.get(p);
        if (data == null) {
            throw new PlayerDataNotExistsException("플레이어 " + p.getName() + "의 데이터를 찾을 수 없습니다");
        }
        return DATA.get(p);
    }
}
