package main.java.commands;

import main.java.commands.item.ItemCommand;
import main.java.commands.lobby.LobbyCommand;
import main.java.commands.random.RandomCommand;

public class BarityCommand {
    public BarityCommand() {
        new LobbyCommand();
        new ItemCommand();
        new RandomCommand();
    }
}
