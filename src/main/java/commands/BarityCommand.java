package main.java.commands;

import main.java.commands.cosmetic.CosmeticCommand;
import main.java.commands.gamecommand.GameCommand;
import main.java.commands.gamecommand.GameVoteCommand;
import main.java.commands.item.ItemCommand;
import main.java.commands.lobby.LobbyCommand;
import main.java.commands.random.RandomCommand;
import main.java.commands.world.WorldCommand;

public class BarityCommand {
    public BarityCommand() {
        new LobbyCommand();
        new ItemCommand();
        new RandomCommand();
        new WorldCommand();
        new GameCommand();
        new GameVoteCommand();
        new CosmeticCommand();
    }
}
