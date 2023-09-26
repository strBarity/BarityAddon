package main.java.addondata;

import daybreak.abilitywar.addon.Addon;
import daybreak.abilitywar.utils.base.Messager;
import main.java.commands.BarityCommand;
import main.java.commands.gamecommand.GameVote;
import main.java.commands.gamecommand.GameVoteListener;
import main.java.commands.item.ItemStorageListener;
import main.java.commands.lobby.*;
import main.java.eventlistener.EventListener;
import main.java.playerdata.PlayerData;
import main.java.playerdata.PlayerDataListener;
import main.java.util.AddonConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BarityAddon extends Addon {

    @Override
    public void onEnable() {
        Messager.sendConsoleMessage("§d[§4Barity§cAddon§d] " + getDisplayName() + "§a이 활성화되었습니다 ✧･ﾟʚ(*´꒳`*)ɞ ✧･ﾟ");

        new BarityCommand();

        EventListener.registerEvents(new EventListener(),
                new LobbyBlockedListener(),
                new LobbyHotbarListener(),
                new ItemStorageListener(),
                new LobbyWorldListener(),
                new LobbyScoreboardListener(),
                new PlayerDataListener(),
                new GameVoteListener(),
                new LobbyChatListener(),
                new LobbyTablistListener());

        AddonConfig.getConfig("itemStorage").loadData();
        AddonConfig.getConfig("lobbyConfig").loadData();
        AddonConfig.getConfig("gameConfig").loadData();

        LobbyScoreboard.startScoreboardTask();

        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                PlayerData.defineData(p);
            }
        }
    }

    @Override
    public void onDisable() {
        GameVote.voteBar.removeAll();
    }
}