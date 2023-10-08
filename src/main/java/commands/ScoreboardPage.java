package main.java.commands;

import main.java.commands.gamecommand.GameScoreboard;
import main.java.commands.lobby.LobbyScoreboard;
import main.java.util.AddonConfig;

import java.util.List;

public enum ScoreboardPage {
    LOBBY(AddonConfig.getConfig("lobbyConfig"), "§8로비 스코어보드 설정", LobbyScoreboard.getAvailableVariablesList()),
    GAME(AddonConfig.getConfig("gameConfig"), "§8인게임 스코어보드 설정", GameScoreboard.getAvailableVariablesList());
    private final AddonConfig config;
    private final String invTitle;
    private final List<String> availableVariables;
    ScoreboardPage(AddonConfig config, String invTitle, List<String> availableVariables) {
        this.config = config;
        this.invTitle = invTitle;
        this.availableVariables = availableVariables;
    }

    public AddonConfig getConfig() {
        return config;
    }

    public String getInvTitle() {
        return invTitle;
    }

    public List<String> getAvailableVariables() {
        return availableVariables;
    }
}
