package yes.mediumdifficulty.streamline.action;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandException;

public class CommandAction extends AbstractAction {
    @Override
    public String handle(String payload) {
        Server server = Bukkit.getServer();
        try {
            return String.valueOf(server.dispatchCommand(server.getConsoleSender(), payload));
        } catch (CommandException exception) {
            return exception.getMessage();
        }
    }

    @Override
    public String getName() {
        return "command";
    }
}
