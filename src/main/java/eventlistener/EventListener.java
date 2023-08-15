package main.java.eventlistener;

import daybreak.abilitywar.AbilityWar;
import daybreak.abilitywar.game.event.GameCreditEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class EventListener implements Listener {

    @EventHandler
    public void onGameCredit(@NotNull GameCreditEvent e) {
        e.addCredit("§d✿ §4딸기§c베리티 애드온§f이 적용되었습니다. §d✧*｡٩(ˊᗜˋ*)و✧*｡");
        e.addCredit("§d✿ §4딸기§c베리티 애드온 §f개발자: §dBarity_ ");
        e.addCredit("§d✿ §b디스코드: §dBarity_ §a(문의 환영!)");
    }

    public static void registerEvents(Listener @NotNull ... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, AbilityWar.getPlugin());
        }
    }

}
