package main.java.commands.world;

import daybreak.abilitywar.AbilityWar;
import daybreak.abilitywar.game.GameManager;
import daybreak.abilitywar.game.event.GameEndEvent;
import daybreak.abilitywar.game.event.GameReadyEvent;
import daybreak.abilitywar.game.event.participant.ParticipantDeathEvent;
import main.java.util.AddonConfig;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.*;

public class WorldListener implements Listener {
    private boolean gameNotStarted = false;
    private static final String ENABLED = "enabled";
    private static final AddonConfig config = AddonConfig.getConfig("worldConfig");

    @EventHandler
    public void onGameReady(GameReadyEvent e) {
        if ((boolean) config.get(ENABLED)) {
            gameNotStarted = false;
            if (Bukkit.getWorld("main") != null) {
                Bukkit.broadcastMessage("§f[§c!§f] §c게임 전용 월드의 생성 이름인 §6\"§emain§6\"§c으로 된 이름의 월드가 이미 존재해 게임 전용 월드를 생성할 수 없습니다");
                gameNotStarted = true;
                Bukkit.getScheduler().scheduleSyncDelayedTask(AbilityWar.getPlugin(), GameManager::stopGame, 1);
                return;
            }
            List<String> worlds = config.getConfig().getStringList("gameWorlds");
            if (worlds == null || worlds.isEmpty()) {
                gameNotStarted = true;
                Bukkit.broadcastMessage("§f[§c!§f] §c게임 전용 월드를 사용하는 기능이 켜져 있으나 게임 전용 월드를 찾을 수 없습니다");
                Bukkit.getScheduler().scheduleSyncDelayedTask(AbilityWar.getPlugin(), GameManager::stopGame, 1);
                return;
            }
            Collections.shuffle(worlds);
            String worldName = worlds.get(0);
            Bukkit.broadcastMessage("§e게임 전용 월드 생성 중...");
            Bukkit.getOnlinePlayers().forEach(p -> p.sendTitle("§e게임 전용 월드 생성 중...", "§d이 작업은 서버를 구동하는 기기의 사양에 따라 시간이 걸릴 수 있습니다", 0, Integer.MAX_VALUE, 0));
            copyWorld(Bukkit.getWorld(worldName).getWorldFolder(), new File("main"));
            Bukkit.createWorld(new WorldCreator("main"));
            Bukkit.broadcastMessage("§a게임 전용 월드 생성 완료! 게임 시작을 진행합니다.");
            Bukkit.broadcastMessage("§a선택된 맵: §f" + ChatColor.translateAlternateColorCodes('&', config.get("gameWorldOptions." + worldName + ".displayName").toString()));
            Bukkit.getScheduler().scheduleSyncDelayedTask(AbilityWar.getPlugin(), () -> {
                Bukkit.getOnlinePlayers().forEach(Player::resetTitle);
                Bukkit.getOnlinePlayers().forEach(p -> p.teleport(Bukkit.getWorld("main").getSpawnLocation()));
            }, 5);
        }
    }
    @EventHandler
    public void onDeath(ParticipantDeathEvent e) {
        if ((boolean) config.get(ENABLED) && YamlConfiguration.loadConfiguration(new File("plugins/AbilityWar/Config.yml")).getString("게임.사망.작업").equals("관전모드")) {
            Location l = e.getPlayer().getLocation();
            Bukkit.getScheduler().scheduleSyncDelayedTask(AbilityWar.getPlugin(), () -> {
                e.getPlayer().teleport(l);
                Bukkit.getScheduler().scheduleSyncDelayedTask(AbilityWar.getPlugin(), () -> e.getPlayer().setGameMode(GameMode.SPECTATOR), 1);
            }, 2);
        }
    }
    @EventHandler
    public void onGameEnd(GameEndEvent e) {
        if ((boolean) config.get(ENABLED) && !gameNotStarted && Bukkit.getWorld("main") != null) {
            Bukkit.broadcastMessage("§a게임 종료! 10초 후 월드 초기화가 진행됩니다.");
            Bukkit.getScheduler().scheduleSyncDelayedTask(AbilityWar.getPlugin(), this::removeMainWorld, 200);
        }
    }

    private void removeMainWorld() {
        Bukkit.broadcastMessage("§e게임 전용 월드를 제거하는 중입니다...");
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().equals(Bukkit.getWorld("main"))) {
                p.teleport(Bukkit.getWorld(AddonConfig.getConfig("lobbyConfig").get("lobby").toString()).getSpawnLocation());
                p.getInventory().clear();
            }
        }
        Bukkit.unloadWorld(Bukkit.getWorld("main"), false);
        Bukkit.getScheduler().scheduleSyncDelayedTask(AbilityWar.getPlugin(), () -> {
            try {
                FileUtils.deleteDirectory(new File("main"));
            } catch (IOException ex) {
                if (ex.getCause() == null) {
                    throw new WorldRemoveFailedException("월드 제거에 실패했습니다: 알 수 없는 오류");
                } else {
                    throw new WorldRemoveFailedException("월드 제거에 실패했습니다: " + ex.getCause().toString());
                }
            }
        }, 10);
    }

    private void copyWorld(@NotNull File source, File target) {
        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.dat"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists() && !target.mkdirs()) {
                        throw new WorldCreateFailedException("월드가 제대로 생성되지 않았습니다: 월드 파일 생성에 실패했습니다");
                    }
                    for (String file : Objects.requireNonNull(source.list())) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyWorld(srcFile, destFile);
                    }
                } else {
                    InputStream in = Files.newInputStream(source.toPath());
                    OutputStream out = Files.newOutputStream(target.toPath());
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new WorldCreateFailedException("월드가 제대로 생성되지 않았습니다: " + e.getCause().toString());
        }
    }
}
