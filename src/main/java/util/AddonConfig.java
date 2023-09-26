package main.java.util;

import daybreak.abilitywar.utils.base.Messager;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class AddonConfig {
    private File configFile;
    private FileConfiguration config;
    private final String fileName;
    private AddonConfig(String fileName) {
        this.fileName = fileName;
        loadData();
    }

    public void loadData() {
        configFile = new File("plugins/AbilityWar/Addon/BarityAddon/" + this.fileName + ".yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        try {
            if (!configFile.exists()) {
                config.save(configFile);
            }
        } catch (Exception e) {
            Messager.sendConsoleMessage("컨피그 파일 로드 중 오류가 발생했습니다.");
        }
    }

    private void reload() {
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            Messager.sendConsoleMessage("컨피그 파일 로드 중 오류가 발생했습니다.");
        }
    }

    public void saveData() {
        try {
            config.save(configFile);
            reload();
        } catch (IOException e) {
            Messager.sendConsoleMessage("컨피그 파일 저장 중 오류가 발생했습니다.");
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public Object get(String path) {
        reload();
        return config.get(path);
    }

    public void set(String path, Object value) {
        config.set(path, value);
        saveData();
    }

    @Contract(pure = true)
    public static @NotNull String boolTranslate(boolean b) {
        if (b) {
            return "§a켜짐";
        }
        else {
            return "§c꺼짐";
        }
    }

    @Contract("_ -> new")
    public static @NotNull AddonConfig getConfig(String fileName) {
        return new AddonConfig(fileName);
    }

}
