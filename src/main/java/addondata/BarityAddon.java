package main.java.addondata;

import daybreak.abilitywar.addon.Addon;
import daybreak.abilitywar.utils.base.Messager;
import main.java.commands.BarityCommand;
import main.java.commands.item.ItemStorageListener;
import main.java.commands.lobby.LobbyBlockedListener;
import main.java.commands.lobby.LobbyHotbarListener;
import main.java.eventlistener.EventListener;

public class BarityAddon extends Addon {

    @Override
    public void onEnable() {
        Messager.sendConsoleMessage("§d[§4Barity§cAddon§d] " + getDisplayName() + "§a이 활성화되었습니다 ✧･ﾟʚ(*´꒳`*)ɞ ✧･ﾟ");

        new BarityCommand();

        EventListener.registerEvents(new EventListener(), new LobbyBlockedListener(), new LobbyHotbarListener(), new ItemStorageListener());
    }
}