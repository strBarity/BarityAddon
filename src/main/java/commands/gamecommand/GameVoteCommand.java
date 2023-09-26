package main.java.commands.gamecommand;

import daybreak.abilitywar.AbilityWar;
import daybreak.abilitywar.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GameVoteCommand extends GameVote {
    public GameVoteCommand() {
        Command gameVoteCommand = new Command() {
            @Override
            protected boolean onCommand(CommandSender sender, String command, String @NotNull [] args) {
                if (sender instanceof Player) {
                    vote((Player) sender);
                }
                return true;
            }
        };
        AbilityWar.getPlugin().getCommands().getMainCommand().addSubCommand("vote", gameVoteCommand);
    }
}
